package model.math;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import model.solver.Solver;

public class ElementBuilder 
{
    /**
     * Builds an element using the provided String assuming non-numeric non-operators are variables 
     * @param s The String with the representation of the element
     * @return An element described by the String
     */
    public static Element Build(String s)
    {
        //Remove parenthises if present
        String z = Peel(s);
        if (z != null) s = z;
        
        //Split the string into its parts
        List<String> root = Crack(s);
        //Check if its a variable or a term
        if (root.size()==1)
        {
            String k = root.get(0);
            try
            {
                Double v = Double.parseDouble(k);
                return new Term(v, 0d);
            }
            catch (Exception e)
            {
                return new Variable(k);
            }
        }
        //Do order of operations (starting at priority 4)
        for (int p = 4; p > 0; p--)
        {
            //Loop through all tokens
            for (int i = 0; i < root.size(); i++)
            {
                //Current token
                String t = root.get(i);
                //Check that t is a operator and has the correct priority
                if (Solver.CheckPriority(t) == p)
                {
                    //Find a, b, and c where a and c are the 
                    //left and right hand peramaters respectivly
                    //and b is the operator
                    String a = null;
                    String b = t;
                    String c = null;
                    if (i > 0) a = root.get(i - 1);
                    if (i < root.size() - 1) c = root.get(i + 1);
                    if (a.isEmpty()) a = null;
                    if (c.isEmpty()) c = null;
                    String h = Solver.Combine(a, b, c);
                    //return null if a, b, and c are in an invalid configuration
                    if (h == null) return null;
                    //replace a, b, and c in root with h
                    root.set(i, h);
                    if (i < root.size() - 1) root.remove(i + 1);
                    if (i > 0) root.remove(i - 1);
                    //decrement i so the next operator does not get skiped
                    i-=2;
                }
            }
        }
        //root should hold only one token now
        String y = root.get(0);
        y = Peel(y);
        //split top level expression 
        List<String> binexp = Crack(y);
        //recursivly solve left and right hand side until finished
        Element e1 = Build(binexp.get(0));
        Element e2 = Build(binexp.get(2));
        return new Expression(e1, binexp.get(1), e2);
    }
    
    /**
     * Removes outer most layer of parentheses or if the don't exist returns null 
     * @param s String to have parentheses peeled
     * @return new string with outer most layer of parentheses removed or null
     */
    private static String Peel(String s)
    {
        if (s.charAt(0)=='(' && s.charAt(s.length() -1)==')')
        {
            int d = -1;
            int c = s.length() - 1;
            for (int i = 1; i < c; i++)
            {
                if (s.charAt(i) == '(') d--;
                if (s.charAt(i) == ')') d++;
                if (d >= 0) return s;
            }
            return s.substring(1, s.length()-1);
        }
        return null;
    }
    
    /**
     * Split string without delimiters into tokens using operators
     * @param s String to be cracked
     * @return List of tokens
     */
    public static List<String> Crack(String s)
    {
        LinkedList<String> tokens = new LinkedList<String>();
        HashMap<Integer, Integer> operators = GetOperatorMap(s);
        int depth = 0;
        String buffer = "";
        boolean clear = false;
        //add to buffer until an operator is found
        //then copy it to the list followed by the
        //operator and reset the buffer
        for (int i = 0; i < s.length(); i++)
        {
            if (clear)
            {
                clear = false;
                buffer = "";
            }
            char c = s.charAt(i);
            //keep content in parentheses togeather 
            if (depth == 0)
            {
                int l = operators.get(i);
                if (l > 0)
                {
                    tokens.add(buffer);
                    int e = i + l;
                    tokens.add(s.substring(i, e));
                    clear = true;
                    i = e - 1;
                }   
            }
            if (s.charAt(i) == '(') depth++;
            else if (s.charAt(i) == ')') depth--;
            buffer += c;
        }
        //copy remainder to the list
        tokens.add(buffer);
        return tokens;
    }
    
    /**
     * Checks a String for operators using solvers and records where operators are and there length
     * @param s The String to be checked
     * @return A Map of character number to length of operators where 0 means no operator
     */
    private static HashMap<Integer, Integer> GetOperatorMap(String s)
    {
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i = 0; i < s.length(); i++)
        {
            int l = Solver.IsOperator(s, i);
            map.put(i, l);
        }
        return map;
    }
}
