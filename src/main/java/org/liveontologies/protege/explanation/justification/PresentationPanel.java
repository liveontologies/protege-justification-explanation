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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
import javax.swing.Scrollable;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

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

public class PresentationPanel extends JPanel
		implements Disposable, OWLModelManagerListener,
		EntailmentSelectionListener, AxiomSelectionModel,
		ExplanationManagerListener, ComputationServiceListener {

	private static final long serialVersionUID = 5025702425365703918L;

	private static final Logger logger = LoggerFactory
			.getLogger(PresentationPanel.class);

	private final OWLEditorKit kit_;
	private final PresentationManager manager_;
	private final JComponent explanationDisplayHolder_;
	private final JComponent serviceSettingsDisplayHolder_;
	private final JScrollPane scrollPane_;
	private final Collection<AxiomsDisplay> panels_;
	private final JComponent headerPanel_;
	private PriorityQueue<Justification<OWLAxiom>> displayedJustifications_;
	private JButton bAdd_;
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
		setLayout(new GridBagLayout());

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
			add(b, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.NORTHEAST, GridBagConstraints.EAST,
					new Insets(2, 0, 2, 0), 0, 0));
		}

		Collection<ComputationService> services = manager.getServices();
		switch (services.size()) {
		case 0:
			break;
		case 1:
			manager.selectService(services.iterator().next());
			JLabel label = new JLabel("Using " + manager.getSelectedService()
					+ " as a computation service");
			add(label, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL,
					new Insets(2, 0, 2, 0), 0, 0));
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
					createHeaderPanel();
					recompute();
				}
			});
			add(selector, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
					GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL,
					new Insets(2, 0, 2, 0), 0, 0));
		}

		selectionModel_ = new AxiomSelectionModelImpl();
		panels_ = new ArrayList<>();
		kit_.getModelManager().addListener(this);

		serviceSettingsDisplayHolder_ = new JPanel(new BorderLayout());
		updateSettingsPanel();
		add(serviceSettingsDisplayHolder_, new GridBagConstraints(0, 1, 2, 1,
				0.0, 0.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 0, 2, 0), 0, 0));

		headerPanel_ = new JPanel(new BorderLayout());
		createHeaderPanel();
		explanationDisplayHolder_ = new Box(BoxLayout.Y_AXIS);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(explanationDisplayHolder_);
		panel.add(headerPanel_);
		JPanel holder = new HolderPanel(new BorderLayout());
		holder.add(panel, BorderLayout.NORTH);
		scrollPane_ = new JScrollPane(holder);
		scrollPane_.setBorder(null);
		scrollPane_.getViewport().setOpaque(false);
		scrollPane_.getViewport().setBackground(null);
		scrollPane_.setOpaque(false);
		JPanel justificationListPanel = new JPanel(new BorderLayout());
		justificationListPanel.add(scrollPane_);
		justificationListPanel.setMinimumSize(new Dimension(10, 10));
		add(justificationListPanel,
				new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0,
						GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
						new Insets(2, 0, 2, 0), 0, 0));

		recompute();
	}

	private JComponent createHeaderPanel() {
		headerPanel_.removeAll();

		bAdd_ = new JButton(getIncrementString());
		bAdd_.setBorder(new CompoundBorder(bAdd_.getBorder(),
				new EmptyBorder(5, 5, 5, 5)));
		bAdd_.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 0, 5),
				bAdd_.getBorder()));
		headerPanel_.add(bAdd_);
		bAdd_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updatePanel(manager_.getPresentationSettings().getIncrement());
			}
		});
		JustificationPreferencesGeneralPanel.addListener(
				new JustificationPreferencesGeneralPanel.PreferencesListener() {
					@Override
					public void valueChanged() {
						bAdd_.setText(getIncrementString());
					}
				});

		return headerPanel_;
	}

	private String getIncrementString() {
		if (displayedJustifications_ == null)
			return "Show next "
					+ manager_.getPresentationSettings().getIncrement()
					+ " justifications";
		int inc = Math.min(manager_.getPresentationSettings().getIncrement(),
				getDisplayedExplanationsAmout()
						- manager_.getPresentationSettings().getCurrentCount());
		return "Show next " + inc + " justifications of "
				+ getDisplayedExplanationsAmout() + " in total";
	}

	private void updateHeaderPanel() {
		int current = manager_.getPresentationSettings().getCurrentCount();

		String sAll = getDisplayedExplanationsAmout() + " justification"
				+ (getDisplayedExplanationsAmout() == 1 ? " is shown."
						: "s are shown.");

		if (current != getDisplayedExplanationsAmout()) {
			bAdd_.setText(getIncrementString());
			headerPanel_.validate();
		} else {
			headerPanel_.removeAll();
			JLabel justificationsCountLabel = new JLabel("All " + sAll);
			headerPanel_.add(justificationsCountLabel, BorderLayout.CENTER);
			headerPanel_.validate();
		}
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(10, 10);
	}

	@Override
	public void explanationLimitChanged(
			PresentationManager presentationManager) {
		selectionChanged();
	}

	@Override
	public void explanationsComputed(OWLAxiom entailment) {
	}

	private class HolderPanel extends JPanel implements Scrollable {

		private static final long serialVersionUID = 1368378506064086576L;

		public HolderPanel(LayoutManager layout) {
			super(layout);
			setOpaque(false);
		}

		@Override
		public Dimension getPreferredScrollableViewportSize() {
			return super.getPreferredSize();
		}

		@Override
		public int getScrollableUnitIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			return 30;
		}

		@Override
		public int getScrollableBlockIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			return 30;
		}

		@Override
		public boolean getScrollableTracksViewportWidth() {
			return true;
		}

		@Override
		public boolean getScrollableTracksViewportHeight() {
			return false;
		}
	}

	@Override
	public void selectionChanged() {
		recompute();
	}

	@Override
	public void redrawingCalled() {
		manager_.clearJustificationsCache();
		recompute();
	}

	private void updateSettingsPanel() {
		JPanel settingsPanel = manager_.getSelectedService().getSettingsPanel();
		serviceSettingsDisplayHolder_.removeAll();
		if (settingsPanel != null)
			serviceSettingsDisplayHolder_.add(settingsPanel, BorderLayout.WEST);
		validate();
	}

	private void recompute() {
		try {
			panels_.forEach(AxiomsDisplay::dispose);
			explanationDisplayHolder_.removeAll();
			explanationDisplayHolder_.validate();

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
			Justification<OWLAxiom> justification = displayedJustifications_
					.poll();
			final AxiomsDisplay display = new AxiomsDisplay(manager_, this,
					justification);
			AxiomsDisplayList displayList = new AxiomsDisplayList(display,
					nJust, justification);
			displayList.setBorder(BorderFactory.createEmptyBorder(2, 0, 10, 0));
			explanationDisplayHolder_.add(displayList);
			panels_.add(display);
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
		kit_.getModelManager().removeListener(this);
		for (AxiomsDisplay panel : panels_) {
			panel.dispose();
		}
		selectionModel_.dispose();
	}

	@Override
	public void handleChange(OWLModelManagerChangeEvent event) {

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
}