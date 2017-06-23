package org.liveontologies.protege.explanation.justification;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;

public class JustificationFactory {

	private final ReferenceQueue<Justification> garbage_ = new ReferenceQueue<>();

	private final Map<Set<OWLAxiom>, WeakJustification> cache_ = new HashMap<>();

	public Justification createJustification(Set<OWLAxiom> axioms) {
		cleanGarbage();
		WeakJustification cached = cache_.get(axioms);
		if (cached == null) {
			cached = new WeakJustification(new Justification(axioms));
		}
		return cached.get();
	}

	private void cleanGarbage() {
		for (;;) {
			WeakJustification next = (WeakJustification) garbage_.poll();
			if (next == null) {
				break;
			}
			cache_.remove(next.key_);
		}
	}

	class WeakJustification extends WeakReference<Justification> {

		private final Set<OWLAxiom> key_;

		public WeakJustification(Justification referent) {
			super(referent, garbage_);
			this.key_ = referent.getAxioms();
			cache_.put(key_, this);
		}

	}

}
