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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Author: Matthew Horridge Stanford University Bio-Medical Informatics Research
 * Group Date: 20/03/2012
 */

public class AxiomsCache {

	private final Map<OWLAxiom, Set<Explanation<OWLAxiom>>> cache_ = new HashMap<>();
	private final Map<OWLAxiom, Map<OWLAxiom, Integer>> popularityCache_ = new HashMap<>();

	public boolean contains(OWLAxiom entailment) {
		return cache_.containsKey(entailment);
	}

	public Set<Explanation<OWLAxiom>> get(OWLAxiom entailment) {
		Set<Explanation<OWLAxiom>> explanations = cache_.get(entailment);
		if (explanations == null) {
			return Collections.emptySet();
		}
		return explanations;
	}

	public void put(Explanation<OWLAxiom> explanation) {
		Set<Explanation<OWLAxiom>> expls = cache_
				.get(explanation.getEntailment());
		if (expls == null) {
			expls = new HashSet<>();
			cache_.put(explanation.getEntailment(), expls);
			getPopularityCache(explanation.getEntailment());
		}
		expls.add(explanation);
	}

	public void put(Set<Explanation<OWLAxiom>> explanations) {
		for (Explanation<OWLAxiom> expl : explanations) {
			put(expl);
		}
	}

	public void clear() {
		cache_.clear();
	}

	public void clear(OWLAxiom entailment) {
		cache_.remove(entailment);
	}

	private Map<OWLAxiom, Integer> getPopularityCache(OWLAxiom entailment) {
		Map<OWLAxiom, Integer> popularities = popularityCache_.get(entailment);
		if (popularities != null)
			return popularities;

		popularityCache_.put(entailment, new HashMap<>());
		return popularityCache_.get(entailment);
	}

	public int getAxiomPopularity(OWLAxiom entailment, OWLAxiom axiom) {
		Map<OWLAxiom, Integer> popularities = getPopularityCache(entailment);
		if (popularities.containsKey(axiom))
			return popularities.get(axiom);

		int count = 0;
		Set<Explanation<OWLAxiom>> explanations = get(entailment);
		for (Explanation<OWLAxiom> explanation : explanations) {
			if (explanation.contains(axiom)) {
				count++;
			}
		}
		popularities.put(axiom, count);
		return count;
	}
}