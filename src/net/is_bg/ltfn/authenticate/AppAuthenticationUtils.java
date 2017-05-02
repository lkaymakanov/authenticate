package net.is_bg.ltfn.authenticate;

import java.io.IOException;
import java.util.Map;

import net.is_bg.ltfn.authenticate.AuthenticationUtils;
import net.is_bg.ltfn.authenticate.AuthenticationUtils.ITokenAuthneticationCallBacks;
import net.is_bg.ltfn.authenticate.InvalidTokenException;
import net.is_bg.ltfn.authenticate.NoTokenException;
import net.is_bg.ltfn.authenticate.TokenAuthenticationParams;
import token.ITokenData;
import token.TokenConstants;
import token.TokenConstants.AUTHENTICATION_TYPE;
import authenticate.AuthenticationException;
import authenticate.IAuthentication;

/**
 * Authentication Utils !!!
 * @author lubo
 *
 */
public class AppAuthenticationUtils {
	
	/***
	 * Gets token based authentication scheme!!!
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static IAuthentication<Boolean>  getTokenAuthenticator(Object httpServletRequest, Map requestParamMap, 
			ITokenAuthenticationConfiguration tokenAuthenticationConfiguration){
		if(tokenAuthenticationConfiguration.getTokenAuthenticationFactory() != null) return tokenAuthenticationConfiguration.getTokenAuthenticationFactory().getAuthentication();
		return 		AuthenticationUtils.getTokenAuthentication(requestParamMap, 
						httpServletRequest, tokenAuthenticationConfiguration);
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
		 * @throws AuthenticationException
		 */
		private  Boolean tokenAuthentication(Object httpServletRequest, Map requestparams) throws AuthenticationException{
			return AppAuthenticationUtils.getTokenAuthenticator(httpServletRequest, requestparams, 
					tokenAuthenticationConfiguration).authenticate();
		}
		
