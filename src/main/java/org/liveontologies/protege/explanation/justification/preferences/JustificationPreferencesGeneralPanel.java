package org.liveontologies.protege.explanation.justification.preferences;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.liveontologies.protege.explanation.justification.JustificationComputationServiceManager;
import org.liveontologies.protege.explanation.justification.service.ComputationService;
import org.protege.editor.core.ui.preferences.PreferencesLayoutPanel;
import org.protege.editor.owl.ui.preferences.OWLPreferencesPanel;

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

public class JustificationPreferencesGeneralPanel extends OWLPreferencesPanel {

	private static final long serialVersionUID = 2271633237554797195L;

	private static ArrayList<PreferencesListener> listeners_ = new ArrayList<PreferencesListener>();

	private SpinnerNumberModel initialAmountM_, incrementM_;

	@Override
	public void initialise() throws Exception {
		setLayout(new BorderLayout());
		PreferencesLayoutPanel panel = new PreferencesLayoutPanel();
		add(panel, BorderLayout.NORTH);

		panel.addGroup("Installed justification services");
		DefaultListModel<String> pluginModel = new DefaultListModel<>();
		JustificationComputationServiceManager manager = JustificationComputationServiceManager
				.get(getOWLEditorKit());
		for (ComputationService service : manager.getServices())
			pluginModel.addElement(service.getName());
		JList<String> pluginList = new JList<>(pluginModel);
		pluginList.setToolTipText(
				"Plugins that provide justification facilities");
		JScrollPane pluginInfoScrollPane = new JScrollPane(pluginList);
		pluginInfoScrollPane.setPreferredSize(new Dimension(300, 100));
		panel.addGroupComponent(pluginInfoScrollPane);

		panel.addGroup("Initial justifications");
		initialAmountM_ = new SpinnerNumberModel(1, 1, 999, 1);
		JComponent spinnerIA = new JSpinner(initialAmountM_);
		spinnerIA.setMaximumSize(spinnerIA.getPreferredSize());
		panel.addGroupComponent(spinnerIA);
		spinnerIA.setToolTipText(JustPrefs.INITIAL_NUMBER_DESCRIPTION);

		panel.addGroup("Increment value");
		incrementM_ = new SpinnerNumberModel(1, 1, 999, 1);
		JComponent spinnerI = new JSpinner(incrementM_);
		spinnerI.setMaximumSize(spinnerI.getPreferredSize());
		panel.addGroupComponent(spinnerI);
		spinnerI.setToolTipText(JustPrefs.INCREMENT_DESCRIPTION);

		panel.addGroup("");
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(e -> reset());
		resetButton.setToolTipText("Resets all settings to default values");
		panel.addGroupComponent(resetButton);

		loadFrom(JustPrefs.create().load());
	}

	@Override
	public void dispose() throws Exception {
	}

	@Override
	public void applyChanges() {
		JustPrefs prefs = JustPrefs.create();
		saveTo(prefs);
		prefs.save();
		for (PreferencesListener listener : listeners_)
			listener.valueChanged();
	}

	private void loadFrom(JustPrefs prefs) {
		incrementM_.setValue(prefs.increment);
		initialAmountM_.setValue(prefs.initialNumber);
	}

	private void saveTo(JustPrefs prefs) {
		prefs.increment = incrementM_.getNumber().intValue();
		prefs.initialNumber = initialAmountM_.getNumber().intValue();
	}

	private void reset() {
		loadFrom(JustPrefs.create());
	}

	public static void addListener(PreferencesListener listener) {
		listeners_.add(listener);
	}

	public static void removeListener(PreferencesListener listener) {
		listeners_.remove(listener);
	}

	public interface PreferencesListener {
		public void valueChanged();
	}
}