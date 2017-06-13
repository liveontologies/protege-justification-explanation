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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.liveontologies.protege.explanation.justification.preferences.JustPrefPanel;
import org.liveontologies.protege.explanation.justification.preferences.JustificationPreferencesGeneralPanel;
import org.liveontologies.protege.explanation.justification.service.ComputationService;
import org.liveontologies.protege.explanation.justification.service.ComputationServiceListener;
import org.protege.editor.core.Disposable;
import org.protege.editor.core.ProtegeManager;
import org.protege.editor.core.plugin.PluginUtilities;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.event.OWLModelManagerChangeEvent;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLRuntimeException;
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
 * Author: Matthew Horridge The University Of Manchester Information Management
 * Group Date: 04-Oct-2008
 * 
 * The component that displays a set of justification
 */

public class PresentationPanel extends JPanel implements Disposable,
		AxiomSelectionModel, ComputationServiceListener, ShowMoreListener {

	private static final long serialVersionUID = 5025702425365703918L;

	private static final Logger logger = LoggerFactory
			.getLogger(PresentationPanel.class);

	private final OWLEditorKit kit_;
	private final PresentationManager manager_;
	private final JScrollPane scrollPane_;
	private final JComponent serviceSettingsDisplayHolder_;
	private final AxiomsFrameList frameList_;
	private PriorityQueue<Justification<OWLAxiom>> displayedJustifications_;
	private JLabel lNumberInfo_;
	private final AxiomSelectionModelImpl selectionModel_;

	public PresentationPanel(JustificationComputationServiceManager manager,
			OWLAxiom entailment) {
		this(new PresentationManager(
				ProtegeManager.getInstance()
						.getFrame(manager.getOWLEditorKit().getWorkspace()),
				manager, entailment));
	}

	public PresentationPanel(PresentationManager manager) {
		this.manager_ = manager;
		manager.setComputationServiceListener(this);
		this.kit_ = this.manager_.getOWLEditorKit();

		selectionModel_ = new AxiomSelectionModelImpl();

		setLayout(new BorderLayout());

		Box headerPanel = new Box(BoxLayout.Y_AXIS);

		JPanel panel1 = new JPanel(new BorderLayout());

		boolean bExplPrefExtPointExists = PluginUtilities.getInstance()
				.getExtensionRegistry()
				.getExtensionPoint("org.protege.editor.core.application",
						"explanationpreferencespanel") == null;
		if (bExplPrefExtPointExists) {
			JButton b = new JButton("�");
			b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						JustPrefPanel prefPanel = new JustPrefPanel();
						prefPanel.setup(prefPanel.getLabel(),
								manager.getOWLEditorKit());
						prefPanel.initialise();
						Dimension screenSize = Toolkit.getDefaultToolkit()
								.getScreenSize();
						int margin = 100;
						int prefWidth = Math.min(screenSize.width - margin,
								850);
						int prefHeight = Math.min(screenSize.height - margin,
								600);
						prefPanel.setPreferredSize(
								new Dimension(prefWidth, prefHeight));
						JOptionPane op = new JOptionPane(prefPanel,
								JOptionPane.PLAIN_MESSAGE,
								JOptionPane.DEFAULT_OPTION);
						JDialog dlg = op.createDialog(
								manager.getOWLEditorKit().getWorkspace(),
								"Justification preferences");
						dlg.setResizable(true);
						dlg.setVisible(true);
						Object o = op.getValue();
						if (o != null)
							if ((Integer) o == JOptionPane.OK_OPTION)
								prefPanel.applyChanges();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
			panel1.add(b, BorderLayout.EAST);
		}

		Collection<ComputationService> services = manager.getServices();
		switch (services.size()) {
		case 0:
			break;
		case 1:
			manager.selectService(services.iterator().next());
			JLabel label = new JLabel("Using " + manager.getSelectedService()
					+ " as a computation service");
			panel1.add(label, BorderLayout.EAST);
			break;
		default:
			JComboBox<ComputationService> selector = new JComboBox<ComputationService>();
			ComputationService serviceToSelect = services.iterator().next();
			for (ComputationService service : services)
				if (service.canComputeJustification(manager.getEntailment())) {
					selector.addItem(service);
					if (JustificationComputationServiceManager.LAST_CHOOSEN_SERVICE_ID == manager
							.getIdForService(service))
						serviceToSelect = service;
				}
			selector.setSelectedItem(serviceToSelect);
			manager.selectService(serviceToSelect);
			selector.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					manager.selectService(
							(ComputationService) selector.getSelectedItem());
					updateSettingsPanel();
					manager.clearJustificationsCache();
					recompute();
				}
			});
			panel1.add(selector, BorderLayout.CENTER);
		}

		headerPanel.add(panel1);

		serviceSettingsDisplayHolder_ = new JPanel(new BorderLayout());
		serviceSettingsDisplayHolder_
				.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		updateSettingsPanel();
		headerPanel.add(serviceSettingsDisplayHolder_);

		add(headerPanel, BorderLayout.NORTH);

		Explanation explanation_ = new Explanation(manager_.getEntailment());
		frameList_ = new AxiomsFrameList(this, manager, this, explanation_);
		scrollPane_ = new JScrollPane(frameList_);
		scrollPane_.setMinimumSize(new Dimension(10, 10));
		scrollPane_.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		scrollPane_.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		add(scrollPane_, BorderLayout.CENTER);

		recompute();
	}

	@Override
	public String getIncrementString() {
		try {
			if (displayedJustifications_ == null)
				return "Show next "
						+ manager_.getPresentationSettings().getIncrement()
						+ " justifications";
			int inc = Math.min(
					manager_.getPresentationSettings().getIncrement(),
					getDisplayedExplanationsAmout() - manager_
							.getPresentationSettings().getCurrentCount());
			return "Show next " + inc + " justifications of "
					+ getDisplayedExplanationsAmout() + " in total";
		} catch (Exception ex) {
			return "Show next justifications";
		}
	}

	public String getNumberString() {
		int current = manager_.getPresentationSettings().getCurrentCount();

		String s = current + " justification"
				+ (current == 1 ? " is shown" : "s are shown");

		if (current == getDisplayedExplanationsAmout())
			return "All " + s;
		else
			return s + " of " + getDisplayedExplanationsAmout() + " in total";
	}

	private void updateHeaderPanel() {
		lNumberInfo_.setText(getNumberString());
		frameList_.setNextSectionVisibility(manager_.getPresentationSettings()
				.getCurrentCount() != getDisplayedExplanationsAmout());
		serviceSettingsDisplayHolder_.validate();
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(10, 10);
	}

	@Override
	public void redrawingCalled() {
		manager_.clearJustificationsCache();
		recompute();
	}

	private void updateSettingsPanel() {
		JPanel settingsPanel = manager_.getSelectedService().getSettingsPanel();
		serviceSettingsDisplayHolder_.removeAll();

		lNumberInfo_ = new JLabel(getIncrementString());
		JustificationPreferencesGeneralPanel.addListener(
				new JustificationPreferencesGeneralPanel.PreferencesListener() {
					@Override
					public void valueChanged() {
						lNumberInfo_.setText(getIncrementString());
					}
				});

		serviceSettingsDisplayHolder_.add(lNumberInfo_, BorderLayout.EAST);
		if (settingsPanel != null)
			serviceSettingsDisplayHolder_.add(settingsPanel, BorderLayout.WEST);
		validate();
	}

	private void recompute() {
		try {
			frameList_.clear();
			scrollPane_.validate();

			Set<Justification<OWLAxiom>> e = manager_.getJustifications();
			setDisplayedExplanationsAmout(e.size());
			displayedJustifications_ = new PriorityQueue<>(
					Math.max(getDisplayedExplanationsAmout(), 1),
					new Comparator<Justification<OWLAxiom>>() {
						@Override
						public int compare(Justification<OWLAxiom> o1,
								Justification<OWLAxiom> o2) {
							int diff = getAxiomTypes(o1).size()
									- getAxiomTypes(o2).size();
							if (diff != 0) {
								return diff;
							}
							diff = getClassExpressionTypes(o1).size()
									- getClassExpressionTypes(o2).size();
							if (diff != 0) {
								return diff;
							}
							return o1.getSize() - o2.getSize();
						}
					});
			displayedJustifications_.addAll(e);
			manager_.getPresentationSettings().setCurrentCount(0);

			updateHeaderPanel();

			updatePanel(manager_.getPresentationSettings().getInitialAmount());
		} catch (OWLRuntimeException e) {
			logger.error("An error occurred whilst computing explanations: {}",
					e.getMessage(), e);
		}
	}

	private void updatePanel(int diff) {
		PresentationSettings settings = manager_.getPresentationSettings();
		int maxCnt = Math.min(settings.getCurrentCount() + diff,
				getDisplayedExplanationsAmout());

		for (int nJust = settings.getCurrentCount()
				+ 1; nJust <= maxCnt; nJust++) {
			frameList_.addJustification(displayedJustifications_.poll(), nJust);
			frameList_.validate();
		}

		settings.setCurrentCount(maxCnt);
		updateHeaderPanel();

		scrollPane_.validate();
	}

	private Set<AxiomType<?>> getAxiomTypes(
			Justification<OWLAxiom> justification) {
		Set<AxiomType<?>> result = new HashSet<>();
		for (OWLAxiom ax : justification.getAxioms()) {
			result.add(ax.getAxiomType());
		}
		return result;
	}

	private Set<ClassExpressionType> getClassExpressionTypes(
			Justification<OWLAxiom> justification) {
		Set<ClassExpressionType> result = new HashSet<>();
		for (OWLAxiom ax : justification.getAxioms()) {
			result.addAll(ax.getNestedClassExpressions().stream()
					.map(OWLClassExpression::getClassExpressionType)
					.collect(Collectors.toList()));
		}
		return result;
	}

	@Override
	public void dispose() {
		frameList_.dispose();
		selectionModel_.dispose();
	}

	@Override
	public void addAxiomSelectionListener(AxiomSelectionListener lsnr) {
		selectionModel_.addAxiomSelectionListener(lsnr);
	}

	@Override
	public void removeAxiomSelectionListener(AxiomSelectionListener lsnr) {
		selectionModel_.removeAxiomSelectionListener(lsnr);
	}

	@Override
	public void setAxiomSelected(OWLAxiom axiom, boolean b) {
		selectionModel_.setAxiomSelected(axiom, b);
	}

	@Override
	public Set<OWLAxiom> getSelectedAxioms() {
		return selectionModel_.getSelectedAxioms();
	}

	@Override
	public void clearSelection() {
		selectionModel_.clearSelection();
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension workspaceSize = kit_.getWorkspace().getSize();
		int width = (int) (workspaceSize.getWidth() * 0.8);
		int height = (int) (workspaceSize.getHeight() * 0.7);
		return new Dimension(width, height);
	}

	private int getDisplayedExplanationsAmout() {
		return manager_.getPresentationSettings().getExplanationsCount();
	}

	private void setDisplayedExplanationsAmout(int count) {
		manager_.getPresentationSettings().setExplanationsCount(count);
	}

	@Override
	public void showMore(int number) {
		updatePanel(number);
	}

	@Override
	public void showMore() {
		showMore(manager_.getPresentationSettings().getIncrement());
	}
}