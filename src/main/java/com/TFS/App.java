package com.TFS;

import java.util.Random;

import org.apache.log4j.Logger;

import com.TFS.model.DBMS;
import com.TFS.model.Database;
import com.TFS.model.Field;
import com.TFS.model.BaseTable;
import com.TFS.model.Tuple;

public class App 
{
	
	private static final Logger log = Logger.getLogger(App.class);
	
	public static DBMS dbms = new DBMS();
	
    public static void main( String[] args )
    {
    	
    	//Database.CreateDatabase("test");
    	//Table.CreateTable("testtable", "test");
    	Database database = dbms.getDatabase("test");
    	//database.ShowTables();
    	BaseTable table = database.getTable("testtable");
    	table.ShowTable();
    	table.ReadTable();
    	table.query();
    	table.WriteTable();
    	
    }
}
