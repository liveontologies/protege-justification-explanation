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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

import org.liveontologies.protege.explanation.justification.preferences.JustPrefs;
import org.protege.editor.core.ui.list.MListButton;
import org.protege.editor.core.ui.list.MListItem;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.frame.OWLFrame;
import org.protege.editor.owl.ui.framelist.ExplainButton;
import org.protege.editor.owl.ui.framelist.OWLFrameList;
import org.protege.editor.owl.ui.framelist.OWLFrameListPopupMenuAction;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Author: Matthew Horridge Stanford University Bio-Medical Informatics Research
 * Group Date: 19/03/2012
 */

public class AxiomsFrameList extends OWLFrameList<Explanation<OWLAxiom>> {

	private static final long serialVersionUID = -8844035741045455140L;

	public static final Color COLOR_SINGLE_POPULARITY = new Color(170, 70, 15);
	public static final Color COLOR_MULTI_POPULARITY = new Color(10, 75, 175);
	public static final Color COLOR_ALL_POPULARITY = new Color(6, 133, 19);
	
	public static final Color INFERRED_BG_COLOR = new Color(255, 255, 215);

	private final PresentationManager manager_;
	private final AxiomSelectionModel axiomSelectionModel_;
	private int buttonRunWidth_ = 0;

	public AxiomsFrameList(AxiomSelectionModel axiomSelectionModel,
			PresentationManager manager,
			OWLFrame<Explanation<OWLAxiom>> explanationOWLFrame) {
		super(manager.getOWLEditorKit(), explanationOWLFrame);
		this.manager_ = manager;
		this.axiomSelectionModel_ = axiomSelectionModel;
		OWLEditorKit kit = manager.getOWLEditorKit();
		setWrap(false);
		setCellRenderer(new AxiomsFrameListRenderer(kit));

		Action moveUpAction = new AbstractAction("Move up") {
			private static final long serialVersionUID = -8758870933492900093L;

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

	private void handleMoveUp() {
		OWLAxiom selectedAxiom = getSelectedAxiom();
		if (selectedAxiom == null) {
			return;
		}
		AxiomsFormattingManager formattingManager = AxiomsFormattingManager
				.getInstance();
		int newIndex = formattingManager.moveAxiomUp(getRootObject(),
				selectedAxiom);
		getFrame().setRootObject(getRootObject());
		setSelectedIndex(newIndex + 1);
	}

	private void handleMoveDown() {
		OWLAxiom selectedAxiom = getSelectedAxiom();
		if (selectedAxiom == null) {
			return;
		}
		AxiomsFormattingManager formattingManager = AxiomsFormattingManager
				.getInstance();
		int newIndex = formattingManager.moveAxiomDown(getRootObject(),
				selectedAxiom);
		getFrame().setRootObject(getRootObject());
		setSelectedIndex(newIndex + 1);
	}

	private void handleIncreaseIndentation() {
		OWLAxiom selectedAxiom = getSelectedAxiom();
		if (selectedAxiom == null) {
			return;
		}
		AxiomsFormattingManager formattingManager = AxiomsFormattingManager
				.getInstance();
		formattingManager.increaseIndentation(getRootObject(), selectedAxiom);
		int selIndex = getSelectedIndex();
		getFrame().setRootObject(getRootObject());
		setSelectedIndex(selIndex);
	}

	private void handleDecreaseIndentation() {
		OWLAxiom selectedAxiom = getSelectedAxiom();
		if (selectedAxiom == null) {
			return;
		}
		AxiomsFormattingManager formattingManager = AxiomsFormattingManager
				.getInstance();
		formattingManager.decreaseIndentation(getRootObject(), selectedAxiom);
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
		if (!(element instanceof AxiomsFrameSectionRow)) {
			return null;
		}
		return ((AxiomsFrameSectionRow) element).getAxiom();
	}

	@Override
	protected List<MListButton> getButtons(Object value) {
		if (value instanceof AxiomsFrameSectionRow) {
			List<MListButton> buttons = Arrays.<MListButton> asList(
					new ExplainButton(new AbstractAction() {
						/**
						 * 
						 */
						private static final long serialVersionUID = 4860966076807447714L;

						public void actionPerformed(ActionEvent e) {
							invokeExplanationHandler();
						}
					}));
			buttonRunWidth_ = buttons.size() * (getButtonDimension() + 2) + 20;
			return buttons;
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public void addToPopupMenu(
			OWLFrameListPopupMenuAction<Explanation<OWLAxiom>> explanationOWLFrameListPopupMenuAction) {
		// NO MENU FOR US
	}

	@Override
	protected Color getItemBackgroundColor(MListItem item) {
		if (item instanceof AxiomsFrameSectionRow) {
			AxiomsFrameSectionRow row = (AxiomsFrameSectionRow) item;
			OWLAxiom axiom = row.getAxiom();

			if (!manager_.getOWLEditorKit().getOWLModelManager().getActiveOntology().containsAxiom(axiom))
				return INFERRED_BG_COLOR;

//			int rowIndex = row.getFrameSection().getRowIndex(row) + 1;
//			if (!isSelectedIndex(rowIndex)) {
//				if (axiomSelectionModel_.getSelectedAxioms().contains(axiom)) {
//					return Color.YELLOW;
//				} else {
//					boolean isInAll = true;
//					for (Explanation<?> expl : manager_.getJustifications(
//							getRootObject().getEntailment())) {
//						if (!expl.contains(axiom)) {
//							isInAll = false;
//							break;
//						}
//					}
//					if (isInAll) {
//						return new Color(245, 255, 235);
//					}
//				}
//			}
		}
		return super.getItemBackgroundColor(item);
	}

	@Override
	protected List<MListButton> getListItemButtons(MListItem item) {
		return Collections.emptyList();
	}

	@Override
	protected Border createListItemBorder(JList list, Object value, int index,
			boolean isSelected, boolean cellHasFocus) {
		return super.createListItemBorder(list, value, index, isSelected,
				cellHasFocus);
	}

	@Override
	protected Border createPaddingBorder(JList list, Object value, int index,
			boolean isSelected, boolean cellHasFocus) {
		return super.createPaddingBorder(list, value, index, isSelected,
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
		if (element instanceof AxiomsFrameSectionRow) {
			AttributedString populatiry = getPopularityString(
					isSelectedIndex(index), (AxiomsFrameSectionRow) element);
			String tt = "";
			for (AttributedCharacterIterator i = populatiry.getIterator(); i
					.getIndex() < i.getEndIndex(); i.next())
				tt += i.current();
			return tt;
		}

		return super.getToolTipText(event);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		int size = getModel().getSize();
		for (int i = 0; i < size; i++) {
			Object element = getModel().getElementAt(i);
			if (element instanceof AxiomsFrameSectionRow) {
				AxiomsFrameSectionRow row = (AxiomsFrameSectionRow) element;
				Rectangle rect = getCellBounds(i, i);
				if (rect.intersects(g.getClip().getBounds())) {
					OWLAxiom entailment = getRootObject().getEntailment();
					if (manager_.getComputedExplanationCount(entailment) > 1) {
						AttributedString popularityString = getPopularityString(
								isSelectedIndex(i), row);
						TextLayout textLayout = new TextLayout(
								popularityString.getIterator(),
								g2.getFontRenderContext());
						float advance = textLayout.getAdvance();
						float x = rect.x + rect.width - advance
								- buttonRunWidth_;
						float h = textLayout.getAscent()
								+ textLayout.getDescent();
						float y = ((rect.height - h) / 2) + rect.y
								+ textLayout.getLeading()
								+ textLayout.getAscent();
						if (JustPrefs.create().load().isPopularityShown)
							textLayout.draw(g2, x, y);

						g2.setColor(Color.LIGHT_GRAY);
						TextLayout numberLayout = new TextLayout(i + ")",
								g2.getFont(), g2.getFontRenderContext());
						float numberX = 20 - numberLayout.getAdvance();
						numberLayout.draw(g2, numberX, y);
					}
				}
			}
		}
	}

	private AttributedString getPopularityString(boolean isSelected,
			AxiomsFrameSectionRow row) {
		int popularity = manager_.getPopularity(row.getRoot().getEntailment(),
				row.getAxiom());
		OWLAxiom entailment = row.getRoot().getEntailment();
		int count = manager_.getComputedExplanationCount(entailment);

		StringBuilder sb = new StringBuilder("In ");
		int start = sb.length();
		final Color highlightColor;
		if (popularity <= 1) {
			sb.append("NO");
			highlightColor = COLOR_SINGLE_POPULARITY;
		} else if (popularity != count) {
			sb.append(popularity - 1);
			highlightColor = COLOR_MULTI_POPULARITY;
		} else {
			sb.append("ALL");
			highlightColor = COLOR_ALL_POPULARITY;
		}
		int end = sb.length();
		sb.append(" other justifications");
		AttributedString as = new AttributedString(sb.toString());
		as.addAttribute(TextAttribute.FOREGROUND, Color.LIGHT_GRAY, 0,
				sb.length());
		if (!isSelected) {
			as.addAttribute(TextAttribute.FOREGROUND, highlightColor, start,
					end);
		}
		return as;
	}
}