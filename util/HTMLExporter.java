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
import model.table.DataSet;
import model.table.Set;

/**
 *
 * @author mvpor
 */
public class HTMLExporter 
{
    public static void Export(File f, Project p) throws Exception
    {
        p.Calculate();
        PrintWriter pw = new PrintWriter(f);
        List<Set> sets = p.GetSets();
        
        /*
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
        */
        
        pw.write("<html><table>");
        
        
        pw.write("<tr>");
        for (int i = 0; i < sets.size(); i++)
        {
            Set s = sets.get(i);
            DataSet ds = null;
            try 
            { 
                ds = (DataSet) s; 
                if (!ds.GetFixedUnc())
                    pw.write("<th colspan=\"2\">");
                else
                    pw.write("<th>");
            } 
            catch (Exception e) 
            {
                pw.write("<th colspan=\"2\">");
            }
            
            String text = s.GetName();
            if (!s.GetUnits().isEmpty())
            {
                text += " / ";
                try 
                {
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
            pw.write(Html.Convert(text));
            pw.write("</th>");
        }
        pw.write("</tr>");
        
        
        
        pw.write("<tr>");
        for (int i = 0; i < sets.size(); i++)
        {
            Set s = sets.get(i);
            pw.write("<th>mag</th>");
            try 
            {
                DataSet ds = (DataSet) s;
                if (!ds.GetFixedUnc())
                {
                    if (ds.GetPercentageUnc())
                    {
                        pw.write("<th>% unc</th>");
                    }
                    else
                    {
                        pw.write("<th>unc</th>");
                    }
                }
            }
            catch(Exception e)
            {
                pw.write("<th>unc</th>");
            }
        }
        pw.write("</tr>");
        
        int rows = p.GetTotalRows();
        for (int r = 0; r < rows; r++)
        {
            pw.write("<tr>");
            for (int i = 0; i < sets.size(); i++)
            {
                Set s = sets.get(i);
                try 
                {
                    DataSet ds = (DataSet) s;
                    pw.write( "<td>" + s.GetData(r).GetV() + "</td>");
                    if (!ds.GetFixedUnc())
                    {
                        if (ds.GetPercentageUnc())
                        {
                            pw.write( "<td>" + s.GetData(r).GetU() + "</td>");
                        }
                        else
                        {
                            pw.write( "<td>" + s.GetData(r).GetU() + "</td>");
                        }
                    }
                }
                catch(Exception e)
                {
                    pw.write( "<td>" + s.GetData(r).GetV() + "</td>");
                    pw.write( "<td>" + s.GetData(r).GetU() + "</td>");
                }
                
            }
            pw.write("</tr>");
        }
        
        pw.write("</table></html>");
        
        pw.close();
        
    }
}
