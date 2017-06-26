package org.liveontologies.protege.explanation.justification.priority;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;

public class PrioritizedJustificationFactory {

	private final JustificationPriorityFactory priorityFactory_;

	PrioritizedJustificationFactory(
			JustificationPriorityFactory priorityFactory) {
		this.priorityFactory_ = priorityFactory;
	}

	public PrioritizedJustificationFactory() {
		this(new JustificationPriorityFactory());
	}

	public JustificationPriorityFactory getPriorityFactory() {
		return priorityFactory_;
	}

	public PrioritizedJustification createJustification(Set<OWLAxiom> axioms) {
		return new PrioritizedJustification(axioms, priorityFactory_);
	}

}
