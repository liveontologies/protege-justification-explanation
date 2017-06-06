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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.liveontologies.protege.explanation.justification.preferences.JustificationPreferencesGeneralPanel;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.editor.OWLObjectEditor;
import org.protege.editor.owl.ui.frame.AbstractOWLFrameSectionRow;
import org.protege.editor.owl.ui.frame.OWLFrameSection;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObject;

public class LoadJustificationsSectionRow
		extends AbstractOWLFrameSectionRow<Explanation, OWLAxiom, OWLAxiom> {

	private String text_ = "Double click to show next";

	protected LoadJustificationsSectionRow(OWLEditorKit owlEditorKit,
			OWLFrameSection<Explanation, OWLAxiom, OWLAxiom> section,
			Explanation rootObject, OWLAxiom axiom) {
		super(owlEditorKit, section, null, rootObject, axiom);
	}

	@Override
	public List<? extends OWLObject> getManipulatableObjects() {
		return new ArrayList<OWLAxiom>();
	}

	@Override
	protected OWLObjectEditor<OWLAxiom> getObjectEditor() {
		return null;
	}

	@Override
	protected OWLAxiom createAxiom(OWLAxiom editedObject) {
		return null;
	}

	@Override
	public String getRendering() {
		return text_;
	}

	public void setText(String text) {
		text_ = text;
	}
}