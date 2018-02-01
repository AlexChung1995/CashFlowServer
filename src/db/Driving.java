package db;

import java.sql.*;
import java.util.Properties;

public class Driving {
	
	public Driving() {
		
	}
	
	public Connection connect(String url, String user, String password) throws SQLException {
		Properties props = new Properties();
		props.setProperty("user",user);
		props.setProperty("password", password);
		Connection db = DriverManager.getConnection(url,props);
		return db;
	}
	
	
}
