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

import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Author: Matthew Horridge Stanford University Bio-Medical Informatics Research
 * Group Date: 20/03/2012
 */

public class AxiomsCache {

	private final Map<OWLAxiom, Set<Justification<OWLAxiom>>> cache_ = new HashMap<>();
	private final Map<OWLAxiom, Map<OWLAxiom, Integer>> popularityCache_ = new HashMap<>();

	public boolean contains(OWLAxiom entailment) {
		return cache_.containsKey(entailment);
	}

	public Set<Justification<OWLAxiom>> get(OWLAxiom entailment) {
		Set<Justification<OWLAxiom>> justifications = cache_.get(entailment);
		if (justifications == null) {
			return Collections.emptySet();
		}
		return justifications;
	}

	public void put(Justification<OWLAxiom> justification) {
		Set<Justification<OWLAxiom>> justifications = cache_
				.get(justification.getEntailment());
		if (justifications == null) {
			justifications = new HashSet<>();
			cache_.put(justification.getEntailment(), justifications);
			getPopularityCache(justification.getEntailment());
		}
		justifications.add(justification);
	}

	public void put(Set<Justification<OWLAxiom>> justifications) {
		for (Justification<OWLAxiom> justification : justifications) {
			put(justification);
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
		Set<Justification<OWLAxiom>> justifications = get(entailment);
		for (Justification<OWLAxiom> justification : justifications) {
			if (justification.contains(axiom)) {
				count++;
			}
		}
		popularities.put(axiom, count);
		return count;
	}
}