package net.is_bg.ltfn.authenticate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import token.ITokenData;
import token.TokenData.TokenDataBuilder;
import token.TokenUtils;
import authenticate.IAuthentication;
import authenticate.TokenCredentials;

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

    /***
     * Returns token credentials from the sessionRequest Map!!!
     * @param httpsesionParamMap
     * @return
     */
    public static TokenCredentials getTokenCredentials(Map httpsesionParamMap){
    	return TokenAuthenticationFactory.getTokenCredentials(httpsesionParamMap);
    }

    /***
     * Extracts user key from token user data!!!
     * @param sdata
     * @return
     */
    public static String getUserKeyFromTokenDataUserData(TokenDataUserData sdata){
    	return (sdata).getUserKey();
    }
  
    /***
     * Extracts connection name  from token user data!!!
     * @param sdata
     * @return
     */
    public static String getConnectionNameFromTokenDataUserData(TokenDataUserData sdata){
    	return (sdata).getDefDbCon();
    }
    
    /***
     * Extracts additional data from token user data!!! In this implementations this is byte array!!!
     * @param sdata
     * @return
     */
    public static Object getTokenDataAdditionalDataFromTokenDataUserData(TokenDataUserData sdata){
    	return (sdata).getTokenData().getAdditionalData();
    }
    
    /***
     * Creates the data structure that is send to client when client requests token info!!!
     * @param tokeData
     * @param userKey
     * @param defdbCon
     * @return
     */
    public static Object createTokenDataUserData(ITokenData tokeData, String userKey, String defdbCon){
    	return new TokenDataUserDataFromServer(tokeData, userKey, defdbCon);
    }
    
    /***
     * A private class used only to encapsulate token details that are send to client!!!
     * @author lubo
     *
     */
    private static class TokenDataUserDataFromServer {
    	
    	    private String userKey;
    		private String defDbCon;
    		private ITokenData tokenData;
    
			public String getUserKey() {
				return userKey;
			}
			public void setUserKey(String userKey) {
				this.userKey = userKey;
			}
			public ITokenData getTokenData() {
				return tokenData;
			}
			TokenDataUserDataFromServer() {
				// TODO Auto-generated constructor stub
		 	}
			public void setTokenData(ITokenData tokenData) {
				this.tokenData = tokenData;
			}
			public String getDefDbCon() {
				return defDbCon;
			}
			public void setDefDbCon(String defDbCon) {
				this.defDbCon = defDbCon;
			}
			
			TokenDataUserDataFromServer(ITokenData tokeData,String userKey, String defdbCon) {
				// TODO Auto-generated constructor stub
				this.tokenData = tokeData;
				this.userKey = userKey;
				this.defDbCon = defdbCon;
		 	}
    			
    }
    
    /***
     * Creates token encrypted token data  that contains the following
     *  </br>- ip address of the request that created this token
     *  </br>- the session id associated with this token
     *  </br>- token id
     *  </br>- user id
     * @param cl
     * @param ipAddress
     * @param sessionId
     * @param userId
     * @param userKey
     * @param callBack a callback & callback param that resolves encryption key
     * @param callBackParam a callback & callback param that resolves encryption key
     * @return
     * @throws Exception
     */
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
