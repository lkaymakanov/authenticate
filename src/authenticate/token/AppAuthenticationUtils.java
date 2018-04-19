package authenticate.token;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import authenticate.token.TokenConstants.AUTHENTICATION_TYPE;



/**
 * Authentication Utils !!!
 * @author lubo
 *
 */
public class AppAuthenticationUtils {
	
	
	/**
	 * Try to get token from Request Map if no token returns null!!!
	 * @param httpsesionParamMap
	 * @return
	 */
	public static String getTokenFromRequestMap(Map httpsesionParamMap){
		return getRequestParam(httpsesionParamMap, TokenConstants.TOKEN_ID_PARAM_NAME);
	}
	
	/***
	 * Check if request contains token in param map!!!
	 * @param httpsesionParamMap
	 * @return
	 */
	public static boolean isTokenInRequest(Map httpsesionParamMap){
		return getTokenFromRequestMap(httpsesionParamMap) != null;
	}
	
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
	 * Gets token based authentication scheme!!!
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static IAuthentication<Boolean>  getTokenAuthenticator(Object httpServletRequest, Map requestParamMap, 
			ITokenAuthenticationConfiguration tokenAuthenticationConfiguration){
		//if(tokenAuthenticationConfiguration.getTokenAuthenticationFactory() != null)
		return tokenAuthenticationConfiguration.getTokenAuthenticationFactory().getAuthentication();
		
		//return 		AuthenticationUtils.getTokenAuthentication(requestParamMap, 
			//			httpServletRequest, tokenAuthenticationConfiguration);
	}
	
	
	
	/***
	 * Check if user is logged!!!
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @throws IOException
	 */
	public static UserNavigationPage checkifLogged(Object httpServletRequest, ITokenAuthenticationConfiguration tokenAuthenticationConfiguration) throws IOException{
		return new HpptServletRequestAuthentication(httpServletRequest, tokenAuthenticationConfiguration).checkiflogged();
	}
	
	
	
	
	/***
	 * Use request data & session data associated with this request to be analyzed if user is logged & to log user
	 * if valid token is present in the request!!!!
	 * @author lubo
	 *
	 */
	private static class HpptServletRequestAuthentication {
		
		
		private Object httpServletRequest;
		private ITokenAuthenticationConfiguration tokenAuthenticationConfiguration;
		
		
		/**Package private constructor!!!*/
		HpptServletRequestAuthentication(Object httpServletRequest,
				ITokenAuthenticationConfiguration tokenAuthenticationConfiguration){
			this.httpServletRequest = httpServletRequest;
			this.tokenAuthenticationConfiguration = tokenAuthenticationConfiguration;
			
		}

		/**
		 * Returns login page base on authentication scheme
		 * @param t
		 * @return
		 */
		private  String getLoginPage(AUTHENTICATION_TYPE t){
			if(t == AUTHENTICATION_TYPE.USERPASS) return tokenAuthenticationConfiguration.getLoginPage();
			return null;
		}
		
		
		/***
		 * Token based Authentication mehtod!!!
		 * @param httpServletRequest
		 * @return
		 * @throws TokenAuthenticationException
		 */
		private  Boolean tokenAuthentication(Object httpServletRequest, Map requestparams) throws TokenAuthenticationException{
			return AppAuthenticationUtils.getTokenAuthenticator(httpServletRequest, requestparams, 
					tokenAuthenticationConfiguration).authenticate();
		}
		
		/**
		 * Check if  login pages is requested!
		 * @param url
		 * @return
		 */
		private boolean isLoginPage(String url){
			List<String> lpages =  tokenAuthenticationConfiguration.getLoginPages();
			for(String lPage : lpages){
				if(url.contains(lPage)) return true;
			}
			return false;
		}
		
		
		
