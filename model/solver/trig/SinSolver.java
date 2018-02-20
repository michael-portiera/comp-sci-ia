package model.solver.trig;

import model.math.Expression;
import model.math.Term;
import model.solver.Solver;

public class SinSolver extends Solver
{
    @Override
    public Term Evaluate(Expression e) 
    {
        Term l = e.l.GetTerm();
        Term r = e.r.GetTerm();
        double v = l.GetV() * Math.sin(r.GetV());
        double u = ((l.GetU() / l.GetV())
                + Math.abs(r.GetU() * Math.cos(r.GetV()) / r.GetV())) * v;
        return new Term(v, u);
    }
    
    @Override
    public String GetSymbol() { return "sin"; }
    
    @Override
    public int GetPriorityLevel() { return 4; }
}
