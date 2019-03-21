package javachatting;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;


public class ip_access extends JFrame{
	
	private JPanel contentPane;
	private String ip_address;
	private JLabel ip_label;
	private JTextField ip_textField;
	private JButton ip_button;
	
	public ip_access(){
		
		  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  setBounds(500, 300, 288, 200);
		  contentPane = new JPanel();
		  contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		  setContentPane(contentPane);
		  contentPane.setLayout(null); //자바 스윙을 실행할 때 컴포넌트가 보이지 않는 현상을 해결!!//
		  
		  ip_label = new JLabel("IP 주소");
		  ip_label.setBounds(35, 57, 90, 25);
		  contentPane.add(ip_label);
		  ip_textField = new JTextField();
		  ip_textField.setBounds(92, 57, 150, 25);
		  ip_textField.setColumns(10);
		  contentPane.add(ip_textField);
		  
		  ip_button = new JButton("IP 접속");
		  ip_button.setBounds(95, 105, 95, 32);
		  contentPane.add(ip_button);
		  
		  ip_button.addActionListener(new ActionListener() { // 가입 버튼 클릭 starts //
				   @Override
			   public void actionPerformed(ActionEvent e) {
					   
					    ip_address = ip_textField.getText(); 
					    if(ip_address.length()<4){
					    	JOptionPane.showMessageDialog(null, "IP 주소를 제대로 입력해 주세요!");
					    }
					    
					    else{
					    	new client(ip_address);
					    	dispose();
					    }
				   }
				  });
		
	}
	
	public static void main(String args[]){
		
		ip_access access=new ip_access();
		access.setVisible(true);
		
	}
	
}

