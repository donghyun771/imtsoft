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
	JTextArea ta; //�޸� ����
	JTextField tf = new JTextField(20); //����
	JButton sBtn, exBtn; // ��ư ����
	JFileChooser open = new JFileChooser(); //���� �� ���丮 ���� ������Ʈ ����

	public void form() {
		fs("Memowriting");
        setSize(500,500);
        sBtn = new JButton("����");
        exBtn = new JButton("��������");
        JPanel topPanel=new JPanel(new FlowLayout(10,10,FlowLayout.LEFT)); //�������� ����
		topPanel.add(tf);
		topPanel.add(sBtn);
		sBtn.addActionListener(this); //������ �߰�
		topPanel.add(exBtn);
		exBtn.addActionListener(this); //������ �߰�
		topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));	//��, ��, ��, �� ����(Padding)
		
		ta = new JTextArea();
		ta.setBorder(BorderFactory.createEmptyBorder(0,10,10,10)); //���� �߰�
		
		add(topPanel,"North");
		add(ta,"Center");
		wp.setLayout(new FlowLayout());
		sh();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sBtn) { //���� ��ư Ŭ�� ��
			dbinsert(User.id, tf.getText(), ta.getText());
		}
		
		if(e.getSource() == exBtn) { //�������� ��ư Ŭ�� ��
			String filePath = "c:\\Temp\\" + tf.getText() + ".txt"; // ��� ����
			imsg("��� : " + filePath); // ���� ��� �˸�
			export(filePath, ta.getText());
		}
		super.actionPerformed(e);
	}
	

	@Override
	public void windowClosing(WindowEvent e) {
		new Memolist().form(); //������ ���� �� �޸� ����Ʈ �� ����
		super.windowClosing(e);
	}

	
	
	
}
