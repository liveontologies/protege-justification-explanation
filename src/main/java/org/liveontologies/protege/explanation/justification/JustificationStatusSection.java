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


import java.awt.event.ActionEvent;
import java.util.Comparator;

import javax.swing.AbstractAction;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.editor.OWLObjectEditor;
import org.protege.editor.owl.ui.frame.AbstractOWLFrameSection;
import org.protege.editor.owl.ui.frame.OWLFrame;
import org.protege.editor.owl.ui.frame.OWLFrameSectionRow;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

public class JustificationStatusSection
		extends AbstractOWLFrameSection<Explanation, OWLAxiom, OWLAxiom> {

	private final MListReloadButton button_;

	protected JustificationStatusSection(OWLEditorKit editorKit,
			OWLFrame<? extends Explanation> frame, PartialListVisualizer justificationPanel) {
		super(editorKit, "", frame);
		button_ = new MListReloadButton(new AbstractAction() {
			private static final long serialVersionUID = 8000443669915588491L;

			@Override
			public void actionPerformed(ActionEvent e) {
				justificationPanel.reset();
			}
		});
	}

	@Override
	public Comparator<OWLFrameSectionRow<Explanation, OWLAxiom, OWLAxiom>> getRowComparator() {
		return null;
	}

	@Override
	protected OWLAxiom createAxiom(OWLAxiom object) {
		return object;
	}

	@Override
	public OWLObjectEditor<OWLAxiom> getObjectEditor() {
		return null;
	}

	@Override
	protected void refill(OWLOntology ontology) {
	}

	@Override
	protected void clear() {
	}

	public MListReloadButton getButton() {
		return button_;
	}

	public void setStatusString(String value) {
		setLabel(value);
	}
}