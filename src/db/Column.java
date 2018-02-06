package db;

import java.sql.Types;

public class Column {
	private int type;
	private String name;
	private String table;
	public Column(int type, String name, String table) {
		this.type = type;
		this.name = name;
		this.table = table;
	}
	public int getType() {
		return this.type;
	}
	public String getColName() {
		return this.name;
	}
	public String getTableName() {
		return this.table;
	}
	public String getFullColName() {
		return this.table+"."+this.name;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDB(String table) {
		this.table = table;
	}
}
