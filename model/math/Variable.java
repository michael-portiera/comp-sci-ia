package model.math;

import java.util.LinkedList;
import java.util.List;

public class Variable extends Term
{
    private String name;
    
    public Variable(String name) 
    {
        super(0d, 0d);
        SetName(name);
    }
    
    public String GetName()
    {
        return name;
    }
    
    public void SetName(String name)
    {
        this.name = name;
    }
    
    @Override
    public String toString()
    {
        return name;
    }
    
    @Override
    public void SetVariable(String name, Term t) 
    {
        if (this.name.equals(name) && t != null)
        {
            SetV(t.GetV());
            SetU(t.GetU());
        }
        return;
    }

    @Override
    public List<String> ListVariables() 
    {
        LinkedList<String> l = new LinkedList<String>();
        l.add(name);
        return l;
    }
    
    public String GetString()
    {
        return GetName();
    }
}
