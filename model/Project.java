package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.math.Element;
import model.math.Term;
import model.table.Constant;
import model.table.DataSet;
import model.table.Set;
import model.table.VariableSet;

public class Project 
{
    private File src;
    
    private String name = "New Project";
    
    public LinkedList<Constant> constants;
    public LinkedList<DataSet> datasets;
    public LinkedList<VariableSet> variables;
    
    public Project(File src)
    {
        this.src = src;
        constants = new LinkedList<Constant>();
        datasets = new LinkedList<DataSet>();
        variables = new LinkedList<VariableSet>();
        if (!src.exists()) try 
        {
            src.createNewFile();
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
        Load();
    }
    
    public String GetName()
    {
        return name;
    }
    
    public File GetSRC()
    {
        return src;
    }
    
    public void SetName(String name)
    {
        this.name = name;
    }
    
    public void SetSRC(File src)
    {
        this.src = src;
    }
    
    public int GetConstantCount()
    { return constants.size(); }
    public int GetDataSetCount() 
    { return datasets.size(); }
    public int GetVariableCount() 
    { return variables.size(); }
    
    public Constant GetConstant(int i) 
    { return constants.get(i); }
    public DataSet GetDataSet(int i) 
    { return datasets.get(i); }
    public VariableSet GetVariable(int i) 
    { return variables.get(i); }
    
    public List<Constant> GetConstants() 
    { return constants; }
    public List<DataSet> GetDataSet() 
    { return datasets; }
    public List<VariableSet> GetVariable() 
    { return variables; }
    
    public void SetConstant(int i, Constant c) 
    { constants.set(i, c); }
    public void SetDataSet(int i, DataSet d) 
    { datasets.set(i, d); }
    public void SetVariable(int i, VariableSet v) 
    { variables.set(i, v); }
    
    public void AddConstant(Constant c) 
    { constants.add(c); }
    public void AddDataSet(DataSet d) 
    { datasets.add(d); }
    public void AddVariable(VariableSet v) 
    { variables.add(v); }

    public void RemoveConstant(int i) 
    { constants.remove(i); }
    public void RemoveDataSet(int i) 
    { datasets.remove(i); }
    public void RemoveVariable(int i) 
    { variables.remove(i); }
    
    public void Load()
    {
        try
        {
            FileInputStream fis = new FileInputStream(src);
            DataInputStream dis = new DataInputStream(fis);
            name = dis.readUTF();
            
            int k = dis.readInt();
            for (int i = 0; i < k; i++) 
                constants.add(new Constant(dis.readUTF()));
            
            k = dis.readInt();
            for (int i = 0; i < k; i++) 
                datasets.add(new DataSet(dis.readUTF()));
            
            k = dis.readInt();
            for (int i = 0; i < k; i++) 
                variables.add(new VariableSet(dis.readUTF()));
            
            dis.close();
        }
        catch (Exception e)
        {
        }
    }
    
    public void Save()
    {
        try
        {
            
            FileOutputStream fos = new FileOutputStream(src);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeUTF(name);

            dos.writeInt(GetConstantCount());
            for (Constant k : constants) dos.writeUTF(k.toString());
            
            dos.writeInt(GetDataSetCount());
            for (DataSet k : datasets) dos.writeUTF(k.toString());
            
            dos.writeInt(GetVariableCount());
            for (VariableSet k : variables) dos.writeUTF(k.toString());
            
            dos.close();
        }
        catch (Exception e)
        {
        }
    }

    public List<Set> GetSets() 
    {
        LinkedList<Set> sets = new LinkedList<Set>();
        for (int i = 0; i < datasets.size(); i++)
        {
            sets.add(datasets.get(i));
        }
        for (int i = 0; i < variables.size(); i++)
        {
            sets.add(variables.get(i));
        }
        return sets;
    }

    public boolean Calculate() 
    {
        int rows = GetTotalRows();
        List<Set> sets = GetSets();
        for (int i = 0; i < sets.size(); i++)
        {
            if (sets.get(i).GetData() == null)
            {
                sets.remove(i);
                i--;
            }
        }
        for (Set set : sets)
        {
            for (Term t : set.GetData())
            {
                if (t.GetU() == null)
                {
                    t.SetU(0d);
                }
            }
        }
        for (int r = 0; r < rows; r++)
        {
            ArrayList<String> knowns = new ArrayList<String>();
            ArrayList<Term> terms = new ArrayList<Term>();
            for (int i = 0; i < GetConstantCount(); i++)
            {
                Constant k = GetConstant(i);
                knowns.add(k.GetName());
                terms.add(k.GetValue());
            }
            for (int i = 0; i < GetDataSetCount(); i++)
            {
                DataSet d = GetDataSet(i);
                knowns.add(d.GetName());
                terms.add(d.GetData(r));
            }
            boolean progress = true;
            while (progress)
            {
                progress = false;
                for (int i = 0; i < GetVariableCount(); i++)
                {
                    VariableSet v = GetVariable(i);
                    if (!v.IsCalculated())
                    {
                        if (v.CanCalculate(knowns))
                        {
                            Element e = v.GetElement();
                            for (int j = 0; j < knowns.size(); j++)
                            {
                                e.SetVariable(knowns.get(j), terms.get(j));
                            }
                            v.SetIsCalculated(true);
                            knowns.add(v.GetName());
                            Term t = e.GetTerm();
                            terms.add(t);
                            v.SetData(r, t);
                            progress = true;
                        }
                    }
                }
            }
            boolean purfect = true;
            for (int i = 0; i < GetVariableCount(); i++)
            {
                VariableSet v = GetVariable(i);
                if (!v.IsCalculated()) purfect = false;
                v.SetIsCalculated(false);
            }
            if (!purfect) return false;
        }
        return true;
    }

    public int GetTotalRows() 
    {
        int m = 0;
        for (int i = 0; i < GetDataSetCount(); i++)
        {
            int s = GetDataSet(i).GetSize();
            if (s > m) m = s; 
        }
        return m;
    }
}
