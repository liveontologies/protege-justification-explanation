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

public class JustificationFrame extends AbstractOWLFrame<Explanation> {

	private final OWLEditorKit editorKit_;
	private final AddJustificationsSection addJustificationsSection_;
	private boolean isAddJustificationsSectionVisible_;

	public JustificationFrame(OWLEditorKit editorKit, Explanation explanation,
			ShowMoreListener showMoreListener) {
		super(editorKit.getOWLModelManager().getOWLOntologyManager());
		editorKit_ = editorKit;

		setRootObject(explanation);

		addJustificationsSection_ = new AddJustificationsSection(editorKit_,
				this, showMoreListener);

		setAddJustificationsSectionVisibility(true);
	}

	public void addSection(int index, String caption) {
		JustificationFrameSection newSection = new JustificationFrameSection(
				editorKit_, this, caption, index);
		addSection(newSection, getSectionCount()
				- (getAddJustificationsSectionVisibility() ? 1 : 0));
		newSection.setRootObject(getRootObject());
	}

	public void clear() {
		clearSections();
		if (getAddJustificationsSectionVisibility())
			addSection(addJustificationsSection_);
		refill();
	}

	public void setAddJustificationsSectionVisibility(boolean isVisible) {
		if (isAddJustificationsSectionVisible_ == isVisible)
			return;
		isAddJustificationsSectionVisible_ = isVisible;
		if (isVisible) {
			addSection(addJustificationsSection_);
			addJustificationsSection_.setRootObject(getRootObject());
		} else {
			getFrameSections().remove(addJustificationsSection_);
			fireContentChanged();
		}
	}

	public boolean getAddJustificationsSectionVisibility() {
		return isAddJustificationsSectionVisible_;
	}

	@Override
	public void dispose() {
		super.dispose();
		addJustificationsSection_.dispose();
	}
}