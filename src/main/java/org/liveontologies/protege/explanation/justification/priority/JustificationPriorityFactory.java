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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;

/**
 * A factory for creating {@link JustificationPriority}
 * 
 * @author Yevgeny Kazakov
 *
 */
public class JustificationPriorityFactory {

	private Map<OWLAxiom, CacheRecord> cache_ = new HashMap<>();

	private final Set<OwlAxiomType> axiomTypes_ = EnumSet
			.noneOf(OwlAxiomType.class);

	private final Set<ClassExpressionType> classExpressionTypes_ = EnumSet
			.noneOf(ClassExpressionType.class);

	public JustificationPriority getPriority(
			Set<? extends OWLAxiom> justification) {
		int size = 0;
		axiomTypes_.clear();
		classExpressionTypes_.clear();
		for (OWLAxiom axiom : justification) {
			size++;
			CacheRecord record = cache_.get(axiom);
			if (record == null) {
				record = new CacheRecord(axiom);
				cache_.put(axiom, record);
			}
			axiomTypes_.add(record.axiomType_);
			classExpressionTypes_.addAll(record.classExpressionTypes_);
		}
		return new JustificationPriority(axiomTypes_.size(),
				classExpressionTypes_.size(), size);
	}

	static class CacheRecord {

		private final OwlAxiomType axiomType_;

		private final Set<ClassExpressionType> classExpressionTypes_ = EnumSet
				.noneOf(ClassExpressionType.class);

		CacheRecord(OWLAxiom axiom) {
			this.axiomType_ = OwlAxiomTypes.getAxiomType(axiom);
			classExpressionTypes_.addAll(axiom.getNestedClassExpressions()
					.stream().map(OWLClassExpression::getClassExpressionType)
					.collect(Collectors.toList()));
		}
	}
}