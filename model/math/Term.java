package model.math;

import java.util.LinkedList;
import java.util.List;

public class Term extends Element
{
    private Double v;
    private Double u;
    
    public Term(Double v, Double u)
    {
        this.v = v;
        this.u = u;
    }
    
    public Term(String s)
    {
        String[] parts = s.split(",");
        if (parts[0].equals("null")) this.v = null;
        else this.v = Double.parseDouble(parts[0]);
        if (parts[1].equals("null")) this.u = null;
        else this.u = Double.parseDouble(parts[1]);
    }
    
    @Override
    public Term GetTerm() 
    {
        return this;
    }
    
    public Double GetV()
    {
        return v;
    }
    
    public Double GetU()
    {
        return u;
    }
    
    public void SetV(Double v)
    {
        this.v = v;
    }
    
    public void SetU(Double u)
    {
        this.u = u;
    }

    @Override
    public String toString()
    {
        return v + "," + u;// + " ± " + u;
    }
    
    @Override
    public void SetVariable(String name, Term t) 
    {
        return;
    }

    @Override
    public List<String> ListVariables() 
    {
        return new LinkedList<String>();
    }

    @Override
    public String GetString() 
    {
        return v + "";
    }
}
