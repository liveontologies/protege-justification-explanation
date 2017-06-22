package org.liveontologies.protege.explanation.justification;

import org.liveontologies.protege.explanation.justification.service.JustificationListener;

public interface JustificationComputationListener
		extends JustificationListener {

	void computationStarted();

	void computationFinished();

}
