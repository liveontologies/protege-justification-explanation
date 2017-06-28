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
 * The enumeration of all types of {@link OWLAxiom} for the lack of such in OWL
 * API.
 * 
 * @author Yevgeny Kazakov
 *
 */
public enum OwlAxiomType {

	//@formatter:off
	OWLDeclarationAxiom(OWLDeclarationAxiom.class),
	OWLSubClassOfAxiom(OWLSubClassOfAxiom.class),
	OWLEquivalentClassesAxiom(OWLEquivalentClassesAxiom.class),
	OWLDisjointClassesAxiom(OWLDisjointClassesAxiom.class),
	OWLDisjointUnionAxiom(OWLDisjointUnionAxiom.class),
	OWLClassAssertionAxiom(OWLClassAssertionAxiom.class),
	OWLSameIndividualAxiom(OWLSameIndividualAxiom.class),
	OWLDifferentIndividualsAxiom(OWLDifferentIndividualsAxiom.class),
	OWLObjectPropertyAssertionAxiom(OWLObjectPropertyAssertionAxiom.class),
	OWLNegativeObjectPropertyAssertionAxiom(OWLNegativeObjectPropertyAssertionAxiom.class),
	OWLDataPropertyAssertionAxiom(OWLDataPropertyAssertionAxiom.class),
	OWLNegativeDataPropertyAssertionAxiom(OWLNegativeDataPropertyAssertionAxiom.class),
	OWLEquivalentObjectPropertiesAxiom(OWLEquivalentObjectPropertiesAxiom.class),
	OWLSubObjectPropertyOfAxiom(OWLSubObjectPropertyOfAxiom.class),
	OWLInverseObjectPropertiesAxiom(OWLInverseObjectPropertiesAxiom.class),
	OWLFunctionalObjectPropertyAxiom(OWLFunctionalObjectPropertyAxiom.class),
	OWLInverseFunctionalObjectPropertyAxiom(OWLInverseFunctionalObjectPropertyAxiom.class),
	OWLSymmetricObjectPropertyAxiom(OWLSymmetricObjectPropertyAxiom.class),
	OWLAsymmetricObjectPropertyAxiom(OWLAsymmetricObjectPropertyAxiom.class),
	OWLTransitiveObjectPropertyAxiom(OWLTransitiveObjectPropertyAxiom.class),
	OWLReflexiveObjectPropertyAxiom(OWLReflexiveObjectPropertyAxiom.class),
	OWLIrreflexiveObjectPropertyAxiom(OWLIrreflexiveObjectPropertyAxiom.class),
	OWLObjectPropertyDomainAxiom(OWLObjectPropertyDomainAxiom.class),
	OWLObjectPropertyRangeAxiom(OWLObjectPropertyRangeAxiom.class),
	OWLDisjointObjectPropertiesAxiom(OWLDisjointObjectPropertiesAxiom.class),
	OWLSubPropertyChainOfAxiom(OWLSubPropertyChainOfAxiom.class),
	OWLEquivalentDataPropertiesAxiom(OWLEquivalentDataPropertiesAxiom.class),
	OWLSubDataPropertyOfAxiom(OWLSubDataPropertyOfAxiom.class),
	OWLFunctionalDataPropertyAxiom(OWLFunctionalDataPropertyAxiom.class),
	OWLDataPropertyDomainAxiom(OWLDataPropertyDomainAxiom.class),
	OWLDataPropertyRangeAxiom(OWLDataPropertyRangeAxiom.class),
	OWLDisjointDataPropertiesAxiom(OWLDisjointDataPropertiesAxiom.class),
	OWLDatatypeDefinitionAxiom(OWLDatatypeDefinitionAxiom.class),
	OWLHasKeyAxiom(OWLHasKeyAxiom.class),
	SWRLRule(SWRLRule.class),
	OWLAnnotationAssertionAxiom(OWLAnnotationAssertionAxiom.class),
	OWLSubAnnotationPropertyOfAxiom(OWLSubAnnotationPropertyOfAxiom.class),
	OWLAnnotationPropertyRangeAxiom(OWLAnnotationPropertyRangeAxiom.class),
	OWLAnnotationPropertyDomainAxiom(OWLAnnotationPropertyDomainAxiom.class);
	  //@formatter:on

	private final Class<? extends OWLAxiom> axiomType_;

	OwlAxiomType(Class<? extends OWLAxiom> axiomType) {
		this.axiomType_ = axiomType;
	}

	public Class<? extends OWLAxiom> getAxiomType() {
		return axiomType_;
	}
}