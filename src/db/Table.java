package db;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Table {
	private HashMap<String,Column> columns;
	private String name;
	private String dbName;
	
	public Table(String name, String dbName) {
		this.name = name;
		this.dbName = dbName;
	}
	public Table(HashMap<String,Column> columns, String name, String dbName) {
		this.columns = columns;
		this.name = name;
		this.dbName = dbName;
	}
	public String toString() {
		String toString = "{name: " + this.name + "\ndbName: " + this.dbName + "\ncolumns:";
		for (int i = 0; i<columns.size(); i++) {
			toString += "\n\t" + columns.get(i).getColName();
		}
		toString += "}";
		return toString;
	}
	public String getName() {
		return this.name;
	}
	public String getDBName() {
		return this.dbName;
	}
	public HashMap<String,Column> getColumns(){
		return this.columns;
	}
	public void addColumn(String name, Column column) {
		this.columns.put(name,column);
	}
}
