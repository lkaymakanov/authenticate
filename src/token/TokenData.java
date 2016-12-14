package token;



class TokenData implements ITokenData{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3161791488920174149L;
	private String tokenId;
	private String tokenSessionId;
	private String userId;
	private String requestIp;
	
	private TokenData(String tokenId, String tokenSessionid, String userId, String requestIp){
		this.tokenId = tokenId;
		this.tokenSessionId = tokenSessionid;
		this.userId = userId;
		this.requestIp = requestIp;
	}
	
	public static class TokenBuilder{
		private String tokenId;
		private String tokenSessionId;
		private String userId;
		private String requestIp;
		
		public TokenBuilder setTokenId(String tokenId) {
			this.tokenId = tokenId;
			return this;
		}
		public TokenBuilder setTokenSessionId(String tokenSessionId) {
			this.tokenSessionId = tokenSessionId;
			return this;
		}
		public TokenBuilder setUserId(String userId) {
			this.userId = userId;
			return this;
		}
		public TokenBuilder setRequestIp(String requestIp) {
			this.requestIp = requestIp;
			return this;
		}
		
		public ITokenData build(){
		    return new TokenData(tokenId, tokenSessionId, userId, requestIp);
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
	
}
