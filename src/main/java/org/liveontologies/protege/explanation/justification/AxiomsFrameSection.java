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

public class AxiomsFrameSection extends
		AbstractOWLFrameSection<Justification<OWLAxiom>, OWLAxiom, OWLAxiom> {

	private static final String LABEL = "";

	private boolean isFilled_ = false;

	public AxiomsFrameSection(OWLEditorKit editorKit,
			OWLFrame<? extends Justification<OWLAxiom>> owlFrame) {
		super(editorKit, LABEL, owlFrame);
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

		AxiomsFormattingManager formattingManager = AxiomsFormattingManager
				.getInstance();
		Justification<OWLAxiom> justification = getRootObject();
		List<OWLAxiom> formatting = formattingManager
				.getOrdering(justification);
		for (OWLAxiom axiom : formatting) {
			int depth = formattingManager.getIndentation(justification, axiom);
			AxiomsFrameSectionRow row = new AxiomsFrameSectionRow(
					getOWLEditorKit(), this, justification, axiom, depth);
			addRow(row);
		}
	}

	@Override
	protected void clear() {
		isFilled_ = false;
	}

	public Comparator<OWLFrameSectionRow<Justification<OWLAxiom>, OWLAxiom, OWLAxiom>> getRowComparator() {
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