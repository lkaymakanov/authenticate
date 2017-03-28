package net.is_bg.ltfn.authenticate;

import java.util.List;

public class TokenAuthenticationParams  {

	private boolean allowTokenAuthentication;
	private List<Object> userLogCallBackParam;
	private boolean supressIpCheck;
	private boolean overWriteUser;
	
	public boolean isAllowTokenAuthentication() {
		return allowTokenAuthentication;
	}
	public void setAllowTokenAuthentication(boolean allowTokenAuthentication) {
		this.allowTokenAuthentication = allowTokenAuthentication;
	}
	public List<Object> getUserLogCallBackParam() {
		return userLogCallBackParam;
	}
	public void setUserLogCallBackParam(List<Object> userLogCallBackParam) {
		this.userLogCallBackParam = userLogCallBackParam;
	}
	public boolean isSupressIpCheck() {
		return supressIpCheck;
	}
	public void setSupressIpCheck(boolean supressIpCheck) {
		this.supressIpCheck = supressIpCheck;
	}
	public boolean isOverWriteUser() {
		return overWriteUser;
	}
	public void setOverWriteUser(boolean overWriteUser) {
		this.overWriteUser = overWriteUser;
	}
	
}
