package net.is_bg.ltfn.authenticate;

import java.io.IOException;
import java.util.Map;

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
	private Map httpsesionParamMap;
	
	/**
	 * Get parameter by name from request
	 * @param paramName
	 * @return
	 */
	private  String  getRequestParam(Map httpsesionParamMap, String paramName){
		String [] p = ((Map<String, String[]>)(httpsesionParamMap)).get(paramName);
		if(p==null || p.length == 0) return null;  
		return p[0];
	}
	
	/***
	 * Get token from request!!!
	 * @return
	 */
	private TokenCredentials getTokenCredentials(){
		String tokenId = getRequestParam(httpsesionParamMap, TokenConstants.TOKEN_ID_PARAM_NAME) == null ? TokenConstants.IVALID_TOKEN_ID : getRequestParam(httpsesionParamMap,
				TokenConstants.TOKEN_ID_PARAM_NAME);
		System.out.println("TOKEN_ID:" +  tokenId);
		//System.out.println("USER_ID: " +  getRequestParam(httpsesionParamMap, TokenConstants.USER_ID_PARAM_NAME));
		return new TokenCredentials(new Token(tokenId));
	}
	
	public TokenAuthenticationFactory(Map httpsesionParamMap){
		this.httpsesionParamMap = httpsesionParamMap;
		tokenAuthentication = new TokenAuthenticationInner(getTokenCredentials());
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
	
	
	/**
	 * The structure of data that is coming from Authentication server!!!
	 * @author lubo
	 *
	 */
	private static class ServerData{
		private TokenDataWrapper tokenData;
		private String userKey;
		private String defDbCon;
		public ServerData(TokenDataWrapper tokenData, String userKey, String defDbCon) {
			super();
			this.tokenData = tokenData;
			this.userKey = userKey;
			this.defDbCon = defDbCon;
		}
		public ServerData() {
			// TODO Auto-generated constructor stub
		}
		public TokenDataWrapper getTokenData() {
			return tokenData;
		}
		public void setTokenData(TokenDataWrapper tokenData) {
			this.tokenData = tokenData;
		}
		public String getUserKey() {
			return userKey;
		}
		public void setUserKey(String userKey) {
			this.userKey = userKey;
		}
		public String getDefDbCon() {
			return defDbCon;
		}
		public void setDefDbCon(String defDbCon) {
			this.defDbCon = defDbCon;
		}
		
	}
	
	private static class TokenDataWrapper<T> implements ITokenData<T>{
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
	private static ServerData  getTokenDataFromAuthenticationServer(IAuthenticationCallBack<Object, String> autenticationCallBack, String tokenId) throws Exception{
		/*	String cfName = dSettings.toClientConfigurationName();
			configureClientConfigurator(dSettings);
			String d = Requester.request(cfName).path(MAIN_PATH).queryParam(TOKEN_PARAM, tokenId).get(MEDIA_TYPE.JSON).getResponseAsObject(String.class);
			ObjectMapper objectMapper = new ObjectMapper(); 
			ServerData w = objectMapper.readValue(d, ServerData.class);
		return w;*/
		return  (ServerData)autenticationCallBack.callBack(tokenId);
	}
	
	
	static boolean isTokenValid(IAuthenticationCallBack<Boolean, String> autenticationCallBack, String tokenId) throws Exception{
		/*String cfName = dSettings.getServerSettings().toClientConfigurationName();
		configureClientConfigurator(dSettings);
		String d = Requester.request(cfName).path(MAIN_PATH).queryParam(TOKEN_PARAM, tokenId).get(MEDIA_TYPE.JSON).getResponseAsObject(String.class);
		return false;*/
		return autenticationCallBack.callBack(tokenId);
	}
	
	/***
	 * Retrieves token data from the authentication server!!!
	 * @param tokenId
	 * @return
	 * @throws Exception 
	 */
	private static ServerData getTokenDataFromTokenAuthenticationServer(IAuthenticationCallBack<Object, String> autenticationCallBack, String tokenId) throws Exception{
	/*	DownloadSettings dSettings = new DownloadSettings();
		dSettings.getServerSettings().fillFromString((String)CONTEXTPARAMS.TOKEN_AUTHENTICATION_SERVER.getValue());
		dSettings.getServerSettings().setContext((String)CONTEXTPARAMS.TOKEN_AUTHENTICATION_SERVER_CONTEXT.getValue());*/
		return getTokenDataFromAuthenticationServer(autenticationCallBack, tokenId);
	}
	
	
	static class TokenAuthenticationInner extends TokenAuthentication<Boolean>{
		IAuthenticationCallBack<ITokenData, String> getTokenDataCallBack;
		IAuthenticationCallBack<String,Object> getIpAddressCallBack;
		IAuthenticationCallBack<Object, String> autenticationCallBack;
		IAuthenticationCallBack<Boolean, Object> checkifUserLoggedCallBack;
		
		public TokenAuthenticationInner(TokenCredentials tokenCredentials) {
			super(tokenCredentials);
		}
		
		/*private User getUserByUserKey(String userKey){
			return AppUtil.getServiceLocator().getUserDao().getUserByUserKey(userKey, ConnectionLoader.getConnectionLoader().getMapConnection().get(0).defDbCon);
		}*/
		
		

		@Override
		public Boolean authenticate() throws AuthenticationException {
			// TODO Auto-generated method stub
			//authenticate with token here 
			//SessionDataBean sb = (SessionDataBean)httpServletRequest.getSession(true).getAttribute(AppConstants.SESSION_DATA_BEAN);
			
			//get the ip that made the authentication request
			String ipAddress = getIpAddressCallBack.callBack(null);   // AppUtil.getIpAdddress(httpServletRequest);
			
			//get token id from request
			String tokenId = getTokentCredentials().getToken().getTokenId();
			
			
			//check if user is logged
			if(checkifUserLoggedCallBack.callBack(null)){
				return true;
			}
			/*if(sb !=null && sb.getVisit()!=null && sb.getVisit().getCurUser() !=null ){
				//user is already logged
				
				
				return true;   
			}*/
			
			//no visit
			
			
			//no token in request
			if(tokenId == null){
				throw new NoTokenException();
			}
			
			//invalid token
			if( tokenId.equals(TokenConstants.IVALID_TOKEN_ID)){
				System.out.println(tokenId);
				throw new InvalidTokenException(TokenConstants.IVALID_TOKEN_ID);
			}
			
			//retrieve tokenData from authentication server
			ServerData tdata = null;
			ITokenData tokenData = null;
			try {
				
				//get token data from server
				tdata = getTokenDataFromTokenAuthenticationServer(autenticationCallBack, tokenId);
				
				//decrypt & analyze token data from server
				tokenData = getTokenDataCallBack.callBack((String)tdata.tokenData.getAdditionalData());
				
				/*//convert to bytes 
				//ObjectMapper om = new ObjectMapper();
				byte [] b = om.convertValue((String)tdata.tokenData.getAdditionalData(), byte[].class);
				AuthenticationEncryptionUtils.getDecoderFactory(
						ApplicationGlobals.getApplicationGlobals().getLocator().getUserDao().getEncryptionKey(tdata.userKey, tdata.defDbCon)).getDecoder().decode(b);
				tokenData =	TokenUtils.deserialize(b, b.length, ITokenData.class);*/
				
				System.out.println(tokenData);
				//tdata.setTokenData(tokenData);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			
			//no token data from server
			if(tdata == null) {
				throw new RuntimeException("Token data is null...");
			}
			
			//ip do not match!!!
			if(!tokenData.getRequestIp().equals(ipAddress)){
				System.out.println("Token Ip = " + tokenData.getRequestIp() + " request Ip =  " +ipAddress);
				throw new RuntimeException("Token Ip = " + tokenData.getRequestIp() + " request Ip =  " +ipAddress);
			}
			
			//log user
			//AuthenticationUtils.logUser(httpServletRequest, getUserByUserKey(tdata.userKey), tokenId, 0);
			
			
			return true;
		}
	}
	
	/***
	 * Configures the targeted end point by the download settings!!!
	 * @param sSettings
	 * @throws UnrecoverableKeyException
	 * @throws KeyManagementException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws CertificateException 
	 */
	/*private static void configureClientConfigurator(ServerSettings settings) throws UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException{
		Ssl ssl =  ClientConfigurator.configure(settings.toClientConfigurationName()).targetEndpoint(settings.toEndPoint()).readTimeout(settings.getReadTimeOut());
		if(settings.isSecure()){
			ssl.protocol(dSettings.getSocketProtocol()).
			keystore(dSettings.getStoreType(), dSettings.getKeystoreFile(), dSettings.getKeystorePass()).
			privateKey(dSettings.getKeyAlias(), dSettings.getKeyPass()).trustAllCerts().complete();
		}else{
			ssl.noSSL().complete();
		}
	}*/
	
	/*	
	*//**The main path pointing to update center jersey servlet*//*
	private final static IREST_PATH MAIN_PATH = new  IREST_PATH() {
		@Override
		public String getPath() {
			// TODO Auto-generated method stub
			return  ((String)CONTEXTPARAMS.TOKEN_AUTHENTICATION_PREFIX.getValue()) + TokenConstants.TOKEN_DATA_PATH;
		}
	}; 
	
	private final static IPARAM TOKEN_PARAM  = new IPARAM() {
		@Override
		public String getStringValue() {
			// TODO Auto-generated method stub
			return TokenConstants.TOKEN_ID_PARAM_NAME;
		}
	};*/
	

	
	public static void main(String [] args) throws Exception{
		//System.out.println(getTokenDataFromTokenAuthenticationServer("45C5C2E62E6D874D7AD93EABCDD2B4A0"));
	}
	

	
}
