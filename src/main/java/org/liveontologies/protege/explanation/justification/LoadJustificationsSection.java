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

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.editor.OWLObjectEditor;
import org.protege.editor.owl.ui.frame.AbstractOWLFrameSection;
import org.protege.editor.owl.ui.frame.OWLFrame;
import org.protege.editor.owl.ui.frame.OWLFrameSectionRow;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

public class LoadJustificationsSection
		extends AbstractOWLFrameSection<Explanation, OWLAxiom, OWLAxiom> {

	private boolean isFilled_ = false;
	// private final Explanation explanation_;

	public LoadJustificationsSection(OWLEditorKit editorKit,
			OWLFrame<? extends Explanation> owlFrame) {
		super(editorKit, "", owlFrame);
		// explanation_ = owlFrame.getRootObject();
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

	public void updateText(String text) {
		setLabel(text);
		if (getRows().size() > 0)
			((LoadJustificationsSectionRow) getRows().get(0)).setText(text);
	}

	@Override
	protected void refill(OWLOntology ontology) {
		if (isFilled_)
			return;
		isFilled_ = true;

		// LoadJustificationsSectionRow row = new LoadJustificationsSectionRow(
		// getOWLEditorKit(), this, explanation_, explanation_.getEntailment());
		// addRow(row);
	}

	@Override
	protected void clear() {
		isFilled_ = false;
	}
}