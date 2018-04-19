package authenticate.token;


public  interface ISessionData {
	public boolean isOverWriteUser();
	public void setOverWriteUser(boolean b);
	public String getSessionIdThatCreatedToken();
	public Object getUser();
	public String getUserId();
	public String getSessionId();
}