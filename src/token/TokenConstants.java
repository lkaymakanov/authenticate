package token;

/**
 * 
 * @author lubo
 *
 */
public class TokenConstants {
	
	public static final String TOKEN_ID_PARAM_NAME = "tokenid";
	public static final String TOKEN_SESSION_ID_PARAM_NAME = "tokensessionid";
	public static final String USER_ID_PARAM_NAME = "uid";
	public static final String TOKEN_DATA_PATH = "/token/data";
	public static final String TOKEN_VERIFY_PATH = "/token/verify";
	public static final String IVALID_TOKEN_ID = "IVALID_TOKEN_ID";

	public enum AUTHENTICATION_TYPE{
		USERPASS,
		TOKEN,
		MIXED;
	}
}
