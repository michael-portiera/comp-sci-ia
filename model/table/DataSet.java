package model.table;

import java.util.ArrayList;
import model.math.Term;
import util.Html;

public class DataSet extends Set
{   
    private boolean fixed_uncertanty;
    private boolean percentage_uncertanty;
    private double unc;
    
    public DataSet(String name, String units, Term... data)
    {
        this.data = new ArrayList<Term>();
        for (int i = 0; i < data.length; i++) this.data.add(data[i]);
        SetName(name);
        SetUnits(units);
    }
    
    public DataSet(String s)
    {
        String[] parts = s.split("\n");
        SetName(parts[0]);
        SetUnits(parts[1]);
        SetFixedUnc(Boolean.parseBoolean(parts[2]));
        SetPercentageUnc(Boolean.parseBoolean(parts[3]));
        this.data = new ArrayList<Term>();
        for (int i = 4; i < parts.length; i++)
        {
            data.add(new Term(parts[i]));
        }   
    }
    
    public String toString()
    {
        String p = GetName() + "\n" + 
                GetUnits() + "\n" + 
                GetFixedUnc() + "\n" + 
                GetPercentageUnc();
        for (Term t : GetData())
        {
            p += "\n" + t;
        }
        return p;
    }
    
    public double GetUnc()
    {
        return unc;
    }
    
    public boolean GetFixedUnc()
    {
        return fixed_uncertanty;
    }
    
    public boolean GetPercentageUnc()
    {
        return percentage_uncertanty;
    }
    
    public void SetUnc(double unc)
    {
        this.unc = unc;
    }
    
    public void SetFixedUnc(boolean fixed_uncertanty)
    {
        this.fixed_uncertanty = fixed_uncertanty;
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
        if (GetFixedUnc())
        {
            html += " &plusmn; " + GetUnc();
            if (GetPercentageUnc())
            {
                html += "%";
            }
        }
        html += "</html>";
        return html;
    }

    @Override
    public boolean IsCalculated() 
    {
        return true;
    }
}
