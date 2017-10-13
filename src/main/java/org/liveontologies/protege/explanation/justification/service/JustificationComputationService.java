package org.liveontologies.protege.explanation.justification.service;

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

import org.protege.editor.core.plugin.ProtegePluginInstance;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;

/**
 * A skeleton for a plugin that can provide justification based explanation for
 * OWL axioms and inconsistent ontologies
 * 
 * @author Alexander Stupnikov Date: 08-02-2017
 * @author Yevgeny Kazakov
 */
public abstract class JustificationComputationService
		implements ProtegePluginInstance {

	private OWLEditorKit kit_;

	private OWLAxiom inconsistentAxiom_;

	/**
	 * Setup function for service. Should be called before computation object
	 * creation.
	 *
	 * @param kit
	 *            OWLEditorKit to store in the service instance.
	 */
	JustificationComputationService setup(OWLEditorKit kit) {
		kit_ = kit;
		OWLDataFactory df = kit.getOWLModelManager().getOWLDataFactory();
		inconsistentAxiom_ = df.getOWLSubClassOfAxiom(df.getOWLThing(),
				df.getOWLNothing());
		return this;
	}

	/**
	 * Check if computation of justifications is supported for a given
	 * {@link OWLAxiom}
	 * 
	 * @param entailment
	 *            an {@link OWLAxiom} for which justification should be computed
	 * 
	 * @return {@code true} if {@link #createComputationManager} can compute the
	 *         output for the given {@link OWLAxiom}
	 */
	public abstract boolean canJustify(OWLAxiom entailment);

	/**
	 * Creates a {@link JustificationComputationManager} computation object
	 * designed to compute everything regarding the service.
	 *
	 * @param entailment
	 *            an axiom to compute justification for
	 * @param listener
	 *            a listener through which to report the computed justifications
	 * @param monitor
	 *            an object that should be used to see if the computation should
	 *            be interrupted
	 * @return {@link JustificationComputationManager} which maintains
	 *         computations for the entailment
	 */
	public abstract JustificationComputationManager createComputationManager(
			OWLAxiom entailment, JustificationListener listener,
			InterruptMonitor monitor);

	/**
	 * @return {@code true} if
	 *         {@link #createInconsistentOntologyJustificationComputation} can
	 *         compute the output
	 */
	public boolean canJustifyInconsistentOntology() {
		return canJustify(inconsistentAxiom_);
	}

	public JustificationComputationManager createInconsistentOntologyJustificationComputation(
			JustificationListener listener, InterruptMonitor monitor) {
		return createComputationManager(inconsistentAxiom_, listener, monitor);
	}

	/**
	 * Should return a name for the plugin
	 * 
	 * @return the name to be displayed in available plugins list
	 */
	public abstract String getName();

	@Override
	public String toString() {
		return getName();
	}

	/**
	 * @return OWLEditorKit for the service.
	 */
	public OWLEditorKit getOWLEditorKit() {
		return kit_;
	}

}