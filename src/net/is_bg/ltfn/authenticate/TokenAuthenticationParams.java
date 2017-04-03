package net.is_bg.ltfn.authenticate;

import java.util.List;

public class TokenAuthenticationParams  {

	private boolean allowTokenAuthentication;
	private List<Object> userLogCallBackParam;
	private boolean supressIpCheck;
	private boolean overWriteUser;
	private boolean verbose = false;
	
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
	public boolean isVerbose() {
		return verbose;
	}
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	
	public String getConfiguration(){
		StringBuilder sb = new StringBuilder();
		sb.append("allowTokenAuthentication:" + allowTokenAuthentication); sb.append("\n");
		sb.append("supressIpCheck:" + supressIpCheck); sb.append("\n");
		sb.append("overWriteUser:" + overWriteUser); sb.append("\n");
		sb.append("verbose:" + verbose);sb.append("\n");
		String s = sb.toString();
		return s; 
	}
	
}
