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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.liveontologies.protege.explanation.justification.service.ComputationService;
import org.liveontologies.protege.explanation.justification.service.ComputationServiceListener;
import org.protege.editor.core.Disposable;
import org.protege.editor.core.ProtegeManager;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.event.OWLModelManagerChangeEvent;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationException;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Date: 04-Oct-2008
 * 
 * The component that displays a set of justification
 */

public class PresentationPanel extends JPanel implements Disposable, OWLModelManagerListener,
		EntailmentSelectionListener, AxiomSelectionModel, ExplanationManagerListener, ComputationServiceListener {

	private final OWLEditorKit kit;
	private final PresentationManager manager;
	private JComponent explanationDisplayHolder;
	private JComponent serviceSettingsDisplayHolder;
	private JScrollPane scrollPane;
	private Collection<AxiomsDisplay> panels;
	private JComponent headerPanel;

	private List<Explanation<OWLAxiom>> lastExplanations;
	private JButton bAdd;

	private AxiomSelectionModelImpl selectionModel;
	private static final Logger logger = LoggerFactory.getLogger(PresentationPanel.class);

	public PresentationPanel(JustificationComputationServiceManager manager, OWLAxiom entailment) {
		this(new PresentationManager(ProtegeManager.getInstance().getFrame(manager.getOWLEditorKit().getWorkspace()),
				manager, entailment));
	}

	public PresentationPanel(PresentationManager manager) {
		this.manager = manager;
		manager.setComputationServiceListener(this);
		this.kit = this.manager.getOWLEditorKit();
		setLayout(new GridBagLayout());

		Collection<ComputationService> services = manager.getServices();
		switch (services.size()) {
		case 0:
			break;
		case 1:
			manager.selectService(services.iterator().next());
			JLabel label = new JLabel("Using " + manager.getSelectedService() + " as a computation service");
			add(label, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST,
					GridBagConstraints.HORIZONTAL, new Insets(2, 0, 2, 0), 0, 0));
			break;
		default:
			JComboBox<ComputationService> selector = new JComboBox<ComputationService>();
			ComputationService serviceToSelect = services.iterator().next();
			for (ComputationService service : services)
				if (service.canComputeJustification(manager.getEntailment())) {
					selector.addItem(service);
					if (JustificationComputationServiceManager.lastChoosenServiceId == manager.getIdForService(service))
						serviceToSelect = service;
				}
			selector.setSelectedItem(serviceToSelect);
			manager.selectService(serviceToSelect);
			selector.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					manager.selectService((ComputationService) selector.getSelectedItem());
					updateSettingsPanel();
					manager.clearJustificationsCache();
					createHeaderPanel();
					recompute();
				}
			});
			add(selector, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST,
					GridBagConstraints.HORIZONTAL, new Insets(2, 0, 2, 0), 0, 0));
		}

		selectionModel = new AxiomSelectionModelImpl();
		panels = new ArrayList<>();
		kit.getModelManager().addListener(this);

		serviceSettingsDisplayHolder = new JPanel(new BorderLayout());
		add(serviceSettingsDisplayHolder, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 0, 2, 0), 0, 0));

		headerPanel = new JPanel(new BorderLayout());
		createHeaderPanel();
		explanationDisplayHolder = new Box(BoxLayout.Y_AXIS);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(explanationDisplayHolder);
		panel.add(headerPanel);
		JPanel holder = new HolderPanel(new BorderLayout());
		holder.add(panel, BorderLayout.NORTH);
		scrollPane = new JScrollPane(holder);
		scrollPane.setBorder(null);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.getViewport().setBackground(null);
		scrollPane.setOpaque(false);
		JPanel explanationListPanel = new JPanel(new BorderLayout());
		explanationListPanel.add(scrollPane);
		explanationListPanel.setMinimumSize(new Dimension(10, 10));
		add(explanationListPanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST,
		GridBagConstraints.BOTH, new Insets(2, 0, 2, 0), 0, 0));

		recompute();
	}

	private JComponent createHeaderPanel() {
		headerPanel.removeAll();

		bAdd = new JButton(getIncrementString());
		bAdd.setBorder(new CompoundBorder(bAdd.getBorder(), new EmptyBorder(5, 5, 5, 5)));
		bAdd.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 0, 5), bAdd.getBorder()));
		headerPanel.add(bAdd);
		bAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updatePanel(manager.getPresentationSettings().getIncrement());
			}
		});
		PresentationPreferencesPanel.addListener(new PresentationPreferencesPanel.PreferencesListener() {
			@Override
			public void valueChanged() {
				bAdd.setText(getIncrementString());
			}
		});

		return headerPanel;
	}
	
	private String getIncrementString()
	{
		if (lastExplanations == null)
			return "Show next " + manager.getPresentationSettings().getIncrement() + " justifications";
		int inc = Math.min(manager.getPresentationSettings().getIncrement(),
				lastExplanations.size() - manager.getPresentationSettings().getCurrentCount());
		return "Show next " + inc + " justifications of " + lastExplanations.size() + " in total";
	}

	private void updateHeaderPanel() {
		int current = manager.getPresentationSettings().getCurrentCount();
		int all = lastExplanations.size();
		
		String sAll = lastExplanations.size() + " justification" + (all == 1 ? " is shown." : "s are shown.");

		if (current != all) {
			bAdd.setText(getIncrementString());
			headerPanel.validate();
		} else {
			headerPanel.removeAll();
			JLabel explanationsCountLabel = new JLabel("All " + sAll);
			headerPanel.add(explanationsCountLabel, BorderLayout.CENTER);
			headerPanel.validate();
		}
	}

	public Dimension getMinimumSize() {
		return new Dimension(10, 10);
	}

	public void explanationLimitChanged(PresentationManager presentationManager) {
		selectionChanged();
	}

	public void explanationsComputed(OWLAxiom entailment) {
	}

	private class HolderPanel extends JPanel implements Scrollable {

		public HolderPanel(LayoutManager layout) {
			super(layout);
			setOpaque(false);
		}

		public Dimension getPreferredScrollableViewportSize() {
			return super.getPreferredSize();
		}

		public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
			return 30;
		}

		public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
			return 30;
		}

		public boolean getScrollableTracksViewportWidth() {
			return true;
		}

		public boolean getScrollableTracksViewportHeight() {
			return false;
		}
	}

	public void selectionChanged() {
		recompute();
	}

	@Override
	public void redrawingCalled() {
		manager.clearJustificationsCache();
		recompute();
	}

	private void updateSettingsPanel() {
		JPanel settingsPanel = manager.getSelectedService().getSettingsPanel();
		serviceSettingsDisplayHolder.removeAll();
		if (settingsPanel != null)
			serviceSettingsDisplayHolder.add(settingsPanel, BorderLayout.WEST);
		serviceSettingsDisplayHolder.validate();
	}

	private void recompute() {
		try {
			panels.forEach(AxiomsDisplay::dispose);
			explanationDisplayHolder.removeAll();
			explanationDisplayHolder.validate();

			lastExplanations = getOrderedExplanations(manager.getJustifications());
			manager.getPresentationSettings().setCurrentCount(0);

			updateHeaderPanel();

			updatePanel(manager.getPresentationSettings().getInitialAmount());
		} catch (ExplanationException e) {
			logger.error("An error occurred whilst computing explanations: {}", e.getMessage(), e);
		}
	}

	private void updatePanel() {
		updatePanel(lastExplanations.size() - manager.getPresentationSettings().getCurrentCount());
	}

	private void updatePanel(int diff) {
		PresentationSettings settings = manager.getPresentationSettings();
		int maxCnt = Math.min(settings.getCurrentCount() + diff, lastExplanations.size());

		for (int explNum = settings.getCurrentCount() + 1; explNum <= maxCnt; explNum++) {
			Explanation<OWLAxiom> explanation = lastExplanations.get(explNum - 1);
			final AxiomsDisplay display = new AxiomsDisplay(manager, this, explanation);
			AxiomsDisplayList displayList = new AxiomsDisplayList(display, explNum);
			displayList.setBorder(BorderFactory.createEmptyBorder(2, 0, 10, 0));
			explanationDisplayHolder.add(displayList);
			panels.add(display);
		}
		settings.setCurrentCount(maxCnt);
		updateHeaderPanel();

		scrollPane.validate();
	}

	protected List<Explanation<OWLAxiom>> getOrderedExplanations(Set<Explanation<OWLAxiom>> explanations) {
		List<Explanation<OWLAxiom>> orderedExplanations = new ArrayList<>();
		orderedExplanations.addAll(explanations);
		Collections.sort(orderedExplanations, new Comparator<Explanation<OWLAxiom>>() {
			public int compare(Explanation<OWLAxiom> o1, Explanation<OWLAxiom> o2) {
				int diff = getAxiomTypes(o1).size() - getAxiomTypes(o2).size();
				if (diff != 0) {
					return diff;
				}
				diff = getClassExpressionTypes(o1).size() - getClassExpressionTypes(o2).size();
				if (diff != 0) {
					return diff;
				}
				return o1.getSize() - o2.getSize();
			}
		});
		return orderedExplanations;
	}

	private Set<AxiomType<?>> getAxiomTypes(Explanation<OWLAxiom> explanation) {
		Set<AxiomType<?>> result = new HashSet<>();
		for (OWLAxiom ax : explanation.getAxioms()) {
			result.add(ax.getAxiomType());
		}
		return result;
	}

	private Set<ClassExpressionType> getClassExpressionTypes(Explanation<OWLAxiom> explanation) {
		Set<ClassExpressionType> result = new HashSet<>();
		for (OWLAxiom ax : explanation.getAxioms()) {
			result.addAll(ax.getNestedClassExpressions().stream().map(OWLClassExpression::getClassExpressionType)
					.collect(Collectors.toList()));
		}
		return result;
	}

	public void dispose() {
		kit.getModelManager().removeListener(this);
		for (AxiomsDisplay panel : panels) {
			panel.dispose();
		}
		selectionModel.dispose();
	}

	public void handleChange(OWLModelManagerChangeEvent event) {

	}

	public void addAxiomSelectionListener(AxiomSelectionListener lsnr) {
		selectionModel.addAxiomSelectionListener(lsnr);
	}

	public void removeAxiomSelectionListener(AxiomSelectionListener lsnr) {
		selectionModel.removeAxiomSelectionListener(lsnr);
	}

	public void setAxiomSelected(OWLAxiom axiom, boolean b) {
		selectionModel.setAxiomSelected(axiom, b);
	}

	public Set<OWLAxiom> getSelectedAxioms() {
		return selectionModel.getSelectedAxioms();
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension workspaceSize = kit.getWorkspace().getSize();
		int width = (int) (workspaceSize.getWidth() * 0.8);
		int height = (int) (workspaceSize.getHeight() * 0.7);
		return new Dimension(width, height);
	}
}