		UserNavigationPage checkiflogged() throws IOException{
			TokenAuthenticationParams tokenParams = tokenAuthenticationConfiguration.getTokenAuthenticationParams();
			ITokenAuthneticationCallBacks callBacks = tokenAuthenticationConfiguration.getTokenAuthenticationCallBackFactory().getITokenAuthneticationCallBacks();

			IRequestHelper reqhlp = tokenAuthenticationConfiguration.getRequestHelperFactory().getIRequestHelper(httpServletRequest);
			ISessionData sessionData = tokenAuthenticationConfiguration.getSessionDataFactory().getISessionData(httpServletRequest);
			Object u = sessionData.getUser();
			
			//user & navigation page! After this mehtod completes if  navigationPage is not null response to this page is set
			UserNavigationPage userNav = new UserNavigationPage();
			userNav.user = u;
			userNav.logged = false;
			AuthenticationLogger l =  new AuthenticationLogger(tokenParams.isVerbose());
			
			//main form & login page
			String loginPage = tokenAuthenticationConfiguration.getLoginPage();
			String mainFormPage  = tokenAuthenticationConfiguration.getMainFormPage();
			
			//get request parameters mao
			Map reqMap = reqhlp.getParameterMap();
			
			//get session that created token
			String sessionIdThatCreatedToken =  sessionData.getSessionIdThatCreatedToken();
			boolean isTokenLogin = (sessionIdThatCreatedToken!=null);
			
			//request URL (request page)
			String url = reqhlp.getRequestURL();
			boolean isLoginPage = isLoginPage(url);
			//boolean isMainFormPage = url.contains(mainFormPage) || mainFormPage.contains(url);
			boolean isUserLogged = (u!=null);
			boolean tokenAuthenticationAllowed = tokenParams.isAllowTokenAuthentication();
			boolean isTokenInRequest = false;
			
			
			//get session from request
			String  tokenInRequest = getTokenFromRequestMap(reqMap);
			isTokenInRequest  = ((tokenInRequest!=null) && tokenAuthenticationAllowed);
			
			if(isUserLogged) {//user is logged
				if(isTokenLogin) {
					//user is logged with token
					if(isTokenInRequest) { //there is  token in request &  logged user
						handleTokenInRequest(tokenInRequest, userNav);
					}else {//there is logged user & no token in request
						//check if session that created token is still valid! if session is invalid exception is thrown
						if(!callBacks.isTokenValidCallBack().callBack(sessionData.getSessionIdThatCreatedToken())) throw new RuntimeException("The session is associated with token that is already invalid...");
						
						if(isLoginPage) {
							userNav.navPage = mainFormPage;  //navigate to main form  page if page is loginPage
						}
					}
				}else {//user is logged without token
					if(isTokenInRequest) {
						handleTokenInRequest(tokenInRequest, userNav);
					}
					
					if(isLoginPage) {
						userNav.navPage = mainFormPage;  //navigate to main form  page if page is loginPage
					}
				}
				
			}else {//no user logged 
				if(isTokenInRequest) {  //no user logged & there is token in request
					handleTokenInRequest(tokenInRequest, userNav);
				}else {//there is no user logged & no token in request
					if(!isLoginPage) {
						userNav.navPage = loginPage;  //navigate to login page if page is no to login page
					}
				}
			}
			return userNav;
		}
		
		
		/***
		 * Compare token user id & user id in application session if equal
		 * @return
		 */
		private static boolean compareTokenUserIdCurrentUserId(String tokenUserId, String currentUserId){
			if(tokenUserId != null) return tokenUserId.equals(currentUserId);
			return false;
		}
		
		
		
