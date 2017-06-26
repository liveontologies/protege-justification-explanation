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

import java.util.ArrayList;

import org.liveontologies.protege.explanation.justification.priority.PrioritizedJustification;
import org.semanticweb.owlapi.model.OWLAxiom;

public class Explanation {
	private final ArrayList<PrioritizedJustification> justifications_;
	private final OWLAxiom entailment_;

	public Explanation(OWLAxiom entailment) {
		justifications_ = new ArrayList<>();
		entailment_ = entailment;
	}

	public PrioritizedJustification getJustification(int index) {
		return justifications_.get(index);
	}

	public int addJustification(PrioritizedJustification justification) {
		justifications_.add(justification);
		return justifications_.size() - 1;
	}

	public OWLAxiom getEntailment() {
		return entailment_;
	}
}