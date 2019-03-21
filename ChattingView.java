package javachatting;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

class ChattingView extends JFrame {  // 채팅 화면 ChattingView class starts  //
	 private JPanel contentPane;
	 private JTextField chatting_textField; // 보낼 메세지 쓰는곳
	 private String id;
	 private String password;
	 private String ip;
	 private int port=8080;
	 JButton send_button; // 전송버튼
	 JTextArea chatting_textArea,chatting_member_textArea;
	 private Socket socket; // 연결소켓
	 private Socket user_list_socket;	// 접속 멤버 리스트를 받아들이는 소켓
	 private InputStream is;
	 private OutputStream os;
	 private DataInputStream dis;
	 private DataOutputStream dos;
	 private InputStream user_list_is;	//접속 멤버의 리스트를 받아들이는 데이터 스트림
	 private DataInputStream user_list_dis;
	 private OutputStream user_list_os;	//접속 신호를 보내는 데이터 스트림
	 private DataOutputStream user_list_dos;
	 
	 public ChattingView(String id, String password, String ip){  // ChattingView constructor starts//
	  super(id+"님의 대화창입니다.");
	  this.id = id;
	  this.password=password;
	  this.ip=ip;
	  
	  
	  init();
	  chatting_textArea.append("채팅방에 입장하였습니다." + "\n");
	  chatting_member_textArea.append("나" + "\n");
	  network();
	 }// ChattingView constructor ends//
	 
	 
	 public void network() {  // 서버에 접속

	try {
	   socket = new Socket(ip, port);
	   user_list_socket = new Socket(ip, 8081);
	   if (socket != null	&&	user_list_socket != null) // socket이 null값이 아닐때, 연결되었을때
	   {
	    Connection(); // 연결 메소드를 호출
	   }
	  } catch (UnknownHostException e) {
	  } catch (IOException e) {
		  chatting_textArea.append("소켓 접속 에러!!\n");
	  }
	 }
	 
	 public void Connection() { // 실직 적인 메소드 연결부분
	  try { // 스트림 설정
	   is = socket.getInputStream();
	   dis = new DataInputStream(is);
	   os = socket.getOutputStream();
	   dos = new DataOutputStream(os);
	   user_list_os = user_list_socket.getOutputStream();
	   user_list_dos = new DataOutputStream(user_list_os);
	   user_list_is = user_list_socket.getInputStream();
	   user_list_dis = new DataInputStream(user_list_is);

	   
	  } catch (IOException e) {
		  chatting_textArea.append("스트림 설정 에러!!\n");
	  }
	  
	  send_Message(id); // 정상적으로 연결되면 id를 전송
	  
	  Thread th = new Thread(new Runnable() { // 스레드를 돌려서 서버로부터 메세지를 수신
	     @Override
	     public void run() {
	      while (true) {
	       try {
	        String msg = dis.readUTF(); // 메세지를 수신한다
	        chatting_textArea.append(msg + "\n");
	       } catch (IOException e) {
	    	   chatting_textArea.append("서버와 연결이 끊겼습니다!!!\n");
	        // 서버와 소켓 통신에 문제가 생겼을 경우 소켓을 닫는다
	        try {
	         os.close();
	         is.close();
	         dos.close();
	         dis.close();
	         socket.close();
	         
		   
	         break; // 에러 발생하면 while문 종료
	        } catch (IOException e1) {
	        }
	       }
	      } // while문 끝
	     }// run메소드 끝
	    });
	  
	  Thread list_th = new Thread(new Runnable() { // 스레드를 돌려서 서버로부터 채팅 유저 리스트를 받음
		     @Override
		     public void run() {
		      while (true) {
		       try {
		    	String user_list=user_list_dis.readUTF();
		    	if(user_list.equals("deleteallinchattingmembertextarea")){
		    	chatting_member_textArea.setText("");
		  	  	chatting_member_textArea.append("나" + "\n");
		    	}
		    	else{
	        	chatting_member_textArea.append(user_list+"\n");
		    	}
		       } catch (IOException e) {
		    	   chatting_textArea.append("유저 리스트 수신 에러!!\n");		    	   
		        // 서버와 소켓 통신에 문제가 생겼을 경우 소켓을 닫는다
		        try {
			  	     user_list_dos.close();
		        	 user_list_os.close();
			  	     user_list_dis.close();
		        	 user_list_is.close();
			  	   	 user_list_socket.close();
			   
		         break; // 에러 발생하면 while문 종료
		        } catch (IOException e1) {
		        }
		       }
		      } // while문 끝
		     }// run메소드 끝
		    });
	  
	  th.start();
	  list_th.start();
	 }
	 
	 public void send_Message(String str) { // 서버로 메세지를 보내는 메소드
	  try {
	   dos.writeUTF(str);
	  } catch (IOException e) {
		  chatting_textArea.append("메세지 송신 에러!!\n");
	  }
	 }
	 
	 public void init() { // GUI를 구성하는 init() method starts//
	  
	  setVisible(true);
	  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  setBounds(700, 200, 400, 400);
	  contentPane = new JPanel();
	  contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	  setContentPane(contentPane);
	  contentPane.setLayout(null);
	  
	  chatting_textArea = new JTextArea();
	  chatting_textArea.setEditable(false);
	  chatting_textArea.setColumns(1);
	  chatting_textArea.setRows(5);
	  JScrollPane Jscrollbar = new JScrollPane();
	  Jscrollbar.setBounds(10, 10, 260, 300);
	  Jscrollbar.setViewportView(chatting_textArea);
	  contentPane.add(Jscrollbar);
	  
	  JScrollPane member_JScroll_bar = new JScrollPane();
	  chatting_member_textArea = new JTextArea();
	  chatting_member_textArea.setColumns(1);
	  chatting_member_textArea.setRows(5);
	  member_JScroll_bar.setBounds(280, 10, 100, 300);
	  member_JScroll_bar.setViewportView(chatting_member_textArea);
	  chatting_member_textArea.setEditable(false); // textArea를 사용자가 수정 못하게끔 막는다.
	  contentPane.add(member_JScroll_bar);
	  	  
	  chatting_textField = new JTextField();
	  chatting_textField.setBounds(10, 320, 260, 40);
	  chatting_textField.setColumns(10);
	  contentPane.add(chatting_textField);
	  
	  send_button = new JButton("전   송");
	  send_button.setBounds(280, 320, 100, 40);
	  send_button.addActionListener(new Myaction());
	  contentPane.add(send_button);

	 } //init() method ends//

	 class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	 {
	  @Override
	  public void actionPerformed(ActionEvent e) {
	   if (e.getSource() == send_button) // 보내기 버튼을 누를 때
	   {
		if(chatting_textField.getText().equals("")){
		    chatting_textField.requestFocus(); 
	   	}//메세지를 입력하지 않고 보내기 버튼을 누르면 문자가 전송되지 않는다.
	   
		else{
		    send_Message(chatting_textField.getText());
		    chatting_textField.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
		    chatting_textField.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
		}
	   
	   }
	  }
	 }
	} // ChattingView class ends //


