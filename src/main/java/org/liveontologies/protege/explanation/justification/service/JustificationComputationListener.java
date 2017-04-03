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


import java.util.Collection;

import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Listener should be created to be provided with computed justifications
 * 
 * @author Alexander Stupnikov
 * Date: 08-02-2017
 */

public interface JustificationComputationListener {

	/**
	 * Should be called when another justification is computed
	 * 
	 * @param justification	another justification for the axiom
	 */
	void foundJustification(Collection<OWLAxiom> justification);
}