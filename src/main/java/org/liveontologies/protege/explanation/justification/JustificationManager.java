package org.liveontologies.protege.explanation.justification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JPanel;

import org.liveontologies.protege.explanation.justification.service.JustificationComputation;
import org.liveontologies.protege.explanation.justification.service.JustificationComputation.InterruptMonitor;
import org.liveontologies.protege.explanation.justification.service.JustificationComputationManager;
import org.liveontologies.protege.explanation.justification.service.JustificationComputationService;
import org.liveontologies.protege.explanation.justification.service.JustificationListener;
import org.liveontologies.protege.explanation.justification.service.JustificationPriorityComparator;
import org.protege.editor.core.log.LogBanner;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to keep track of the computed justifications for a particular entailed
 * {@link OWLAxiom}
 * 
 * @author Yevgeny Kazakov
 *
 */
public class JustificationManager implements
		JustificationComputationManager.ChangeListener, JustificationListener {

	// logger for this class
	private static final Logger LOGGER_ = LoggerFactory
			.getLogger(JustificationManager.class);

	/**
	 * the manager for the registered justification computation services
	 */
	private final JustificationComputationServiceManager serviceMan_;

	/**
	 * the entailement for which the computations are managed
	 */
	private final OWLAxiom entailment_;

	private final JustificationComputationListener computationListener_;

	private final InterruptMonitor interruptMonitor_;

	private final ExecutorService executorService_;

	private final Queue<Justification> justifications_ = new PriorityQueue<>();

	/**
	 * the size of #justifications_
	 */
	private int justificationCount_ = 0;

	/**
	 * counts how many times each axiom occurs in a justification
	 */
	private final Map<OWLAxiom, Integer> axiomsPopularity_ = new HashMap<>();

	private JustificationComputationService selectedComputationService_;

	private JustificationComputationManager computationManager_;

	private JPanel computationSettingsPanel_;

	/**
	 * the listeners to be notified when {@link #justifications_} have changed
	 */
	private final List<ChangeListener> listeners_ = new ArrayList<ChangeListener>();

	JustificationManager(JustificationComputationServiceManager serviceManager,
			OWLAxiom entailment,
			JustificationComputationListener computationListener,
			InterruptMonitor interruptMonitor) {
		this.serviceMan_ = serviceManager;
		this.entailment_ = entailment;
		this.computationListener_ = computationListener;
		this.interruptMonitor_ = interruptMonitor;
		this.executorService_ = Executors.newSingleThreadExecutor();
	}

	JustificationManager(OWLEditorKit kit, OWLAxiom entailment,
			JustificationComputationListener computationListener,
			InterruptMonitor interruptMonitor) throws Exception {
		this(JustificationComputationServiceManager.get(kit), entailment,
				computationListener, interruptMonitor);
	}

	/**
	 * @return the axiom for which the proofs are managed
	 */
	public OWLAxiom getEntailment() {
		return entailment_;
	}

	public OWLEditorKit getOwlEditorKit() {
		return serviceMan_.getOwlEditorKit();
	}

	/**
	 * @return the {@link JustificationComputationService} that can be used for
	 *         computing justifications for the {@link #getEntailment()}, i.e.,
	 *         those for which
	 *         {@link JustificationComputationService#canJustify(OWLAxiom)} is
	 *         {@code true}
	 * 
	 * @see #getEntailment()
	 */
	public Collection<JustificationComputationService> getServices() {
		List<JustificationComputationService> result = new ArrayList<>();
		for (JustificationComputationService service : serviceMan_
				.getServices()) {
			if (service.canJustify(entailment_)) {
				result.add(service);
			}
		}
		return result;
	}

	private void resetJustifications() {
		justifications_.clear();
		justificationCount_ = 0;
		axiomsPopularity_.clear();
	}

	private void addJustification(Set<OWLAxiom> justification) {
		justifications_.add(new Justification(justification));
		justificationCount_++;
		for (OWLAxiom axiom : justification) {
			Integer popularity = axiomsPopularity_.get(axiom);
			if (popularity == null) {
				popularity = 0;
			}
			popularity++;
			axiomsPopularity_.put(axiom, popularity);
		}
	}

	public JPanel getSettingsPanel() {
		return computationSettingsPanel_;
	}

	/**
	 * Sets the {@link JustificationComputationService} that should be used for
	 * computing justifications for {@link #getEntailment()}; the computed
	 * justifications are updated if necessary. The
	 * {@link JustificationComputationService} must be from
	 * {@link #getServices()}.
	 * 
	 * @param service
	 * @see #getEntailment()
	 * @see #getServices()
	 */
	public synchronized void selectJusificationService(
			JustificationComputationService service) {
		if (selectedComputationService_ == service) {
			return;
		}
		selectedComputationService_ = service;
		if (computationManager_ != null) {
			computationManager_.removeListener(this);
		}
		computationManager_ = service.createComputationManager(entailment_,
				this, interruptMonitor_);
		computationSettingsPanel_ = computationManager_.getSettingsPanel();
		notifySettingsPanelChanged();
		recomputeJustifications();
		computationManager_.addListener(this);
	}

	@Override
	public void computationChanged() {
		recomputeJustifications();
	}

	@Override
	public void settingsPanelChanged() {
		notifySettingsPanelChanged();
	}

	@Override
	public void justificationFound(Set<OWLAxiom> justification) {
		addJustification(justification);
		computationListener_.justificationFound(justification);
	}

	void recomputeJustifications() {
		resetJustifications();
		try {
			computationListener_.computationStarted();
			executorService_.submit(new ExplanationComputationTask());
		} catch (OWLRuntimeException e) {
			LOGGER_.info("Justification computation terminated early by user");
		}
	}

	public synchronized void addListener(ChangeListener listener) {
		listeners_.add(listener);
	}

	public synchronized void removeListener(ChangeListener listener) {
		listeners_.remove(listener);
	}

	public Justification pollJustification() {
		if (justificationCount_ == 0) {
			return null;
		}
		// else
		justificationCount_--;
		return justifications_.poll();
	}

	public int getRemainingJustificationCount() {
		return justificationCount_;
	}

	public int getPopularity(OWLAxiom axiom) {
		return axiomsPopularity_.get(axiom);
	}

	private void notifyJustificationsRecomputed() {
		int i = 0;
		try {
			for (; i < listeners_.size(); i++) {
				listeners_.get(i).justificationsRecomputed();
			}
		} catch (Throwable e) {
			LOGGER_.warn("Remove the listener due to an exception", e);
			removeListener(listeners_.get(i));
		}
	}

	private void notifySettingsPanelChanged() {
		int i = 0;
		try {
			for (; i < listeners_.size(); i++) {
				listeners_.get(i).settingsPanelChanged();
			}
		} catch (Throwable e) {
			LOGGER_.warn("Remove the listener due to an exception", e);
			removeListener(listeners_.get(i));
		}
	}

	public interface ChangeListener {
		/**
		 * fired when the justifications supported by a
		 * {@link JustificationManager} have been recomputed
		 */
		void justificationsRecomputed();

		/**
		 * fired when {@link JustificationManager#getSettingsPanel()} may return
		 * a different value
		 */
		void settingsPanelChanged();
	}

	// TODO: use SwingWorker
	private class ExplanationComputationTask implements Runnable,
			JustificationPriorityComparator<Justification> {

		private final JustificationComputation computation_;

		private ExplanationComputationTask() {
			this.computation_ = computationManager_.getComputation();
			computation_.setPrefferredPriority(this);
		}

		@Override
		public void run() {
			try {
				LOGGER_.info(LogBanner.start("Computing Justifications"));
				LOGGER_.info("Computing justifications for {}", entailment_);
				computationListener_.computationStarted();
				computation_.startComputation();
			} catch (Throwable e) {
				LOGGER_.info("Exception while computing justifications", e);
			} finally {
				computationListener_.computationFinished();
				LOGGER_.info("A total of {} justifications have been computed",
						justificationCount_);
				LOGGER_.info(LogBanner.end());
				notifyJustificationsRecomputed();
			}
		}

		@Override
		public int compare(Justification j1, Justification j2) {
			return j1.compareTo(j2);
		}

		@Override
		public Justification getPriority(Set<OWLAxiom> justification) {
			return new Justification(justification);
		}

	}

}
