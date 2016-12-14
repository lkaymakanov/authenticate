package token;

import java.io.Serializable;

/***
 * Data associated with a  token id!!!
 * @author lubo
 *
 */
public interface ITokenData extends Serializable{
	/***
	 * The unique id of the token!!!
	 * @return
	 */
	public String getTokenId();
	
	/***
	 * The session associated with this token this is the session that token is attached to!!!
	 * @return
	 */
	public String getTokenSessionId();
	
	/***
	 * The user that logged with this token!!!
	 * @return
	 */
	public String getUserId();
	
	/**
	 * The ip address that uses this token!!!
	 * @return
	 */
	public String getRequestIp();
}
