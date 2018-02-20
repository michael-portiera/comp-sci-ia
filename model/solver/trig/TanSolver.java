package model.solver.trig;

import model.math.Expression;
import model.math.Term;
import model.solver.Solver;

public class TanSolver extends Solver
{
    @Override
    public Term Evaluate(Expression e) 
    {
        Term l = e.l.GetTerm();
        Term r = e.r.GetTerm();
        double v = l.GetV() * Math.tan(r.GetV());
        double u = ((l.GetU() / l.GetV())
                +  (r.GetU() * Math.pow(Math.cos(r.GetV()),-2) / r.GetV())) * v;
        return new Term(v, u);
    }
    
    @Override
    public String GetSymbol() { return "tan"; }

    @Override
    public int GetPriorityLevel() { return 4; }
}
