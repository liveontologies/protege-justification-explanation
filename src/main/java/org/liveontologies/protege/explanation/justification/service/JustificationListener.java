package org.liveontologies.protege.explanation.justification.service;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * A object using which the computed justifications are reported
 * 
 * @author Alexander Stupnikov Date: 27-02-2017
 * @author Yevgeny Kazakov
 *
 */
public interface JustificationListener {

	/**
	 * called each time a new justification is computed
	 * 
	 * @param justification
	 *            the computed justification
	 */
	void justificationFound(Set<OWLAxiom> justification);
}
