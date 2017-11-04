public class User {

	private String user_name;
	private String family_name;
	private String login_name;
	private String id;
	private char[] pw;
	
	/*
	 * stores user name, family name, login name, password, and unique user id 
	 */
	
	public User(String user, String family, String login_name, char[] password) {

		this.user_name = user;
		this.family_name = family;
		this.login_name = login_name;
		this.pw = password;
	}

	public void set_userID(String i) {

		id = i;
	}
	
	public String get_userID() {

		return id;
	}

	public String get_name(){
		
		return user_name;
	}

	public String get_login() {
		
		return login_name;
	}
	
	public char[] get_pw() {

		return pw;
	}
}

