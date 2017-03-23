package net.is_bg.ltfn.authenticate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.is_bg.ltfn.authenticate.AuthenticationUtils.ITokenAuthneticationCallBacksFactory;
import token.IToken;
import token.ITokenData;
import token.TokenConstants;
import authenticate.AuthenticationException;
import authenticate.IAuthentication;
import authenticate.IAuthenticationFactory;
import authenticate.TokenAuthentication;
import authenticate.TokenCredentials;

/***
 * Token Authentication Factory!!!
 * @author lubo
 *
 */
class TokenAuthenticationFactory implements IAuthenticationFactory<Boolean> {

	private IAuthentication<Boolean> tokenAuthentication;
	
	/**
	 * Get parameter by name from request
	 * @param paramName
	 * @return
	 */
	static String  getRequestParam(Map httpsesionParamMap, String paramName){
		String [] p = ((Map<String, String[]>)(httpsesionParamMap)).get(paramName);
		if(p==null || p.length == 0) return null;  
		return p[0];
	}
	
	/***
	 * Get token from request!!!
	 * @return
	 */
	static TokenCredentials getTokenCredentials(Map httpsesionParamMap){
		String tokenId = getRequestParam(httpsesionParamMap, TokenConstants.TOKEN_ID_PARAM_NAME) == null ? TokenConstants.IVALID_TOKEN_ID : 
			getRequestParam(httpsesionParamMap, TokenConstants.TOKEN_ID_PARAM_NAME);
		return new TokenCredentials(new Token(tokenId));
	}
	
	TokenAuthenticationFactory(
			Map httpsessionParamMap,
			Object httpServletRequest,
			ITokenAuthneticationCallBacksFactory tokenAuthneticationCallBackFactory,
			TokenAuthenticationParams tokenParams){
		tokenAuthentication = new TokenAuthenticationInner(httpServletRequest,
				getTokenCredentials(httpsessionParamMap), 
				tokenAuthneticationCallBackFactory, tokenParams);
	}
	
	
	
	@Override
	public IAuthentication<Boolean> getAuthentication() {
		// TODO Auto-generated method stub
		return tokenAuthentication;
	}
	
	static class Token implements IToken{
		/**
		 * 
		 */
		private static final long serialVersionUID = 2506633470549538750L;
		String tokeId;
		Token(String tokeId){
			this.tokeId = tokeId;
		}
		@Override
		public byte[] serialize() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getTokenId() {
			// TODO Auto-generated method stub
			return tokeId;
		}
		
	}
	
	
	static class ServerData {
		private TokenDataWrapper tokenData;
		private String userKey;
		private String defDbCon;
		ServerData(TokenDataWrapper tokenData, String userKey, String defDbCon) {
			super();
			this.tokenData = tokenData;
			this.userKey = userKey;
			this.defDbCon = defDbCon;
		}
		ServerData() {
			// TODO Auto-generated constructor stub
		}
		TokenDataWrapper getTokenData() {
			return tokenData;
		}
		void setTokenData(TokenDataWrapper tokenData) {
			this.tokenData = tokenData;
		}
		String getUserKey() {
			return userKey;
		}
		void setUserKey(String userKey) {
			this.userKey = userKey;
		}
		String getDefDbCon() {
			return defDbCon;
		}
		void setDefDbCon(String defDbCon) {
			this.defDbCon = defDbCon;
		}
	}
	
	static class TokenDataWrapper<T> implements ITokenData<T>{
		private T additionalData;
		private String requestIp;
		private String tokenId;
		private String tokenSessionId;
		private String userId;
		
		public TokenDataWrapper(){
			
		}
		
		public void setAdditionalData(T additionalData) {
			this.additionalData = additionalData;
		}

		public void setRequestIp(String requestIp) {
			this.requestIp = requestIp;
		}

		public void setTokenId(String tokenId) {
			this.tokenId = tokenId;
		}

