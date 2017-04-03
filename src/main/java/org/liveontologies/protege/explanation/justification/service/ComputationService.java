package org.liveontologies.protege.explanation.justification.service;

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


import javax.swing.JPanel;

import org.protege.editor.core.plugin.ProtegePluginInstance;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

/**
 * A skeleton for a plugin that can provide justification based explanation for
 * OWL axioms and inconsistent ontologies
 * 
 * @author Alexander Stupnikov
 * Date: 08-02-2017
 */

public abstract class ComputationService implements ProtegePluginInstance {

	/**
     * Creates computation object designed to compute everything regarding the service.
     *
     * @param entailment	An axiom to compute justification for.
     *
     * @return	JustificationComputation for the service. Null if plugin is not able to manage the entailment.
     */
	public abstract JustificationComputation createJustificationComputation(OWLAxiom entailment);
	
	public JustificationComputation createInconsistentOntologyJustificationComputation(){
		OWLDataFactory df = getOWLEditorKit().getOWLModelManager().getOWLDataFactory();
		OWLSubClassOfAxiom entailment = df.getOWLSubClassOfAxiom(df.getOWLThing(), df.getOWLNothing());
		return createJustificationComputation(entailment);
	}
	
	/**
	 * 
	 * @param entailment
	 * @return if returns true createJustificationComputation should not return null
	 */
	public boolean canComputeJustification(OWLAxiom entailment) {
		return createJustificationComputation(entailment) == null;
	}
	
	private OWLEditorKit kit;
	
	/**
	 * Should return a name for the plugin
	 * 
	 * @return	the name to be displayed in available plugins list
	 */
	public abstract String getName();
	
	@Override
	public String toString() {
		return getName();
	}

	/**
     * Returns OWLEditorKit for the service.
     *
     * @return	OWLEditorKit for the service.
     */
	public OWLEditorKit getOWLEditorKit() {
		return kit;
	}
	
	/**
     * Setup function for service.
     * Should be called before computation object creation.
     *
     * @param kit	OWLEditorKit to store in the service instance.
     */
	ComputationService stp(OWLEditorKit kit) {
		this.kit = kit;
		return this;
	}
	
	private ComputationServiceListener listener = null;
	
	void setListener(ComputationServiceListener listener) {
		this.listener = listener;
	}
	
	/**
	 * Could Return visual control pane which will be displayed while the plugin is chosen
	 * 
	 * @return the panel to be displayed. Null if panel is not provided.
	 */
	public JPanel getSettingsPanel() {
		return null;
	}
	
	/**
	 * Allows plugin to ask to re-display justification by calling this method.
	 * When called, JustificationComputation will be requested again.
	 */
	public void settingsChanged() {
		if (listener != null)
			listener.redrawingCalled();
	}
}