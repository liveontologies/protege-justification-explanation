package org.liveontologies.protege.explanation.justification.priority;

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