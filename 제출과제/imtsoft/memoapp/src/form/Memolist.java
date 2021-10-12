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
	JList list;				//리스트
	JTextField inputField;	//테스트 입력 Field
	JButton addBtn;	//추가 버튼
	JButton modifyBtn;	//수정 버튼
	JButton delBtn;		//삭제 버튼
	String user = User.id; // 유저 정보
	DefaultListModel model;	//JList에 보이는 실제 데이터
	JScrollPane scrolled; // 스크롤 패널
	String content; // Memo.java에 값을 넘기기 위한 변수
	String title; // Memo.java에 값을 넘기기 위한 변수
	
	public void form() {
		fs("메모 리스트");
		setSize(620,500); //사이즈 설정
		
		model=new DefaultListModel(); // model 실제 보이는 값
		list=new JList(model); //리스트에 model 생성
		inputField=new JTextField(25); //검색창
		addBtn=new JButton("추가"); // 추가 버튼
		modifyBtn = new JButton("열람, 수정"); //열람, 수정 버튼
		delBtn=new JButton("삭제"); //삭제버튼
		
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	//하나만 선택 될 수 있도록
		
		inputField.addKeyListener(this);	//엔터 처리
		addBtn.addMouseListener(this);		//아이템 추가
		modifyBtn.addMouseListener(this);   //아이템 수정
		delBtn.addMouseListener(this);		//아이템 삭제
		list.addListSelectionListener(this);	//항목 선택시

		
		try {
			//사용자 정보에 따른 메모 제목 가져오기
			rs = db.rs("select memo_title from memo where u_id = '" + user + "'");
			while (rs.next()) {
                //GET VALUES
                String title = rs.getString(1);
                //리스트에 메모제목추가
                model.addElement(title);
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JPanel topPanel=new JPanel(new FlowLayout(10,10,FlowLayout.LEFT));
		topPanel.add(inputField); // 텍스트 필드 추가
		topPanel.add(addBtn); //추가버튼
		topPanel.add(modifyBtn); // 열람, 수정버튼 추가
		topPanel.add(delBtn); //삭제 버튼 추가
		topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));	//상, 좌, 하, 우 공백(Padding)
		
		scrolled=new JScrollPane(list); //스크롤 리스트
		scrolled.setBorder(BorderFactory.createEmptyBorder(0,10,10,10)); //여백 설정
		
		add(topPanel,"North");  // topPanel 위쪽에 생성
		add(scrolled,"Center");	//가운데 list 생성
		
		wp.setLayout(new FlowLayout()); // 레이아웃 설정
		sh(); // AFrame에 sh() 실행
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == addBtn) { // 추가 버튼 클릭 시
			dispose(); // 사라지게 하기
			new Memowriting().form(); //작성 폼 열기
		}
		if(e.getSource() == delBtn) { // 삭제 버튼 클릭 시
			wmsg("삭제완료"); //삭제완료
			try {
				//memo_id 찾기
				rs = db.rs("select memo_id from memo where memo_title = '" + list.getSelectedValue() + "'");
				if(rs.next()) {
					String r = rs.getString(1);
					// memo_id 에 따른 메모를 데이터베이스에서 삭제
					int res = db.res("delete from memo where memo_id = '" + r + "'");
				}
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//삭제 즉각 반응을 위해 리스트에서 삭제
			int selected=list.getSelectedIndex(); // 선택된 정보를 변수에 저장
			removeItem(selected, model); // 삭제
		}
		
		//수정 버튼 클릭 시
		if(e.getSource() == modifyBtn && list.getSelectedValue() != null) { //선택되어 있는 것이 있는 지 확인
			try {
				//db에서 선택된 것에 따른 메모 정보 가져와서 Memo.java에 저장
				rs = db.rs("select memo_title, memo_content from memo where memo_title = '" + list.getSelectedValue() + "'");
				while (rs.next()){
					//GET VALUES
					title = rs.getString(1);
	                content = rs.getString(2);
				}
				new Memo(title, content); // Memo.java 에 제목, 내용 저장
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// 폼 숨기기
			dispose();
			new Memomodify().form(); // 메모 수정 폼 실행
		} else if(e.getSource() == modifyBtn && list.getSelectedValue() == null){
			wmsg("항목을 선택하세요"); //선택된 것이 없을 시
		}
		super.mouseClicked(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode==KeyEvent.VK_ENTER) { //엔터 키 입력 시
			searchItem(inputField.getText(), model); //서치아이템 함수 실행
			inputField.requestFocus();	//다음 입력을 편하게 받기 위해서 TextField에 포커스 요청
		}else if(keyCode==KeyEvent.VK_BACK_SPACE) { // 백스페이스 입력 시
			searchItem(inputField.getText(), model); //서치아이템 함수 실행
			inputField.requestFocus();	//다음 입력을 편하게 받기 위해서 TextField에 포커스 요청
		}
		super.keyReleased(e);
	}
	
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(!e.getValueIsAdjusting()) {	//이거 없으면 mouse 눌릴때, 뗄때 각각 한번씩 호출되서 총 두번 호출
			System.out.println("selected :"+list.getSelectedValue()); // 콘솔창에 선택된 값 출력
		}
		super.valueChanged(e);
	}
	
	
	@Override
	public void windowClosing(WindowEvent e) {
		new Login().form(); // X버튼 클릭 시 로그인 폼으로 이동
		super.windowClosing(e);
	}
}
