package javachatting;


import java.awt.event.*;
import java.net.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class server extends JFrame{				//server 클래스 시작//
	
	 private JTextField chatting_textField; 			// 채팅입력창
	 private JButton inserting_Button; 				// 입력버튼
	 JTextArea chatting_textArea; 					// 채팅창
	 JTextArea chatting_member_textArea; 				// 몇 명인지 인원 체크
	 private ServerSocket server_socket; 				// 채팅 서버소켓
	 private Socket chatting_text_socket; 				// 채팅소켓
	 private Vector vc = new Vector(); 				// 연결된 사용자를 저장할 벡터

	 private ServerSocket user_list_server_socket; 				
	 private Socket user_list_socket; 				

	 
	 public server() { 						// GUI를 구성하는 server constructor starts//
		 
		  try{ 								//try starts//
		  System.out.println("server starting...");															
		  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  setVisible(true);
		  setSize(400,400);
	  	  setLayout(null);
		  setLocation(700,200);						
	  
		  JScrollPane JScroll_bar = new JScrollPane();
		  chatting_textArea = new JTextArea();
		  chatting_textArea.setColumns(1);
		  chatting_textArea.setRows(5);
		  JScroll_bar.setBounds(10, 10, 260, 300);
		  JScroll_bar.setViewportView(chatting_textArea);
		  chatting_textArea.setEditable(false); 																							
		  add(JScroll_bar);
		  
		  chatting_textField = new JTextField();
		  chatting_textField.setBounds(10, 320, 260, 40);
		  chatting_textField.setColumns(10);
		  chatting_textField.setEditable(true);																		
		  add(chatting_textField);
		  
		  JScrollPane member_JScroll_bar = new JScrollPane();
		  chatting_member_textArea = new JTextArea();
		  chatting_member_textArea.setColumns(1);
		  chatting_member_textArea.setRows(5);
		  member_JScroll_bar.setBounds(280, 10, 100, 300);
		  member_JScroll_bar.setViewportView(chatting_member_textArea);															
		  chatting_member_textArea.setEditable(false);
		  add(member_JScroll_bar);

		  inserting_Button = new JButton("입력");
		  inserting_Button.setBounds(280, 320, 100, 40);
		  add(inserting_Button);
		  
		  log_in_server log_in_server=new log_in_server();
		  joining_server joining_server=new joining_server(); 			//회원가입 서버 시작//
		  server_start();
		  } 								//try ends//
		  
		  catch(Exception e){}
		  
	     } 								//server constructor ends//

	 
	 private void server_start() { 					//server_start method starts//
		 
		  try {								//try 시작
		   
		   server_socket = new ServerSocket(8080); 				// 서버가 포트 여는부분
		   user_list_server_socket = new ServerSocket(8081);
		   
		   if(server_socket!=null	&&	user_list_server_socket!=null) 						// server socket이 정상적으로 열렸을때		
		   {
		  	Thread th = new Thread(new Runnable() { 				// 사용자 접속을 받을 스레드
			@Override
		        
				public void run() {

		   		while (true) { 								// 사용자 접속을 계속해서 받기 위해 while문
					try {
							if(vc.size()==0){
								 chatting_textArea.append("사용자 접속을 기다리는 중...\n");
							}
						
								 chatting_text_socket = server_socket.accept();
								 user_list_socket = user_list_server_socket.accept();
								 UserInfo user = new UserInfo(chatting_text_socket, user_list_socket,vc);// 연결된 소켓 정보는 금방 사라지므로, user 클래스 형태로 객체 생성
                                                                 						// 매개변수로 현재 연결된 소켓과, 벡터를 담아둔다
								 vc.add(user); 						// 해당 벡터에 사용자 객체를 추가
								 user.start(); 						// 만든 객체의 스레드 실행
							 
					     } catch (IOException e) {
				        	chatting_textArea.append("!!!! accept 에러 발생... !!!!\n");
					     }
		  		     }
			    }
		  });

			  th.start();
		   }								
		  }								//try 끝
 
	         catch (IOException e) {
			  System.out.println("소켓이 이미 사용중입니다...");
			  System.exit(0);
		  }
		 } 							//server_start method ends//
	 
	 
	 
	 class UserInfo extends Thread { 				//내부클래스 UserInfo 클래스 시작//
		  private InputStream is;
		  private OutputStream os, list_os;			//채팅 내용을 보내는 스트림과 채팅 멤버 리스트를 보내는 스트림//
		  private DataInputStream dis;
		  private DataOutputStream dos, list_dos;					//채팅 내용을 보내는 데이터 스트림//
		  private Socket user_socket, list_socket; 					//채팅 내용을 주고 받는 소켓//
		  private Vector user_vc;
      	  private String id = "";

		  public UserInfo(Socket soc1, Socket soc2, Vector vc) 			// 생성자메소드
		  {
		   									// 매개변수로 넘어온 자료 저장
		   this.user_socket = soc1;
		   this.list_socket = soc2;
		   this.user_vc = vc;
		   User_network();
		  }

		  public void User_network() { 					//User_network method starts//
		   try {
		    is = user_socket.getInputStream();
		    dis = new DataInputStream(is);
		    os = user_socket.getOutputStream();
		    dos = new DataOutputStream(os);
		    id = dis.readUTF(); 						// 사용자의 닉네임 받는부분

		    list_os = list_socket.getOutputStream();
		    list_dos = new DataOutputStream(list_os);

		    chatting_textArea.append("접속자 ID " +id+"\n");
		    chatting_member_textArea.append(id+"\n");
			   try {
				    dos.writeUTF("정상 접속 되었습니다.");
   				    for (int i = 0; i < user_vc.size(); i++) {
   				    		UserInfo imsi = (UserInfo) user_vc.elementAt(i);
   				    		imsi.list_dos.writeUTF(id);
			   				list_dos.writeUTF(imsi.id);
			   			}
				   } 
				   catch (IOException e) {
					   chatting_textArea.append("메시지 송신 에러 발생\n"); 
				   }							// 연결된 사용자에게 정상접속을 알림
		    inserting_Button.addActionListener(new ActionListener() { 
			   @Override
			   public void actionPerformed(ActionEvent e) { 	//채팅창 입력 이벤트 시작//
			    if (chatting_textField.getText().equals("") || chatting_textField.getText().length()==0){					
				    chatting_textField.requestFocus();
			    	} 							//채팅창에 아무 것도 입력하지 않을 때는 버튼을 눌러도 메세지가 보내지지 않게
	
			    else{																
			    	try
			    	{
	    				
			   			try {						//서버의 메세지를 클라이언트들에게 보내기 시작//

			   				    for (int i = 0; i < user_vc.size(); i++) {
			   				    	UserInfo imsi = (UserInfo) user_vc.elementAt(i);
			   				    	imsi.dos.writeUTF("운영자님께서 "+chatting_textField.getText()+"라고 하십니다.");
			   				    }
			   				    
			   			} 						//서버의 메세지를 클라이언트들에게 보내기 끝//

			   			catch (IOException ee) {		//서버의 메세지를 클라이언트들에게 보내기 실패 시 시작//
				  			chatting_textArea.append("메시지 송신 에러 발생\n"); 
			   			}					//서버의 메세지를 클라이언트들에게 보내기 실패 시 끝//
			    			
			    		}
			    		catch(Exception er)
			    		{
			    			System.out.println(er);
			    		}
			    	
    					chatting_textArea.append("운영자님께서 "+chatting_textField.getText()+"라고 하십니다."+"\n");
    					chatting_textField.setText("");
    					chatting_textField.requestFocus();		// 커서를 다시 텍스트 필드로 위치시킨다
    					
				    }							//채팅창에 입력한 메세지를 클라이언트에게 전달 끝
				   }
				  }); 						// 채팅창 입력 이벤트 끝
		    
		   } catch (Exception e) {
			   chatting_textArea.append("스트림 셋팅 에러\n");
		   }
		  }								//User_network method ends//
  
		  
		  public void run() 						// 클라이언트로부터 메세지를 수신하고 다른 클라이언트들에게 전달할 run()메소드 시작
		  {
		   while (true) {
		    try {	     							// 사용자에게 받는 메세지
		     String msg = dis.readUTF();
		     chatting_textArea.append(id+"로부터 들어온 메세지 : " + msg+"\n");		// 사용자로부터 받은 메세지를 서버 화면에 표시
		     for (int i = 0; i < user_vc.size(); i++) {					//클라이언트로부터 받은 메세지를 다른 클라이언트들에게 전달 시작
		     UserInfo imsi = (UserInfo) user_vc.elementAt(i);
		     try {
		     		imsi.dos.writeUTF(id+" : "+msg);
		     	}

		     	catch (IOException e) {
		     		chatting_textArea.append("메시지 송신 에러 발생\n"); 
		     	}
		      }
		    }										//클라이언트로부터 받은 메세지를 다른 클라이언트들에게 전달 끝

		    catch (IOException e)						//클라이언트의 접속이 끊길 시 예외 처리 시작
		    {
		     
		     try {									//별 의미 없는 try문 시작
		      dos.close();
		      dis.close();
		      user_socket.close();
		      vc.removeElement( this ); 							// 에러가난 현재 객체를 벡터에서 지운다
		      chatting_textArea.append(vc.size() +" : 현재 벡터에 담겨진 사용자 수\n");
		      chatting_textArea.append("사용자 접속 끊어짐 자원 반납\n");
		      chatting_member_textArea.setText("");
		      String accessing_id="";
		      
			   for (int i = 0; i < user_vc.size(); i++) {
				    UserInfo imsi = (UserInfo) user_vc.elementAt(i);
  				    accessing_id=imsi.id;
				    chatting_member_textArea.append(accessing_id+"\n");
				   }			   //채팅방 접속 유저의 리스트를 업데이트한다.
			   
			    for (int i = 0; i < user_vc.size(); i++) {					//클라이언트로부터 받은 메세지를 다른 클라이언트들에게 전달 시작
				     UserInfo imsi = (UserInfo) user_vc.elementAt(i);
				     imsi.list_dos.writeUTF("deleteallinchattingmembertextarea");}
			    
			    for (int i = 0; i < user_vc.size(); i++) {					//클라이언트로부터 받은 메세지를 다른 클라이언트들에게 전달 시작
				     UserInfo imsi = (UserInfo) user_vc.elementAt(i);
					    for (int j = 0; j < user_vc.size(); j++) {					//클라이언트로부터 받은 메세지를 다른 클라이언트들에게 전달 시작
						     UserInfo list = (UserInfo) user_vc.elementAt(j);
						     if(imsi.id.equals(list.id)){}
						     else{
						     imsi.list_dos.writeUTF(list.id);}
						     }
						    }
			   
		      break;
		     
		     } catch (Exception ee) {							//별 의미 없는 try문 끝, 마찬가지로 별 의미 없는 catch문 시작
		     
		     }										//별 의미 없는 catch문 끝
		    }									//클라이언트의 접속이 끊길 시 예외 처리 끝
		   }
		  }								//run메소드 끝
		 } 							// 내부 클래스 userInfo 클래스 끝//
	 
	public static void main(String args[]){			//main method starts//

			server server=new server();
			server.setVisible(true);

	}							//main method ends//
}
