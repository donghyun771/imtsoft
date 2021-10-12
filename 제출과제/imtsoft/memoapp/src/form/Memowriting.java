package form;  

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.util.Date;
import java.text.SimpleDateFormat;

import form.User;
import jdbc.db;

public class Memowriting extends Aframe{
	JTextArea ta; //메모 내용
	JTextField tf = new JTextField(20); //제목
	JButton sBtn, exBtn; // 버튼 선언
	JFileChooser open = new JFileChooser(); //파일 및 디렉토리 선택 컴포넌트 선언

	public void form() {
		fs("Memowriting");
        setSize(500,500);
        sBtn = new JButton("저장");
        exBtn = new JButton("내보내기");
        JPanel topPanel=new JPanel(new FlowLayout(10,10,FlowLayout.LEFT)); //왼쪽으로 정렬
		topPanel.add(tf);
		topPanel.add(sBtn);
		sBtn.addActionListener(this); //리스너 추가
		topPanel.add(exBtn);
		exBtn.addActionListener(this); //리스너 추가
		topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));	//상, 좌, 하, 우 공백(Padding)
		
		ta = new JTextArea();
		ta.setBorder(BorderFactory.createEmptyBorder(0,10,10,10)); //공백 추가
		
		add(topPanel,"North");
		add(ta,"Center");
		wp.setLayout(new FlowLayout());
		sh();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sBtn) { //저장 버튼 클릭 시
			dbinsert(User.id, tf.getText(), ta.getText());
		}
		
		if(e.getSource() == exBtn) { //내보내기 버튼 클릭 시
			String filePath = "c:\\Temp\\" + tf.getText() + ".txt"; // 경로 설정
			imsg("경로 : " + filePath); // 저장 경로 알림
			export(filePath, ta.getText());
		}
		super.actionPerformed(e);
	}
	

	@Override
	public void windowClosing(WindowEvent e) {
		new Memolist().form(); //윈도우 종료 시 메모 리스트 폼 실행
		super.windowClosing(e);
	}

	
	
	
}
