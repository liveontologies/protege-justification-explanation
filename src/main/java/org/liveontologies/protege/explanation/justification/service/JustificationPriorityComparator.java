package org.liveontologies.protege.explanation.justification.service;

import java.util.Comparator;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * An assignment of objects using which justifications can be compared
 * 
 * @author Yevgeny Kazakov
 *
 * @param <P>
 *            the types of objects that define the priority of justifications
 */
public interface JustificationPriorityComparator<P> extends Comparator<P> {

	/**
	 * @param justification
	 * @return the priority, based on which the provided object is compared.
	 */
	P getPriority(Set<OWLAxiom> justification);

}
