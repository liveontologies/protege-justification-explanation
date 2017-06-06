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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;

import org.protege.editor.core.ui.list.MListButton;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.editor.OWLObjectEditor;
import org.protege.editor.owl.ui.frame.AbstractOWLFrameSectionRow;
import org.protege.editor.owl.ui.frame.OWLFrameSection;
import org.protege.editor.owl.ui.framelist.ExplainButton;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Author: Matthew Horridge Stanford University Bio-Medical Informatics Research
 * Group Date: 19/03/2012
 */

public class AxiomsFrameSectionRow
		extends AbstractOWLFrameSectionRow<Explanation, OWLAxiom, OWLAxiom> {

	private int depth_;
	private List<MListButton> buttons_;

	public AxiomsFrameSectionRow(OWLEditorKit owlEditorKit,
			OWLFrameSection<Explanation, OWLAxiom, OWLAxiom> section,
			Explanation rootObject, OWLAxiom axiom, int depth) {
		super(owlEditorKit, section, getOntologyForAxiom(owlEditorKit, axiom),
				rootObject, axiom);
		depth_ = depth;
		buttons_ = null;
	}

	public int getDepth() {
		return depth_;
	}

	private static OWLOntology getOntologyForAxiom(OWLEditorKit editorKit,
			OWLAxiom axiom) {
		return null;
	}

	@Override
	public String getRendering() {
		String rendering = super.getRendering().replaceAll("\\s", " ");
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < depth_; i++) {
			builder.append("        ");
		}
		builder.append(rendering);
		return builder.toString();
	}

	@Override
	public List<MListButton> getAdditionalButtons() {
		return Collections.emptyList();
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
	public List<? extends OWLObject> getManipulatableObjects() {
		return Arrays.asList(getAxiom());
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public boolean isDeleteable() {
		return true;
	}

	@Override
	public boolean isInferred() {
		return false;
	}

	public List<MListButton> getButtons() {
		return buttons_;
	}

	public void setButtons(List<MListButton> buttons) {
		buttons_ = buttons;
	}
}