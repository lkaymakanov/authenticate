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
	private Object data;
	
	private AuthenticationException() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	private AuthenticationException(String message, Throwable t, Object data){
		super(message, t);
		this.data = data;
	}
	
	private AuthenticationException(String message){
		super(message);
	}
	
	
	public <T> T getData() {
		return (T) data;
	}


	public static class AuthenticationExceptionBuilder<T>{
		private  String message; 
		private  Throwable t;
		private  T data;

		public AuthenticationExceptionBuilder<T> setMessage(String message) {
			this.message = message;
			return this;
		}

		public AuthenticationExceptionBuilder<T> setData(T data) {
			this.data = data;
			return this;
		}

		public AuthenticationExceptionBuilder<T> setT(Throwable t) {
			this.t = t;
			return this;
		}
		
		public AuthenticationException build(){
			return t == null  ? new AuthenticationException(message) : new AuthenticationException(message, t, data); 
		}
	}
	
	
}
