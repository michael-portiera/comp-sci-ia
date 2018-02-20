package gui;

import java.util.LinkedList;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import model.Project;
import model.math.Term;
import model.table.DataSet;

public class TableModel extends AbstractTableModel 
{
    Project project;
    LinkedList<Column> cols;
    
    public TableModel(Project project)
    {
        this.project = project;
        this.cols = new LinkedList<Column>();
        RefreshCols();
    }
    
    private Column GetColumn(int col)
    {
        RefreshCols();
        return cols.get(col);
    }
    
    public void RefreshCols()
    {
        cols.clear();
        for (int i = 0; i < project.GetDataSetCount(); i++)
        {
            DataSet s =  project.GetDataSet(i);
            cols.add(new Column(s, false));
            if (!s.GetFixedUnc())
                cols.add(new Column(s, true));
        }
    }
    
    private class Column
    {
        DataSet s;
        boolean u;
        
        public Column(DataSet s, boolean u)
        {
            this.s = s;
            this.u = u;
        }
        
        public DataSet GetDataSet()
        {
            return s;
        }
        
        public Double GetRow(int r)
        {
            if (r >= s.GetSize())
                return null;
            if (u) return s.GetData(r).GetU();
            else return s.GetData(r).GetV();
        }
        
        public void SetRow(int r, Double v)
        {
            while (r >= s.GetSize()) s.AddData(new Term(null, null));
            Term t = s.GetData(r);
            if (u) t.SetU(v);
            else t.SetV(v);
        }
        
        public boolean HasUncertantyColumn()
        {
            return u;
        }
    }
    
    @Override
    public int getRowCount() 
    {
        int rowcount = 0;
        for (DataSet s : project.GetDataSet())
        {
            int datacount = s.GetData().size();
            if (datacount > rowcount)
                rowcount = datacount;
        }
        return rowcount + 1;
    }

    @Override
    public int getColumnCount() 
    {
        int colcount = 0;
        for (DataSet s : project.GetDataSet())
        {
            if (s.GetFixedUnc()) colcount += 1;
            else colcount += 2;
        }
        return colcount;
    }
    
    @Override
    public String getColumnName(int col) 
    {
        Column c = GetColumn(col);
        DataSet s = c.GetDataSet();
        String name = s.GetName() + " (" + s.GetUnits() + ")";
        if (c.HasUncertantyColumn())
        {
            name += " unc";
        }
        return name;
    }

    @Override
    public String getValueAt(int row, int col) 
    {
        if (row >= getRowCount() - 1) return null;
        Double val = GetColumn(col).GetRow(row);
        if (val == null) return null;
        return val.toString();
    }
    
    public Class getColumnClass(int col) 
    {
        return String.class;
    }
    
    public boolean isCellEditable(int row, int col)
    {
        return true;
    }
    
    public void setValueAt(Object value, int row, int col) 
    {
        Column c = GetColumn(col);
        try
        {
            if (value.toString().isEmpty())
            {
                c.SetRow(row, null);
            }
            else
            {
                double v = Double.parseDouble(value.toString());
                c.SetRow(row, v);
            }
        }
        catch (Exception e)
        {
            c.SetRow(row, null);
        }            
        fireTableChanged(new TableModelEvent(this));
    }
}
