package javachatting;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;


public class client extends JFrame{ //로그인 화면 client class starts//
 private JPanel contentPane;
 private JTextField id_TextField; // ID를 입력받을곳
 private JPasswordField password_Field; // password를 입력받을곳
 private Socket socket; // 연결소켓
 private InputStream is;
 private OutputStream os;
 private DataInputStream dis;
 private ObjectOutputStream oos;
 private String id,password,ip;
  
 public client(String str1) // client() constructor starts//
 {
  ip=str1;
  setVisible(true);	 
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  setBounds(400, 200, 288, 320);
  contentPane = new JPanel();
  contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
  setContentPane(contentPane);
  contentPane.setLayout(null); //자바 스윙을 실행할 때 컴포넌트가 보이지 않는 현상을 해결!!//
  
  
  JLabel id_label = new JLabel("아이디");
  id_label.setBounds(35, 57, 90, 34);
  contentPane.add(id_label);
  id_TextField = new JTextField();
  id_TextField.setBounds(92, 57, 150, 21);
  id_TextField.setColumns(10);
  contentPane.add(id_TextField);

  JLabel password_label = new JLabel("비밀번호");
  password_label.setBounds(35, 111, 90, 34);
  contentPane.add(password_label);
  password_Field = new JPasswordField();
  password_Field.setBounds(92, 111, 150, 21);
  password_Field.setColumns(10);
  contentPane.add(password_Field);
  
  JLabel question_label = new JLabel("아직 회원이 아니세요?");
  question_label.setBounds(20, 165, 130, 34);
  contentPane.add(question_label);
  
  JButton joining_button = new JButton("가입하기");
  joining_button.setBounds(160, 165, 95, 42);
  contentPane.add(joining_button);
  joining_button.addActionListener(new ActionListener() { // 가입 버튼 클릭 starts //
		   @Override
	   public void actionPerformed(ActionEvent e) { 	
		    joining join=new joining(ip);
		    join.setVisible(true);
		   }
		  }); // 가입 버튼 클릭 ends //

    
  JButton access_Button = new JButton("접    속");
  access_Button.setBounds(36, 225, 206, 45);
  contentPane.add(access_Button);
  access_Button.addActionListener(new ActionListener() {  //access_button event starts//
   
   @Override
   public void actionPerformed(ActionEvent arg0) { 
    
    id = id_TextField.getText(); // transmit ID
    password= password_Field.getText();; // transmit Password
    server();							//로그인 서버에 접속
    }
  });  //access_button event ends//
 }  //client() constructor ends//
 
 	public void server(){				//로그인 서버에 접속 시작//
 		try{
			socket=new Socket(ip,8082);
    		user_info userinfo=new user_info(id, password);
    		os=socket.getOutputStream();
    		oos=new ObjectOutputStream(os);
    		oos.writeObject(userinfo);
    		
    		  Thread th = new Thread(new Runnable() { //서버로부터 응답을 기다리는 메소드 시작 //
     			   @Override
     			   public void run() {
     			    while (true) {
     			     try {
    		    		is=socket.getInputStream();
     		    		dis=new DataInputStream(is);
     		    		String result=dis.readUTF();
     		    		int check_result=Integer.parseInt(result);
     		    		if(check_result==1)
     		    		{    
     		    			ChattingView view = new ChattingView(id,password,ip);
     		    			dispose();
 		    			
     		    			oos.close();
     		    			dis.close();
     		    			os.close();
     		    			is.close();
     		    			socket.close();
     		    			
     		    			break;}
     		    		
     		    		else if(check_result==2){
     		    			JOptionPane.showMessageDialog(null, "아이디와 비밀번호를 정확하게 입력해주세요!");
     		    			break;
     		    		}
     		    		
     		    		else{}
     		    		
     			     }
      			     
     			     catch (Exception e) {
      			    	JOptionPane.showMessageDialog(null, "로그인 서버와 연결이 끊겼습니다!");
      			    	dispose();
      			    	break;
     			     }
     			    }
     			   }
     			  });//서버로부터 응답을 기다리는 메소드 끝
     		  
    		  th.start();
 		} 	
 		
 		catch(Exception e){
 			
 		}
 	}
 
} //class client ends//