		/**
		 * Check if user is logged! If user is not logged & valid token is present in request
		 * user associated with this token is retrieved from token Authentication server
		 * & user is being logged!!
		 */
		UserNavigationPage checkiflogged() throws IOException{
			TokenAuthenticationParams tokenParams = tokenAuthenticationConfiguration.getTokenAuthenticationParams();
			ITokenAuthneticationCallBacks callBacks = tokenAuthenticationConfiguration.getTokenAuthenticationCallBackFactory().getITokenAuthneticationCallBacks();
			IRequestHelper reqhlp = tokenAuthenticationConfiguration.getRequestHelperFactory().getIRequestHelper(httpServletRequest);
			ISessionData sessionData = tokenAuthenticationConfiguration.getSessionDataFactory().getISessionData(httpServletRequest);
			Object u = sessionData.getUser();
			UserNavigationPage userNav = new UserNavigationPage();
			AuthenticationLogger l =  new AuthenticationLogger(tokenParams.isVerbose());
			
			userNav.user = u;
			if(u == null){
				l.log("No user is logged..");
				//user is not logged and page is not login page - navigate to login page!!!!
				if(!reqhlp.getRequestURL().contains(getLoginPage(AUTHENTICATION_TYPE.USERPASS))){
					userNav.navPage = getLoginPage(AUTHENTICATION_TYPE.USERPASS);
				}
			}else {
				boolean isTokenLogin = sessionData.getTokenId() != null;
				l.log("isToken Login:" + isTokenLogin);
				Map reqMap = reqhlp.getParameterMap();
				String  tokenId = AuthenticationUtils.getTokenFromRequestMap(reqMap);
				l.log("Token id from request is " + tokenId);
				if(tokenId != null && tokenId.equals(TokenConstants.IVALID_TOKEN_ID)) tokenId = null;
				boolean isTokenInRequest = (tokenId !=null);
				l.log("isTokenInRequest " + isTokenInRequest);
				
				if(isTokenLogin){
					if(!isTokenInRequest) {
						if(!callBacks.isTokenValidCallBack().callBack(sessionData.getTokenId())) throw new RuntimeException("The session is associated with token that is already invalid...");
					}//;//throw new RuntimeException("This session is associated with token & no token is found in request...");
					else if(!callBacks.isTokenValidCallBack().callBack(tokenId)) throw new RuntimeException("Invalid token in request...");
					else{
						ITokenData  tokenData = callBacks.decryptTokenDataCallBack().callBack(tokenId);
						l.log("token data is " + tokenData == null ? "null"  : tokenData.toString());
						
						//check if token is associated with different sessionId
						checkTokenSessionAssociation(l, tokenData.getTokenSessionId(), sessionData);
						
						//valid token in request 
						if(!tokenId.equals(sessionData.getTokenId())){
							//token in request is different from token in user session
							l.log("Token in request is " + tokenId + " Token in session is " + sessionData.getTokenId());
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
				if(reqhlp.getRequestURL().contains(getLoginPage(AUTHENTICATION_TYPE.USERPASS))){
					userNav.navPage = tokenAuthenticationConfiguration.getMainFormPage();
				}
				userNav.setLogged(true);
			}
			
			//try token login if token login is supported & user is not logged in
			boolean ta = tokenParams.isAllowTokenAuthentication();
			if(ta && !userNav.isLogged()  ){
				ITokenData  tokenData = callBacks.decryptTokenDataCallBack().callBack(AuthenticationUtils.getTokenFromRequestMap(reqhlp.getParameterMap()));
				l.log("token data is " + tokenData == null ? "null"  : tokenData.toString());
				checkTokenSessionAssociation(l,  tokenData.getTokenSessionId(), sessionData);
				try {
					if(tokenAuthentication(httpServletRequest, reqhlp.getParameterMap())){
						userNav.setLogged(true);
						userNav.navPage = null;
					}
				} catch (NoTokenException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//throw new RuntimeException(e);
				}catch (AuthenticationException e) {
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
		}
	}
	
	/**
	 * Check if token is already associated with other session in the current application that uses token 
	 * Authentication scheme!!!
	 * @param l
	 * @param tokenId
	 * @param sessionData
	 */
	private static void checkTokenSessionAssociation(AuthenticationLogger l, String tokenSessionId, ISessionData sessionData){
		if(tokenSessionId == null || tokenSessionId.equals(TokenConstants.IVALID_TOKEN_ID)) return;
		@SuppressWarnings("unused")
		String thisSessionId = sessionData.getSessionId(); l.log("Session Id that created token  is " + tokenSessionId + ", This session id is " + thisSessionId);
		String sessionAssociatedWithToken = AuthenticationUtils.getSessionAssociatedWithToken(tokenSessionId);  l.log("Session associated with this token is " + sessionAssociatedWithToken);
		if(sessionAssociatedWithToken != null && !thisSessionId.equals(sessionAssociatedWithToken))  throw new RuntimeException("Token is already associated with other session id...  Use Other token or log out & log in again..");
	}	

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
	
	
	
	public static class UserNavigationPage {
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
	}
	
	public static  interface ISessionData {
		public boolean isOverWriteUser();
		public void setOverWriteUser(boolean b);
		public String getTokenId();
		public Object getUser();
		public String getSessionId();
	}
	
	public static  interface IRequestHelper{
		public String getIpAddress();
		public String getTokenId();
		public String getSessionId();
		String getRequestURL();
		Map getParameterMap();
	}
	
	public static interface IRequestHelperFactory {
		public IRequestHelper getIRequestHelper(Object request);
		
	}
	
	public static interface ISessionDataFactory {
		public ISessionData getISessionData(Object httpServletRequest);
	}
	
	/***
	 * Interface that contains user login in  functions!!!
	 * @author lubo
	 *
	 */
	public static interface ILogUser {
		public  Object logUser(Object httpServletRequest,  String userKey, String tokenId, String dbIndex);
		public  Object logUser(Object httpServletRequest,  Object user, String tokenId, String defDbConn);
		
	}
	
	public static interface ILogUserFactory {
		public ILogUser getILogUser();
	}
	
	
}