		public void setTokenSessionId(String tokenSessionId) {
			this.tokenSessionId = tokenSessionId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public TokenDataWrapper(T additionalData, String requestIp,
				String tokenId, String tokenSessionId, String userId) {
			super();
			this.additionalData = additionalData;
			this.requestIp = requestIp;
			this.tokenId = tokenId;
			this.tokenSessionId = tokenSessionId;
			this.userId = userId;
		}
	
		@Override
		public T getAdditionalData() {
			// TODO Auto-generated method stub
			return additionalData;
		}

		

		@Override
		public String getRequestIp() {
			// TODO Auto-generated method stub
			return requestIp;
		}

		@Override
		public String getTokenId() {
			// TODO Auto-generated method stub
			return tokenId;
		}

		@Override
		public String getTokenSessionId() {
			// TODO Auto-generated method stub
			return tokenSessionId;
		}

		@Override
		public String getUserId() {
			// TODO Auto-generated method stub
			return userId;
		}
	}
	

	
	/**
	 * 
	 * @param dSettings
	 * @return
	 * @throws Exception
	 */
	private static TokenDataUserData  getTokenDataFromAuthenticationServer(IAuthenticationCallBack<Object, String> autenticationCallBack, String tokenId) throws Exception{
		return (TokenDataUserData)autenticationCallBack.callBack(tokenId);
	}
	
	
	/**
	 * Check if token is valid!!!
	 * @param autenticationCallBack
	 * @param tokenId
	 * @return
	 * @throws Exception
	 */
	static boolean isTokenValid(IAuthenticationCallBack<Boolean, String> autenticationCallBack, String tokenId) throws Exception{
		return autenticationCallBack.callBack(tokenId);
	}
	
	
	
	static class TokenAuthenticationInner extends TokenAuthentication<Boolean>{
	    private ITokenAuthneticationCallBacksFactory tokenAuthenticationCallBackFactory;
		private TokenAuthenticationParams tokenParams;
		private Object httpservletRequest;
		
		public TokenAuthenticationInner(Object httpservletRequest, TokenCredentials tokenCredentials, 
				ITokenAuthneticationCallBacksFactory tokenAuthenticationCallBackFactory,
				TokenAuthenticationParams tokenParams) {
			super(tokenCredentials);
			this.httpservletRequest = httpservletRequest;
			this.tokenAuthenticationCallBackFactory = tokenAuthenticationCallBackFactory;
			this.tokenParams = tokenParams;
		}
		

		@Override
		public Boolean authenticate() throws AuthenticationException {
			// TODO Auto-generated method stub
			//authenticate with token here 

			//get token id from request
			String tokenId = getTokentCredentials().getToken().getTokenId();
			
			//check if token is valid
			/*if(!isTokenValidCallBack(tokenId)){
				throw new RuntimeException("Token data is null...");
			}*/
			
			//check if user is logged
			if(tokenAuthenticationCallBackFactory.getITokenAuthneticationCallBacks().checkifUserLoggedCallBack().callBack(httpservletRequest)){
				return true;
			}
			
			
			//no visit
			
			//no token in request
			if(tokenId == null){
				throw new NoTokenException();
			}
			
			//invalid token
			if( tokenId.equals(TokenConstants.IVALID_TOKEN_ID)){
				throw new InvalidTokenException(TokenConstants.IVALID_TOKEN_ID);
			}
			
			//retrieve tokenData from authentication server
			TokenDataUserData tdata = null;
			ITokenData tokenData = null;
			try {
				
				//get token data from server
				tdata = getTokenDataFromAuthenticationServer(tokenAuthenticationCallBackFactory.getITokenAuthneticationCallBacks().autenticationCallBack(), tokenId);
				
				//decrypt & analyze token data from server
				tokenData = tokenAuthenticationCallBackFactory.getITokenAuthneticationCallBacks().getTokenDataCallBack().callBack(tdata);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			
			//no token data from server
			if(tdata == null) {
				throw new RuntimeException("Token data is null...");
			}
			
			//get the ip that made the authentication request
			String ipAddress = tokenAuthenticationCallBackFactory.getITokenAuthneticationCallBacks().getIpAddressCallBack().callBack(httpservletRequest);  
			
			//ip do not match!!!
			if(!tokenParams.isSupressIpCheck() && !tokenData.getRequestIp().equals(ipAddress)){
				//System.out.println("Token Ip = " + tokenData.getRequestIp() + " request Ip =  " +ipAddress);
				throw new RuntimeException("Token Ip = " + tokenData.getRequestIp() + " request Ip =  " +ipAddress);
			}
			
			tokenParams.getUserLogCallBackParam().add(httpservletRequest);
			tokenParams.getUserLogCallBackParam().add(tokenId);
			tokenParams.getUserLogCallBackParam().add(tdata.getUserKey());
			tokenParams.getUserLogCallBackParam().add(tdata.getDefDbCon());
			
			//log user
			tokenAuthenticationCallBackFactory.getITokenAuthneticationCallBacks().userLogCallBack().callBack(tokenParams.getUserLogCallBackParam());
			
			return true;
		}
	}
	
	

	
}
