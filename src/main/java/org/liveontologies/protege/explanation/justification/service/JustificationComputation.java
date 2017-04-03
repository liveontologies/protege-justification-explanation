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


import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Designed to compute and keep track of justifications
 * 
 * @author Alexander Stupnikov
 * Date: 08-02-2017
 */

public abstract class JustificationComputation {
	
	private final OWLAxiom entailment;

	/**
	 * Constructs a computation object
	 * 
	 * @param entailment	an axiom to compute justifications for
	 */
	public JustificationComputation(OWLAxiom entailment) {
		this.entailment = entailment;
	}

	/**
	 * Should initiate a computation process
	 */
	public abstract void startComputation();

	/**
	 * When called, asks the plugin to stop computation process
	 */
	public abstract void interruptComputation();
	
	/**
	 * Should return whether the computation object was notified to stop computation process
	 * 
	 * @return	whether the computation object was notified to stop computation process or not
	 */
	public abstract boolean isComputationInterrupted();

	/**
	 * Should store a listener to notify about computed justifications
	 * 
	 * @param listener	listener which should be notified about computed justifications
	 */
	public abstract void addComputationListener(JustificationComputationListener listener);

	/**
	 * Should remove the specified listener
	 * 
	 * @param listener	listener to notify no more
	 */
	public abstract void removeComputationListener(JustificationComputationListener listener);

	/**
	 * Returns the entailment
	 * 
	 * @return	an axiom to compute justifications for
	 */
	public OWLAxiom getEntailment() {
		return entailment;
	}
}