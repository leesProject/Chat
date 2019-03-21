package javachatting;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;


public class log_in_server {
	  private InputStream is;
	  private OutputStream os;
	  private ObjectInputStream ois;
	  private DataOutputStream dos;
	  private int check;
	  private ServerSocket log_in_server_socket; //회원확인 서버소켓
	  private Socket log_in_socket; // 회원확인 소켓

	  
	  public log_in_server(){
		  try{log_in_server_socket=new ServerSocket(8082);
		  server_access();
		  }
		  catch(Exception e){System.out.println("로그인 서버 접속 중" +e+"	발생!"); }
	  }
	  
	  public void server_access(){
		  
		  
		  try {
			   if(log_in_server_socket!=null) // socket 이 정상적으로 열렸을때
			   {
				   Connection();
			   }
			  } 
		  catch(Exception e){System.out.println("회원 정보 확인 서버 접속 중 "+e+"발생");
				}
	  }
	  
	 public void Connection() { //Connection method starts//
			 
		  Thread log_in_th = new Thread(new Runnable() { // 회원가입 접속을 받을 스레드
	      @Override
	      public void run() {
	    	  while (true) {
	    		  try {
	    			  log_in_socket = log_in_server_socket.accept(); // accept가 일어나기 전까지는 무한 대기중
	    			  log_in_Info login_info=new log_in_Info(log_in_socket);
	    			  login_info.start();
	    		  	} catch (IOException e) {
			    	 	System.out.println("로그인 서버 접속 중 "+e+" 발생!");
			     }
			    }
			   }
			  });
		  log_in_th.start();
			 } //Connection method ends//
	 
	 class log_in_Info extends Thread{	//로그인 정보를 주고 받는 joiningInfo class starts//
		  private InputStream is;
		  private OutputStream os;
		  private ObjectInputStream ois;
		  private DataOutputStream dos;
	      private int check;
		  private Socket login_info_socket;
	      private String info_array[]=new String[2];
	      
		  public log_in_Info(Socket soc) // 생성자메소드
		  {
		   // 매개변수로 넘어온 자료 저장
		   this.login_info_socket = soc;
		   login_info_stream();
		  }
		  
		  public void login_info_stream() { //회원 확인 스트림을 설정하는 login_info_stream() method//
			   try {
			    is = login_info_socket.getInputStream();
			    ois = new ObjectInputStream(is);
			    os = login_info_socket.getOutputStream();
			    dos = new DataOutputStream(os);
			    
			   } catch (Exception e) {
				   System.out.println("회원 확인 서버 클래스의 login_info_stream() method에서 "+e+" 발생!");
			   }
			  }	//회원 확인 스트림을 설정하는 login_info_stream() method//
		  
		  public void run(){
			  while(true){
				  try{
					  user_info u_info=(user_info)ois.readObject();
					  info_array[0]=u_info.getId();
					  info_array[1]=u_info.getPasswd();
					  database_select db_select=new database_select(info_array[0],info_array[1]);
					  check= db_select.select_check();//로그인이 제대로 되었는지 답을 보낸다//
					  dos.writeUTF(String.valueOf(check));				  
				  }
				  
				  catch(IOException e){
					  try{
					      dos.close();
					      ois.close();
					      login_info_socket.close();
					      System.out.println("사용자의 회원가입 접속이 끊어짐");
					      break;
					  }
					  
					  catch(Exception ee){
						  System.out.println("log_in_server class의 log_in_Info class, run() method에서 "+ee+" 발생!");
					  }					  
					  
				  }
				  
				  catch(ClassNotFoundException e){
					  					  
				  }
				  
			  }
			  
		  }
		 
	 }	//로그인 정보를 받는 class ends//
	
	 public int check_result(){
		 return check;
	 }
	  
}

