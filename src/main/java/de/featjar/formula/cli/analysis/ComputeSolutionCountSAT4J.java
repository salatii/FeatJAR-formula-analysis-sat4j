/*
 * Copyright (C) 2023 Elias Kuiter
 *
 * This file is part of FeatJAR-cli.
 *
 * cli is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3.0 of the License,
 * or (at your option) any later version.
 *
 * cli is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with cli. If not, see <https://www.gnu.org/licenses/>.
 *
 * See <https://github.com/FeatureIDE/FeatJAR-cli> for further information.
 */
package de.featjar.formula.cli.analysis;

import de.featjar.base.computation.IComputation;
import de.featjar.formula.analysis.VariableMap;
import de.featjar.formula.analysis.bool.BooleanClauseList;

import java.math.BigInteger;

public class ComputeSolutionCountSAT4J extends ASAT4JAnalysisCommand<BigInteger, BigInteger> {
    @Override
    public String getDescription() {
        return "Queries SAT4J for the number of solutions of a given formula";
    }

    @Override
    public de.featjar.formula.analysis.sat4j.ComputeSolutionCountSAT4J newAnalysis(
            IComputation<BooleanClauseList> clauseList) {
        return new de.featjar.formula.analysis.sat4j.ComputeSolutionCountSAT4J(clauseList);
    }

    @Override
    public IComputation<BigInteger> interpret(IComputation<BigInteger> count, IComputation<VariableMap> variableMap) {
        return count;
    }
}
