package javachatting;

import java.awt.*;
import java.awt.Event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.sql.*;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class joining extends JFrame{ //회원가입 클래스 시작//
	
	private JPanel contentPane;
	JLabel name_label,id_label,password_label,password_confirm_label,email_label,birth_label,sex_label;
	JLabel birth_yy_label,birth_mm_label,birth_dd_label;
	private JTextField name_txtField,id_txtField,email_txtField;
	private JTextField birth_yy_txtField, birth_mm_txtField, birth_dd_txtField;
	private JPasswordField password_Field,password_confirm_Field;
	private Choice sex_choice;
	JButton confirm_button;
	private String info_string[]=new String [6]; //회원가입 정보 저장 배열//
	
    private Socket socket; // 연결소켓
    private InputStream is;
    private OutputStream os;
    private DataInputStream dis;
    private ObjectOutputStream oos;
    
    private String ip_address;
    private int check_result=-1;
	
    public joining(String str1){ //회원가입 컨스트럭터 시작//
    
    ip_address=str1;
    
    setVisible(true);
    setBounds(400, 200, 400, 392);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
    setContentPane(contentPane);
    contentPane.setLayout(null); //자바 스윙을 실행할 때 컴포넌트가 보이지 않는 현상을 해결!!//
    
    //라벨 만들기 시작//
    
    name_label=new JLabel("이름");
    id_label=new JLabel("아이디");
    password_label=new JLabel("비밀번호");
    password_confirm_label=new JLabel("비밀번호 재입력");
    email_label=new JLabel("이메일 주소");
    birth_label=new JLabel("생년월일");
    sex_label=new JLabel("성별");
    
    birth_yy_label=new JLabel("년");
    birth_mm_label=new JLabel("월");
    birth_dd_label=new JLabel("일");
    
    name_label.setHorizontalAlignment(JLabel.CENTER);
    id_label.setHorizontalAlignment(JLabel.CENTER);
    password_label.setHorizontalAlignment(JLabel.CENTER);
    password_confirm_label.setHorizontalAlignment(JLabel.CENTER);
    email_label.setHorizontalAlignment(JLabel.CENTER);
    birth_label.setHorizontalAlignment(JLabel.CENTER);
    sex_label.setHorizontalAlignment(JLabel.CENTER);
    
    name_label.setBounds(20,25,110,20);
    id_label.setBounds(20,55,110,20);
    password_label.setBounds(20,85,110,20);
    password_confirm_label.setBounds(20,115,110,20);
    email_label.setBounds(20,145,110,20);
    birth_label.setBounds(20,175,110,20);
    sex_label.setBounds(20,205,110,20);
    
    contentPane.add(name_label);
    contentPane.add(id_label);
    contentPane.add(password_label);
    contentPane.add(password_confirm_label);
    contentPane.add(email_label);
    contentPane.add(birth_label);
    contentPane.add(sex_label);
    
    //라벨 끝//
    
    name_txtField=new JTextField();
    id_txtField=new JTextField();
    password_Field=new JPasswordField();
    password_confirm_Field=new JPasswordField();
    email_txtField=new JTextField();
    birth_yy_txtField=new JTextField(4);
    birth_mm_txtField=new JTextField(2);
    birth_dd_txtField=new JTextField(2);
    sex_choice=new Choice();
    sex_choice.add("남");
    sex_choice.add("여");
    
    name_txtField.setBounds(150,25,200,20);
    id_txtField.setBounds(150,55,200,20);
    password_Field.setBounds(150,85,200,20);
    password_confirm_Field.setBounds(150,115,200,20);
    email_txtField.setBounds(150,145,200,20);
    birth_yy_txtField.setBounds(150,175,50,20);
    birth_yy_label.setBounds(205,175,20,20);
    birth_mm_txtField.setBounds(225,175,40,20);
    birth_mm_label.setBounds(270,175,20,20);
    birth_dd_txtField.setBounds(295,175,40,20);
    birth_dd_label.setBounds(340,175,40,20);
    sex_choice.setBounds(200,205,100,20);
    
    contentPane.add(name_txtField);
    contentPane.add(id_txtField);
    contentPane.add(password_Field);
    contentPane.add(password_confirm_Field);
    contentPane.add(email_txtField);
    contentPane.add(birth_yy_txtField);
    contentPane.add(birth_mm_txtField);
    contentPane.add(birth_dd_txtField);
    contentPane.add(birth_yy_label);
    contentPane.add(birth_mm_label);
    contentPane.add(birth_dd_label);
    contentPane.add(sex_choice);
    
    confirm_button=new JButton("확인");
    confirm_button.setBounds(130,270,150,40);
    contentPane.add(confirm_button);
    
    confirm_button.addActionListener(new ActionListener() { // 확인 버튼을 눌렀을 때 event starts//
		   @Override
	   public void actionPerformed(ActionEvent e) { 	//actionPerformed starts//
	    if (name_txtField.getText().length()<2){
	    	JOptionPane.showMessageDialog(null, "이름을 2자 이상 입력해주세요!");
	    	
	    } // 이름을 입력하지 않을 때
	    
	    else if (name_txtField.getText().length()>10){
	    	JOptionPane.showMessageDialog(null, "이름을 10자 이하로 입력해주세요!");
	    	
	    } // 이름을 입력하지 않을 때
	    
	    else if(id_txtField.getText().length()<6){
	    	JOptionPane.showMessageDialog(null, "id를 6자 이상 입력해주세요!");
	    }  // id를 입력하지 않을 때
	    
	    else if(password_Field.getText().length()<6){
	    	JOptionPane.showMessageDialog(null, "비밀번호를 6자 이상 입력해주세요!");
	    }  // 비밀번호를 입력하지 않을 때
	    
	    else if(password_confirm_Field.getText().equals("")){
	    	JOptionPane.showMessageDialog(null, "비밀번호를 확인해주세요!");
	    }  // 비밀번호를 확인하지 않을 때
	    
	    else if(!password_Field.getText().equals(password_confirm_Field.getText())){
	    	JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다!");
	    }  // 비밀번호가 일치하지 않을 때
	    
	    else if(email_txtField.getText().equals("")){
	    	JOptionPane.showMessageDialog(null, "이메일 주소를 입력해주세요!");
	    }  // 이메일 주소를 입력하지 않을 때
 
	    else if(Integer.parseInt(birth_yy_txtField.getText())<1900||Integer.parseInt(birth_yy_txtField.getText())>2015){
	    	JOptionPane.showMessageDialog(null, "생일을 제대로 입력해주세요!");
	    }  // 생일을 입력하지 않을 때
	    
	    else if(Integer.parseInt(birth_mm_txtField.getText())<1||Integer.parseInt(birth_mm_txtField.getText())>13){
	    	JOptionPane.showMessageDialog(null, "생일을 제대로 입력해주세요!");
	    	
	    }  // 생일을 입력하지 않을 때
	    else if(Integer.parseInt(birth_dd_txtField.getText())<1||Integer.parseInt(birth_dd_txtField.getText())>32){
	    	JOptionPane.showMessageDialog(null, "생일을 제대로 입력해주세요!");
	    }  // 생일을 입력하지 않을 때
	    
	    else{
	    	String month="";
	    	String day="";
	    	info_string[0]=name_txtField.getText();
	    	info_string[1]=id_txtField.getText();
	    	info_string[2]=password_Field.getText();
	    	info_string[3]=email_txtField.getText();
	    	info_string[5]="";
	    	
	    	if(birth_mm_txtField.getText().length()==1){
	    		month="0"+birth_mm_txtField.getText();		
	    	}
	    	else if(birth_dd_txtField.getText().length()==1){
	    		day="0"+birth_dd_txtField.getText();
	    	}
	    	else{
	    		month=birth_mm_txtField.getText();
	    		day=birth_dd_txtField.getText();
	    	}
	    	info_string[4]=birth_yy_txtField.getText()+month+day;
	    	server();
	    	} // 서버에 연결
	   }
	  });  // 확인 버튼을 눌렀을 때 event ends//
    }  //회원가입 컨스트럭터 끝//
    
    	public void server(){ //서버 연결 starts, server starts//
    		try{
    			socket=new Socket(ip_address,8090);
    			server_network();
    		}
    		
    		catch(Exception e){System.out.println("server 메소드에서 발생한 "+e);}
    		
    	} //server method ends//
    	
    	public void server_network(){ //server_network method starts//
    		try
    		{
    		user_info userinfo=new user_info(info_string[0],info_string[1],info_string[2],info_string[3],info_string[4],info_string[5]);
    		os=socket.getOutputStream();
    		oos=new ObjectOutputStream(os);
    		oos.writeObject(userinfo);
   		
  		  Thread th = new Thread(new Runnable() { //서버로부터 응답을 기다리는 메소드 시작 //
  			   @Override
  			   public void run() {
  			    while (true) { // 사용자 접속을 계속해서 받기 위해 while문
  			     try {
  		    		is=socket.getInputStream();
  		    		dis=new DataInputStream(is);
  		    		String result=dis.readUTF();
  		    		check_result=Integer.parseInt(result);
  		    		
  		    		if(check_result==1){JOptionPane.showMessageDialog(null, "가입을 축하드립니다!");
  		    		dispose();
  		    		oos.close();
  		    		dis.close();
  		    		os.close();
  		    		dis.close();
  		    		socket.close();
  		    		break;
  		    		}
  		    		
  		    		else if(check_result==2){JOptionPane.showMessageDialog(null, "이미 사용 중인 아이디 입니다. 다른 아이디를 입력해주세요!");
  		    		id_txtField.setText("");
  		    		id_txtField.requestFocus();
  		    		}
  		    		
  		    		else if(check_result==-1){
  		    		JOptionPane.showMessageDialog(null, "회원 가입 서버와 연결이 끊겼습니다!");
  		    		dispose();
  		    		}
  		    		
  			     } 

  			     	catch (Exception e) {
  	  			     	JOptionPane.showMessageDialog(null, "회원 가입에 실패했습니다! 다시 한 번 시도해 주세요!");
  	  			     	dispose();
  			    	 break;
  			     }
  			    }
  			   }
  			  });//서버로부터 응답을 기다리는 메소드 끝
  		  
  		  	th.start();
  		  
    		}
    		
    		catch(Exception e){System.out.println("server_network에서 일어난 "+e);}
    	}//server_network method ends//
    
}  //회원가입 클래스 끝//

