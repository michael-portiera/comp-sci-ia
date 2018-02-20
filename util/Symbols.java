package util;

import java.util.ArrayList;
import java.util.LinkedList;
import org.apache.commons.lang3.StringEscapeUtils;

public class Symbols 
{
    public static String ToUnicodeSymbols(String s)
    {
        return StringEscapeUtils.unescapeHtml4(s);
    }
}
