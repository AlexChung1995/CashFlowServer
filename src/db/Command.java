package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.function.Function;

public class Command {
	
	private Function<Table, ResultSet> functional;
	private String statementName;
	private PreparedStatement statement;
	public Table table;
	
	public Command(Function<Table, ResultSet> functional, String statementName, PreparedStatement statement, Table table) {
		this.functional = functional;
		this.statementName = statementName;
		this.statement = statement;
		this.table = table;
	}
	
	public PreparedStatement getStatement() {
		return this.statement;
	}
	
	
}
