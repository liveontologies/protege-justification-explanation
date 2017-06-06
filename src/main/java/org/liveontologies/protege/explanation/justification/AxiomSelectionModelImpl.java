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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.protege.editor.core.Disposable;
import org.semanticweb.owlapi.model.OWLAxiom;
/*
 * Copyright (C) 2008, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/**
 * Author: Matthew Horridge The University Of Manchester Information Management
 * Group Date: 09-Oct-2008
 */

public class AxiomSelectionModelImpl
		implements AxiomSelectionModel, Disposable {

	private final Set<OWLAxiom> selectedAxioms_;
	private final List<AxiomSelectionListener> listeners_;

	public AxiomSelectionModelImpl() {
		selectedAxioms_ = new HashSet<>();
		listeners_ = new ArrayList<>();
	}

	public void clearSelection() {
		while (selectedAxioms_.size() > 0) {
			setAxiomSelected(selectedAxioms_.iterator().next(), false);
		}
	}

	public void setAxiomSelected(OWLAxiom axiom, boolean b) {
		if (b) {
			if (!selectedAxioms_.contains(axiom)) {
				selectedAxioms_.add(axiom);
				fireEvent(axiom, true);
			}
		} else {
			if (selectedAxioms_.contains(axiom)) {
				selectedAxioms_.remove(axiom);
				fireEvent(axiom, false);
			}
		}
	}

	protected void fireEvent(OWLAxiom axiom, boolean added) {
		for (AxiomSelectionListener listener : new ArrayList<>(listeners_)) {
			if (added) {
				listener.axiomAdded(this, axiom);
			} else {
				listener.axiomRemoved(this, axiom);
			}
		}
	}

	public Set<OWLAxiom> getSelectedAxioms() {
		return Collections.unmodifiableSet(selectedAxioms_);
	}

	public void addAxiomSelectionListener(AxiomSelectionListener lsnr) {
		listeners_.add(lsnr);
	}

	public void removeAxiomSelectionListener(AxiomSelectionListener lsnr) {
		listeners_.remove(lsnr);
	}

	@Override
	public void dispose() {
		while (listeners_.size() != 0)
			removeAxiomSelectionListener(listeners_.get(0));
	}
}