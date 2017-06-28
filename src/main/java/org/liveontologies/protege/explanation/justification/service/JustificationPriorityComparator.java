package org.liveontologies.protege.explanation.justification.service;

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