package form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.util.StringUtils;

import jdbc.db;

//GUI�� �ʿ��� ������ �Լ� ������ ��Ƶξ� ���� ���� �����ٰ� �������� ����
public class Aframe extends JFrame implements ActionListener, MouseListener, WindowListener, KeyListener, ListSelectionListener {
	// ���� ����
	public static JPanel jp, cp, np, sp, ep, wp, op, mp, p1; 
	public static JScrollPane jsp;
	public static JTable jta;
	public static JTextField jf;
	public static JLabel jl;
	public static JComboBox jc;
	public static DefaultTableModel tmodel;
	public static DefaultTableCellRenderer cell;
	public static ResultSet rs;
	public static DecimalFormat decif;
	public static JList jli;
	
	// ������Ʈ ��ġ�� �����ϴ� ���� ����
	public static String n = BorderLayout.NORTH;
	public static String s = BorderLayout.SOUTH;
	public static String e = BorderLayout.EAST;
	public static String w = BorderLayout.WEST;
	public static String c = BorderLayout.CENTER;
	public static String sql; // sql���� ��Ʈ������ �ѱ�� ���� ����
	public void fs(String t) {
		setTitle(t); //Ÿ��Ʋ ����
		addWindowListener(this);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); // ��ϵǾ� �ִ� ������  WindowListener������Ʈ�� ȣ�� �� �ڵ������� �������� ���� �ı�
		setLocationRelativeTo(null); //������ â�� ȭ�� ����� ���
		
		// Container ���� ��ġ���� Component �߰�
		add(np = new JPanel(new BorderLayout()),n);
		add(sp = new JPanel(new BorderLayout()),s);
		add(ep = new JPanel(new BorderLayout()),e);
		add(wp = new JPanel(new BorderLayout()),w);
		add(cp = new JPanel(new BorderLayout()),c);
	}
	//�ð�
	public String time() {
		SimpleDateFormat fourteen_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // ����Ͻú��� 14�ڸ� ���� 
		Date date_now = new Date(System.currentTimeMillis()); // ����ð��� ������ Date������ �����Ѵ�
		System.out.println(fourteen_format.format(date_now)); // 14�ڸ� �������� ����Ѵ�
		return fourteen_format.format(date_now);
	}
	// txt�� ��������
	public void export(String path, String content) {
		try {
			   FileWriter fileWriter = new FileWriter(path);
			   fileWriter.write(content); //�޸� ���� txt�� ����
			   
			   fileWriter.close(); //��Ʈ�� �ݱ�
			 } catch (IOException e1) {
			   // TODO Auto-generated catch block
			   e1.printStackTrace();
			 }
	}
	// db�� ���� ������Ʈ
	public void dbupdate(String memoid, String title, String content) {
		try {
			time();
			// �޸� ���� ������Ʈ
			int res = db.res("update memo set memo_title = '" + title + "', memo_content = '" + content + "', memo_time = '" + time() +"' where memo_id = '" + memoid + "'");
			new Memo(title);
			dispose();
			new Memolist().form();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	//�����ͺ��̽��� ���� ����
	public void dbinsert(String user, String title, String content) {
		
		try {
			// �޸� ���� ����
			int res = db.res("insert into memo(u_id, memo_title, memo_content, memo_time)"
					+ "values('" + user + "','"+ title + "','" + content + "','" + time() + "')");
			new Memo(title);
			dispose();
			new Memolist().form();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	//�˻�
	public void searchItem(String inputText, DefaultListModel model) {
		model.clear(); // ����Ʈ�� ���� Ŭ����
		try {// �Էµ� ������ ��¥ ���İ� ��ġ�ϸ� �Էµ� ������ ���� ��¥ ������ �˻� �� ����Ʈ�� �߰�
			ResultSet rs = db.rs("select memo_title from memo where "
					+ "memo_time like '" + inputText + "%'");
			while(rs.next()) {
				String title = rs.getString(1);
				if(title != null) {
					model.addElement(title);
				}else {
					wmsg("�˻� ��� ����");
				}
			}
		} catch (SQLException e) { //�Էµ� ������ ��¥ ���İ� ���� ������ �Էµ� ������ ���� ����� ���뿡�� �˻� �� �߰�
			try {
				ResultSet rs = db.rs("select memo_title from memo where "
						+ "memo_title like '%" + inputText + "%' or "
						+ "memo_content like '%" + inputText + "%'");
				while(rs.next()) {
					String title = rs.getString(1);
					if(title != null) {
						model.addElement(title);
					}else {
						wmsg("�˻� ��� ����");
					}
				}
			} catch (SQLException e3) {
				e3.printStackTrace();
			}
			//e.printStackTrace();
		}
	}
	//����
	public void removeItem(int index, DefaultListModel model) {
		if(index<0) {
			if(model.size()==0) return;	//�ƹ��͵� ����Ǿ� ���� ������ return
			index=0;	//�� �̻��̸� ���� ���� list index
		}
		// ����Ʈ���� ����
		model.remove(index);
	}
	
	//�������� ���̵��� ����
	public void sh() {
		setVisible(true);
	}
	
	public void sh_p() {
		pack(); // Jframe�� ���빰�� �˸°� ������ ũ�� ����
		setVisible(true);
	}
	
	//������Ʈ ��Ʈ ����
	public void font(JComponent c, int i) {
		c.setFont(new Font("�������", Font.BOLD,i));
	}
	
	//���ڻ� ����
	public void fg(JComponent c, Color color) {
		c.setForeground(color);
	}
	//������Ʈ �� ����
	public void emp(JComponent c, int t, int l, int b, int r) {
		c.setBorder(new EmptyBorder(t, l, b, r));
	}
	
	
	// ������Ʈ �⺻ũ�� ����
	public void size(JComponent c, int x, int y) {
		c.setPreferredSize(new Dimension(x, y));
	}
	
	//�޼��� â
	public void wmsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Message", 0);
	}
	public void imsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "message", 1);
	}
	
	//�Լ� ����� �ʿ��� ������ ������ �Ͽ� ���
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
