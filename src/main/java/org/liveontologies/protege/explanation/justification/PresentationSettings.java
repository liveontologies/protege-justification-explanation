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
 * Date: 23/03/2017
 */

public class PresentationSettings {
	private int explanationsDisplayedCnt = 0;
	
	public int getIncrement() {
		return PresentationPreferencesPanel.getIncrement();
	}
	
	public int getInitialAmount() {
		return PresentationPreferencesPanel.getInitialAmount();
	}
	
	public void setCurrentCount(int count) {
		explanationsDisplayedCnt = count;
	}

	public int getCurrentCount() {
		return explanationsDisplayedCnt;
	}
}