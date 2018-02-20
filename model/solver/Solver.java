package model.solver;

import model.solver.trig.CosSolver;
import model.solver.trig.SinSolver;
import model.solver.trig.TanSolver;
import model.solver.std.SubtractionSolver;
import model.solver.std.PowerSolver;
import model.solver.std.DivisionSolver;
import model.solver.std.MultiplicationSolver;
import model.solver.std.AdditionSolver;
import java.util.HashMap;
import java.util.Map;
import model.math.Expression;
import model.math.Term;

public abstract class Solver 
{
    public static HashMap<String, Solver> solvers = Init();
    
    /**
     * Loads an instance of each of the various solvers into a map
     * @return A map that maps the symbols to the solvers
     */
    private static HashMap<String, Solver> Init()
    {
        HashMap<String, Solver> m = new HashMap<String, Solver>();
        MapAdd(m, new AdditionSolver());
        MapAdd(m, new SubtractionSolver());
        MapAdd(m, new MultiplicationSolver());
        MapAdd(m, new DivisionSolver());
        MapAdd(m, new PowerSolver());
        
        MapAdd(m, new SinSolver());
        MapAdd(m, new CosSolver());
        MapAdd(m, new TanSolver());
        return m;
    }
    
    private static void MapAdd(Map<String, Solver> m, Solver s)
    {
        m.put(s.GetSymbol(), s);
    }
    
    /**
     * Solves an expression for the equivalent term
     * @param e The expression to be solved
     * @return The equivalent term to the expression
     */
    public static Term Solve(Expression e)
    {
        try
        {
            return solvers.get(e.o).Evaluate(e);
        }
        catch (Exception ex)
        {
            System.out.println("Operand: '" + e.o + "' has no definition");
            return null;
        }
    }
    
    /**
     * Checks if the character in position i is the first character of an Operator
     * @param s The string to be checked
     * @param i The character to be checked
     * @return the length of the operator (i.e. "sin" = 3,  "+" = 1) or zero if the character is not the first in a operator
     */
    public static int IsOperator(String s, int i)
    {
        for (Solver solver : solvers.values())
        {
            int l = solver.CheckSymbol(s, i);
            if (l > 0) return l;
        }
        return 0;
    }
    
    /**
     * An method for evaluating an expression with the solvers operator
     * @param e The expression to be solved (must contain the correct operator)
     * @return The term equivalent to the expression
     */
    public abstract Term Evaluate(Expression e);
    
    /**
     * Gets the symbol of the solver (i.e. "sin" or "+") 
     * @return the solver's symbol
     */
    public abstract String GetSymbol();
    
    /**
     * Gets the priority level in the order of operations
     * @return Returns a integer indicating the priority of the solver in the order of operations
     */
    public abstract int GetPriorityLevel();
    
    /**
     * Checks if a string is an operator with a solver
     * @param s The string to be checked
     * @return returns true if s is an operator or false otherwise
     */
    public static boolean IsOperator(String s)
    {
        for (Solver solver : solvers.values())
        {
            if (solver.GetSymbol().equals(s))
                return true;
        }
        return false;
    }
    
    /**
     * Checks the priority of an operator passed as a string
     * @param s The string containing the operator to be checked
     * @return The priority level in the order of operations or 0 if not an operator
     */
    public static int CheckPriority(String s)
    {
        for (Solver solver : solvers.values())
        {
            if (solver.GetSymbol().equals(s))
                return solver.GetPriority(s);
        }
        return 0;
    }
    
    /**
     * Combines tokens containing two parameters and an operator. Corrects expressions to have two parameters if possible (i.e. "sin(x)" -> "1sin(x)") 
     * @param a the left hand side parameter or null
     * @param b the operator
     * @param c the right hand side parameter or null
     * @return A corrected string with parentheses around it (i.e. "a+b" -> "(a+b)") or null if invalid
     */
    public static String Combine(String a, String b, String c)
    {
        int p = CheckPriority(b);
        
        if (p <= 0) return null;
        if (p > 4) return null;
        switch (p) 
        {
            case 1:
                if (a == null) a = "0";
                if (b == null) b = "0";
                break;
            case 2:
                if (a == null) a = "1";
                if (b == null) b = "1";
                break;
            case 3:
                if (a == null) return null;
                if (b == null) b = "1";
                break;
            case 4:
                if (a == null) a = "1";
                if (b == null) return null;
                break;
        }
        return "(" + a + b + c + ")";
    }
    
    /**
     * Gets the priority of a solver with a given string
     * @param s the string to be checked
     * @return The priority level of the operator or zero if the s does not start with the operators symbol
     */
    public int GetPriority(String s)
    {
        try
        {
            String sym = GetSymbol();
            int l = sym.length();
            String sub = s.substring(0, l);
            if (sub.equals(sym)) return GetPriorityLevel();
        }
        catch(Exception e) { }
        return 0;
    }
    
    /**
     * Checks if the solvers symbol begins at character i of String s
     * @param s The String to be checked
     * @param i The character to check
     * @return The length of the operator or 0 if the symbol does not match
     */
    public int CheckSymbol(String s, int i)
    {
        try
        {
            String sym = GetSymbol();
            int l = sym.length();
            String sub = s.substring(i, i + l);
            if (sub.equals(sym)) return l;
        }
        catch(Exception e) { }
        return 0;
    }
}
