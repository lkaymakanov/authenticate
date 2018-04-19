package authenticate.token;

import java.util.List;

/**
 * Interface that contains the callbacks used on the different steps of
 * token authentication!!!
 * @author lubo
 *
 */
public interface ITokenAuthneticationCallBacks {
	  /***/
	  IAuthenticationCallBack<TokenData, Object> decryptTokenDataCallBack();
	  /***/
	  IAuthenticationCallBack<String, Object> getIpAddressCallBack();
	  IAuthenticationCallBack<Object, String> getTokenDataFromServerCallBack();
	  IAuthenticationCallBack<Object, List<Object>> userLogCallBack();
	  IAuthenticationCallBack<Boolean, String> isTokenValidCallBack();
}

