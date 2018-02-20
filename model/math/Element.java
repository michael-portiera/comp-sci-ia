package model.math;

import java.util.List;

public abstract class Element 
{
    public abstract Term GetTerm();
    public abstract void SetVariable(String name, Term t);
    public abstract List<String> ListVariables();
    public abstract String GetString();
}
