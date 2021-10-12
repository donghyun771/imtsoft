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
	String title = Memo.title; // Memo.java�� ���� ���� ������ ����
	String content = Memo.content;// Memo.java�� ���� ���� ������ ����
	JTextField tf = new JTextField(title, 25); // ���� �Է�â
	JTextArea ta = new JTextArea(content); // �޸� ���� �Է�â 
	JButton sBtn, exBtn; //����, �������� ��ư
	public void form() {
		fs("Memomodify");// Ÿ��Ʋ ����
        setSize(500,500); //ũ�� ����
        sBtn = new JButton("����");
        exBtn = new JButton("��������");
        JPanel topPanel=new JPanel(new FlowLayout(10,10,FlowLayout.LEFT)); //���� �г� ��������
		topPanel.add(tf); // �ؽ�Ʈ �ʵ� �߰�
		topPanel.add(sBtn); // ���� ��ư�߰�
		topPanel.add(exBtn); // �������� ��ư �߰�
		sBtn.addActionListener(this); // ������ �߰�
		exBtn.addActionListener(this); // ������ �߰�
		topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));	//��, ��, ��, �� ����(Padding)
		
		ta.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));  // ���� �߰�
		
		add(topPanel,"North"); // ���ʿ� �߰�
		add(ta,"Center"); //�߰��� �߰�
		wp.setLayout(new FlowLayout()); //���̾ƿ� ����
		sh();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sBtn) { //���� ��ư Ŭ�� ��
			ResultSet rs;
			try {
				//memo_id ��������
				rs = db.rs("select memo_id from memo where memo_title = '" + this.title + "'");
				if(rs.next()) {
					String r = rs.getString(1);
					//memo_id�� ���� memo ������Ʈ
					dbupdate(r, tf.getText(), ta.getText());
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if(e.getSource() == exBtn) {//�������� ��ư Ŭ�� ��
			String filePath = "c:\\Temp\\" + tf.getText() + ".txt"; // ��� ����
			imsg("��� : " + filePath); // ���� ��� �˸�
			export(filePath, ta.getText());
		}
		super.actionPerformed(e);
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		new Memolist().form(); // ������ â ���� �� �޸� ����Ʈ �� ����
		super.windowClosing(e);
	}
	
	
}
