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


import org.protege.editor.owl.ui.explanation.io.InconsistentOntologyPluginInstance;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Author: Matthew Horridge
 * Stanford University
 * Bio-Medical Informatics Research Group
 * Date: 18/03/2012
 */

public class InconsistentOntologyPresentationService implements InconsistentOntologyPluginInstance {

	private OWLEditorKit editorKit;
	private JustificationComputationServiceManager manager;

	@Override
	public void setup(OWLEditorKit editorKit) {
		this.editorKit = editorKit;
	}

	@Override
	public void initialise() throws Exception {
		manager = new JustificationComputationServiceManager(editorKit, "org.liveontologies.protege.explanation.justification", "JustificationService");
	}

	@Override
	public void dispose() throws Exception {
	}

	@Override
	public void explain(OWLOntology ontology) {
		OWLDataFactory df = editorKit.getOWLModelManager().getOWLDataFactory();
		OWLSubClassOfAxiom entailment = df.getOWLSubClassOfAxiom(df.getOWLThing(), df.getOWLNothing());
		PresentationPanel panel = new PresentationPanel(manager, entailment);

		JOptionPane op = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
		JDialog dlg = op.createDialog("Inconsistent ontology explanation");
		dlg.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				panel.dispose();
				dlg.dispose();
			}
		});
		dlg.setModal(false);
		dlg.setResizable(true);
		dlg.setVisible(true);
	}
}