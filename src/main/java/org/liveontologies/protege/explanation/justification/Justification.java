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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;

public class Justification<E> {

	private E entailment_;
	private Set<OWLAxiom> justification_;

	public Justification(E entailment, Set<OWLAxiom> justification) {
		this.entailment_ = entailment;
		this.justification_ = Collections
				.unmodifiableSet(new HashSet<OWLAxiom>(justification));
	}

	public E getEntailment() {
		return entailment_;
	}

	public Set<OWLAxiom> getAxioms() {
		return justification_;
	}

	public int getSize() {
		return justification_.size();
	}

	public boolean contains(OWLAxiom axiom) {
		return justification_.contains(axiom);
	}
}