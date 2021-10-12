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

//GUI에 필요한 변수와 함수 선언을 모아두어 여러 폼에 가져다가 쓰기위한 파일
public class Aframe extends JFrame implements ActionListener, MouseListener, WindowListener, KeyListener, ListSelectionListener {
	// 변수 선언
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
	
	// 컴포넌트 위치를 지정하는 변수 선언
	public static String n = BorderLayout.NORTH;
	public static String s = BorderLayout.SOUTH;
	public static String e = BorderLayout.EAST;
	public static String w = BorderLayout.WEST;
	public static String c = BorderLayout.CENTER;
	public static String sql; // sql문을 스트링으로 넘기기 위한 선언
	public void fs(String t) {
		setTitle(t); //타이틀 지정
		addWindowListener(this);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); // 등록되어 있는 임의의  WindowListener오브젝트를 호출 후 자동적으로 프레임을 숨겨 파기
		setLocationRelativeTo(null); //윈도우 창을 화면 가운데에 띄움
		
		// Container 위에 위치별로 Component 추가
		add(np = new JPanel(new BorderLayout()),n);
		add(sp = new JPanel(new BorderLayout()),s);
		add(ep = new JPanel(new BorderLayout()),e);
		add(wp = new JPanel(new BorderLayout()),w);
		add(cp = new JPanel(new BorderLayout()),c);
	}
	//시간
	public String time() {
		SimpleDateFormat fourteen_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 년월일시분초 14자리 포멧 
		Date date_now = new Date(System.currentTimeMillis()); // 현재시간을 가져와 Date형으로 저장한다
		System.out.println(fourteen_format.format(date_now)); // 14자리 포멧으로 출력한다
		return fourteen_format.format(date_now);
	}
	// txt로 내보내기
	public void export(String path, String content) {
		try {
			   FileWriter fileWriter = new FileWriter(path);
			   fileWriter.write(content); //메모 내용 txt로 저장
			   
			   fileWriter.close(); //스트림 닫기
			 } catch (IOException e1) {
			   // TODO Auto-generated catch block
			   e1.printStackTrace();
			 }
	}
	// db에 정보 업데이트
	public void dbupdate(String memoid, String title, String content) {
		try {
			time();
			// 메모 정보 업데이트
			int res = db.res("update memo set memo_title = '" + title + "', memo_content = '" + content + "', memo_time = '" + time() +"' where memo_id = '" + memoid + "'");
			new Memo(title);
			dispose();
			new Memolist().form();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	//데이터베이스에 정보 저장
	public void dbinsert(String user, String title, String content) {
		
		try {
			// 메모 정보 삽입
			int res = db.res("insert into memo(u_id, memo_title, memo_content, memo_time)"
					+ "values('" + user + "','"+ title + "','" + content + "','" + time() + "')");
			new Memo(title);
			dispose();
			new Memolist().form();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	//검색
	public void searchItem(String inputText, DefaultListModel model) {
		model.clear(); // 리스트뷰 내용 클리어
		try {// 입력된 정보가 날짜 형식과 일치하면 입력된 정보에 따른 날짜 정보를 검색 후 리스트에 추가
			ResultSet rs = db.rs("select memo_title from memo where "
					+ "memo_time like '" + inputText + "%'");
			while(rs.next()) {
				String title = rs.getString(1);
				if(title != null) {
					model.addElement(title);
				}else {
					wmsg("검색 결과 없음");
				}
			}
		} catch (SQLException e) { //입력된 정보가 날짜 형식과 맞지 않으면 입력된 정보에 따라 제목과 내용에서 검색 후 추가
			try {
				ResultSet rs = db.rs("select memo_title from memo where "
						+ "memo_title like '%" + inputText + "%' or "
						+ "memo_content like '%" + inputText + "%'");
				while(rs.next()) {
					String title = rs.getString(1);
					if(title != null) {
						model.addElement(title);
					}else {
						wmsg("검색 결과 없음");
					}
				}
			} catch (SQLException e3) {
				e3.printStackTrace();
			}
			//e.printStackTrace();
		}
	}
	//삭제
	public void removeItem(int index, DefaultListModel model) {
		if(index<0) {
			if(model.size()==0) return;	//아무것도 저장되어 있지 않으면 return
			index=0;	//그 이상이면 가장 상위 list index
		}
		// 리스트에서 삭제
		model.remove(index);
	}
	
	//프레임이 보이도록 설정
	public void sh() {
		setVisible(true);
	}
	
	public void sh_p() {
		pack(); // Jframe의 내용물에 알맞게 윈도우 크기 조절
		setVisible(true);
	}
	
	//컴포넌트 폰트 설정
	public void font(JComponent c, int i) {
		c.setFont(new Font("맑은고딕", Font.BOLD,i));
	}
	
	//글자색 설정
	public void fg(JComponent c, Color color) {
		c.setForeground(color);
	}
	//컴포넌트 별 여백
	public void emp(JComponent c, int t, int l, int b, int r) {
		c.setBorder(new EmptyBorder(t, l, b, r));
	}
	
	
	// 컴포넌트 기본크기 설정
	public void size(JComponent c, int x, int y) {
		c.setPreferredSize(new Dimension(x, y));
	}
	
	//메세지 창
	public void wmsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Message", 0);
	}
	public void imsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "message", 1);
	}
	
	//함수 선언부 필요한 곳에서 재정의 하여 사용
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
