package authenticate;

/**
 * Authentication exception thrown on unsuccessful authentication !!!
 * @author lubo
 */
public class AuthenticationException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 386962401523854871L;
	public AuthenticationException() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public AuthenticationException(String message, Throwable t){
		super(message, t);
	}
	
	public AuthenticationException(String message){
		super(message);
	}
	
}
