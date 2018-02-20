package db;

import java.sql.PreparedStatement;
import java.util.HashMap;

public abstract class Table {
	
	protected PreparedStatement insertStatement;
	public abstract void insert();
	
	protected PreparedStatement updateStatement;
	public abstract void update();
	
	protected PreparedStatement deleteStatement;
	public abstract void delete();
	
	protected Column [] columns;
	protected HashMap<String,Command> commands;//all sql commands except for queries
	protected HashMap<String,Command> queries;//just queries
	
	public abstract void startQuery(String queryName);
	public abstract void startCommand(String commandName); 
	
}
