package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Init {
	
	public static void createTable(Connection conn, String tableName, HashMap<String,String> properties) throws SQLException {
		String command = "CREATE TABLE " + tableName + "( \n";
		Iterator<Entry<String,String>> iterate = properties.entrySet().iterator();
		while (iterate.hasNext()) {
			command += iterate.next().getKey() + " " + iterate.next().getValue() + ",\n";
		}
		System.out.println(command);
		conn.createStatement().execute(command);
	}
	
	
	
}
