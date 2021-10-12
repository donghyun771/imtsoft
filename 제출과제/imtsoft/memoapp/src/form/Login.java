package form;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import com.mysql.cj.util.StringUtils;
import jdbc.db;

public class Login extends Aframe{

	JTextField jt[] = { new JTextField(15), new JPasswordField(15)}; // ���̵�, ��й�ȣ �Է� �ʵ�
	String ln[] = "���̵�,��й�ȣ".split(","); // �Է� �ʵ� �� �ؽ�Ʈ
	JButton jb = new JButton("�α���"); // ��ư ����
	JLabel jl1 = new JLabel("ȸ������"); // ��ư ����
	
	public void form() {
		fs("�α���"); // Ÿ��Ʋ ����
		np.add(jl = new JLabel("�޸��"), 0); // �� ����
		jl.setFont(new Font("HY������M", 1, 25)); // ��Ʈ ����
		emp(np,  10, 0, 0, 0); // ����
		setSize(400, 250); // ������ ũ�� ����
		
		cp.setLayout(new GridLayout(0, 1)); // center�� �׸��� ���̾ƿ� ����
		// �Է� �ʵ� ��ġ, ũ�� ����
		for (int i = 0; i<jt.length; i++) {
			cp.add(jp = new JPanel()); // center�� JPanel ����
			jp.add(jl = new JLabel(ln[i], 4)); // ln �迭 ������� �ؽ�Ʈ ����
			emp(jl, 0, 0, 0, 10); //������Ʈ �� ���� ����
			size(jl, 80, 30); // ������ ����
			jp.add(jt[i]); // jt �迭 ������� �гο� �ؽ�Ʈ �ʵ� ����
		}
		ep.add(jb);// ������ �гο� �α��� ��ư �߰�
		jb.addActionListener(this); // Ŭ�� ��
		emp(ep, 0, 0, 10, 5); // ����
		
		sp.setLayout(new FlowLayout(4)); //���̾ƿ� ����
		sp.add(jl1);// ������ �ؿ� ȸ������ �߰�
		jl1.addMouseListener(this);//Ŭ�� �� �̺�Ʈ �߰�
		
		sh();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jb) { // �α��� ��ư Ŭ�� ��
			try {
				if(StringUtils.isNullOrEmpty(jt[0].getText())) {
					wmsg("��ĭ�� �����մϴ�"); // ���̵� �Է� �ʵ� ��ĭȮ��
				}
				else if (StringUtils.isNullOrEmpty(jt[1].getText())) {
					wmsg("��ĭ�� �����մϴ�"); //��� �Է� �ʵ� ��ĭȮ��
				}
				else {
					// �ؽ�Ʈ �ʵ忡 �Էµ� ������ DB�� ����
					ResultSet rs = db.rs("select * from user where u_id ='" + jt[0].getText() + "'and u_pw='" + jt[1].getText() +"'");
					// �����Ͽ� �ִ� ���̵�� �н����尡 DB�� ������ ����Ʈ ������ �Ѿ
					if(rs.next()) {
						imsg(rs.getString(2) + "�� ȯ���մϴ�.");
						new User(jt[0].getText());
						dispose();
						new Memolist().form();
					}
					else {
						//��ġ���� ������ ���â
						wmsg("ȸ�������� ��ġ���� �ʽ��ϴ�.");
					}
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		super.actionPerformed(e);
	}
	
	// ȸ������ �ؽ�Ʈ Ŭ�� �� ȸ������ �� �̵�
	@Override
	public void mousePressed(MouseEvent e) {
		dispose();
		new Signup().form();
		super.mousePressed(e);
	}

}
