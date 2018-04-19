package authenticate.token;

/***
 * Interface that contains user login in  functions!!!
 * @author lubo
 *
 */
public  interface ILogUser {
	public  Object logUser(Object httpServletRequest,  String userKey, String tokenId, String dbIndex);
	public  Object logUser(Object httpServletRequest,  Object user, String tokenId, String defDbConn);
	
}