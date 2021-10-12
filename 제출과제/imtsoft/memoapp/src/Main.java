import java.sql.SQLException;

import form.Login;
import jdbc.db;

public class Main {

	public static void main(String[] args) throws SQLException {
		new db().DBS();// db와 연결
		new Login().form(); // 로그인 폼 실행
	}

}
