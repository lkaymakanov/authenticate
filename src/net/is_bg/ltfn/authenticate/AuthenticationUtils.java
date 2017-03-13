package net.is_bg.ltfn.authenticate;

import java.util.Map;

import token.ITokenData;
import token.TokenData.TokenDataBuilder;
import token.TokenUtils;
import authenticate.IAuthentication;

/**
 * Authentication Utils !!!
 * 
 * @author lubo
 *
 */
public class AuthenticationUtils {
	


    /**
     * Gets a user password based authentication!!!
     * 
     * @param user
     * @param pass
     * @param dbConnName
     * @return
     */
    public static IAuthentication getUserPassAuthenticator(String user,
									 String pass, IAuthenticationCallBack callBack, Object callBackParam) {
    	return new UserPassAuthenticationFactory(user, pass, callBack, callBackParam).getAuthentication();
    }


  
    
    public static  ITokenData<byte[]> createTokenData(ClassLoader cl, String ipAddress, String sessionId, long userId, 
    					  String userKey, IAuthenticationCallBack<String, String> callBack, String callBackParam
						 )
	    throws Exception {

		TokenDataBuilder<byte[]> tokenDataBuilder = new TokenDataBuilder<byte[]>();
		tokenDataBuilder.setRequestIp(ipAddress);
		tokenDataBuilder.setTokenId(sessionId);
		tokenDataBuilder.setTokenSessionId(sessionId);
		tokenDataBuilder.setUserId(userId + "");
	
		// generate token
		ITokenData<byte[]> tdata = tokenDataBuilder.build();
	
		// encrypt token with user key
		tokenDataBuilder = new TokenDataBuilder<byte[]>();
		//String userKey = curUser.getUserKey();
		tokenDataBuilder.setUserId(userKey);
		String userEncryptionKey = callBack.callBack(callBackParam);
		byte[] data = AuthenticationEncryptionUtils.getEncoderFactory(cl, userEncryptionKey)
							   .getEncoder()
							   .encode(TokenUtils.serialize(tdata));
		tokenDataBuilder.setAdditionalData(data);
		return tokenDataBuilder.build();
    }
    
    /***
     * Get Token authentication 
     * @param httpsesionParamMap
     * @param getTokenDataCallBack
     * @param getIpAddressCallBack
     * @param autenticationCallBack
     * @param checkifUserLoggedCallBack
     * @return
     */
    public static IAuthentication getTokenAuthentication(Map httpsesionParamMap,
			IAuthenticationCallBack<ITokenData, String> getTokenDataCallBack,
			IAuthenticationCallBack<String,Object> getIpAddressCallBack,
			IAuthenticationCallBack<Object, String> autenticationCallBack,
			IAuthenticationCallBack<Boolean, Object> checkifUserLoggedCallBack,
			IAuthenticationCallBack<Object, Object> userLoggedCallBack,
			Object userLogParam){
    	return new TokenAuthenticationFactory(httpsesionParamMap, getTokenDataCallBack, getIpAddressCallBack, autenticationCallBack, checkifUserLoggedCallBack
    			,userLoggedCallBack, userLogParam).getAuthentication();
    }
    
    
    /**
     * Check if token is valid!!!
     * @param callBack
     * @param tokenId
     * @return
     * @throws Exception
     */
    public static Boolean isTokenValid(IAuthenticationCallBack<Boolean, String> callBack, String tokenId) throws Exception{
    	return TokenAuthenticationFactory.isTokenValid(callBack, tokenId);
    }
    
    
}
