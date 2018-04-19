package authenticate.token;


public  interface ISessionDataFactory {
	public ISessionData getISessionData(Object httpServletRequest);
}