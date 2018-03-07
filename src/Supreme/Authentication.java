package Supreme;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.Column;
import db.Command;
import db.Table;
import utils.StringUtils;

public class Authentication extends Table {
	
	private PreparedStatement addKey;
	private PreparedStatement updateKey;
	private PreparedStatement validateKey;
	private SecureRandom random;

	public static enum Status {
		authorized,available,unauthorized 
	}
	
	public Authentication(Connection db) throws SQLException {
		this.prepareStatements(db);
		this.random = new SecureRandom();
	}
	
	public Authentication(Column [] columns, PreparedStatement insertStatement, PreparedStatement updateStatement, PreparedStatement deleteStatement, Connection db) throws SQLException {
		this.insertStatement = insertStatement;
		this.updateStatement = updateStatement;
		this.deleteStatement = deleteStatement;
		this.columns = columns;
		this.prepareStatements(db);
	}
	
	public Authentication(Column [] columns, String insertStatement, String updateStatement, String deleteStatement, Connection db) throws SQLException {
		this.insertStatement = db.prepareStatement(insertStatement);
		this.updateStatement = db.prepareStatement(updateStatement);
		this.deleteStatement = db.prepareStatement(deleteStatement);
		this.columns = columns;
		this.prepareStatements(db);
	}
	
	@Override
	public void insert() {
		
		
	}

	@Override
	public void update() {
		
		
	}

	@Override
	public void delete() {
		
		
	}
	
	public void startQuery(String queryName) {
		Command query = this.queries.get(queryName);
		if (query != null) {
			
		}
	}
	
	public void startCommand(String commandName) {
		Command command = this.commands.get(commandName);
		if (command != null) {
			
		}
	}
	
	private void prepareStatements(Connection db) throws SQLException {
		this.prepareAddKey(db);
		this.prepareAuthorizeKey(db);
		this.prepareValidateKey(db);
	}
	
	//-------------------------------------generation and saving of random keys----------------------------------------------------
	
	public byte[] generateRandomKey(int length){
		byte[] key = new byte[length];
		random.nextBytes(key); 
		return key;
	}
	
	public String generateRandomString(int length) {
		return StringUtils.stringify(generateRandomKey(length),2);
	}
	
	public boolean add(String key) throws SQLException {
		this.addKey.setString(1, key);
		return this.addKey.execute();
	}
	
	private void prepareAddKey(Connection connection) throws SQLException {
		String keyAdd = "INSERT INTO authentication VALUES (?);";
		this.addKey = connection.prepareStatement(keyAdd);
	}
	
	//-------------------------------------saving of digital fingerprint and claimant of keys----------------------------------------------------
	
	private void prepareAuthorizeKey(Connection connection) throws SQLException {
		String authorization = "UPDATE authentication SET (status, number_of_processors, user_profile, processor_identifier, os, computer_name, processor_architecture, java_home, username)" +
								"= (?, ?, ?, ?, ?, ?, ?, ?, ?)" +
								"WHERE key = ?;";
		this.updateKey = connection.prepareStatement(authorization);
	}
	
	public int authorize(String key, Status status, int number_of_processors, String user_profile, String processor_identifier, String os, String computer_name, String processor_architecture, String java_home, String username) throws SQLException {
		this.updateKey.setString(1, status.toString());
		this.updateKey.setInt(2, number_of_processors);
		this.updateKey.setString(3, user_profile);
		this.updateKey.setString(4, processor_identifier);
		this.updateKey.setString(5, os);
		this.updateKey.setString(6, computer_name);
		this.updateKey.setString(7, processor_architecture);
		this.updateKey.setString(8, java_home);
		this.updateKey.setString(9, username);
		this.updateKey.setString(10, key);
		return this.updateKey.executeUpdate(); 
	}
	
	//-------------------------------------validation of client key----------------------------------------------------
	
	private void prepareValidateKey(Connection connection) throws SQLException {
		String keyValidate =  "SELECT * FROM authentication WHERE (key, status, number_of_processors, user_profile, processor_identifier, os, computer_name, processor_architecture, java_home, username)" + 
								"= (?,?,?,?,?,?,?,?,?);";
		this.validateKey = connection.prepareStatement(keyValidate);
	}
	
	public ResultSet validate(String key, int number_of_processors, String user_profile, String processor_identifier, String os, String computer_name, String processor_architecture, String java_home, String username) throws SQLException {
		this.validateKey.setString(1, key);
		this.validateKey.setInt(2, number_of_processors);
		this.validateKey.setString(3, user_profile);
		this.validateKey.setString(4, processor_identifier);
		this.validateKey.setString(5, os);
		this.validateKey.setString(6, computer_name);
		this.validateKey.setString(7, processor_architecture);
		this.validateKey.setString(8, java_home);
		this.validateKey.setString(9, username);
		return this.validateKey.executeQuery();
	}
	

}
