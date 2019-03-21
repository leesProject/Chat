package javachatting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Calendar;

public class database_insert {
	String name,id,passwd,email,birth, sex, joined;
	Connection con;
	Statement st;
	int i=0;
	
	public database_insert(String str1,String str2,String str3,String str4,String str5,String str6){
		
		Calendar cal=Calendar.getInstance();
		String year=String.valueOf(cal.get(Calendar.YEAR));
		String month=String.valueOf(cal.get(Calendar.MONTH)+1);
		String day=String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		
		name=str1;
		id=str2;
		passwd=str3;
		email=str4;
		birth=str5;
		sex=str6;
		joined=year+month+day;
		
		access_database();
	}

	public void access_database(){ //access_database() method starts//
	try{
		con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/server","root","1234");
		st=con.createStatement();
		insert_data();
		}
		catch(Exception e){System.out.println("데이터베이스 접속 도중 "+e+"가 발생했습니다.");}
	}   //access_database() method ends //
	
	public void insert_data(){
		try{
		String insert_query="insert into members(name, id, passwd, email, birth, sex, joined)	values('"+name+"','"+id+"','"+passwd+"','"+email+"',"+birth+", '"+sex+"',"+joined+");";
		st.executeUpdate(insert_query);
		System.out.println(name+"님이 가입하셨습니다!");
		i=1;
		}
		catch(com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException entity_integrity_exception){
		i=2;
		}
		catch(Exception e){
		System.out.println("데이터베이스 입력 도중 "+e+"가 발생했습니다.");
		i=0;
		}
		insert_check();
	}
	
	public int insert_check(){
		return i;
	}
}

