package org.liveontologies.protege.explanation.justification;

import java.awt.event.ActionEvent;

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

import java.util.Comparator;

import javax.swing.AbstractAction;

import org.protege.editor.core.ui.list.MListButton;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.editor.OWLObjectEditor;
import org.protege.editor.owl.ui.frame.AbstractOWLFrameSection;
import org.protege.editor.owl.ui.frame.OWLFrame;
import org.protege.editor.owl.ui.frame.OWLFrameSectionRow;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * A section for loading justifications
 * 
 * @author Yevgeny Kazakov
 *
 */
public class AddJustificationsSection
		extends AbstractOWLFrameSection<Explanation, OWLAxiom, OWLAxiom> {

	private final MListButton button_;

	private boolean isFilled_ = false;

	private final PartialListVisualizer justificationPanel_;

	public AddJustificationsSection(OWLEditorKit editorKit,
			OWLFrame<? extends Explanation> owlFrame,
			PartialListVisualizer justificationPanel) {
		super(editorKit, "", owlFrame);
		justificationPanel_ = justificationPanel;
		button_ = new MListMoreButton(new AbstractAction() {
			private static final long serialVersionUID = 7260664426335623869L;

			@Override
			public void actionPerformed(ActionEvent e) {
				justificationPanel.showNext();
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
		if (isFilled_)
			return;
		isFilled_ = true;
	}

	@Override
	protected void clear() {
		isFilled_ = false;
	}

	public MListButton getButton() {
		return button_;
	}

	public String getToolTipText() {
		return justificationPanel_.getShowMoreJustificationsDescription();
	}
}