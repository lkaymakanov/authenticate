package net.is_bg.ltfn.authenticate;

import token.ITokenData;

public class TokenDataUserData {

	private String userKey;
	private String defDbCon;
	private ITokenData tokenData;
	
	
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	public ITokenData getTokenData() {
		return tokenData;
	}
	public void setTokenData(ITokenData tokenData) {
		this.tokenData = tokenData;
	}
	public String getDefDbCon() {
		return defDbCon;
	}
	public void setDefDbCon(String defDbCon) {
		this.defDbCon = defDbCon;
	}
}
