import java.sql.SQLException;

import form.Login;
import jdbc.db;

public class Main {

	public static void main(String[] args) throws SQLException {
		new db().DBS();// db�� ����
		new Login().form(); // �α��� �� ����
	}

}
