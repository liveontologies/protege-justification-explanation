package org.liveontologies.protege.explanation.justification.preferences;

import org.protege.editor.core.prefs.Preferences;
import org.protege.editor.core.prefs.PreferencesManager;

public class JustPrefs {
	private static final String PREFS_KEY_ = "JUSTIFICATION_EXPLANATION_PREFS",
			INCREMENT_KEY_ = "INCREMENT",
			INITIAL_AMOUNT_KEY_ = "INITIAL_AMOUNT",
			IS_AXIOM_POPULARITY_SHOWN_KEY_ = "AXIOM_POPULARITY";

	public final static String INCREMENT_DESCRIPTION = "The number of additional justifications displayed after click on “Show next” button",
			INITIAL_AMOUNT_DESCRIPTION = "The number of justifications displayed at the beginning",
			AXIOM_POPULARITY_DESCRIPTION = "Sets whether the popularity amount for each axiom would be displayed";

	private final static int DEFAULT_INCREMENT_ = 10; 
	private final static int DEFAULT_INITIAL_AMOUNT_ = 20;
	private final static boolean DEFAULT_IS_AXIOM_POPULARITY_SHOWN_ = false;

	public int increment = DEFAULT_INCREMENT_;
	public int initialAmount = DEFAULT_INITIAL_AMOUNT_;
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
		initialAmount = prefs.getInt(INITIAL_AMOUNT_KEY_,
				DEFAULT_INITIAL_AMOUNT_);
		isPopularityShown = prefs.getBoolean(IS_AXIOM_POPULARITY_SHOWN_KEY_,
				DEFAULT_IS_AXIOM_POPULARITY_SHOWN_);
		return this;
	}

	public JustPrefs save() {
		Preferences prefs = getPrefs();
		prefs.putInt(INCREMENT_KEY_, increment);
		prefs.putInt(INITIAL_AMOUNT_KEY_, initialAmount);
		prefs.putBoolean(IS_AXIOM_POPULARITY_SHOWN_KEY_, isPopularityShown);
		return this;
	}

	public JustPrefs reset() {
		increment = DEFAULT_INCREMENT_;
		initialAmount = DEFAULT_INITIAL_AMOUNT_;
		isPopularityShown = DEFAULT_IS_AXIOM_POPULARITY_SHOWN_;
		return this;
	}
}