package org.liveontologies.protege.explanation.justification.service;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * An object using which justification can be computed
 * 
 * @author Yevgeny Kazakov
 *
 */
public abstract class JustificationComputation {

	private final JustificationListener listener_;

	private final InterruptMonitor monitor_;

	public JustificationComputation(JustificationListener listener,
			InterruptMonitor monitor) {
		this.listener_ = listener;
		this.monitor_ = monitor;
	}

	/**
	 * Initiate the computation of justifications;
	 */
	public abstract void startComputation();

	/**
	 * Should be called each time a new justification is found
	 * 
	 * @param justification
	 */
	protected void notifyJustificationFound(Set<OWLAxiom> justification) {
		listener_.justificationFound(justification);
	}

	/**
	 * @return {@code true} if the computation has been interrupted and
	 *         {@code false} otherwise
	 */
	public boolean isInterrupted() {
		return monitor_.isInterrupted();
	}

	public interface InterruptMonitor {

		/**
		 * @return {@code true} if the computation should be interrupted
		 */
		boolean isInterrupted();

	}

}
