package util;

import java.util.regex.Pattern;
import model.math.Element;
import model.math.ElementBuilder;
import model.solver.Solver;

public class Html 
{
    public static String Convert(String txt)
    {
        String html = txt;
        //html = html.replace(";", "</span>");
        html = html.replace("^", "<span><sup>");
        html = html.replace("_", "<span><sub>");
        html = html.replace("\\", "</span>");
        return "<span>" + html + "</span>";
        //html = html.replace("_", "<sub>");
        //return null;
    }
}
