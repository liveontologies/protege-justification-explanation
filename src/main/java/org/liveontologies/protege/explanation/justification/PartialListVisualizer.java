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

/**
 * Represents visual component which can display (some) elements from the list
 * 
 * @author Alexander
 */

public interface PartialListVisualizer {

	/**
	 * Asks visualizer to display next elements
	 * @param number	how much elements should be displayed additionally
	 */
	void showNext(int number);

	/**
	 * Asks visualizer to display next elements
	 */
	void showNext();

	/**
	 * Asks visualizer to reset displaying state
	 */
	void reset();

	/**
	 * @return an information for user about how much elements will be displayed
	 *         additionally
	 */
	String getIncrementString();

	/**
	 * @return number of elements displaying currently
	 */
	int getDisplayedJustificationCount();
}