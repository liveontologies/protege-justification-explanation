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
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.protege.editor.core.Disposable;
import org.protege.editor.owl.OWLEditorKit;
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
 * Author: Matthew Horridge The University Of Manchester Information Management
 * Group Date: 03-Oct-2008
 *
 * An interface to a component that can display justifications.
 */

public class AxiomsDisplay extends JPanel
		implements AxiomSelectionListener, Disposable {

	private static final long serialVersionUID = -3309833245922441823L;

	private final AxiomsFrame frame_;
	private final AxiomsFrameList frameList_;
	private final AxiomSelectionModel axiomSelectionModel_;
	private boolean isTransmittingSelectionToModel_ = false;
	private final Explanation explanation_;

	public AxiomsDisplay(PresentationManager manager,
			AxiomSelectionModel selectionModel, OWLAxiom entailment) {
		OWLEditorKit editorKit = manager.getOWLEditorKit();
		axiomSelectionModel_ = selectionModel;
		explanation_ = new Explanation(entailment);
		frame_ = new AxiomsFrame(editorKit);
		setLayout(new BorderLayout());
		frame_.setRootObject(explanation_);
		frameList_ = new AxiomsFrameList(selectionModel, manager, frame_);
		add(frameList_, BorderLayout.NORTH);
		frameList_.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		frameList_.getSelectionModel()
				.addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						transmitSelectionToModel();
					}
				});

		axiomSelectionModel_
				.addAxiomSelectionListener(new AxiomSelectionListener() {
					@Override
					public void axiomAdded(AxiomSelectionModel source,
							OWLAxiom axiom) {
						respondToAxiomSelectionChange();
					}

					@Override
					public void axiomRemoved(AxiomSelectionModel source,
							OWLAxiom axiom) {
						respondToAxiomSelectionChange();
					}
				});
	}

	public void addJustification(Justification<OWLAxiom> justification,
			int justificationNo) {
		int index = explanation_.addJustification(justification);
		frame_.addSection(index,
				String.format("Justification %s with %d axioms",
						justificationNo, justification.getSize()));
		frameList_.validate();
	}

	public void clear() {
		frame_.clear();
	}

	private void respondToAxiomSelectionChange() {
		if (!isTransmittingSelectionToModel_) {
			frameList_.clearSelection();
			frameList_.repaint(frameList_.getVisibleRect());
		}
		frameList_.repaint(frameList_.getVisibleRect());
	}

	private void transmitSelectionToModel() {
		try {
			isTransmittingSelectionToModel_ = true;
			for (int i = 1; i < frameList_.getModel().getSize(); i++) {
				Object element = frameList_.getModel().getElementAt(i);
				if (element instanceof AxiomsFrameSectionRow) {
					AxiomsFrameSectionRow row = (AxiomsFrameSectionRow) element;
					OWLAxiom ax = row.getAxiom();
					axiomSelectionModel_.setAxiomSelected(ax,
							frameList_.isSelectedIndex(i));
				}
			}
		} finally {
			isTransmittingSelectionToModel_ = false;
		}
	}

	@Override
	public void dispose() {
		frame_.dispose();
		frameList_.dispose();
	}

	@Override
	public void axiomAdded(AxiomSelectionModel source, OWLAxiom axiom) {
		System.out.println("SEL: " + axiom);
	}

	@Override
	public void axiomRemoved(AxiomSelectionModel source, OWLAxiom axiom) {
	}
}