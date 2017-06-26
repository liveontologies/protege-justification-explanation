package org.liveontologies.protege.explanation.justification.priority;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * A justification (set of {@link OWLAxiom}) that compared using the
 * corresponding {@link JustificationPriority}
 * 
 * @author Yevgeny Kazakov
 */
public class PrioritizedJustification
		implements Comparable<PrioritizedJustification> {

	private final Set<OWLAxiom> justification_;

	private final JustificationPriority priority_;

	/**
	 * Creates a new {@link PrioritizedJustification}
	 * 
	 * @param justification
	 *            the set of {@link OWLAxiom}s this justification contains
	 * @param priorityFactory
	 *            specifies how to compute the {@link JustificationPriority} for
	 *            the justificaiton
	 */
	PrioritizedJustification(Set<OWLAxiom> justification,
			JustificationPriorityFactory priorityFactory) {
		this.justification_ = justification;
		this.priority_ = priorityFactory.getPriority(justification);
	}

	public Set<OWLAxiom> getAxioms() {
		return justification_;
	}

	public int getSize() {
		return priority_.getSize();
	}

	public boolean contains(OWLAxiom axiom) {
		return justification_.contains(axiom);
	}

	@Override
	public int compareTo(PrioritizedJustification o) {
		return priority_.compareTo(o.priority_);
	}

	@Override
	public int hashCode() {
		return justification_.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PrioritizedJustification) {
			PrioritizedJustification other = (PrioritizedJustification) obj;
			return justification_.equals(other.justification_);
		}
		// else
		return false;
	}

}