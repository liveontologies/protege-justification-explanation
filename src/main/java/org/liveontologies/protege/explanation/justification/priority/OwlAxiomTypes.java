package org.liveontologies.protege.explanation.justification.priority;

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

import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitorEx;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.SWRLRule;

/**
 * A collection of static methods for manipulating with {@link OwlAxiomType}s
 * 
 * @author Yevgeny Kazakov
 */
public class OwlAxiomTypes {

	private static final OwlAxiomTypeGettingVisitor AXIOM_TYPE_GETTER_ = new OwlAxiomTypeGettingVisitor();

	/**
	 * @param axiom
	 * @return the {@link OwlAxiomType} of the given {@link OWLAxiom}
	 */
	public static OwlAxiomType getAxiomType(OWLAxiom axiom) {
		return axiom.accept(AXIOM_TYPE_GETTER_);
	}

	private static class OwlAxiomTypeGettingVisitor
			implements OWLAxiomVisitorEx<OwlAxiomType> {

		@Override
		public OwlAxiomType visit(OWLAnnotationAssertionAxiom axiom) {
			return OwlAxiomType.OWLAnnotationAssertionAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLSubAnnotationPropertyOfAxiom axiom) {
			return OwlAxiomType.OWLSubAnnotationPropertyOfAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLAnnotationPropertyDomainAxiom axiom) {
			return OwlAxiomType.OWLAnnotationPropertyDomainAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLAnnotationPropertyRangeAxiom axiom) {
			return OwlAxiomType.OWLAnnotationPropertyRangeAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLSubClassOfAxiom axiom) {
			return OwlAxiomType.OWLSubClassOfAxiom;
		}

		@Override
		public OwlAxiomType visit(
				OWLNegativeObjectPropertyAssertionAxiom axiom) {
			return OwlAxiomType.OWLNegativeObjectPropertyAssertionAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLAsymmetricObjectPropertyAxiom axiom) {
			return OwlAxiomType.OWLAsymmetricObjectPropertyAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLReflexiveObjectPropertyAxiom axiom) {
			return OwlAxiomType.OWLReflexiveObjectPropertyAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLDisjointClassesAxiom axiom) {
			return OwlAxiomType.OWLDisjointClassesAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLDataPropertyDomainAxiom axiom) {
			return OwlAxiomType.OWLDataPropertyDomainAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLObjectPropertyDomainAxiom axiom) {
			return OwlAxiomType.OWLObjectPropertyDomainAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLEquivalentObjectPropertiesAxiom axiom) {
			return OwlAxiomType.OWLEquivalentObjectPropertiesAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
			return OwlAxiomType.OWLNegativeDataPropertyAssertionAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLDifferentIndividualsAxiom axiom) {
			return OwlAxiomType.OWLDifferentIndividualsAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLDisjointDataPropertiesAxiom axiom) {
			return OwlAxiomType.OWLDisjointDataPropertiesAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLDisjointObjectPropertiesAxiom axiom) {
			return OwlAxiomType.OWLDisjointObjectPropertiesAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLObjectPropertyRangeAxiom axiom) {
			return OwlAxiomType.OWLObjectPropertyRangeAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLObjectPropertyAssertionAxiom axiom) {
			return OwlAxiomType.OWLObjectPropertyAssertionAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLFunctionalObjectPropertyAxiom axiom) {
			return OwlAxiomType.OWLFunctionalObjectPropertyAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLSubObjectPropertyOfAxiom axiom) {
			return OwlAxiomType.OWLSubObjectPropertyOfAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLDisjointUnionAxiom axiom) {
			return OwlAxiomType.OWLDisjointUnionAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLSymmetricObjectPropertyAxiom axiom) {
			return OwlAxiomType.OWLSymmetricObjectPropertyAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLDataPropertyRangeAxiom axiom) {
			return OwlAxiomType.OWLDataPropertyRangeAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLFunctionalDataPropertyAxiom axiom) {
			return OwlAxiomType.OWLFunctionalDataPropertyAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLEquivalentDataPropertiesAxiom axiom) {
			return OwlAxiomType.OWLEquivalentDataPropertiesAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLClassAssertionAxiom axiom) {
			return OwlAxiomType.OWLClassAssertionAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLEquivalentClassesAxiom axiom) {
			return OwlAxiomType.OWLEquivalentClassesAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLDataPropertyAssertionAxiom axiom) {
			return OwlAxiomType.OWLDataPropertyAssertionAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLTransitiveObjectPropertyAxiom axiom) {
			return OwlAxiomType.OWLTransitiveObjectPropertyAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
			return OwlAxiomType.OWLIrreflexiveObjectPropertyAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLSubDataPropertyOfAxiom axiom) {
			return OwlAxiomType.OWLSubDataPropertyOfAxiom;
		}

		@Override
		public OwlAxiomType visit(
				OWLInverseFunctionalObjectPropertyAxiom axiom) {
			return OwlAxiomType.OWLInverseFunctionalObjectPropertyAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLSameIndividualAxiom axiom) {
			return OwlAxiomType.OWLSameIndividualAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLSubPropertyChainOfAxiom axiom) {
			return OwlAxiomType.OWLSubPropertyChainOfAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLInverseObjectPropertiesAxiom axiom) {
			return OwlAxiomType.OWLInverseObjectPropertiesAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLHasKeyAxiom axiom) {
			return OwlAxiomType.OWLHasKeyAxiom;
		}

		@Override
		public OwlAxiomType visit(SWRLRule rule) {
			return OwlAxiomType.SWRLRule;
		}

		@Override
		public OwlAxiomType visit(OWLDeclarationAxiom axiom) {
			return OwlAxiomType.OWLDeclarationAxiom;
		}

		@Override
		public OwlAxiomType visit(OWLDatatypeDefinitionAxiom axiom) {
			return OwlAxiomType.OWLDatatypeDefinitionAxiom;
		}
	}
}