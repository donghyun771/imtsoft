package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class db {
	public static Connection con;
	public static Statement stmt;
	
	//MySQL�� ����
	public static void DBS() throws SQLException {
		con = DriverManager.getConnection("jdbc:mysql://localhost/?serverTimezone=UTC&allowLoadLocallnfile=true", "root", "1234");
		stmt = con.createStatement();
		stmt.execute("use imt_memo"); // db�� imt_memo ��Ű�� ���
	}
	
	// db���� ������ �������� Query���� ����ϱ� ���� �Լ� (select)
	public static ResultSet rs(String sql) throws SQLException {
		db.DBS();
		System.out.println(sql);
		return db.stmt.executeQuery(sql);
	}
	
	//insert, update �� db�� ������ �����ϱ� ���� Query���� ����ϱ� ���� �Լ�
	public static int res(String sql) throws SQLException {
		db.DBS();
		System.out.println(sql);
		return db.stmt.executeUpdate(sql);
	}
}