		/***
		 * Processes a token coming from incoming request!!!
		 * @param tokenInRequest
		 * @param userNav
		 */
		private void handleTokenInRequest(String tokenInRequest,  UserNavigationPage userNav) {
			TokenAuthenticationParams tokenParams = tokenAuthenticationConfiguration.getTokenAuthenticationParams();
			ISessionData sessionData = tokenAuthenticationConfiguration.getSessionDataFactory().getISessionData(httpServletRequest);
			IRequestHelper reqhlp = tokenAuthenticationConfiguration.getRequestHelperFactory().getIRequestHelper(httpServletRequest);
			ITokenAuthneticationCallBacks callBacks = tokenAuthenticationConfiguration.getTokenAuthenticationCallBackFactory().getITokenAuthneticationCallBacks();
			
			//boolean isTokenInRequest  = ((tokenInRequest!=null) && tokenParams.isAllowTokenAuthentication());
			boolean isUserLogged = (userNav.user!=null);
			
			//try to decrypt token from request
			TokenData tokenData = callBacks.decryptTokenDataCallBack().callBack(tokenInRequest);
			
			//get user id from token
			String tokenUserId = (tokenData == null) ? null : tokenData.getUserId();
			
			//get session that created token
			String sIdThatCreatedToken = tokenData.getSessionIdThatCreatedThisToken();
			
			
			if(isUserLogged) {  //user is already logged
				//if session id in token is different from sessionIdthatcreated token in current session
				//or user Id from token is different from user id in current session over write current user with user from token
				if(!sIdThatCreatedToken.equals(sessionData.getSessionIdThatCreatedToken()) || !compareTokenUserIdCurrentUserId(tokenUserId, sessionData.getUserId())) {
					sessionData.setOverWriteUser(tokenParams.isOverWriteUser());
					try{
						tokenAuthentication(httpServletRequest, reqhlp.getParameterMap());
						userNav.setLogged(true);
						userNav.navPage = null;   //userNav.user = //
					}catch(Exception e){
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				}
			}else {//no user is logged 
				//try to authenticate with token from request
				try {
					tokenAuthentication(httpServletRequest, reqhlp.getParameterMap());
				}catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
		
		/**
		 * Check if user is logged! If user is not logged & valid token is present in request
		 * user associated with this token is retrieved from token Authentication server
		 * & user is being logged!!
		 */
		
	/*	private UserNavigationPage checkifloggedOld() throws IOException{
			
			
			TokenAuthenticationParams tokenParams = tokenAuthenticationConfiguration.getTokenAuthenticationParams();
			ITokenAuthneticationCallBacks callBacks = tokenAuthenticationConfiguration.getTokenAuthenticationCallBackFactory().getITokenAuthneticationCallBacks();
			IRequestHelper reqhlp = tokenAuthenticationConfiguration.getRequestHelperFactory().getIRequestHelper(httpServletRequest);
			ISessionData sessionData = tokenAuthenticationConfiguration.getSessionDataFactory().getISessionData(httpServletRequest);
			Object u = sessionData.getUser();
			UserNavigationPage userNav = new UserNavigationPage();
			AuthenticationLogger l =  new AuthenticationLogger(tokenParams.isVerbose() true);
			
			boolean isTokenLogin = sessionData.getSessionIdThatCreatedToken() != null;
			
			
			String loginPage = tokenAuthenticationConfiguration.getLoginPage();
			String mainFormPage  = tokenAuthenticationConfiguration.getMainFormPage();
			userNav.user = u;
			userNav.navPage = null;
			l.log("========== REQUEST URL IS ====" + reqhlp.getRequestURL());
			if(u == null){
				//l.log("No user is logged..");
				//user is not logged and page is not login page - navigate to login page!!!!
				if(!isLoginPage(reqhlp.getRequestURL())){
					userNav.navPage = loginPage;//getLoginPage(AUTHENTICATION_TYPE.USERPASS);
				}
			}else {
				//boolean isTokenLogin = sessionData.getTokenId() != null;
				Map reqMap = reqhlp.getParameterMap();
				String  tokenId = TokenAuthenticationUtils.getTokenFromRequestMap(reqMap);
				//if(tokenId)l.log("Token id from request is " + tokenId);
				if(tokenId != null && tokenId.equals(TokenConstants.IVALID_TOKEN_ID)) tokenId = null;
				boolean isTokenInRequest = (tokenId !=null);
				//l.log("isTokenInRequest " + isTokenInRequest);
				
				if(isTokenLogin){
					if(!isTokenInRequest) {
						if(!callBacks.isTokenValidCallBack().callBack(sessionData.getSessionIdThatCreatedToken())) throw new RuntimeException("The session is associated with token that is already invalid...");
					}//;//throw new RuntimeException("This session is associated with token & no token is found in request...");
					//else if(!callBacks.isTokenValidCallBack().callBack(tokenId)) throw new RuntimeException("Invalid token in request...");
					else{
						TokenData  tokenData = callBacks.decryptTokenDataCallBack().callBack(tokenId);
						l.log("token data is " + tokenData == null ? "null"  : tokenData.toString());
						
						//get user id from token
						String tokenUserId = (tokenData == null) ? null : tokenData.getUserId();
						
						//check if token is associated with different sessionId
						checkTokenSessionAssociation(l, tokenData.getSessionIdThatCreatedThisToken(), sessionData);
						
						//valid token in request 
						if(!tokenId.equals(sessionData.getSessionIdThatCreatedToken()) || !compareTokenUserIdCurrentUserId(tokenUserId, sessionData.getUserId())){
							//token in request is different from token in user session
							l.log("Token in request is " + tokenId + " Token in session is " + sessionData.getSessionIdThatCreatedToken());
							l.log("Overwrite user is " + tokenParams.isOverWriteUser());
							sessionData.setOverWriteUser(tokenParams.isOverWriteUser());
							try{
								tokenAuthentication(httpServletRequest, reqhlp.getParameterMap());
							}catch(Exception e){
								e.printStackTrace();
								throw new RuntimeException(e);
							}
						}
					}
				}
				
				//no token login 
				//there is user logged & we opened login -- goto main form
				if(isLoginPage(reqhlp.getRequestURL())){
					l.log("Navigating to " + mainFormPage);
					userNav.navPage = mainFormPage;
				}
				userNav.setLogged(true);
			}
			
			//try token login if token login is supported & user is not logged in
			boolean ta = tokenParams.isAllowTokenAuthentication();
			if(ta && !userNav.isLogged()  ){
				TokenData  tokenData = callBacks.decryptTokenDataCallBack().callBack(AuthenticationUtils.getTokenFromRequestMap(reqhlp.getParameterMap()));
				l.log("token data is " + tokenData == null ? "null"  : tokenData.toString());
				checkTokenSessionAssociation(l,  tokenData.getSessionIdThatCreatedThisToken(), sessionData);
				try {
					if(tokenAuthentication(httpServletRequest, reqhlp.getParameterMap())){
						userNav.setLogged(true);
						userNav.navPage = null;
					}
				} catch (NoTokenException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//throw new RuntimeException(e);
				}catch (TokenAuthenticationException e) {
					// TODO: handle exception
					e.printStackTrace();
					//throw new RuntimeException(e);
				}
				catch (InvalidTokenException e) {
					// TODO: handle exception
					e.printStackTrace();
					//throw new RuntimeException(e);
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					//throw new RuntimeException(e);
				}
			}
			
			return userNav;
		}*/
	
	
	/**
	 * Check if token is already associated with other session in the current application that uses token 
	 * Authentication scheme!!!
	 * @param l
	 * @param tokenId
	 * @param sessionData
	 */
	/*private static void checkTokenSessionAssociation(AuthenticationLogger l, String tokenSessionId, ISessionData sessionData){
		if(tokenSessionId == null || tokenSessionId.equals(TokenConstants.IVALID_TOKEN_ID)) return;
		@SuppressWarnings("unused")
		String thisSessionId = sessionData.getSessionId(); l.log("Session Id that created token  is " + tokenSessionId + ", This session id is " + thisSessionId);
		String sessionAssociatedWithToken = TokenAuthenticationUtils.getSessionAssociatedWithToken(tokenSessionId);  l.log("Session associated with this token is " + sessionAssociatedWithToken);
		if(sessionAssociatedWithToken != null && !thisSessionId.equals(sessionAssociatedWithToken))  throw new RuntimeException("Token is already associated with other session id...  Use Other token or log out & log in again..");
	}	*/

	/***
	 * <pre>
	 * Prints more detailed log about logging activity
	 * if verbose argument is set to true!!!
	 * </pre>
	 * @author lubo
	 *
	 */
	private static class AuthenticationLogger{
		private boolean verbose;
		
		AuthenticationLogger(boolean verbose){
			this.verbose = verbose;
		}
		
		void log(String l){
			if(verbose) System.out.println(l);
		}
	}
	
	
	
	/*public static class UserNavigationPage {
		String navPage;
		Object user;
		boolean logged = false;
		public String getNavPage() {
			return navPage;
		}
		public Object getUser() {
			return user;
		}
		public boolean isLogged() {
			return logged;
		}
		public void setLogged(boolean looged) {
			this.logged = looged;
		}
	}*/
	
}
