package form;

import java.sql.ResultSet;
import com.mysql.cj.protocol.Resultset;

public class Memo {
	static String title; // �޸� ������ ������ ���� ����
	static String content; //�޸� ������ ������ ���� ����
	
	public Memo(String title) { 
		this.title = title; // ������ �޴� ������
	}
	
	public Memo(String title, String content) {
		// ����� ������ �޴� ������
		this.title = title;
		this.content = content;
	}

}
