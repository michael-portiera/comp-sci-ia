package model.solver.std;

import model.math.Expression;
import model.math.Term;
import model.solver.Solver;

public class AdditionSolver extends Solver 
{
    @Override
    public Term Evaluate(Expression e) 
    {
        Term l = e.l.GetTerm();
        Term r = e.r.GetTerm();
        
        double v = l.GetV() + r.GetV();
        double u = l.GetU() + r.GetU();
        return new Term(v, u);
    }
    
    @Override
    public String GetSymbol() { return "+"; }
    
    @Override
    public int GetPriorityLevel() { return 1; }
}
