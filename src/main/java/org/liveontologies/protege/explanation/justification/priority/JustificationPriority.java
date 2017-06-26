package org.liveontologies.protege.explanation.justification.priority;

/**
 * A priority that specifies in which order justifications should be displayed
 * and, preferably, computed
 * 
 * @author Yevgeny Kazakov
 */
public class JustificationPriority
		implements Comparable<JustificationPriority> {

	private final int axiomTypeCount_, classExpressionTypeCount_, size_;

	JustificationPriority(int axiomTypeCount, int classExpressionTypeCount,
			int size) {
		this.axiomTypeCount_ = axiomTypeCount;
		this.classExpressionTypeCount_ = classExpressionTypeCount;
		this.size_ = size;
	}

	int getAxiomTypeCount() {
		return axiomTypeCount_;
	}

	int getClassExpressionTypeCount() {
		return classExpressionTypeCount_;
	}

	int getSize() {
		return size_;
	}

	@Override
	public int compareTo(JustificationPriority o) {
		int diff = axiomTypeCount_ - o.axiomTypeCount_;
		if (diff != 0) {
			return diff;
		}
		diff = classExpressionTypeCount_ - o.classExpressionTypeCount_;
		if (diff != 0) {
			return diff;
		}
		return size_ - o.size_;
	}

	@Override
	public int hashCode() {
		return axiomTypeCount_ * 113 + classExpressionTypeCount_ * 117
				+ size_ * 119;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof JustificationPriority) {
			JustificationPriority other = (JustificationPriority) obj;
			return axiomTypeCount_ == other.axiomTypeCount_
					&& classExpressionTypeCount_ == other.classExpressionTypeCount_
					&& size_ == other.size_;
		}
		// else
		return false;
	}

}
