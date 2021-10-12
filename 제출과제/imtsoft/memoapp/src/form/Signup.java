package form;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mysql.cj.util.StringUtils;

import jdbc.db;

public class Signup extends Aframe{
	//�ʿ��� �ؽ�Ʈ �ʵ�� ��ư�� �迭�� ����
	JTextField jt[] = new JTextField[4]; // ������ �Է¹��� �ؽ�Ʈ �ʵ�
	String ln[] = "�̸�:,���̵�:,��й�ȣ:,��й�ȣüũ:".split(","); //�ؽ�Ʈ
	JButton jb[] = new JButton[2]; //��ư
	String bn[] = "ȸ������,���".split(","); // ȸ������, ��� ��ư
	
	JButton jb1 = new JButton("�ߺ�Ȯ��"); // �ߺ�Ȯ�� ��ư
	
	public void form() {
		fs("ȸ������");
		setSize(400, 250);
		cp.setLayout(new GridLayout(0, 1));
		// for ���� �̿��� �迭 ������� �гο� �߰�
		for (int i = 0; i < ln.length; i++) {
			cp.add(jp = new JPanel(new FlowLayout(0)));
			jp.add(jl = new JLabel(ln[i]));
			size(jl, 100, 30);
			jp.add(jt[i] = new JTextField(15));
			jt[i].addKeyListener(this);
			if (i == 1) {
				jp.add(jb1);
				jb1.addActionListener(this);
			}
		}
		// ��ư �߰�
		sp.setLayout(new FlowLayout(4));
		for (int i = 0; i < bn.length; i++) {
			sp.add(jb[i] = new JButton(bn[i]));
			jb[i].addActionListener(this);
		}
		emp(sp, 20, 0, 20, 10);
		jb[0].setEnabled(false); // ���̵� �ߺ�Ȯ�� ������ Ȱ��ȭ X
		sh();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//�ߺ�Ȯ�� ��ư Ŭ��
		if(e.getSource() == jb1) {
			try {
				// �ߺ�Ȯ��
				ResultSet rs = db.rs("select u_id from user where u_id = '" + jt[1].getText() + "'");
				if(rs.next()) {
					wmsg("��� �Ұ��� ���̵��Դϴ�."); // �ߺ�O
				} else {
					imsg("��� ������ ���̵��Դϴ�."); // �ߺ�X
					jb[0].setEnabled(true); // �ߺ�Ȯ�� �Ϸ� �� Ȱ��ȭ
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		//ȸ������ ��ư Ŭ��
		if (e.getSource() == jb[0]) {
			try { //��ĭ Ȯ��
				if(StringUtils.isNullOrEmpty(jt[0].getText())) {
					wmsg("��ĭ�� �����մϴ�"); 
				}
				else if (StringUtils.isNullOrEmpty(jt[1].getText())) {
					wmsg("��ĭ�� �����մϴ�"); 
				}
				else if (StringUtils.isNullOrEmpty(jt[2].getText())) {
					wmsg("��ĭ�� �����մϴ�.");
				}
				else if (StringUtils.isNullOrEmpty(jt[3].getText())) {
					wmsg("��ĭ�� �����մϴ�"); 
				}
				else if (! jt[3].getText().equals(jt[2].getText())) {
					wmsg("��й�ȣ�� ��ġ���� �ʽ��ϴ�."); //��� ���Է�
				}
				else {
					int res = db.res("insert into user values('" + jt[1].getText() + "', '" + jt[0].getText() +"', '" + jt[2].getText() + "')");
					// �̸�, ���̵�, �н����� DB�� ����
					imsg("ȸ������ �Ϸ�");
					dispose();
					new Login().form(); // �α��� �� ����
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		// ��� ��ư Ŭ�� �� �α��� ������ �̵�
		if(e.getSource() == jb[1]) {
			dispose();
			new Login().form();
		}
		super.actionPerformed(e);
	}

	@Override
	public void windowClosing(WindowEvent e) {
		new Login().form(); // ������ â ���� �� �α��� �� ����
		super.windowClosing(e);
	}
	
	
	
	
	
	
}
