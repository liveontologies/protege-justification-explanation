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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.protege.editor.core.Disposable;
import org.protege.editor.core.ui.list.MListButton;
import org.protege.editor.core.ui.list.MListItem;
import org.protege.editor.core.ui.list.MListSectionHeader;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.framelist.ExplainButton;
import org.protege.editor.owl.ui.framelist.OWLFrameList;
import org.protege.editor.owl.ui.framelist.OWLFrameListPopupMenuAction;
import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Author: Matthew Horridge Stanford University Bio-Medical Informatics Research
 * Group Date: 19/03/2012
 */

public class JustificationFrameList extends OWLFrameList<Explanation>
		implements AxiomSelectionListener, Disposable {

	private static final long serialVersionUID = -8844035741045455140L;

	private static final Color INFERRED_BG_COLOR_ = new Color(255, 255, 215);
	private static final Color HIGHLIGHT_COLOR_ = new Color(230, 215, 246);

	private static final int AXIOM_INDENT_ = 30;

	private final JustificationManager manager_;
	private final AxiomSelectionModel axiomSelectionModel_;
	private final ShowMoreListener showMoreListener_;
	private final Explanation explanation_;
	private final JustificationFrame frame_;
	private boolean isTransmittingSelectionToModel_ = false;

	public JustificationFrameList(AxiomSelectionModel axiomSelectionModel,
			JustificationManager manager, ShowMoreListener showMoreListener,
			Explanation explanation) {
		this(axiomSelectionModel,
				manager, new JustificationFrame(manager.getOwlEditorKit(),
						explanation, showMoreListener),
				showMoreListener, explanation);
	}

	private JustificationFrameList(AxiomSelectionModel axiomSelectionModel,
			JustificationManager manager, JustificationFrame frame,
			ShowMoreListener showMoreListener, Explanation explanation) {
		super(manager.getOwlEditorKit(), frame);
		frame_ = frame;
		manager_ = manager;
		axiomSelectionModel_ = axiomSelectionModel;
		showMoreListener_ = showMoreListener;
		explanation_ = explanation;
		OWLEditorKit kit = manager.getOwlEditorKit();
		setWrap(false);
		setCellRenderer(new JustificationFrameListRenderer(kit));

		getSelectionModel()
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

		Action moveUpAction = new AbstractAction("Move up") {
			private static final long serialVersionUID = -8758870933492900093L;

			@Override
			public void actionPerformed(ActionEvent e) {
				handleMoveUp();
			}
		};
		getActionMap().put(moveUpAction.getValue(Action.NAME), moveUpAction);
		getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_MASK),
				moveUpAction.getValue(Action.NAME));

		Action moveDownAction = new AbstractAction("Move down") {
			private static final long serialVersionUID = -7554058930748542853L;

			@Override
			public void actionPerformed(ActionEvent e) {
				handleMoveDown();
			}
		};
		getActionMap().put(moveDownAction.getValue(Action.NAME),
				moveDownAction);
		getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.CTRL_MASK),
				moveDownAction.getValue(Action.NAME));

		Action increaseIndentation = new AbstractAction(
				"Increase indentation") {
			private static final long serialVersionUID = 3264353432939432586L;

			@Override
			public void actionPerformed(ActionEvent e) {
				handleIncreaseIndentation();
			}
		};
		getActionMap().put(increaseIndentation.getValue(Action.NAME),
				increaseIndentation);
		getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.CTRL_MASK),
				increaseIndentation.getValue(Action.NAME));

		Action decreaseIndentation = new AbstractAction(
				"decrease indentation") {
			private static final long serialVersionUID = 4625665722123561472L;

			@Override
			public void actionPerformed(ActionEvent e) {
				handleDecreaseIndentation();
			}
		};
		getActionMap().put(decreaseIndentation.getValue(Action.NAME),
				decreaseIndentation);
		getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.CTRL_MASK),
				decreaseIndentation.getValue(Action.NAME));
	}

	public void addJustification(Justification justification,
			int justificationNo) {
		int index = explanation_.addJustification(justification);
		frame_.addSection(index,
				String.format("Justification %s with %d axioms",
						justificationNo, justification.getSize()));
		validate();
	}

	private void respondToAxiomSelectionChange() {
		if (!isTransmittingSelectionToModel_) {
			clearSelection();
			repaint(getVisibleRect());
		}
		repaint(getVisibleRect());
	}

	private void transmitSelectionToModel() {
		try {
			isTransmittingSelectionToModel_ = true;
			axiomSelectionModel_.clearSelection();
			for (int i = 0; i < getModel().getSize(); i++) {
				Object element = getModel().getElementAt(i);
				if (element instanceof JustificationFrameSectionRow)
					if (isSelectedIndex(i)) {
						JustificationFrameSectionRow row = (JustificationFrameSectionRow) element;
						OWLAxiom ax = row.getAxiom();
						axiomSelectionModel_.setAxiomSelected(ax, true);
					}
			}
		} finally {
			isTransmittingSelectionToModel_ = false;
		}
	}

	public void clear() {
		frame_.clear();
	}

	public void setNextSectionVisibility(boolean isVisible) {
		frame_.setNextSectionVisibility(isVisible);
	}

	public boolean getNextSectionVisibility() {
		return frame_.getNextSectionVisibility();
	}

	@Override
	public void axiomAdded(AxiomSelectionModel source, OWLAxiom axiom) {
		System.out.println("SEL: " + axiom);
	}

	@Override
	public void axiomRemoved(AxiomSelectionModel source, OWLAxiom axiom) {
	}

	private void handleMoveUp() {
		OWLAxiom selectedAxiom = getSelectedAxiom();
		if (selectedAxiom == null) {
			return;
		}
		JustificationFormattingManager formattingManager = JustificationFormattingManager
				.getInstance();
		boolean hasMoved = formattingManager.moveAxiomUp(
				manager_.getEntailment(), getSelectedJustification(),
				selectedAxiom);
		int selIndex = getSelectedIndex();
		getFrame().setRootObject(getRootObject());
		setSelectedIndex(selIndex - (hasMoved ? 1 : 0));
	}

	private void handleMoveDown() {
		OWLAxiom selectedAxiom = getSelectedAxiom();
		if (selectedAxiom == null) {
			return;
		}
		JustificationFormattingManager formattingManager = JustificationFormattingManager
				.getInstance();
		boolean hasMoved = formattingManager.moveAxiomDown(
				manager_.getEntailment(), getSelectedJustification(),
				selectedAxiom);
		int selIndex = getSelectedIndex();
		getFrame().setRootObject(getRootObject());
		setSelectedIndex(selIndex + (hasMoved ? 1 : 0));
	}

	private void handleIncreaseIndentation() {
		OWLAxiom selectedAxiom = getSelectedAxiom();
		if (selectedAxiom == null) {
			return;
		}
		JustificationFormattingManager formattingManager = JustificationFormattingManager
				.getInstance();
		formattingManager.increaseIndentation(manager_.getEntailment(),
				getSelectedJustification(), selectedAxiom);
		int selIndex = getSelectedIndex();
		getFrame().setRootObject(getRootObject());
		setSelectedIndex(selIndex);
	}

	private void handleDecreaseIndentation() {
		OWLAxiom selectedAxiom = getSelectedAxiom();
		if (selectedAxiom == null) {
			return;
		}
		JustificationFormattingManager formattingManager = JustificationFormattingManager
				.getInstance();
		formattingManager.decreaseIndentation(manager_.getEntailment(),
				getSelectedJustification(), selectedAxiom);
		int selIndex = getSelectedIndex();
		getFrame().setRootObject(getRootObject());
		setSelectedIndex(selIndex);
	}

	private OWLAxiom getSelectedAxiom() {
		int selectedIndex = getSelectedIndex();
		if (selectedIndex == -1) {
			return null;
		}
		Object element = getModel().getElementAt(selectedIndex);
		if (!(element instanceof JustificationFrameSectionRow)) {
			return null;
		}
		return ((JustificationFrameSectionRow) element).getAxiom();
	}

	private Justification getSelectedJustification() {
		int selectedIndex = getSelectedIndex();
		if (selectedIndex == -1) {
			return null;
		}
		Object element = getModel().getElementAt(selectedIndex);
		if (!(element instanceof JustificationFrameSectionRow)) {
			return null;
		}
		return ((JustificationFrameSection) ((JustificationFrameSectionRow) element)
				.getFrameSection()).getJustification();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Color oldColor = g.getColor();
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(oldColor);
	}

	@Override
	protected List<MListButton> getButtons(Object value) {
		if (value instanceof JustificationFrameSectionRow) {
			if (((JustificationFrameSectionRow) value).getButtons() == null)
				((JustificationFrameSectionRow) value)
						.setButtons(createAxiomsRowButtons());
			return ((JustificationFrameSectionRow) value).getButtons();
		}
		if (value instanceof MListSectionHeader)
			if (value instanceof LoadJustificationsSection) {
				return Collections.singletonList(
						((LoadJustificationsSection) value).getButton());
			}
		return Collections.emptyList();
	}

	@Override
	public void addToPopupMenu(
			OWLFrameListPopupMenuAction<Explanation> justificationOWLFrameListPopupMenuAction) {
		// NO MENU FOR US
	}

	@Override
	protected Color getItemBackgroundColor(MListItem item) {
		if (item instanceof JustificationFrameSectionRow) {
			JustificationFrameSectionRow row = (JustificationFrameSectionRow) item;
			OWLAxiom axiom = row.getAxiom();

			if (!manager_.getOwlEditorKit().getOWLModelManager()
					.getActiveOntology().containsAxiom(axiom))
				return INFERRED_BG_COLOR_;

			int rowIndex = row.getFrameSection().getRowIndex(row) + 1;
			if (!isSelectedIndex(rowIndex))
				if (axiomSelectionModel_.getSelectedAxioms().contains(axiom))
					return HIGHLIGHT_COLOR_;
		}
		return super.getItemBackgroundColor(item);
	}

	@Override
	protected List<MListButton> getListItemButtons(MListItem item) {
		if (item instanceof JustificationFrameSectionRow) {
			if (((JustificationFrameSectionRow) item).getButtons() == null)
				((JustificationFrameSectionRow) item)
						.setButtons(createAxiomsRowButtons());
			return ((JustificationFrameSectionRow) item).getButtons();
		}
		return Collections.emptyList();
	}

	private List<MListButton> createAxiomsRowButtons() {
		return Arrays
				.<MListButton> asList(new ExplainButton(new AbstractAction() {
					private static final long serialVersionUID = 4860966076807447714L;

					@Override
					public void actionPerformed(ActionEvent e) {
						invokeExplanationHandler();
					}
				}));
	}

	@Override
	protected Border createListItemBorder(JList list, Object value, int index,
			boolean isSelected, boolean cellHasFocus) {
		if (value instanceof JustificationFrameSectionRow) {
			Insets insets = super.createListItemBorder(list, value, index,
					isSelected, cellHasFocus).getBorderInsets(this);
			return BorderFactory.createMatteBorder(insets.top,
					((JustificationFrameSectionRow) value).getDepth()
							* AXIOM_INDENT_,
					insets.bottom, insets.right, list.getBackground());
		}
		return super.createListItemBorder(list, value, index, isSelected,
				cellHasFocus);
	}

	@Override
	public String getToolTipText(MouseEvent event) {
		if (event == null)
			return super.getToolTipText();

		Point location = event.getPoint();
		int index = locationToIndex(location);
		if (index < 0 || !getCellBounds(index, index).contains(location))
			return super.getToolTipText();

		Object element = getModel().getElementAt(index);
		if (element instanceof JustificationFrameSectionRow)
			return getPopularityString((JustificationFrameSectionRow) element);

		if (element instanceof LoadJustificationsSection)
			return showMoreListener_.getIncrementString();

		return super.getToolTipText(event);
	}

	private String getPopularityString(JustificationFrameSectionRow row) {
		int popularity = manager_.getPopularity(row.getAxiom());
		int count = manager_.getRemainingJustificationCount();
		if (popularity == 1) {
			return "Axiom appears only in THIS justification";
		} else if (popularity == count) {
			return "Axiom appears in ALL justifications";
		} else {
			return String.format("Axiom appears in %s justifications",
					popularity);
		}
	}
}