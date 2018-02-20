/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import model.Project;
import model.table.Constant;
import model.table.DataSet;
import model.table.Set;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author mvpor
 */
public class CSVExporter 
{
    public static void Export(File f, Project p) throws Exception
    {
        p.Calculate();
        PrintWriter pw = new PrintWriter(f);
        List<Set> sets = p.GetSets();
        
        for (int i = 0; i < p.GetConstantCount(); i++)
        {
            Constant con = p.GetConstant(i);
            
            String text = con.GetName();
            text += " / ";
            text += "± " + con.GetValue().GetU();
            if (con.GetPercentageUnc())
                text += "%";
            text = Symbols.ToUnicodeSymbols(text);
            pw.write(StringEscapeUtils.escapeCsv(text) + ",");
            pw.write(con.GetValue() + "\n");
        }
        
        pw.write("\n");
        
        for (int i = 0; i < sets.size(); i++)
        {
            Set s = sets.get(i);
            String text = s.GetName();
            if (!s.GetUnits().isEmpty())
            {
                text += " / ";
                try 
                {
                    DataSet ds = (DataSet) s;
                    if (ds.GetFixedUnc())
                    {
                        text += "± " + ds.GetUnc();
                        if (ds.GetPercentageUnc())
                        {
                            text += "%";
                        }
                        else
                        {
                            text += " " + s.GetUnits();
                        }
                    }
                    else
                    {
                        text += s.GetUnits();
                    }
                }
                catch(Exception e)
                {
                    text += s.GetUnits();
                }
                
            }
            text = Symbols.ToUnicodeSymbols(text);
            pw.write(StringEscapeUtils.escapeCsv(text) + ",");
            try 
            {
                DataSet ds = (DataSet) s;
                if (!ds.GetFixedUnc())
                pw.write("< Merge <,");
            }
            catch(Exception e)
            {
                pw.write("< Merge <,");
            }
        }
        
        pw.write("\n");
        
        for (int i = 0; i < sets.size(); i++)
        {
            Set s = sets.get(i);
            pw.write(StringEscapeUtils.escapeCsv("mag") + ",");
            try 
            {
                DataSet ds = (DataSet) s;
                if (!ds.GetFixedUnc())
                {
                    if (ds.GetPercentageUnc())
                    {
                        pw.write(StringEscapeUtils.escapeCsv("% unc") + ",");
                    }
                    else
                    {
                        pw.write(StringEscapeUtils.escapeCsv("unc") + ",");
                    }
                }
            }
            catch(Exception e)
            {
                pw.write(StringEscapeUtils.escapeCsv("unc") + ",");
            }
        }
        
        pw.print("\n");
        
        int rows = p.GetTotalRows();
        for (int r = 0; r < rows; r++)
        {
            for (int i = 0; i < sets.size(); i++)
            {
                Set s = sets.get(i);
                try 
                {
                    DataSet ds = (DataSet) s;
                    pw.write(StringEscapeUtils.escapeCsv(s.GetData(r).GetV() + "") + ",");
                    if (!ds.GetFixedUnc())
                    {
                        if (ds.GetPercentageUnc())
                        {
                            pw.write(StringEscapeUtils.escapeCsv(s.GetData(r).GetU() + "%") + ",");
                        }
                        else
                        {
                            pw.write(StringEscapeUtils.escapeCsv(s.GetData(r).GetU() + "") + ",");
                        }
                    }
                }
                catch(Exception e)
                {
                    pw.write(StringEscapeUtils.escapeCsv(s.GetData(r).GetV() + "") + ",");
                    pw.write(StringEscapeUtils.escapeCsv(s.GetData(r).GetU() + "") + ",");
                }
                
            }
            pw.print("\n");
        }
        
        pw.close();
        
    }
}
