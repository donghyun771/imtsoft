package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class db {
	public static Connection con;
	public static Statement stmt;
	
	//MySQL과 연결
	public static void DBS() throws SQLException {
		con = DriverManager.getConnection("jdbc:mysql://localhost/?serverTimezone=UTC&allowLoadLocallnfile=true", "root", "1234");
		stmt = con.createStatement();
		stmt.execute("use imt_memo"); // db에 imt_memo 스키마 사용
	}
	
	// db에서 정보를 가져오는 Query문을 사용하기 위한 함수 (select)
	public static ResultSet rs(String sql) throws SQLException {
		db.DBS();
		System.out.println(sql);
		return db.stmt.executeQuery(sql);
	}
	
	//insert, update 등 db에 정보를 저장하기 위한 Query문을 사용하기 위한 함수
	public static int res(String sql) throws SQLException {
		db.DBS();
		System.out.println(sql);
		return db.stmt.executeUpdate(sql);
	}
}
