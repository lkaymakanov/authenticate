package authenticate.token;

import java.io.Serializable;


/***
 * Structure of token data exchanged between applications
 * @author Lubo
 *
 */
public class TokenData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5197647880606629297L;
	private String tokenId;
	private String userId;
	private String userName;
	private String ipAddress;
	private String sessionIdThatCreatedThisToken;
	private String dataSourceName;
	private String signer = "LTF";
	
	
	private TokenData(String tokenId, String userId, String userName, String ipAddress,
			String sessionIdThatCreatedThisToken, String dataSourceName, String signer) {
		this.tokenId = tokenId;
		this.userId = userId;
		this.userName = userName;
		this.ipAddress = ipAddress;
		this.sessionIdThatCreatedThisToken = sessionIdThatCreatedThisToken;
		this.dataSourceName = dataSourceName;
		this.signer = signer;
		
	}
	private TokenData() {
	}
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getSessionIdThatCreatedThisToken() {
		return sessionIdThatCreatedThisToken;
	}
	public void setSessionIdThatCreatedThisToken(String sessionIdThatCreatedThisToken) {
		this.sessionIdThatCreatedThisToken = sessionIdThatCreatedThisToken;
	}
	public String getDataSourceName() {
		return dataSourceName;
	}
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
	public String getSigner() {
		return signer;
	}
	public void setSigner(String signer) {
		this.signer = signer;
	}
	
	/**
     * Creates token data!!!
     * @param userId
     * @param ip
     * @param tokenSessionId
     * @param tokenId
     * @param issiuer
     * @param dbconName
     * @return
     */
    public static TokenData createTokenData(String tokenId, String userId, String userName, 
    										String ipAddress, String sessionIdThatCreatedThisToken,  String dataSourceName,  
    										String signer){
    	return   new TokenData(tokenId, userId, userName, ipAddress, sessionIdThatCreatedThisToken, dataSourceName, signer );
    }
	
    
    public static TokenData emptyTokenData() {
    	return new TokenData();
    }
	
}
