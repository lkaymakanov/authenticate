package net.is_bg.ltfn.authenticate;

import java.io.IOException;
import java.util.Map;

import net.is_bg.ltfn.authenticate.AuthenticationUtils;
import net.is_bg.ltfn.authenticate.AuthenticationUtils.ITokenAuthneticationCallBacks;
import net.is_bg.ltfn.authenticate.AuthenticationUtils.ITokenAuthneticationCallBacksFactory;
import net.is_bg.ltfn.authenticate.InvalidTokenException;
import net.is_bg.ltfn.authenticate.NoTokenException;
import net.is_bg.ltfn.authenticate.TokenAuthenticationParams;
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
	 * Gets a user token based authentication scheme!!!
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static IAuthentication<Boolean>  getTokenAuthenticator(Object httpServletRequest, Map requestParamMap, 
			ITokenAuthneticationCallBacksFactory itokenAuthenticationCallBackFactory,
			IRequestHelperFactory requestHelperFactory, ISessionDataFactory sessionDataFactory,
			TokenAuthenticationParams tokenParams){
		return 		AuthenticationUtils.getTokenAuthentication(requestParamMap, 
						httpServletRequest,  itokenAuthenticationCallBackFactory,tokenParams);
	}
	
	
	
	/***
	 * Check if user is logged!!!
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @throws IOException
	 */
	public static UserNavigationPage checkifLogged(Object httpServletRequest, ITokenAuthneticationCallBacksFactory itokenAuthenticationCallBackFactory,
			IRequestHelperFactory requestHelperFactory, ISessionDataFactory sessionDataFactory,
			TokenAuthenticationParams tokenParams) throws IOException{
		return new HpptServletRequestAuthentication(httpServletRequest, itokenAuthenticationCallBackFactory,  requestHelperFactory,  sessionDataFactory, tokenParams).checkiflogged();
	}
	
	
	
	
	
	private static class HpptServletRequestAuthentication {
		
		static  String LOGIN_PAGE = "pages/login.jsf";
		static  String MAIN_FORM_PAGE = "pages/mainform.jsf";
		static  String EMPTY_PAGE = "/pages/empty.jsf";
		private Object httpServletRequest;

		ITokenAuthneticationCallBacksFactory itokenAuthenticationCallBackFactory;
		TokenAuthenticationParams tokenParams;
		IRequestHelperFactory requestHelperFactory;
		ISessionDataFactory sessionDataFactory;
		
		/**Package private constructor!!!*/
		HpptServletRequestAuthentication(Object httpServletRequest,
				ITokenAuthneticationCallBacksFactory itokenAuthenticationCallBackFactory,
				IRequestHelperFactory requestHelperFactory, ISessionDataFactory sessionDataFactory,
				TokenAuthenticationParams tokenParams){
			this.httpServletRequest = httpServletRequest;
			this.itokenAuthenticationCallBackFactory = itokenAuthenticationCallBackFactory;
			this.sessionDataFactory = sessionDataFactory;
			this.requestHelperFactory = requestHelperFactory;
			this.tokenParams = tokenParams;
		}

		/**
		 * Returns login page base on authentication scheme
		 * @param t
		 * @return
		 */
		private  String getLoginPage(AUTHENTICATION_TYPE t){
			if(t == AUTHENTICATION_TYPE.USERPASS) return LOGIN_PAGE;
			return null;
		}
		
		
		/***
		 * Authentication based on token!!!
		 * @param httpServletRequest
		 * @return
		 * @throws AuthenticationException
		 */
		private  Boolean tokenAuthentication(Object httpServletRequest, Map requestparams) throws AuthenticationException{
			return AppAuthenticationUtils.getTokenAuthenticator(httpServletRequest, requestparams, itokenAuthenticationCallBackFactory, requestHelperFactory, sessionDataFactory, tokenParams).authenticate();
		}
		
		/**
		 * Check if user is logged!
		 */
		UserNavigationPage checkiflogged() throws IOException{
			ITokenAuthneticationCallBacks callBacks = itokenAuthenticationCallBackFactory.getITokenAuthneticationCallBacks();
			IRequestHelper reqhlp = requestHelperFactory.getIRequestHelper(httpServletRequest);
			ISessionData sessionData = sessionDataFactory.getISessionData(httpServletRequest);
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
					if(!isTokenInRequest)  ;//throw new RuntimeException("This session is associated with token & no token is found in request...");
					else if(!callBacks.isTokenValidCallBack().callBack(tokenId)) throw new RuntimeException("Invalid token in request...");
					else{
						//check if token is associated with different sessionId
						@SuppressWarnings("unused")
						String thisSessionId = sessionData.getSessionId(); l.log("This session id is " + thisSessionId);
						String sessionAssociatedWithToken = AuthenticationUtils.getSessionAssociatedWithToken(tokenId);  l.log("Session associated with this token is " + sessionAssociatedWithToken);
						
						if(sessionAssociatedWithToken != null && !thisSessionId.equals(sessionAssociatedWithToken))  throw new RuntimeException("Token is already associated with other session id...");
						
						//valid token in request 
						if(!tokenId.equals(sessionData.getTokenId())){
							//token in request is different from token in user session
							l.log("Token i request is " + tokenId + " Token in session is " + sessionData.getTokenId());
							l.log("overwrite user is " + tokenParams.isOverWriteUser());
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
					userNav.navPage = MAIN_FORM_PAGE;
				}
				userNav.setLogged(true);
			}
			
			//try token login if token login is supported & user is not logged in
			boolean ta = tokenParams.isAllowTokenAuthentication();
			if(ta && !userNav.isLogged()){
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
	
	
	public static interface ILogUser {
		public  Object logUser(Object httpServletRequest,  String userKey, String tokenId, String dbIndex);
		public  Object logUser(Object httpServletRequest,  Object user, String tokenId, String defDbConn);
		
	}
	
	
	
	public static interface ILogUserFactory {
		public ILogUser getILogUser();
	}
	
	
}
