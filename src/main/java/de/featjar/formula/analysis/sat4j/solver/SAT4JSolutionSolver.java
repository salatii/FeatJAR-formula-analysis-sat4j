/*
 * Copyright (C) 2023 Sebastian Krieter
 *
 * This file is part of FeatJAR-formula-analysis-sat4j.
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
package de.featjar.formula.analysis.sat4j.solver;

import de.featjar.formula.analysis.bool.BooleanClauseList;
import de.featjar.formula.analysis.sat4j.solver.ISelectionStrategy.FixedStrategy;
import de.featjar.formula.analysis.sat4j.solver.ISelectionStrategy.InverseFixedStrategy;
import de.featjar.formula.analysis.sat4j.solver.ISelectionStrategy.UniformRandomStrategy;
import de.featjar.formula.analysis.sat4j.solver.strategy.FixedLiteralSelectionStrategy;
import de.featjar.formula.analysis.sat4j.solver.strategy.FixedOrderHeap;
import de.featjar.formula.analysis.sat4j.solver.strategy.FixedOrderHeap2;
import de.featjar.formula.analysis.sat4j.solver.strategy.RandomSelectionStrategy;
import de.featjar.formula.analysis.sat4j.solver.strategy.UniformRandomSelectionStrategy;
import java.util.Random;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.minisat.core.IOrder;
import org.sat4j.minisat.core.Solver;
import org.sat4j.minisat.orders.NegativeLiteralSelectionStrategy;
import org.sat4j.minisat.orders.PositiveLiteralSelectionStrategy;
import org.sat4j.minisat.orders.RSATPhaseSelectionStrategy;
import org.sat4j.minisat.orders.VarOrderHeap;

/**
 * ...
 *
 * @author Sebastian Krieter
 */
public class SAT4JSolutionSolver extends SAT4JSolver {
    protected final int[] order;
    protected ISelectionStrategy strategy;

    public SAT4JSolutionSolver(BooleanClauseList clauseList) {
        super(clauseList);
        strategy = ISelectionStrategy.original();
        order = new int[clauseList.getVariableCount()];
        setOrderFix();
        ((Solver<?>) internalSolver).getOrder().init();
    }

    @Override
    protected Solver<?> newInternalSolver() {
        return (Solver<?>) SolverFactory.newDefault();
    }

    public int[] getOrder() {
        return order;
    }

    public ISelectionStrategy getSelectionStrategy() {
        return strategy;
    }

    public void setOrder(int[] order) {
        assert order.length <= this.order.length;
        System.arraycopy(order, 0, this.order, 0, order.length);
    }

    public void setOrderFix() {
        for (int i = 0; i < order.length; i++) {
            order[i] = i + 1;
        }
    }

    public void shuffleOrder() {
        shuffleOrder(new Random());
    }

    public void shuffleOrder(Random rnd) {
        for (int i = order.length - 1; i >= 0; i--) {
            final int index = rnd.nextInt(i + 1);
            final int a = order[index];
            order[index] = order[i];
            order[i] = a;
        }
    }

    private void setSelectionStrategy(IOrder strategy) {
        ((Solver<?>) internalSolver).setOrder(strategy);
        ((Solver<?>) internalSolver).getOrder().init();
    }

    public void setSelectionStrategy(ISelectionStrategy strategy) {
        this.strategy = strategy;
        switch (strategy.strategy()) {
            case FastRandom:
                setSelectionStrategy(new FixedOrderHeap(new RandomSelectionStrategy(), order));
                break;
            case Fixed:
                setSelectionStrategy(
                        new FixedOrderHeap( //
                                new FixedLiteralSelectionStrategy(((FixedStrategy) strategy).getModel()), //
                                order));
                break;
            case InverseFixed:
                setSelectionStrategy(
                        new FixedOrderHeap( //
                                new FixedLiteralSelectionStrategy(((InverseFixedStrategy) strategy).getModel()), //
                                order));
                break;
                //            case MIGRandom:
                //                setSelectionStrategy(new FixedOrderHeap2(
                //                        new UniformRandomSelectionStrategy(((MIGRandomStrategy) strategy).getDist()),
                // order));
                //                break;
            case Negative:
                setSelectionStrategy(new FixedOrderHeap(new NegativeLiteralSelectionStrategy(), order));
                break;
            case Original:
                setSelectionStrategy(new VarOrderHeap(new RSATPhaseSelectionStrategy()));
                break;
            case Positive:
                setSelectionStrategy(new FixedOrderHeap(new PositiveLiteralSelectionStrategy(), order));
                break;
            case UniformRandom:
                setSelectionStrategy(new FixedOrderHeap2(
                        new UniformRandomSelectionStrategy(((UniformRandomStrategy) strategy).getDist()), order));
                break;
            default:
                throw new IllegalStateException(String.valueOf(strategy.strategy()));
        }
    }
}
