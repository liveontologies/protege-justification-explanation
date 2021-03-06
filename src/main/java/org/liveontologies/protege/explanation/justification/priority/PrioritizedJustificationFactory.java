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