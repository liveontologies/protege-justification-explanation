package org.liveontologies.protege.explanation.justification;

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


import org.liveontologies.protege.explanation.justification.service.ComputationService;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.explanation.ExplanationResult;
import org.protege.editor.owl.ui.explanation.ExplanationService;
import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * @author Alexander Stupnikov
 * Date: 08-02-2017
 */

public class PresentationService extends ExplanationService {

	private JustificationComputationServiceManager manager;

	@Override
	public void initialise() throws Exception {
		OWLEditorKit kit = getOWLEditorKit();
		manager = new JustificationComputationServiceManager(kit, "org.liveontologies.protege.explanation.justification", "JustificationService");
	}

	@Override
	public boolean hasExplanation(OWLAxiom axiom) {
		for (ComputationService service : manager.getServices())
			if (service.canComputeJustification(axiom))
				return true;
		return false;
	}

	@Override
	public ExplanationResult explain(OWLAxiom axiom) {
		PresentationPanel panel = new PresentationPanel(manager, axiom);
		return new PresentationPanelResult(panel);
	}

	@Override
	public void dispose() throws Exception {
	}
}