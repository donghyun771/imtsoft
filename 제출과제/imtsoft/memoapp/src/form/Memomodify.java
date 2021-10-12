package form;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import form.Memo;
import jdbc.db;

public class Memomodify extends Aframe{
	String title = Memo.title; // Memo.java의 제목 정보 변수에 저장
	String content = Memo.content;// Memo.java의 내용 정보 변수에 저장
	JTextField tf = new JTextField(title, 25); // 제목 입력창
	JTextArea ta = new JTextArea(content); // 메모 내용 입력창 
	JButton sBtn, exBtn; //저장, 내보내기 버튼
	public void form() {
		fs("Memomodify");// 타이틀 설정
        setSize(500,500); //크기 설정
        sBtn = new JButton("저장");
        exBtn = new JButton("내보내기");
        JPanel topPanel=new JPanel(new FlowLayout(10,10,FlowLayout.LEFT)); //위쪽 패널 왼쪽정령
		topPanel.add(tf); // 텍스트 필드 추가
		topPanel.add(sBtn); // 저장 버튼추가
		topPanel.add(exBtn); // 내보내기 버튼 추가
		sBtn.addActionListener(this); // 리스너 추가
		exBtn.addActionListener(this); // 리스너 추가
		topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));	//상, 좌, 하, 우 공백(Padding)
		
		ta.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));  // 공백 추가
		
		add(topPanel,"North"); // 위쪽에 추가
		add(ta,"Center"); //중간에 추가
		wp.setLayout(new FlowLayout()); //레이아웃 설정
		sh();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sBtn) { //저장 버튼 클릭 시
			ResultSet rs;
			try {
				//memo_id 가져오기
				rs = db.rs("select memo_id from memo where memo_title = '" + this.title + "'");
				if(rs.next()) {
					String r = rs.getString(1);
					//memo_id에 따른 memo 업데이트
					dbupdate(r, tf.getText(), ta.getText());
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if(e.getSource() == exBtn) {//내보내기 버튼 클릭 시
			String filePath = "c:\\Temp\\" + tf.getText() + ".txt"; // 경로 설정
			imsg("경로 : " + filePath); // 저장 경로 알림
			export(filePath, ta.getText());
		}
		super.actionPerformed(e);
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		new Memolist().form(); // 윈도우 창 닫을 시 메모 리스트 폼 실행
		super.windowClosing(e);
	}
	
	
}
