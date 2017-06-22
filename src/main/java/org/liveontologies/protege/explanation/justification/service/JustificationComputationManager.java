package org.liveontologies.protege.explanation.justification.service;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.liveontologies.protege.explanation.justification.service.JustificationComputation.InterruptMonitor;

/*-
 * #%L
 * Protege Justification Explanation
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2016 - 2017 Live Ontologies Project
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Manages computation of justifications for a given {@link OWLAxiom}
 * 
 * @author Alexander Stupnikov Date: 08-02-2017
 * 
 * @author Yevgeny Kazakov
 */
public abstract class JustificationComputationManager {

	/**
	 * the axiom for which the justifications are computed
	 */
	private final OWLAxiom entailment_;

	/**
	 * the {@link JustificationListener} using which new justifications are
	 * reported
	 */
	private final JustificationListener listener_;

	/**
	 * the {@link InterruptMonitor} that is used to check if the computation has
	 * been interrupted
	 */
	private final InterruptMonitor monitor_;

	/**
	 * listeners to be notified when justifications may have changed
	 */
	private final List<ChangeListener> changeListeners_ = new ArrayList<>();

	/**
	 * Constructs a computation object
	 * 
	 * @param entailment
	 *            an axiom to compute justifications for
	 * @param listener
	 *            the {@link JustificationListener} using which new
	 *            justifications are reported
	 * @param monitor
	 *            the {@link InterruptMonitor} that is used to check if the
	 *            computation has been interrupted
	 * 
	 */
	public JustificationComputationManager(OWLAxiom entailment,
			JustificationListener listener, InterruptMonitor monitor) {
		this.entailment_ = entailment;
		this.listener_ = listener;
		this.monitor_ = monitor;
	}

	/**
	 * @return an axiom to compute justifications for
	 */
	public OWLAxiom getEntailment() {
		return entailment_;
	}

	public JustificationListener getJustificationListener() {
		return listener_;
	}

	public InterruptMonitor getInterruptMonitor() {
		return monitor_;
	}

	public void addListener(ChangeListener listener) {
		changeListeners_.add(listener);
	}

	public void removeListener(ChangeListener listener) {
		changeListeners_.remove(listener);
	}

	/**
	 * @return the {@link JPanel} that can display settings for the computation
	 *         of the justification or {@code null} if no setting panel is
	 *         required.
	 */
	public JPanel getSettingsPanel() {
		return null;
	}

	/**
	 * 
	 * @return the {@link JustificationComputation} that can be used for
	 *         computing justification; this computation should use the
	 *         {@link #getJustificationListener()} for reporting justifications
	 *         and {@link #getInterruptMonitor()} for monitoring when interrupt
	 *         has been requested
	 */
	public abstract JustificationComputation getComputation();

	/**
	 * should be called each time the computed justification may have changed
	 */
	protected void notifyComputationChanged() {
		for (ChangeListener listener : changeListeners_) {
			listener.computationChanged();
		}
	}

	public interface ChangeListener {
		/**
		 * fired when the justifications computed by the
		 * {@link JustificationComputationManager} may have changed, i.e., when
		 * computed again, the result may differ
		 */
		void computationChanged();
	}

}