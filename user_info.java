package javachatting;

import java.io.Serializable;

class user_info implements Serializable{
	private String name="";
	private String id="";
	private String passwd="";	
	private String email="";
	private String birth="";
	private String sex="";
	
	public user_info(String str1, String str2){
		id=str1;
		passwd=str2;
	}
	
	public user_info(String str1,String str2,String str3,String str4,String str5,String str6){
		name=str1;
		id=str2;
		passwd=str3;	
		email=str4;
		birth=str5;
		sex=str6;
	}
	
	public String getName(){
		
		return name;
	}
	
	public String getId(){
		
		return id;
	}
	
	public String getPasswd(){
		
		return passwd;
	}
	
	public String getEmail(){
		
		return email;
	}
	
	public String getBirth(){
		
		return birth;
	}
	
	public String getSex(){
		
		return sex;
	}

}

