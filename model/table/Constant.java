package model.table;

import model.math.Term;
import util.Html;
import util.HtmlEncodeable;

public class Constant implements HtmlEncodeable
{
    private String name;
    private String units;
    private Term value;
    private boolean percentage_uncertanty;
    
    public Constant(String name, String units, Term value)
    {
        SetName(name);
        SetUnits(units);
        SetValue(value);
    }
    
    public Constant(String s)
    {
        String[] parts = s.split("\n");
        SetName(parts[0]);
        SetUnits(parts[1]);
        SetValue(new Term(parts[2]));
    }
    
    public String toString()
    {
        return GetName() + "\n" + GetUnits() + "\n" + GetValue();
    }
    
    public String GetName()
    {
        return name;
    }
    
    public String GetUnits()
    {
        return units;
    }
    
    public Term GetValue()
    {
        return value;
    }
    
    public void SetName(String name)
    {
        this.name = name;
    }
    
    public void SetUnits(String units)
    {
        this.units = units;
    }
    
    public void SetValue(Term value)
    {
        this.value = value;
    }
    
    public boolean GetPercentageUnc()
    {
        return percentage_uncertanty;
    }
    
    public void SetPercentageUnc(boolean percentage_uncertanty)
    {
        this.percentage_uncertanty = percentage_uncertanty;
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
        html += " = ";
        Term t = GetValue();
        html += t.GetV() + " &plusmn; " + t.GetU();
        if (GetPercentageUnc()) html += "%";
        html += "</html>";
        return html;
    }

    public void SetVal(double d) 
    {
        value.SetV(d);
    }
    
    public void SetUnc(double d) 
    {
        value.SetU(d);
    }
}
