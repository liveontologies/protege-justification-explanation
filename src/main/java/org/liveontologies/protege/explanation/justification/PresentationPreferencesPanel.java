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

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.protege.editor.core.ui.preferences.PreferencesLayoutPanel;
import org.protege.editor.owl.ui.preferences.OWLPreferencesPanel;

/**
 * @author Alexander Stupnikov
 * Date: 23-03-2017
 */

public class PresentationPreferencesPanel extends OWLPreferencesPanel {

	private SpinnerNumberModel initialAmountM, incrementM;

	static private int increment = 10;
	static private int initialAmount = 20;

	static private ArrayList<PreferencesListener> listeners = new ArrayList<PreferencesListener>();

	@Override
	public void initialise() throws Exception {
		setLayout(new BorderLayout());
		PreferencesLayoutPanel panel = new PreferencesLayoutPanel();
		add(panel, BorderLayout.NORTH);

		panel.addGroup("Initial justifications");
		initialAmountM = new SpinnerNumberModel(getInitialAmount(), 1, 999, 1);
		JComponent spinnerIA = new JSpinner(initialAmountM);
		spinnerIA.setMaximumSize(spinnerIA.getPreferredSize());
		panel.addGroupComponent(spinnerIA);
		spinnerIA.setToolTipText("Amount of justifications displayed at the beginning");

		panel.addGroup("Increment value");
		incrementM = new SpinnerNumberModel(getIncrement(), 1, 999, 1);
		JComponent spinnerI = new JSpinner(incrementM);
		spinnerI.setMaximumSize(spinnerI.getPreferredSize());
		panel.addGroupComponent(spinnerI);
		spinnerI.setToolTipText("Amount of additional justifications displayed after click on “Show next” button");
	}

	@Override
	public void dispose() throws Exception {
	}

	@Override
	public void applyChanges() {
		setInitialAmount(initialAmountM.getNumber().intValue());
		setIncrement(incrementM.getNumber().intValue());
		for (PreferencesListener listener : listeners)
			listener.valueChanged();
	}

	static public int getIncrement() {
		return increment;
	}

	static private void setIncrement(int value) {
		increment = value;
	}

	static public int getInitialAmount() {
		return initialAmount;
	}

	static public void setInitialAmount(int value) {
		initialAmount = value;
	}

	public static void addListener(PreferencesListener listener) {
		listeners.add(listener);
	}

	public static void removeListener(PreferencesListener listener) {
		listeners.remove(listener);
	}

	public interface PreferencesListener {
		public void valueChanged();
	}
}