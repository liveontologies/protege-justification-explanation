package org.liveontologies.protege.explanation.justification.priority;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;

/**
 * A factory for creating {@link JustificationPriority}
 * 
 * @author Yevgeny Kazakov
 *
 */
public class JustificationPriorityFactory {

	private Map<OWLAxiom, CacheRecord> cache_ = new HashMap<>();

	private final Set<OwlAxiomType> axiomTypes_ = EnumSet
			.noneOf(OwlAxiomType.class);

	private final Set<ClassExpressionType> classExpressionTypes_ = EnumSet
			.noneOf(ClassExpressionType.class);

	public JustificationPriority getPriority(
			Set<? extends OWLAxiom> justification) {
		int size = 0;
		axiomTypes_.clear();
		classExpressionTypes_.clear();
		for (OWLAxiom axiom : justification) {
			size++;
			CacheRecord record = cache_.get(axiom);
			if (record == null) {
				record = new CacheRecord(axiom);
				cache_.put(axiom, record);
			}
			axiomTypes_.add(record.axiomType_);
			classExpressionTypes_.addAll(record.classExpressionTypes_);
		}
		return new JustificationPriority(axiomTypes_.size(),
				classExpressionTypes_.size(), size);
	}

	static class CacheRecord {

		private final OwlAxiomType axiomType_;

		private final Set<ClassExpressionType> classExpressionTypes_ = EnumSet
				.noneOf(ClassExpressionType.class);

		CacheRecord(OWLAxiom axiom) {
			this.axiomType_ = OwlAxiomTypes.getAxiomType(axiom);
			classExpressionTypes_.addAll(axiom.getNestedClassExpressions()
					.stream().map(OWLClassExpression::getClassExpressionType)
					.collect(Collectors.toList()));
		}

	}

}
