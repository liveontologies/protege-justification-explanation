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
import java.util.Set;

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
import javax.swing.SwingUtilities;

import org.liveontologies.protege.explanation.justification.preferences.JustPrefPanel;
import org.liveontologies.protege.explanation.justification.preferences.JustPrefs;
import org.liveontologies.protege.explanation.justification.service.JustificationComputationService;
import org.protege.editor.core.Disposable;
import org.protege.editor.core.plugin.PluginUtilities;
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
 * Group Date: 04-Oct-2008
 * 
 * The component that displays a set of justification
 */

public class PresentationPanel extends JPanel
		implements Disposable, AxiomSelectionModel,
		JustificationManager.ChangeListener, ShowMoreListener {

	private static final long serialVersionUID = 5025702425365703918L;

	private final JustificationManager manager_;
	private final ExplanationGeneratorProgressDialog progressDialog_;
	private final JScrollPane scrollPane_;
	private final JComponent serviceSettingsDisplayHolder_;
	private final JustificationFrameList frameList_;
	private final JLabel lNumberInfo_;
	private final AxiomSelectionModelImpl selectionModel_;
	private int displayedJustificationCount_ = 0;

	public PresentationPanel(JustificationComputationServiceManager manager,
			OWLAxiom entailment) {
		this.selectionModel_ = new AxiomSelectionModelImpl();
		this.progressDialog_ = new ExplanationGeneratorProgressDialog(
				manager.getOwlEditorKit());
		this.manager_ = new JustificationManager(manager, entailment,
				progressDialog_, progressDialog_);
		manager_.addListener(this);

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
								manager.getOwlEditorKit());
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
								manager.getOwlEditorKit().getWorkspace(),
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

		headerPanel.add(panel1);

		serviceSettingsDisplayHolder_ = new JPanel(new BorderLayout());
		serviceSettingsDisplayHolder_
				.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		headerPanel.add(serviceSettingsDisplayHolder_);

		lNumberInfo_ = new JLabel();

		add(headerPanel, BorderLayout.NORTH);

		Explanation explanation_ = new Explanation(manager_.getEntailment());
		frameList_ = new JustificationFrameList(this, manager_, this,
				explanation_);
		scrollPane_ = new JScrollPane(frameList_);
		scrollPane_.setMinimumSize(new Dimension(10, 10));
		scrollPane_.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		scrollPane_.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		add(scrollPane_, BorderLayout.CENTER);

		Collection<JustificationComputationService> services = manager
				.getServices();
		JustificationComputationService selectedService;
		switch (services.size()) {
		case 0:
			break;
		case 1:
			selectedService = services.iterator().next();
			manager_.selectJusificationService(selectedService);
			JLabel label = new JLabel(
					"Using " + selectedService + " as a computation service");
			panel1.add(label, BorderLayout.EAST);
			break;
		default:
			JComboBox<JustificationComputationService> selector = new JComboBox<JustificationComputationService>();
			selectedService = services.iterator().next();
			for (JustificationComputationService service : services)
				if (service.canJustify(entailment)) {
					selector.addItem(service);
				}
			selector.setSelectedItem(selectedService);
			manager_.selectJusificationService(selectedService);
			selector.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					manager_.selectJusificationService(
							(JustificationComputationService) selector
									.getSelectedItem());
				}
			});
			panel1.add(selector, BorderLayout.CENTER);
		}

	}

	@Override
	public String getIncrementString() {
		int remainingJustificationsCount = manager_
				.getRemainingJustificationCount();
		int inc = Math.min(JustPrefs.create().load().increment,
				remainingJustificationsCount);
		return "Show next " + inc + " justifications of "
				+ (displayedJustificationCount_ + remainingJustificationsCount)
				+ " in total";
	}

	public String getNumberString() {
		String s = displayedJustificationCount_ + " justification"
				+ (displayedJustificationCount_ == 1 ? " is shown"
						: "s are shown");
		int remainingJustificationsCount = manager_
				.getRemainingJustificationCount();

		if (remainingJustificationsCount == 0) {
			return "All " + s;
		} else {
			return s + " of " + (displayedJustificationCount_
					+ remainingJustificationsCount) + " in total";
		}
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(10, 10);
	}

	private void refreshSettingsPanel() {
		serviceSettingsDisplayHolder_.removeAll();
		JPanel settingsPanel = manager_.getSettingsPanel();

		serviceSettingsDisplayHolder_.add(lNumberInfo_, BorderLayout.EAST);
		if (settingsPanel != null) {
			serviceSettingsDisplayHolder_.add(settingsPanel, BorderLayout.WEST);
		}
		serviceSettingsDisplayHolder_.validate();
	}

	private void refreshCounters() {
		lNumberInfo_.setText(getNumberString());
		lNumberInfo_.validate();
		frameList_.setAddJustificationsSectionVisibility(
				manager_.getRemainingJustificationCount() != 0);
	}

	private void reloadJustifications() {
		displayedJustificationCount_ = 0;
		frameList_.clear();
		loadMoreJustifications(JustPrefs.create().load().initialNumber);
		refreshCounters();
	}

	private void loadMoreJustifications(int maxToLoad) {
		for (;;) {
			if (maxToLoad <= 0) {
				break;
			}
			maxToLoad--;
			Justification next = manager_.pollJustification();
			if (next == null) {
				break;
			}
			displayedJustificationCount_++;
			frameList_.addJustification(next, displayedJustificationCount_);
		}
		refreshCounters();
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
		Dimension workspaceSize = manager_.getOwlEditorKit().getWorkspace()
				.getSize();
		int width = (int) (workspaceSize.getWidth() * 0.8);
		int height = (int) (workspaceSize.getHeight() * 0.7);
		return new Dimension(width, height);
	}

	@Override
	public void showMore(int number) {
		loadMoreJustifications(number);
	}

	@Override
	public void showMore() {
		showMore(JustPrefs.create().load().increment);
	}

	@Override
	public void justificationsRecomputed() {
		SwingUtilities.invokeLater(() -> reloadJustifications());
	}

	@Override
	public void settingsPanelChanged() {
		SwingUtilities.invokeLater(() -> refreshSettingsPanel());
	}
}