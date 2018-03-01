package db;

import java.sql.*;
import org.postgresql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

public class Driving {
	
	protected Connection db;
	
	public Driving(String url, String user, String password) throws SQLException{
		this.db = this.connect(url, user, password);
	}
	
	public Connection connect(String url, String user, String password) throws SQLException{
		Properties props = new Properties();
		props.setProperty("user",user);
		props.setProperty("password", password);
		this.db = DriverManager.getConnection(url,props);
		return db;
	}
	
	public Connection getDB() {
		return this.db;
	}
	
	public void createTable(String tableName, HashMap<String,String> properties) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, SQLException {
		String command = "CREATE TABLE " + tableName + "( \n";
		Iterator<Entry<String,String>> iterate = properties.entrySet().iterator();
		while (iterate.hasNext()) {
			Entry<String,String> entry = iterate.next();
			command += entry.getKey() + " " + entry.getValue() + ",\n";
			System.out.println(Types.class.getField(entry.getValue()).getInt(null) + " " + Types.class.getField(entry.getValue()).getName());
		}
		System.out.println(command);
		this.db.createStatement().execute(command);
	}
	
	protected void initTables() {
		
	}
	
}