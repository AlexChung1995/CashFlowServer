package Supreme;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import db.Driving;
import utils.StringUtils;

public class SupremeDriving extends Driving {

	private Authentication authentication;
	
	public SupremeDriving(String url, String user, String password) throws SQLException {
		super(url, user, password);
		authentication = new Authentication(this.getDB());
	}
	
	protected void initTables() {
		
	}
	
	public Authentication getAuthentication() {
		return this.authentication;
	}

}
