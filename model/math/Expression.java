package model.math;

import java.util.LinkedList;
import java.util.List;
import model.solver.Solver;

public class Expression extends Element
{
    public Element l;
    public Element r;
    public String o;
    
    public Expression(Element l, String o, Element r)
    {
        this.l = l;
        this.o = o;
        this.r = r;
    }

    @Override
    public Term GetTerm() 
    {
        Term t = Solver.Solve(this);
        return t;
    }
    
    @Override
    public String toString()
    {
        return "(" + l + o + r + ")";
    }

    @Override
    public void SetVariable(String name, Term t) 
    {
        l.SetVariable(name, t);
        r.SetVariable(name, t);
    }

    @Override
    public List<String> ListVariables() 
    {
        LinkedList<String> vars = new LinkedList<String>();
        vars.addAll(l.ListVariables());
        vars.addAll(r.ListVariables());
        return vars;
    }

    @Override
    public String GetString() 
    {
        return "(" + l.GetString() + o + r.GetString() + ")";
    }
}
