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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.liveontologies.protege.explanation.justification.priority.PrioritizedJustification;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;

import uk.ac.manchester.cs.owl.explanation.ordering.ExplanationOrderer;
import uk.ac.manchester.cs.owl.explanation.ordering.ExplanationTree;
import uk.ac.manchester.cs.owl.explanation.ordering.Tree;
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
 * Group Date: 23-Oct-2008
 */

public class JustificationFormattingManager {

	private final static JustificationFormattingManager instance_ = new JustificationFormattingManager();

	private final Map<PrioritizedJustification, Map<OWLAxiom, Integer>> indents_;
	private final Map<PrioritizedJustification, List<OWLAxiom>> ordering_;

	private JustificationFormattingManager() {
		indents_ = new HashMap<>();
		ordering_ = new HashMap<>();
	}

	public static JustificationFormattingManager getInstance() {
		return instance_;
	}

	private void init(OWLAxiom entailment, PrioritizedJustification justification) {
		ExplanationOrderer orderer = new ProtegeExplanationOrderer(
				OWLManager.createOWLOntologyManager());
		ExplanationTree tree = orderer.getOrderedExplanation(entailment,
				justification.getAxioms());
		List<OWLAxiom> ordering = new ArrayList<>();
		Map<OWLAxiom, Integer> im = new HashMap<>();
		fill(tree, ordering, im);
		indents_.put(justification, im);
		ordering_.put(justification, ordering);

	}

	private static void fill(Tree<OWLAxiom> tree, List<OWLAxiom> ordering,
			Map<OWLAxiom, Integer> indents) {
		if (!tree.isRoot()) {
			ordering.add(tree.getUserObject());
			indents.put(tree.getUserObject(), tree.getPathToRoot().size() - 2);
		} else if (tree.getChildCount() == 0) {
			ordering.add(tree.getUserObject());
			indents.put(tree.getUserObject(), 0);
		}
		for (Tree<OWLAxiom> child : tree.getChildren()) {
			fill(child, ordering, indents);
		}
	}

	private void initIfNecessary(OWLAxiom entailment,
			PrioritizedJustification justification) {
		if (!indents_.containsKey(justification)) {
			init(entailment, justification);
		}
	}

	public int getIndentation(OWLAxiom entailment, PrioritizedJustification justification,
			OWLAxiom axiom) {
		if (!justification.getAxioms().contains(axiom)) {
			throw new IllegalArgumentException(
					"The explanation does not contain the specified axiom: "
							+ axiom + "  " + justification);
		}
		initIfNecessary(entailment, justification);
		Integer i = indents_.get(justification).get(axiom);
		if (i != null) {
			return i;
		} else {
			return 0;
		}
	}

	public void setIndentation(OWLAxiom entailment, PrioritizedJustification justification,
			OWLAxiom axiom, int indentation) {
		initIfNecessary(entailment, justification);
		indents_.get(justification).put(axiom, indentation);
	}

	public void increaseIndentation(OWLAxiom entailment,
			PrioritizedJustification justification, OWLAxiom axiom) {
		initIfNecessary(entailment, justification);
		Integer indent = getIndentation(entailment, justification, axiom);
		setIndentation(entailment, justification, axiom, indent + 1);
	}

	public void decreaseIndentation(OWLAxiom entailment,
			PrioritizedJustification justification, OWLAxiom axiom) {
		initIfNecessary(entailment, justification);
		Integer indent = getIndentation(entailment, justification, axiom);
		indent = indent - 1;
		if (indent < 0) {
			indent = 0;
		}
		setIndentation(entailment, justification, axiom, indent);
	}

	public boolean moveAxiomUp(OWLAxiom entailment, PrioritizedJustification justification,
			OWLAxiom axiom) {
		initIfNecessary(entailment, justification);
		List<OWLAxiom> ordering = ordering_.get(justification);
		// Lowest index is 1 - the entailment is held in position 0
		int index = ordering.indexOf(axiom);
		boolean hasMoved = index > 0;
		if (hasMoved) {
			index--;
		}
		ordering.remove(axiom);
		ordering.add(index, axiom);
		return hasMoved;
	}

	public boolean moveAxiomDown(OWLAxiom entailment,
			PrioritizedJustification justification, OWLAxiom axiom) {
		initIfNecessary(entailment, justification);
		List<OWLAxiom> ordering = ordering_.get(justification);
		// Lowest index is 1 - the entailment is held in position 0
		int index = ordering.indexOf(axiom);
		boolean hasMoved = index < ordering.size() - 1;
		if (hasMoved) {
			index++;
		}
		ordering.remove(axiom);
		ordering.add(index, axiom);
		return hasMoved;
	}

	public List<OWLAxiom> getOrdering(OWLAxiom entailment,
			PrioritizedJustification justification) {
		initIfNecessary(entailment, justification);
		return Collections.unmodifiableList(ordering_.get(justification));
	}

	public void clearFormatting(PrioritizedJustification justification) {
		indents_.remove(justification);
		ordering_.remove(justification);
	}
}