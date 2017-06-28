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