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

	JTextField jt[] = { new JTextField(15), new JPasswordField(15)}; // 아이디, 비밀번호 입력 필드
	String ln[] = "아이디,비밀번호".split(","); // 입력 필드 옆 텍스트
	JButton jb = new JButton("로그인"); // 버튼 생성
	JLabel jl1 = new JLabel("회원가입"); // 버튼 생성
	
	public void form() {
		fs("로그인"); // 타이틀 지정
		np.add(jl = new JLabel("메모앱"), 0); // 라벨 생성
		jl.setFont(new Font("HY헤드라인M", 1, 25)); // 폰트 지정
		emp(np,  10, 0, 0, 0); // 여백
		setSize(400, 250); // 프레임 크기 지정
		
		cp.setLayout(new GridLayout(0, 1)); // center에 그리드 레이아웃 생성
		// 입력 필드 위치, 크기 설정
		for (int i = 0; i<jt.length; i++) {
			cp.add(jp = new JPanel()); // center에 JPanel 생성
			jp.add(jl = new JLabel(ln[i], 4)); // ln 배열 순서대로 텍스트 생성
			emp(jl, 0, 0, 0, 10); //컴포넌트 별 여백 설성
			size(jl, 80, 30); // 사이즈 설정
			jp.add(jt[i]); // jt 배열 순서대로 패널에 텍스트 필드 생성
		}
		ep.add(jb);// 오른쪽 패널에 로그인 버튼 추가
		jb.addActionListener(this); // 클릭 시
		emp(ep, 0, 0, 10, 5); // 여백
		
		sp.setLayout(new FlowLayout(4)); //레이아웃 설정
		sp.add(jl1);// 오른쪽 밑에 회원가입 추가
		jl1.addMouseListener(this);//클릭 시 이벤트 추가
		
		sh();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jb) { // 로그인 버튼 클릭 시
			try {
				if(StringUtils.isNullOrEmpty(jt[0].getText())) {
					wmsg("빈칸이 존재합니다"); // 아이디 입력 필드 빈칸확인
				}
				else if (StringUtils.isNullOrEmpty(jt[1].getText())) {
					wmsg("빈칸이 존재합니다"); //비번 입력 필드 빈칸확인
				}
				else {
					// 텍스트 필드에 입력된 정보를 DB와 대조
					ResultSet rs = db.rs("select * from user where u_id ='" + jt[0].getText() + "'and u_pw='" + jt[1].getText() +"'");
					// 대조하여 있는 아이디와 패스워드가 DB에 있으면 리스트 폼으로 넘어감
					if(rs.next()) {
						imsg(rs.getString(2) + "님 환영합니다.");
						new User(jt[0].getText());
						dispose();
						new Memolist().form();
					}
					else {
						//일치하지 않으면 경고창
						wmsg("회원정보가 일치하지 않습니다.");
					}
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		super.actionPerformed(e);
	}
	
	// 회원가입 텍스트 클릭 시 회원가입 폼 이동
	@Override
	public void mousePressed(MouseEvent e) {
		dispose();
		new Signup().form();
		super.mousePressed(e);
	}

}
