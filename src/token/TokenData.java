package token;



public class TokenData<T> implements ITokenData<T>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3161791488920174149L;
	private String tokenId;
	private String tokenSessionId;
	private String userId;
	private String requestIp;
	private T data;
	
	private TokenData(String tokenId, String tokenSessionid, String userId, String requestIp, T data){
		this.tokenId = tokenId;
		this.tokenSessionId = tokenSessionid;
		this.userId = userId;
		this.requestIp = requestIp;
		this.data = data;
	}
	
	public static class TokenDataBuilder<T>{
		private String tokenId;
		private String tokenSessionId;
		private String userId;
		private String requestIp;
		private T data;
		
		public TokenDataBuilder<T> setTokenId(String tokenId) {
			this.tokenId = tokenId;
			return this;
		}
		public TokenDataBuilder<T> setTokenSessionId(String tokenSessionId) {
			this.tokenSessionId = tokenSessionId;
			return this;
		}
		public TokenDataBuilder<T> setUserId(String userId) {
			this.userId = userId;
			return this;
		}
		public TokenDataBuilder<T> setRequestIp(String requestIp) {
			this.requestIp = requestIp;
			return this;
		}
		
		public TokenDataBuilder<T> setAdditionalData(T data){
			this.data = data;
			return this;
		}
		
		public ITokenData<T> build(){
			TokenData<T> d =  new TokenData<T>(tokenId, tokenSessionId, userId, requestIp, data);
			return d;
		}
	}
	
	public String getTokenId() {
		return tokenId;
	}

	public String getTokenSessionId() {
		return tokenSessionId;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public String getRequestIp() {
		return requestIp;
	}
	
	public T getAdditionalData() {
		// TODO Auto-generated method stub
		return data;
	}
}
