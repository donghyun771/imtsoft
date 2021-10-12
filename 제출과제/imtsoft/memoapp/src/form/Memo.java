package form;

import java.sql.ResultSet;
import com.mysql.cj.protocol.Resultset;

public class Memo {
	static String title; // 메모 제목을 저장할 변수 선언
	static String content; //메모 내용을 저장할 변수 선언
	
	public Memo(String title) { 
		this.title = title; // 제목을 받는 생성자
	}
	
	public Memo(String title, String content) {
		// 제목과 내용을 받는 생성자
		this.title = title;
		this.content = content;
	}

}
