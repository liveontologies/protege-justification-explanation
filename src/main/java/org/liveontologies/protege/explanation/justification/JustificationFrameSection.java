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

import java.util.Comparator;
import java.util.List;

import org.liveontologies.protege.explanation.justification.priority.PrioritizedJustification;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.editor.OWLObjectEditor;
import org.protege.editor.owl.ui.frame.AbstractOWLFrameSection;
import org.protege.editor.owl.ui.frame.OWLFrame;
import org.protege.editor.owl.ui.frame.OWLFrameSectionRow;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Author: Matthew Horridge Stanford University Bio-Medical Informatics Research
 * Group Date: 19/03/2012
 */

public class JustificationFrameSection
		extends AbstractOWLFrameSection<Explanation, OWLAxiom, OWLAxiom> {

	private boolean isFilled_ = false;
	private final Explanation explanation_;
	private final int justificationIndex_;

	public JustificationFrameSection(OWLEditorKit editorKit,
			OWLFrame<? extends Explanation> owlFrame, String caption,
			int justificationIndex) {
		super(editorKit, caption, owlFrame);
		explanation_ = owlFrame.getRootObject();
		justificationIndex_ = justificationIndex;
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
		if (isFilled_) {
			return;
		}
		isFilled_ = true;

		PrioritizedJustification justification = getJustification();

		if (justification.getSize() == 0) {
			setLabel("The axiom is a tautology");
		} else {
			JustificationFormattingManager formattingManager = JustificationFormattingManager
					.getInstance();
			OWLAxiom entailment = explanation_.getEntailment();
			List<OWLAxiom> formatting = formattingManager
					.getOrdering(entailment, justification);
			for (OWLAxiom axiom : formatting) {
				int depth = formattingManager.getIndentation(entailment,
						justification, axiom);
				JustificationFrameSectionRow row = new JustificationFrameSectionRow(
						getOWLEditorKit(), this, explanation_, axiom, depth);
				addRow(row);
			}
		}
	}

	@Override
	protected void clear() {
		isFilled_ = false;
	}

	public PrioritizedJustification getJustification() {
		return explanation_.getJustification(justificationIndex_);
	}

	@Override
	public Comparator<OWLFrameSectionRow<Explanation, OWLAxiom, OWLAxiom>> getRowComparator() {
		return null;
	}

	@Override
	public boolean canAdd() {
		return false;
	}

	@Override
	public boolean canAcceptDrop(List<OWLObject> objects) {
		return false;
	}
}