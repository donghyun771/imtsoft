package form;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.Object;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

import form.User;
import jdbc.db;

public class Memolist extends Aframe{
	JList list;				//����Ʈ
	JTextField inputField;	//�׽�Ʈ �Է� Field
	JButton addBtn;	//�߰� ��ư
	JButton modifyBtn;	//���� ��ư
	JButton delBtn;		//���� ��ư
	String user = User.id; // ���� ����
	DefaultListModel model;	//JList�� ���̴� ���� ������
	JScrollPane scrolled; // ��ũ�� �г�
	String content; // Memo.java�� ���� �ѱ�� ���� ����
	String title; // Memo.java�� ���� �ѱ�� ���� ����
	
	public void form() {
		fs("�޸� ����Ʈ");
		setSize(620,500); //������ ����
		
		model=new DefaultListModel(); // model ���� ���̴� ��
		list=new JList(model); //����Ʈ�� model ����
		inputField=new JTextField(25); //�˻�â
		addBtn=new JButton("�߰�"); // �߰� ��ư
		modifyBtn = new JButton("����, ����"); //����, ���� ��ư
		delBtn=new JButton("����"); //������ư
		
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	//�ϳ��� ���� �� �� �ֵ���
		
		inputField.addKeyListener(this);	//���� ó��
		addBtn.addMouseListener(this);		//������ �߰�
		modifyBtn.addMouseListener(this);   //������ ����
		delBtn.addMouseListener(this);		//������ ����
		list.addListSelectionListener(this);	//�׸� ���ý�

		
		try {
			//����� ������ ���� �޸� ���� ��������
			rs = db.rs("select memo_title from memo where u_id = '" + user + "'");
			while (rs.next()) {
                //GET VALUES
                String title = rs.getString(1);
                //����Ʈ�� �޸������߰�
                model.addElement(title);
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JPanel topPanel=new JPanel(new FlowLayout(10,10,FlowLayout.LEFT));
		topPanel.add(inputField); // �ؽ�Ʈ �ʵ� �߰�
		topPanel.add(addBtn); //�߰���ư
		topPanel.add(modifyBtn); // ����, ������ư �߰�
		topPanel.add(delBtn); //���� ��ư �߰�
		topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));	//��, ��, ��, �� ����(Padding)
		
		scrolled=new JScrollPane(list); //��ũ�� ����Ʈ
		scrolled.setBorder(BorderFactory.createEmptyBorder(0,10,10,10)); //���� ����
		
		add(topPanel,"North");  // topPanel ���ʿ� ����
		add(scrolled,"Center");	//��� list ����
		
		wp.setLayout(new FlowLayout()); // ���̾ƿ� ����
		sh(); // AFrame�� sh() ����
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == addBtn) { // �߰� ��ư Ŭ�� ��
			dispose(); // ������� �ϱ�
			new Memowriting().form(); //�ۼ� �� ����
		}
		if(e.getSource() == delBtn) { // ���� ��ư Ŭ�� ��
			wmsg("�����Ϸ�"); //�����Ϸ�
			try {
				//memo_id ã��
				rs = db.rs("select memo_id from memo where memo_title = '" + list.getSelectedValue() + "'");
				if(rs.next()) {
					String r = rs.getString(1);
					// memo_id �� ���� �޸� �����ͺ��̽����� ����
					int res = db.res("delete from memo where memo_id = '" + r + "'");
				}
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//���� �ﰢ ������ ���� ����Ʈ���� ����
			int selected=list.getSelectedIndex(); // ���õ� ������ ������ ����
			removeItem(selected, model); // ����
		}
		
		//���� ��ư Ŭ�� ��
		if(e.getSource() == modifyBtn && list.getSelectedValue() != null) { //���õǾ� �ִ� ���� �ִ� �� Ȯ��
			try {
				//db���� ���õ� �Ϳ� ���� �޸� ���� �����ͼ� Memo.java�� ����
				rs = db.rs("select memo_title, memo_content from memo where memo_title = '" + list.getSelectedValue() + "'");
				while (rs.next()){
					//GET VALUES
					title = rs.getString(1);
	                content = rs.getString(2);
				}
				new Memo(title, content); // Memo.java �� ����, ���� ����
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// �� �����
			dispose();
			new Memomodify().form(); // �޸� ���� �� ����
		} else if(e.getSource() == modifyBtn && list.getSelectedValue() == null){
			wmsg("�׸��� �����ϼ���"); //���õ� ���� ���� ��
		}
		super.mouseClicked(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode==KeyEvent.VK_ENTER) { //���� Ű �Է� ��
			searchItem(inputField.getText(), model); //��ġ������ �Լ� ����
			inputField.requestFocus();	//���� �Է��� ���ϰ� �ޱ� ���ؼ� TextField�� ��Ŀ�� ��û
		}else if(keyCode==KeyEvent.VK_BACK_SPACE) { // �齺���̽� �Է� ��
			searchItem(inputField.getText(), model); //��ġ������ �Լ� ����
			inputField.requestFocus();	//���� �Է��� ���ϰ� �ޱ� ���ؼ� TextField�� ��Ŀ�� ��û
		}
		super.keyReleased(e);
	}
	
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(!e.getValueIsAdjusting()) {	//�̰� ������ mouse ������, ���� ���� �ѹ��� ȣ��Ǽ� �� �ι� ȣ��
			System.out.println("selected :"+list.getSelectedValue()); // �ܼ�â�� ���õ� �� ���
		}
		super.valueChanged(e);
	}
	
	
	@Override
	public void windowClosing(WindowEvent e) {
		new Login().form(); // X��ư Ŭ�� �� �α��� ������ �̵�
		super.windowClosing(e);
	}
}
