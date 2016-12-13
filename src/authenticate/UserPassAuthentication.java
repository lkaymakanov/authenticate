package authenticate;

public abstract class UserPassAuthentication<T> implements IAuthentication<T>{
	private UserPassCredetials credentials;
	
	public UserPassAuthentication(String user, String pass){
		this(new UserPassCredetials(user, pass));
	}
	
	private  UserPassAuthentication(UserPassCredetials cr) {
		// TODO Auto-generated constructor stub
		this.credentials = cr;
	}

	public UserPassCredetials getCredentials() {
		return credentials;
	}
}
