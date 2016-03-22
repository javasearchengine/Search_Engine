package SingleProcess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 
public class DataBase {
 
	public Connection conn = null;
 
	public DataBase() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/Crawler";
			conn = DriverManager.getConnection(url, "root", "root");
			//System.out.println("conn built");
		} catch (SQLException e) {
			System.out.println("Database Error");
		} catch (ClassNotFoundException e) {
			System.out.println("Database Error");
		}
	}
 
	public ResultSet runSql(String sql) throws SQLException {
		Statement sta = conn.createStatement();
		return sta.executeQuery(sql);
	}
 
	public boolean runSql2(String sql) throws SQLException {
		Statement sta = conn.createStatement();
		return sta.execute(sql);
	}
 
	@Override
	protected void finalize() throws Throwable {
		if (conn != null || !conn.isClosed()) {
			conn.close();
		}
	}
}