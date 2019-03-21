package javachatting;

import java.net.*;
import java.io.*;
import java.util.Vector;


class joining_server {

	private ServerSocket joining_server_socket; //회원가입 서버소켓
	private Socket joining_socket; // 회원가입 소켓

	
	public joining_server(){  //회원가입 소켓 starts, joining_server() constructor starts//
		  try {
			   joining_server_socket = new ServerSocket(8090); // 서버가 포트 여는부분
			   if(joining_server_socket!=null) // socket 이 정상적으로 열렸을때
			   {
				   Connection();
			   }
			  } catch (IOException e) {
				  System.out.println("회원가입 소켓이 이미 사용중입니다...");
				  System.exit(0);
			  }
			 } //joining_server() constructor ends//
	
	 public void Connection() { //Connection method starts//
		 
		  Thread joining_th = new Thread(new Runnable() { // 회원가입 접속을 받을 스레드
			  
	      @Override
		   public void run() {
		    while (true) { // 회원가입 접속을 계속해서 받기 위해 while문
		     try {
		      joining_socket = joining_server_socket.accept(); // accept가 일어나기 전까지는 무한 대기중
		      joiningInfo joining_info=new joiningInfo(joining_socket);
		      joining_info.start();
		     } catch (IOException e) {
		    	 	System.out.println("회원가입 접속 중 "+e+" 발생!");
		     }
		    }
		   }
		  });
		  joining_th.start();
		  		  
		 } //Connection method ends//
	 
	 class joiningInfo extends Thread{	//회원가입 정보를 주고 받는 joiningInfo class starts//
		  private InputStream is;
		  private OutputStream os;
		  private ObjectInputStream ois;
		  private DataOutputStream dos;
	      private int check;
		  private Socket user_info_socket;
	      private String info_array[]=new String[6];
	      
		  public joiningInfo(Socket soc) // 생성자메소드
		  {
		   // 매개변수로 넘어온 자료 저장
		   this.user_info_socket = soc;
		   user_info_stream();
		  }
		  
		  public void user_info_stream() { //회원 가입 스트림을 설정하는 user_info_stream() method//
			   try {
			    is = user_info_socket.getInputStream();
			    ois = new ObjectInputStream(is);
			    os = user_info_socket.getOutputStream();
			    dos = new DataOutputStream(os);
			    
			   } catch (Exception e) {
				   System.out.println("회원가입 서버 클래스의 user_info() method에서 "+e+" 발생!");
			   }
			  }	//회원 가입 스트림을 설정하는 user_info_stream() method//
		  
		  public void run(){
			  while(true){
				  try{
					  user_info u_info=(user_info)ois.readObject();
					  info_array[0]=u_info.getName();
					  info_array[1]=u_info.getId();
					  info_array[2]=u_info.getPasswd();
					  info_array[3]=u_info.getEmail();
					  info_array[4]=u_info.getBirth();
					  info_array[5]=u_info.getSex();
					  database_insert db_insert=new database_insert(info_array[0],info_array[1],info_array[2],info_array[3],info_array[4],info_array[5]);
					  check= db_insert.insert_check();//회원 가입이 제대로 되었는지 답을 보낸다//
					  dos.writeUTF(String.valueOf(check));				  
				  }
				  
				  catch(IOException e){
					  try{
					      dos.close();
					      ois.close();
					      user_info_socket.close();
					      break;
					  }
					  
					  catch(Exception ee){
						  System.out.println("joining_server class의 joinInfo class, run() method에서 "+ee+" 발생!");
					  }					  
					  
				  }
				  
				  catch(ClassNotFoundException e){
					  					  
				  }
				  
			  }
			  
		  }
		 
	 }	//회원가입 정보를 주고 받는 joiningInfo class ends//
	
}

