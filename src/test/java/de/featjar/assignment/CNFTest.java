/*
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
 * See <https://github.com/FeatureIDE/FeatJAR-formula-analysis-sat4j> for further information.
 */
package de.featjar.assignment;

import static org.junit.jupiter.api.Assertions.fail;

import de.featjar.formula.analysis.Analysis;
import de.featjar.analysis.mig.ConditionallyCoreDeadAnalysisMIG;
import de.featjar.analysis.sat4j.AddRedundancyAnalysis;
import de.featjar.analysis.sat4j.AtomicSetAnalysis;
import de.featjar.analysis.sat4j.CauseAnalysis;
import de.featjar.analysis.sat4j.ContradictionAnalysis;
import de.featjar.analysis.sat4j.CoreDeadAnalysis;
import de.featjar.analysis.sat4j.CountSolutionsAnalysis;
import de.featjar.analysis.sat4j.HasSolutionAnalysis;
import de.featjar.analysis.sat4j.IndependentContradictionAnalysis;
import de.featjar.analysis.sat4j.IndependentRedundancyAnalysis;
import de.featjar.analysis.sat4j.IndeterminateAnalysis;
import de.featjar.analysis.sat4j.RemoveRedundancyAnalysis;
import de.featjar.formula.clauses.CNF;
import de.featjar.formula.clauses.LiteralList;
import de.featjar.formula.structure.formula.predicate.Literal;
import de.featjar.formula.tmp.TermMap;
import de.featjar.formula.structure.formula.connective.And;
import de.featjar.formula.structure.formula.connective.Or;
import de.featjar.transform.CNFSlicer;
import de.featjar.base.data.Problem;
import de.featjar.base.data.Result;
import de.featjar.base.task.Executor;
import de.featjar.base.log.Log;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CNFTest {

    @Test
    public void testAnalyses() {
        final TermMap variables = new TermMap();
        final Literal a = variables.createLiteral("a");
        final Literal b = variables.createLiteral("b");
        final Literal c = variables.createLiteral("c");
        final Literal d = variables.createLiteral("d");
        final Literal e = variables.createLiteral("e");

        final And formula = new And(
                new Or(d),
                new Or(e.invert()),
                new Or(a, b),
                new Or(a.invert(), c),
                new Or(d, b, e.invert()),
                new Or(b.invert(), c, d),
                new Or(c.invert(), d.invert(), e.invert()));

        final ModelRepresentation rep = new ModelRepresentation(formula);

        executeAnalysis(rep, new HasSolutionAnalysis());
        executeAnalysis(rep, new AddRedundancyAnalysis());
        executeAnalysis(rep, new AtomicSetAnalysis());
        executeAnalysis(rep, new CauseAnalysis());
        executeAnalysis(rep, new ContradictionAnalysis());
        executeAnalysis(rep, new CoreDeadAnalysis());
        executeAnalysis(rep, new CountSolutionsAnalysis());
        executeAnalysis(rep, new IndependentContradictionAnalysis());
        executeAnalysis(rep, new IndependentRedundancyAnalysis());
        executeAnalysis(rep, new IndeterminateAnalysis());
        executeAnalysis(rep, new RemoveRedundancyAnalysis());
        executeAnalysis(rep, new ConditionallyCoreDeadAnalysisMIG());

        final CNF cnf = rep.get(CNFComputation.fromFormula());
        final CNFSlicer slicer = new CNFSlicer(new LiteralList(2));
        final CNF slicedCNF = Executor.apply(slicer, cnf).orElse(Log::problems);

        cnf.adapt(slicedCNF.getVariableMap()).orElse(Log::problems);
        slicedCNF.adapt(cnf.getVariableMap()).orElse(Log::problems);
    }

    private void executeAnalysis(ModelRepresentation rep, Analysis<?> analysis) {
        final Result<?> result = rep.getResult(analysis);
        Feat.log().info(analysis.getClass().getName());
        result.map(Object::toString).orElse(this::reportProblems);
    }

    private void reportProblems(List<Problem> problems) {
        Log.problems(problems);
        fail();
    }
}
