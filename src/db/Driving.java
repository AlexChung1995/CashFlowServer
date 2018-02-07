package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

public class Driving {
	
	private Connection db;
	private HashMap<String,Table> tables;//keep in memory for efficiency when server is running
	private HashMap<String,PreparedStatement> queries;//saved precompiled queries
	
	
	public Driving(HashMap<String,Table> tables, HashMap<String,PreparedStatement> queries, String url, String user, String password) throws SQLException, ClassNotFoundException {
		this.db = this.connect(url, user, password);
		this.tables = tables;
		this.queries = queries;
	}
	
	public Driving(String url, String user, String password) throws SQLException, ClassNotFoundException {
		this.db = this.connect(url, user, password);
		
		PreparedStatement statement = this.db.prepareStatement("SELECT * FROM ?;");
		ResultSet tables = this.db.getMetaData().getTables(null, null, null, null);
		while (tables.next()) {
			ResultSetMetaData columnsMetaData = statement.getResultSet().getMetaData();
			HashMap<String,Column> columns = new HashMap<String,Column>();
			for (int i = 0; i>columnsMetaData.getColumnCount(); i++) {
				columns.put(columnsMetaData.getColumnName(i),new Column(columnsMetaData.getColumnType(i),columnsMetaData.getColumnName(i),tables.getString("TABLE_NAME")));
			}
			Table table = new Table(columns,tables.getString("TABLE_NAME"),this.db.getCatalog());
			this.tables.put(table.getName(),table);
		}
	}
	
	public Connection connect(String url, String user, String password) throws SQLException, ClassNotFoundException {
		Properties props = new Properties();
		String jdbcUrl = "jdbc:"+url;
		props.setProperty("user",user);
		props.setProperty("password", password);
		Class.forName("org.postgresql.Driver");
		this.db = DriverManager.getConnection(jdbcUrl,props);
		return db;
	}
	
	public Connection getDB() {
		return this.db;
	}
	
	public void createTable(String tableName, HashMap<String,String> properties) throws SQLException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		String command = "CREATE TABLE " + tableName + "( \n";
		Iterator<Entry<String,String>> iterate = properties.entrySet().iterator();
		Table table = new Table(tableName,this.db.getCatalog());
		while (iterate.hasNext()) {
			Entry<String,String> entry = iterate.next();
			command += entry.getKey() + " " + entry.getValue() + ",\n";
			System.out.println(Types.class.getField(entry.getValue()).getInt(null) + " " + Types.class.getField(entry.getValue()).getName());
			Column column = new Column(Types.class.getField(entry.getValue()).getInt(null),entry.getKey(),tableName);
			table.addColumn(entry.getKey(), column);
		}
		System.out.println(command);
		this.tables.put(tableName, table);
		this.db.createStatement().execute(command);
	}
	
	public void insert(String tableName, HashMap<String,String> values) throws SQLException {
		String command = "INSERT INTO " + tableName + " (";
		String valuesString = "VALUES(";
		Table table = this.tables.get(tableName);
		Iterator<Entry<String,Column>> columns = table.getColumns().entrySet().iterator();
		while (columns.hasNext()) {
			Entry<String,Column> column = columns.next();
			command += column.getKey() + ", ";
			valuesString += column.getValue() + ", ";
		}
		command += ")";
		valuesString += ");";
		PreparedStatement statement = this.db.prepareStatement(command + valuesString);
		
	}
	
	public void addColumn(String tableName,String columnName, Types type) {
		
	}
	
}