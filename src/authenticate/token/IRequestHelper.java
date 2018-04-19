package authenticate.token;

import java.util.Map;

public  interface IRequestHelper{
	public String getIpAddress();
	public String getTokenId();
	public String getSessionId();
	String getRequestURL();
	Map getParameterMap();
}