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

import java.awt.*;
import org.protege.editor.owl.ui.explanation.ExplanationResult;

/**
 * Author: Matthew Horridge Stanford University Bio-Medical Informatics Research
 * Group Date: 18/03/2012
 */

public class PresentationPanelResult extends ExplanationResult {

	private static final long serialVersionUID = -92356477757932140L;

	private final PresentationPanel panel_;

	public PresentationPanelResult(PresentationPanel panel) {
		panel_ = panel;
		setLayout(new BorderLayout());
		add(panel_);
	}

	@Override
	public void dispose() {
		panel_.dispose();
	}
}