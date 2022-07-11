/* -----------------------------------------------------------------------------
 * formula-analysis-sat4j - Analysis of propositional formulas using Sat4j
 * Copyright (C) 2022 Sebastian Krieter
 * 
 * This file is part of formula-analysis-sat4j.
 * 
 * formula-analysis-sat4j is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3.0 of the License,
 * or (at your option) any later version.
 * 
 * formula-analysis-sat4j is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with formula-analysis-sat4j. If not, see <https://www.gnu.org/licenses/>.
 * 
 * See <https://github.com/FeatJAR/formula-analysis-sat4j> for further information.
 * -----------------------------------------------------------------------------
 */
package org.spldev.analysis.sat4j.twise;

import java.util.*;

import org.spldev.clauses.solutions.combinations.*;
import org.spldev.clauses.solutions.combinations.IteratorFactory.*;

/**
 * Combines multiple {@link CombinationIterator iterators} and returns results
 * from each iterator by turns.
 *
 * @author Sebastian Krieter
 */
public class MergeIterator implements CombinationIterator {

	protected final List<CombinationIterator> setIterators;
	protected final long numberOfCombinations;
	protected final int t;

	private int iteratorIndex = -1;

	public MergeIterator(int t, List<List<PresenceCondition>> expressionSets, IteratorID id) {
		this.t = t;
		setIterators = new ArrayList<>(expressionSets.size());
		long sumNumberOfCombinations = 0;
		for (final List<PresenceCondition> expressions : expressionSets) {
			final CombinationIterator iterator = IteratorFactory.getIterator(id, expressions.size(), t);
			setIterators.add(iterator);
			sumNumberOfCombinations += iterator.size();
		}
		numberOfCombinations = sumNumberOfCombinations;
	}

	@Override
	public boolean hasNext() {
		for (final CombinationIterator iterator : setIterators) {
			if (iterator.hasNext()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int[] next() {
		for (int i = 0; i < setIterators.size(); i++) {
			iteratorIndex = (iteratorIndex + 1) % setIterators.size();
			final CombinationIterator iterator = setIterators.get(iteratorIndex);
			if (iterator.hasNext()) {
				final int[] next = iterator.next();
				if (next != null) {
					return next;
				}
			}
		}
		return null;
	}

	@Override
	public long getIndex() {
		long mergedIndex = setIterators.get(iteratorIndex).getIndex();
		for (int i = iteratorIndex - 1; i >= 0; i--) {
			mergedIndex += setIterators.get(i).size();
		}
		return mergedIndex;
	}

	@Override
	public void reset() {
		iteratorIndex = 0;
		for (final CombinationIterator iterator : setIterators) {
			iterator.reset();
		}
	}

	@Override
	public Iterator<int[]> iterator() {
		return this;
	}

	@Override
	public long size() {
		return numberOfCombinations;
	}

}
