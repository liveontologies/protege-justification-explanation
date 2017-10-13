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

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * An object using which justification can be computed
 * 
 * @author Yevgeny Kazakov
 *
 */
public abstract class JustificationComputation {

	private final JustificationListener listener_;

	private final InterruptMonitor monitor_;

	public JustificationComputation(JustificationListener listener,
			InterruptMonitor monitor) {
		this.listener_ = listener;
		this.monitor_ = monitor;
	}

	/**
	 * Sets the preferred priority for returning of justifications
	 * 
	 * @param comparator
	 *            {@link JustificationPriorityComparator} that defines the order
	 *            in which the justifications should be reported by the
	 *            computation: justification with the smaller values of
	 *            {@link JustificationPriorityComparator#getPriority} under the
	 *            comparator should be returned first
	 * @return {@code true} if the value has been set and will be used in the
	 *         computation and {@code false} otherwise
	 * 
	 * @param <P>
	 *            the type of objects used to compare justifications
	 */
	public <P> boolean setPrefferredPriority(
			JustificationPriorityComparator<P> comparator) {
		return false;
	}

	/**
	 * Initiate the computation of justifications;
	 */
	public abstract void startComputation();

	/**
	 * Should be called each time a new justification is found
	 * 
	 * @param justification
	 *            a new justification found by this computation
	 */
	protected void notifyJustificationFound(Set<OWLAxiom> justification) {
		listener_.justificationFound(justification);
	}

	/**
	 * @return {@code true} if the computation has been interrupted and
	 *         {@code false} otherwise
	 */
	public boolean isInterrupted() {
		return monitor_.isInterrupted();
	}

	public interface InterruptMonitor {

		/**
		 * @return {@code true} if the computation should be interrupted
		 */
		boolean isInterrupted();
	}
}