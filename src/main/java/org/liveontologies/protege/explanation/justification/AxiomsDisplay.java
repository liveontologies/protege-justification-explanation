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


import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.protege.editor.core.Disposable;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;
/*
 * Copyright (C) 2008, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/**
 * Author: Matthew Horridge
 * The University Of Manchester
 * Information Management Group
 * Date: 03-Oct-2008
 *
 * An interface to a component that can display justifications.
 */

public class AxiomsDisplay extends JPanel implements AxiomSelectionListener, Disposable {

	private Explanation<OWLAxiom> explanation;
	private AxiomsFrame frame;
	private final AxiomsFrameList frameList;
	private AxiomSelectionModel axiomSelectionModel;
	private boolean transmittingSelectionToModel = false;

	public AxiomsDisplay(PresentationManager manager, AxiomSelectionModel selectionModel,
			Explanation<OWLAxiom> explanation) {
		OWLEditorKit editorKit = manager.getOWLEditorKit();
		this.axiomSelectionModel = selectionModel;
		this.explanation = explanation;
		frame = new AxiomsFrame(editorKit);
		setLayout(new BorderLayout());
		frameList = new AxiomsFrameList(selectionModel, manager, frame);
		add(frameList, BorderLayout.NORTH);
		frame.setRootObject(explanation);
		frameList.setBorder(BorderFactory.createEmptyBorder(7, 10, 7, 10));

		frameList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				transmitSelectionToModel();
			}
		});

		axiomSelectionModel.addAxiomSelectionListener(new AxiomSelectionListener() {
			public void axiomAdded(AxiomSelectionModel source, OWLAxiom axiom) {
				respondToAxiomSelectionChange();
			}

			public void axiomRemoved(AxiomSelectionModel source, OWLAxiom axiom) {
				respondToAxiomSelectionChange();
			}
		});
	}

	private void respondToAxiomSelectionChange() {
		if (!transmittingSelectionToModel) {
			frameList.clearSelection();
			frameList.repaint(frameList.getVisibleRect());
		}
		frameList.repaint(frameList.getVisibleRect());
	}

	private void transmitSelectionToModel() {
		try {
			transmittingSelectionToModel = true;
			for (int i = 1; i < frameList.getModel().getSize(); i++) {
				Object element = frameList.getModel().getElementAt(i);
				if (element instanceof AxiomsFrameSectionRow) {
					AxiomsFrameSectionRow row = (AxiomsFrameSectionRow) element;
					OWLAxiom ax = row.getAxiom();
					axiomSelectionModel.setAxiomSelected(ax, frameList.isSelectedIndex(i));
				}
			}
		} finally {
			transmittingSelectionToModel = false;
		}
	}

	public Explanation<OWLAxiom> getExplanation() {
		return explanation;
	}

	public void dispose() {
		frame.dispose();
		frameList.dispose();
	}

	@Override
	public void axiomAdded(AxiomSelectionModel source, OWLAxiom axiom) {
		System.out.println("SEL: " + axiom);
	}

	@Override
	public void axiomRemoved(AxiomSelectionModel source, OWLAxiom axiom) {
	}
}
