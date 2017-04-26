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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.*;

import org.liveontologies.protege.explanation.justification.service.ComputationService;
import org.liveontologies.protege.explanation.justification.service.ComputationServiceListener;
import org.liveontologies.protege.explanation.justification.service.ComputationServiceListenerManager;
import org.liveontologies.protege.explanation.justification.service.JustificationComputation;
import org.liveontologies.protege.explanation.justification.service.JustificationComputationListener;
import org.protege.editor.core.log.LogBanner;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationGeneratorInterruptedException;
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
 * Group Date: 03-Oct-2008
 */

public class PresentationManager {

	private static final Logger logger = LoggerFactory
			.getLogger(PresentationManager.class);
	public static final Marker MARKER = MarkerFactory.getMarker("Presentation");

	private final OWLAxiom entailment_;
	private final JustificationComputationServiceManager manager_;
	private final PresentationSettings presentationSettings_;
	private final ExecutorService executorService_;
	private AxiomsCache axiomsCache_;
	private JFrame parentWindow_;
	private ComputationServiceListenerManager listenerManager_ = null;

	public PresentationManager(JFrame parentWindow,
			JustificationComputationServiceManager manager,
			OWLAxiom entailment) {
		entailment_ = entailment;
		manager_ = manager;
		parentWindow_ = parentWindow;
		presentationSettings_ = new PresentationSettings();
		axiomsCache_ = new AxiomsCache();
		executorService_ = Executors.newSingleThreadExecutor();
	}

	public void setComputationServiceListener(
			ComputationServiceListener listener) {
		listenerManager_ = new ComputationServiceListenerManager(listener,
				manager_.getSelectedService());
	}

	public PresentationSettings getPresentationSettings() {
		return presentationSettings_;
	}

	public Collection<ComputationService> getServices() {
		return manager_.getServices();
	}

	public void selectService(ComputationService service) {
		if (listenerManager_ != null)
			listenerManager_.changeService(service);
		manager_.selectService(service);
	}

	public ComputationService getSelectedService() {
		return manager_.getSelectedService();
	}

	public OWLAxiom getEntailment() {
		return entailment_;
	}

	public Set<Explanation<OWLAxiom>> getJustifications() {
		return getJustifications(entailment_);
	}

	public Set<Explanation<OWLAxiom>> getJustifications(OWLAxiom entailment) {
		if (!axiomsCache_.contains(entailment)) {
			Set<Explanation<OWLAxiom>> expls = computeJustifications(
					entailment);
			axiomsCache_.put(expls);
		}
		return axiomsCache_.get(entailment);
	}

	private Set<Explanation<OWLAxiom>> computeJustifications(
			OWLAxiom entailment) {
		if (getSelectedService() == null)
			return null;
		logger.info(LogBanner.start("Computing Explanations"));
		logger.info(MARKER, "Computing explanations for {}", entailment);
		ComputationService service = getSelectedService();

		OWLDataFactory df = manager_.getOWLEditorKit().getOWLModelManager()
				.getOWLDataFactory();
		OWLSubClassOfAxiom impossibleAxiom = df
				.getOWLSubClassOfAxiom(df.getOWLThing(), df.getOWLNothing());

		JustificationComputation computation = (entailment
				.equalsIgnoreAnnotations(impossibleAxiom))
						? service
								.createInconsistentOntologyJustificationComputation()
						: service.createJustificationComputation(entailment);

		ExplanationGeneratorProgressDialog progressDialog = new ExplanationGeneratorProgressDialog(
				parentWindow_, computation);

		ExplanationGeneratorCallable callable = new ExplanationGeneratorCallable(
				entailment, computation, progressDialog);
		try {
			executorService_.submit(callable);
		} catch (ExplanationGeneratorInterruptedException e) {
			logger.info(MARKER,
					"Explanation computation terminated early by user");
		}
		progressDialog.reset();
		progressDialog.setVisible(true);
		HashSet<Explanation<OWLAxiom>> explanations = new HashSet<>(
				callable.found_);
		logger.info(MARKER, "A total of {} explanations have been computed",
				explanations.size());
		logger.info(LogBanner.end());
		return explanations;
	}

	public int getComputedExplanationCount(OWLAxiom entailment) {
		if (axiomsCache_.contains(entailment)) {
			return axiomsCache_.get(entailment).size();
		} else {
			return -1;
		}
	}

	public int getPopularity(OWLAxiom entailment, OWLAxiom axiom) {
		return axiomsCache_.getAxiomPopularity(entailment, axiom);
	}

	public OWLEditorKit getOWLEditorKit() {
		return manager_.getOWLEditorKit();
	}

	public void clearJustificationsCache() {
		axiomsCache_ = new AxiomsCache();
	}

	public String getIdForService(ComputationService service) {
		return manager_.getIdForService(service);
	}

	private class ExplanationGeneratorCallable
			implements Callable<Set<Explanation<OWLAxiom>>>,
			JustificationComputationListener {

		private final OWLAxiom entailment_;
		private final ExplanationGeneratorProgressDialog progressDialog_;
		private final JustificationComputation computation_;
		private Set<Explanation<OWLAxiom>> found_ = new HashSet<>();

		private ExplanationGeneratorCallable(OWLAxiom entailment,
				JustificationComputation computation,
				ExplanationGeneratorProgressDialog progressDialog) {
			entailment_ = entailment;
			progressDialog_ = progressDialog;
			computation_ = computation;
			computation.addComputationListener(this);
		}

		/**
		 * Computes a result, or throws an exception if unable to do so.
		 * 
		 * @return computed result
		 * @throws Exception
		 *             if unable to compute a result
		 */
		public Set<Explanation<OWLAxiom>> call() throws Exception {
			found_ = new HashSet<>();
			progressDialog_.reset();
			try {
				computation_.startComputation();
			} finally {
				SwingUtilities
						.invokeLater(() -> progressDialog_.setVisible(false));
			}
			return found_;
		}

		@Override
		public void foundJustification(Collection<OWLAxiom> justification) {
			found_.add(new Explanation<OWLAxiom>(entailment_,
					new HashSet<>(justification)));
			progressDialog_.setExplanationCount(found_.size());
		}
	}
}