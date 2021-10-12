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
	//필요한 텍스트 필드와 버튼을 배열에 저장
	JTextField jt[] = new JTextField[4]; // 정보를 입력받을 텍스트 필드
	String ln[] = "이름:,아이디:,비밀번호:,비밀번호체크:".split(","); //텍스트
	JButton jb[] = new JButton[2]; //버튼
	String bn[] = "회원가입,취소".split(","); // 회원가입, 취소 버튼
	
	JButton jb1 = new JButton("중복확인"); // 중복확인 버튼
	
	public void form() {
		fs("회원가입");
		setSize(400, 250);
		cp.setLayout(new GridLayout(0, 1));
		// for 문을 이용해 배열 순서대로 패널에 추가
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
		// 버튼 추가
		sp.setLayout(new FlowLayout(4));
		for (int i = 0; i < bn.length; i++) {
			sp.add(jb[i] = new JButton(bn[i]));
			jb[i].addActionListener(this);
		}
		emp(sp, 20, 0, 20, 10);
		jb[0].setEnabled(false); // 아이디 중복확인 전에는 활성화 X
		sh();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//중복확인 버튼 클릭
		if(e.getSource() == jb1) {
			try {
				// 중복확인
				ResultSet rs = db.rs("select u_id from user where u_id = '" + jt[1].getText() + "'");
				if(rs.next()) {
					wmsg("사용 불가한 아이디입니다."); // 중복O
				} else {
					imsg("사용 가능한 아이디입니다."); // 중복X
					jb[0].setEnabled(true); // 중복확인 완료 시 활성화
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		//회원가입 버튼 클릭
		if (e.getSource() == jb[0]) {
			try { //빈칸 확인
				if(StringUtils.isNullOrEmpty(jt[0].getText())) {
					wmsg("빈칸이 존재합니다"); 
				}
				else if (StringUtils.isNullOrEmpty(jt[1].getText())) {
					wmsg("빈칸이 존재합니다"); 
				}
				else if (StringUtils.isNullOrEmpty(jt[2].getText())) {
					wmsg("빈칸이 존재합니다.");
				}
				else if (StringUtils.isNullOrEmpty(jt[3].getText())) {
					wmsg("빈칸이 존재합니다"); 
				}
				else if (! jt[3].getText().equals(jt[2].getText())) {
					wmsg("비밀번호와 일치하지 않습니다."); //비번 재입력
				}
				else {
					int res = db.res("insert into user values('" + jt[1].getText() + "', '" + jt[0].getText() +"', '" + jt[2].getText() + "')");
					// 이름, 아이디, 패스워드 DB에 저장
					imsg("회원가입 완료");
					dispose();
					new Login().form(); // 로그인 폼 실행
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		// 취소 버튼 클릭 시 로그인 폼으로 이동
		if(e.getSource() == jb[1]) {
			dispose();
			new Login().form();
		}
		super.actionPerformed(e);
	}

	@Override
	public void windowClosing(WindowEvent e) {
		new Login().form(); // 윈도우 창 종료 시 로그인 폼 실행
		super.windowClosing(e);
	}
	
	
	
	
	
	
}
