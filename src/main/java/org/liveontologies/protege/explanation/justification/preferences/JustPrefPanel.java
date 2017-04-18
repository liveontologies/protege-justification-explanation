package org.liveontologies.protege.explanation.justification.preferences;

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
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import org.protege.editor.core.Disposable;
import org.protege.editor.core.prefs.Preferences;
import org.protege.editor.core.prefs.PreferencesManager;
import org.protege.editor.core.ui.preferences.PreferencesPanel;
import org.protege.editor.core.ui.preferences.PreferencesPanelPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JustPrefPanel extends PreferencesPanel
		implements Disposable {

	private final Map<String, PreferencesPanel> map = new HashMap<>();

	private final Map<String, JComponent> componentMap = new HashMap<>();

	private final JTabbedPane tabbedPane = new JTabbedPane();

	private final Logger logger = LoggerFactory
			.getLogger(JustPrefPanel.class);

	private static final String JUST_PREFS_HISTORY_PANEL_KEY = "just.prefs.history.panel";

	@Override
	public void initialise() throws Exception {
		setLayout(new BorderLayout());

		JustificationPreferencesPanelPluginLoader loader = new JustificationPreferencesPanelPluginLoader(
				getEditorKit());
		Set<PreferencesPanelPlugin> plugins = new TreeSet<>((o1, o2) -> {
			String s1 = o1.getLabel();
			String s2 = o2.getLabel();
			return s1.compareTo(s2);
		});
		plugins.addAll(loader.getPlugins());

		for (PreferencesPanelPlugin plugin : plugins) {
			try {
				PreferencesPanel panel = plugin.newInstance();
				panel.initialise();
				String label = plugin.getLabel();
				final JScrollPane sp = new JScrollPane(panel);
				sp.setBorder(new EmptyBorder(0, 0, 0, 0));
				map.put(label, panel);
				componentMap.put(label, sp);
				tabbedPane.addTab(label, sp);
			} catch (Throwable e) {
				logger.warn(
						"An error occurred whilst trying to instantiate the justification preferences panel plugin '{}': {}",
						plugin.getLabel(), e);
			}
		}

		add(tabbedPane);

		updatePanelSelection(null);
	}

	@Override
	public void dispose() throws Exception {
		final Preferences prefs = PreferencesManager.getInstance()
				.getApplicationPreferences(JustPrefPanel.class);
		prefs.putString(JUST_PREFS_HISTORY_PANEL_KEY, getSelectedPanel());
		for (PreferencesPanel panel : new ArrayList<>(map.values())) {
			try {
				panel.dispose();
			} catch (Throwable e) {
				logger.warn(
						"An error occurred whilst disposing of the justification preferences panel plugin '{}': {}",
						panel.getLabel(), e);
			}
		}
		map.clear();
	}

	protected String getSelectedPanel() {
		Component c = tabbedPane.getSelectedComponent();
		if (c instanceof JScrollPane) {
			c = ((JScrollPane) c).getViewport().getView();
		}
		for (String tabName : map.keySet()) {
			if (c.equals(map.get(tabName))) {
				return tabName;
			}
		}
		return null;
	}

	public void updatePanelSelection(String selectedPanel) {
		if (selectedPanel == null) {
			final Preferences prefs = PreferencesManager.getInstance()
					.getApplicationPreferences(
							JustPrefPanel.class);
			selectedPanel = prefs.getString(JUST_PREFS_HISTORY_PANEL_KEY, null);
		}
		Component c = componentMap.get(selectedPanel);
		if (c != null) {
			tabbedPane.setSelectedComponent(c);
		}
	}

	@Override
	public void applyChanges() {
		for (PreferencesPanel panel : new ArrayList<>(map.values())) {
			try {
				panel.applyChanges();
			} catch (Throwable e) {
				logger.warn(
						"An error occurred whilst trying to save the preferences for the justification preferences panel '{}': {}",
						panel.getLabel(), e);
			}
		}
	}
}