package authenticate;

public class AuthenticationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 386962401523854871L;
	private Object exceptionResult;
	
	public AuthenticationException(Object exceptionResult){
		this.exceptionResult = exceptionResult;
	}
	
	public AuthenticationException() {
		// TODO Auto-generated constructor stub
		this(null);
	}
	
	public Object getExceptionResult() {
		return exceptionResult;
	}
	
	
}
