package net.is_bg.ltfn.authenticate;

import java.util.HashMap;
import java.util.List;
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
	

	private static Map<String, String> tokenIdSessionIdMap = new HashMap<String, String>();
	
	/***
	 * Associates token id & session Id!!!
	 * @param tokenId
	 * @param sessionId
	 */
	public static void addTokenIdSessionId(String tokenId, String sessionId){
		synchronized(tokenIdSessionIdMap){
			tokenIdSessionIdMap.put(tokenId, sessionId);
		}
	}
	
	/***
	 * Returns the session associated with this token if any!!!
	 * @param tokenId
	 * @return
	 */
	public static String getSessionAssociatedWithToken(String tokenId){
		synchronized(tokenIdSessionIdMap){
			return tokenIdSessionIdMap.get(tokenId);
		}
	}
	
	/***
	 * Removes token from tokenIdSessionIdMap
	 * @param tokenId
	 */
	public static void invalidateTokenId(String tokenId){
		if(tokenId  == null) return ;
		synchronized(tokenIdSessionIdMap){
			String sessionId = tokenIdSessionIdMap.get(tokenId);
			tokenIdSessionIdMap.remove(sessionId);
		}
	}

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


    
    public static String getUserKeyFromServerData(Object sdata){
    	return ((ServerDataEx)sdata).getUserKey();
    }
  
    public static String getConnectionNameFromServerData(Object sdata){
    	return ((ServerDataEx)sdata).getDefDbCon();
    }
    
    public static Object getTokenDataAdditionalData(Object sdata){
    	return ((ServerDataEx)sdata).getTokenData().getAdditionalData();
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
    		Object httpServletRequest,
			IAuthenticationCallBack<ITokenData, Object> getTokenDataCallBack,
			IAuthenticationCallBack<String, Object> getIpAddressCallBack,
			IAuthenticationCallBack<Object, String> autenticationCallBack,
			IAuthenticationCallBack<Boolean, Object> checkifUserLoggedCallBack,
			IAuthenticationCallBack<Object, List<Object>> userLoggedCallBack,
			List<Object> userLogParam){
    	return new TokenAuthenticationFactory(httpsesionParamMap, httpServletRequest,
    			getTokenDataCallBack, getIpAddressCallBack, autenticationCallBack, checkifUserLoggedCallBack
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
