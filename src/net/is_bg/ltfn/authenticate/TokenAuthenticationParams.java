package net.is_bg.ltfn.authenticate;

import java.util.List;

/***
 * Token authentication parameters!!!
 * @author lubo
 *
 */
public class TokenAuthenticationParams  {

	/**Allows token authentication if true*/
	private boolean allowTokenAuthentication;
	/*** For now list of three output parameters  passed into user log function!!!*/
	private List<Object> userLogCallBackParam;
	/**If true doesn't take into account the ip address of the incoming request!!!*/
	private boolean supressIpCheck;
	/**Overwrites user in current user session if valid token is passed in request!!!*/
	private boolean overWriteUser;
	/**Outputs detailed information when logging is performed!!!*/
	private boolean verbose = false;
	
	/**Allows token authentication if true*/
	public boolean isAllowTokenAuthentication() {
		return allowTokenAuthentication;
	}
	/**Allows token authentication if true*/
	public void setAllowTokenAuthentication(boolean allowTokenAuthentication) {
		this.allowTokenAuthentication = allowTokenAuthentication;
	}
	public List<Object> getUserLogCallBackParam() {
		return userLogCallBackParam;
	}
	public void setUserLogCallBackParam(List<Object> userLogCallBackParam) {
		this.userLogCallBackParam = userLogCallBackParam;
	}
	/**If false doesn't take into account the ip address of the incoming request!!!*/
	public boolean isSupressIpCheck() {
		return supressIpCheck;
	}
	/**If false doesn't take into account the ip address of the incoming request!!!*/
	public void setSupressIpCheck(boolean supressIpCheck) {
		this.supressIpCheck = supressIpCheck;
	}
	/**Overwrites user in current user session if valid token is passed in request!!!*/
	public boolean isOverWriteUser() {
		return overWriteUser;
	}
	/**Overwrites user in current user session if valid token is passed in request!!!*/
	public void setOverWriteUser(boolean overWriteUser) {
		this.overWriteUser = overWriteUser;
	}
	/**Outputs detailed information when logging is performed!!!*/
	public boolean isVerbose() {
		return verbose;
	}
	/**Outputs detailed information when logging is performed!!!*/
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
