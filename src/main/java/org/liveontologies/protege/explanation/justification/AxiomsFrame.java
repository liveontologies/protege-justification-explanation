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

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.frame.AbstractOWLFrame;

/**
 * Author: Matthew Horridge Stanford University Bio-Medical Informatics Research
 * Group Date: 19/03/2012
 */

public class AxiomsFrame extends AbstractOWLFrame<Explanation> {

	private final OWLEditorKit editorKit_;
	private final LoadJustificationsSection showMoreSection_;

	public AxiomsFrame(OWLEditorKit editorKit, Explanation explanation) {
		super(editorKit.getOWLModelManager().getOWLOntologyManager());
		editorKit_ = editorKit;

		setRootObject(explanation);

		showMoreSection_ = new LoadJustificationsSection(editorKit_, this);

		clear();
	}

	public void addSection(int index, String caption) {
		addSection(new AxiomsFrameSection(editorKit_, this, caption, index),
				getSectionCount() - 1);
		refill();
	}

	public void clear() {
		clearSections();
		addSection(showMoreSection_);
		refill();
	}
}