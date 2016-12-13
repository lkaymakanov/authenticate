package authenticate;

public class UserPassCredetials implements ICredentials {
	private String user;
	private String pass;
	
	public UserPassCredetials(String user, String pass) {
		// TODO Auto-generated constructor stub
		this.user = user;
		this.pass = pass;
	}

	public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
	}
}
