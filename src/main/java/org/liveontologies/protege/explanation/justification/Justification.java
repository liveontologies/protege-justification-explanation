package org.liveontologies.protege.explanation.justification;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;

public class Justification implements Comparable<Justification> {

	private Set<OWLAxiom> axioms_;

	private final int axiomTypeCount_, classExpressionTypeCount_, size_;

	public Justification(Set<OWLAxiom> axioms) {
		this.axioms_ = axioms;
		Set<AxiomType<?>> axiomTypes = new HashSet<>();
		Set<ClassExpressionType> classExpressionTypes = new HashSet<>();
		// TODO: slow! cache these values for each axiom
		for (OWLAxiom ax : getAxioms()) {
			axiomTypes.add(ax.getAxiomType());
			classExpressionTypes.addAll(ax.getNestedClassExpressions().stream()
					.map(OWLClassExpression::getClassExpressionType)
					.collect(Collectors.toList()));
		}
		this.axiomTypeCount_ = axiomTypes.size();
		this.classExpressionTypeCount_ = classExpressionTypes.size();
		this.size_ = axioms.size();
	}

	public Set<OWLAxiom> getAxioms() {
		return axioms_;
	}

	public int getSize() {
		return size_;
	}

	public boolean contains(OWLAxiom axiom) {
		return axioms_.contains(axiom);
	}

	@Override
	public int compareTo(Justification o) {
		int diff = axiomTypeCount_ - o.axiomTypeCount_;
		if (diff != 0) {
			return diff;
		}
		diff = classExpressionTypeCount_ - o.classExpressionTypeCount_;
		if (diff != 0) {
			return diff;
		}
		return getSize() - o.getSize();
	}

}