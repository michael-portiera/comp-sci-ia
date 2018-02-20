package model.table;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import model.math.Element;
import model.math.ElementBuilder;
import model.math.Term;
import util.Html;

public class VariableSet extends Set
{
    private String definition;
    private Element element;
    private List<String> unknowns;
    private boolean calculated;
    
    public VariableSet(String name, String units, String definition)
    {
        SetName(name);
        SetUnits(units);
        SetElementByDefinition(definition);
        data = new ArrayList<Term>();
    }
    
    public VariableSet(String s)
    {
        String[] parts = s.split("\n");
        SetName(parts[0]);
        SetUnits(parts[1]);
        SetElementByDefinition(parts[2]);
        data = new ArrayList<Term>();
    }
    
    public String toString()
    {
        return GetName() + "\n" + GetUnits() + "\n" + GetDefinition();
    }
    
    public boolean CanCalculate(List<String> known)
    {
        return GetUndefined(known).size() == 0;
    }
    
    public List<String> GetUndefined(List<String> known)
    {
        LinkedList<String> undefined = new LinkedList<String>();
        for (String u : unknowns)
        {
            boolean okay = false;
            for (String k : known)
            {
                if (u.equals(k))
                {
                    okay = true;
                    break;
                }
            }
            if (!okay) undefined.add(u);
        }
        return undefined;
    }
    
    public Element GetElement()
    {
        return element;
    }
    
    public String GetDefinition()
    {
        return definition;
    }
    
    public void SetElementByDefinition(String definition)
    {
        this.definition = definition;
        this.element = ElementBuilder.Build(definition);
        Refresh();
    }
    
    public void Refresh()
    {
        unknowns = this.element.ListVariables();
    }
    
    public void Reset()
    {
        GetData().clear();
        SetCalculated(false);
    }
    
    private void SetCalculated(boolean calculated)
    {
        this.calculated = calculated;
    }
    
    @Override
    public boolean IsCalculated() 
    {
        return calculated;
    }

    @Override
    public String GetHTML() 
    {
        String html = "<html>";
        html += Html.Convert(GetName());
        if (!GetUnits().isEmpty())
        {
            html += " (" + Html.Convert(GetUnits()) + ")";
        }
        html += "</html>";
        return html;
    }

    public void SetIsCalculated(boolean b) 
    {
        calculated = b;
    }
}
