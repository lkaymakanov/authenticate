package authenticate;

import java.io.Serializable;


public class TokenData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3161791488920174149L;
	private String tokenId;
	private String tokenSessionId;
	private String userId;
	
	
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public String getTokenSessionId() {
		return tokenSessionId;
	}
	public void setTokenSessionId(String tokenSessionId) {
		this.tokenSessionId = tokenSessionId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
