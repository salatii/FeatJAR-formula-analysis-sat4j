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
import de.featjar.formula.analysis.bool.BooleanSolutionList;
import de.featjar.formula.analysis.value.ComputeValueRepresentationOfSolutionList;
import de.featjar.formula.analysis.value.ValueSolutionList;

public class ComputeSolutionsSAT4J extends ASAT4JAnalysisCommand<ValueSolutionList, BooleanSolutionList> {
    @Override
    public String getDescription() {
        return "Queries SAT4J for all solutions of a given formula";
    }

    @Override
    public de.featjar.formula.analysis.sat4j.ComputeSolutionsSAT4J newAnalysis(
            IComputation<BooleanClauseList> clauseList) {
        return new de.featjar.formula.analysis.sat4j.ComputeSolutionsSAT4J(clauseList);
    }

    @Override
    public IComputation<ValueSolutionList> interpret(
            IComputation<BooleanSolutionList> booleanSolutionList, IComputation<VariableMap> variableMap) {
        return new ComputeValueRepresentationOfSolutionList(booleanSolutionList, variableMap);
    }

    @Override
    public String serializeResult(ValueSolutionList valueSolutionList) {
        return valueSolutionList.print();
    }
}
