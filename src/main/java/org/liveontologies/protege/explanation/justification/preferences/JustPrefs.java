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


import org.protege.editor.core.prefs.Preferences;
import org.protege.editor.core.prefs.PreferencesManager;

public class JustPrefs {
	private static final String PREFS_KEY_ = "JUSTIFICATION_EXPLANATION_PREFS",
			INCREMENT_KEY_ = "INCREMENT",
			INITIAL_NUMBER_KEY_ = "INITIAL_NUMBER",
			IS_AXIOM_POPULARITY_SHOWN_KEY_ = "AXIOM_POPULARITY";

	public final static String INCREMENT_DESCRIPTION = "The number of additional justifications displayed after click on “Show next” button",
			INITIAL_NUMBER_DESCRIPTION = "The number of justifications displayed at the beginning",
			AXIOM_POPULARITY_DESCRIPTION = "Sets whether the popularity number for each axiom would be displayed";

	private final static int DEFAULT_INCREMENT_ = 10; 
	private final static int DEFAULT_INITIAL_NUMBER_ = 20;
	private final static boolean DEFAULT_IS_AXIOM_POPULARITY_SHOWN_ = false;

	public int increment = DEFAULT_INCREMENT_;
	public int initialNumber = DEFAULT_INITIAL_NUMBER_;
	public boolean isPopularityShown = DEFAULT_IS_AXIOM_POPULARITY_SHOWN_;

	private JustPrefs() {

	}

	private static Preferences getPrefs() {
		PreferencesManager prefMan = PreferencesManager.getInstance();
		return prefMan.getPreferencesForSet(PREFS_KEY_,
				JustPrefs.class);
	}

	public static JustPrefs create() {
		return new JustPrefs();
	}

	public JustPrefs load() {
		Preferences prefs = getPrefs();
		increment = prefs.getInt(INCREMENT_KEY_, DEFAULT_INCREMENT_);
		initialNumber = prefs.getInt(INITIAL_NUMBER_KEY_,
				DEFAULT_INITIAL_NUMBER_);
		isPopularityShown = prefs.getBoolean(IS_AXIOM_POPULARITY_SHOWN_KEY_,
				DEFAULT_IS_AXIOM_POPULARITY_SHOWN_);
		return this;
	}

	public JustPrefs save() {
		Preferences prefs = getPrefs();
		prefs.putInt(INCREMENT_KEY_, increment);
		prefs.putInt(INITIAL_NUMBER_KEY_, initialNumber);
		prefs.putBoolean(IS_AXIOM_POPULARITY_SHOWN_KEY_, isPopularityShown);
		return this;
	}

	public JustPrefs reset() {
		increment = DEFAULT_INCREMENT_;
		initialNumber = DEFAULT_INITIAL_NUMBER_;
		isPopularityShown = DEFAULT_IS_AXIOM_POPULARITY_SHOWN_;
		return this;
	}
}