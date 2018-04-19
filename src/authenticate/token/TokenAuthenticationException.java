package authenticate.token;

/**
 * Authentication exception thrown on unsuccessful authentication !!!
 * @author lubo
 */
public class TokenAuthenticationException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 386962401523854871L;
	private Object data;
	
	
	private TokenAuthenticationException(String message, Throwable t, Object data){
		super(message, t);
		this.data = data;
	}
	
	private TokenAuthenticationException(String message, Object data){
		super(message);
		this.data = data;
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
		
		public TokenAuthenticationException build(){
			return t == null  ? new TokenAuthenticationException(message, data) : new TokenAuthenticationException(message, t, data); 
		}
	}
	
	
}
