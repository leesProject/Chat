package javachatting;

import java.sql.*;
import java.util.Calendar;

public class database_select {

	String id, password, last_access;
	Connection con;
	Statement select_st, update_st;
	ResultSet select_result;
	int i=0;
	
	public database_select(String id, String password){
		
		this.id=id;
		this.password=password;
		access_database();
	}
	
	public void access_database(){		//access_database 메소드 시작
		try{
			con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/server","root","1234");
			select_st=con.createStatement();
			checkId();
		}
		
		catch(Exception e){
			System.out.println("로그인 정보 데이터베이스 접근 중 발생한 "+e);			
		}
	}		//access_database 메소드 끝
	
	public void checkId(){											//회원 아이디와 패스워드를 검사하는 메소드 시작//
				String select_query="select passwd from members where id='"+id+"';";
				try{
				select_result=select_st.executeQuery(select_query);
				select_result.next();
					if(password.equals(select_result.getString(1))){	//회원 아이디와 패스워드가 일치할 때//
						access_update();
						i=1;
						System.out.println(id+"의 로그인 허용!");
						}

				}
				
				catch( java.sql.SQLException e){					//회원 아이디와 패스워드가 일치하지 않을 때//
					System.out.println("아이디와 패스워드 확인 중 발생한 "+e);
					i=2;}

			}														//회원 아이디와 패스워드를 검사하는 메소드 끝//
	
	public void access_update(){									//마지막 접속 날짜를 갱신하는 메소드 시작//
		
			Calendar cal=Calendar.getInstance();
			String year=String.valueOf(cal.get(Calendar.YEAR));
			String month=String.valueOf(cal.get(Calendar.MONTH)+1);
			String day=String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
			last_access=year+month+day;
			
			try{
				update_st=con.createStatement();
				String update_query="update members set last_access="+last_access+" where id='"+id+"';";
				update_st.executeUpdate(update_query);
			}
			
			catch(Exception e){
				System.out.println("접속 날짜 갱신 중 발생한 "+e);				
			}
	}																//마지막 접속 날짜를 갱신하는 메소드 끝//
	
	public int select_check(){
		return i;		
	}
	
}

