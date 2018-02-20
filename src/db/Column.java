package db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class Column {
	
	protected Types type;
	protected String name;
	protected int position;
	
	public Column(Types type, String name) { 
		this.type = type;
		this.name = name;
	}
	
	public void setValue(int position, PreparedStatement statement, int value) throws SQLException {
		statement.setInt(position, value);
	}
	
	public void setValue(int position, PreparedStatement statement, String value) throws SQLException {
		statement.setString(position, value);
	}
	
	public void setValue(int position, PreparedStatement statement, float value) throws SQLException {
		statement.setFloat(position, value);
	}
	
}
