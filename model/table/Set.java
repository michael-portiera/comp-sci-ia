package model.table;

import java.util.ArrayList;
import model.math.Term;
import util.HtmlEncodeable;

public abstract class Set implements HtmlEncodeable
{
    protected ArrayList<Term> data;
    protected String name;
    protected String units;
    
    public int GetSize()
    {
        if (!IsCalculated()) return -1;
        return data.size();
    }
    
    public ArrayList<Term> GetData()
    {
        if (!IsCalculated()) return null;
        return data;
    }
    
    public Term GetData(int i)
    {
        return data.get(i);
    }
    
    public String GetName()
    {
        return name;
    }
    
    public String GetUnits()
    {
        return units;
    }
    
    public void AddData(Term t)
    {
        data.add(t);
    }
    
    public void SetData(int i, Term t)
    {
        while (i >= data.size())
            data.add(null);
        data.set(i, t);
    }
    
    public void SetName(String name)
    {
        this.name = name;
    }
    
    public void SetUnits(String units)
    {
        this.units = units;
    }
    
    public abstract boolean IsCalculated();
}
