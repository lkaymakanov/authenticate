package net.is_bg.ltfn.authenticate;

import authenticate.IAuthenticationFactory;
import net.is_bg.ltfn.authenticate.AppAuthenticationUtils.IRequestHelperFactory;
import net.is_bg.ltfn.authenticate.AppAuthenticationUtils.ISessionDataFactory;
import net.is_bg.ltfn.authenticate.AuthenticationUtils.ITokenAuthneticationCallBacksFactory;

public interface ITokenAuthenticationConfiguration {
	String getLoginPage();
	String getMainFormPage();
	String getEmptyPage();
	String getTokenAuthenticationPrefix();
	String getTokenAuthenticationContext();
	
	IServerSettings getServerSettings();
	IAuthenticationFactory getTokenAuthenticationFactory();
	ITokenAuthneticationCallBacksFactory getTokenAuthenticationCallBackFactory();
	IRequestHelperFactory getRequestHelperFactory(); 
	ISessionDataFactory getSessionDataFactory();
	TokenAuthenticationParams getTokenAuthenticationParams();
	String getConfiguration();
	
	
	public static class TokenAuthenticationConfigurationBuilder{
		String loginPage, mainFormPage, emptyPage, tokenAuthenticationPrefix, tokenAuthenticationContext;
		IServerSettings serverSettings;
		ITokenAuthneticationCallBacksFactory tokenAuthenticationCallBackFactory;
		IRequestHelperFactory requestHelperFactory;
		ISessionDataFactory sessionDataFactory;
		TokenAuthenticationParams tokenAuthenticationParams;
		IAuthenticationFactory tokenAuthenticationFactory;
		public TokenAuthenticationConfigurationBuilder setLoginPage(String loginPage) {
			this.loginPage = loginPage;
			return this;
		}
		public TokenAuthenticationConfigurationBuilder setMainFormPage(String mainFormPage) {
			this.mainFormPage = mainFormPage;
			return this;
		}
		public TokenAuthenticationConfigurationBuilder setEmptyPage(String emptyPage) {
			this.emptyPage = emptyPage;
			return this;
		}
		public TokenAuthenticationConfigurationBuilder setTokenAuthenticationPrefix(String tokenAuthenticationPrefix) {
			this.tokenAuthenticationPrefix = tokenAuthenticationPrefix;
			return this;
		}
		public TokenAuthenticationConfigurationBuilder setTokenAuthenticationContext(String tokenAuthenticationContext) {
			this.tokenAuthenticationContext = tokenAuthenticationContext;
			return this;
		}
		public TokenAuthenticationConfigurationBuilder setServerSettings(IServerSettings serverSettings) {
			this.serverSettings = serverSettings;
			return this;
		}
		public TokenAuthenticationConfigurationBuilder setTokenAuthenticationCallBackFactory(
				ITokenAuthneticationCallBacksFactory tokenAuthenticationCallBackFactory) {
			this.tokenAuthenticationCallBackFactory = tokenAuthenticationCallBackFactory;
			return this;
		}
		public TokenAuthenticationConfigurationBuilder setRequestHelperFactory(IRequestHelperFactory requestHelperFactory) {
			this.requestHelperFactory = requestHelperFactory;
			return this;
		}
		public TokenAuthenticationConfigurationBuilder setSessionDataFactory(ISessionDataFactory sessionDataFactory) {
			this.sessionDataFactory = sessionDataFactory;
			return this;
		}
		public TokenAuthenticationConfigurationBuilder setTokenAuthenticationParams(TokenAuthenticationParams tokenAuthenticationParams) {
			this.tokenAuthenticationParams = tokenAuthenticationParams;
			return this;
		}
		
		public TokenAuthenticationConfigurationBuilder setTokenAuthenticationFactory(IAuthenticationFactory tokenFactory){
			this.tokenAuthenticationFactory = tokenFactory;
			return this;
		}

		public ITokenAuthenticationConfiguration build(){
			return new TokenAuthenticationConfiguration( loginPage,  mainFormPage,  emptyPage,
					 tokenAuthenticationPrefix,  tokenAuthenticationContext,  serverSettings,
					 tokenAuthenticationCallBackFactory,
					 requestHelperFactory,  sessionDataFactory,
					 tokenAuthenticationParams, tokenAuthenticationFactory);
		}
	}
	
	
	static class TokenAuthenticationConfiguration implements ITokenAuthenticationConfiguration{
		String loginPage, mainFormPage, emptyPage, tokenAuthenticationPrefix, tokenAuthenticationContext;
		IServerSettings serverSettings;
		ITokenAuthneticationCallBacksFactory tokenAuthenticationCallBackFactory;
		IRequestHelperFactory requestHelperFactory;
		ISessionDataFactory sessionDataFactory;
		TokenAuthenticationParams tokenAuthenticationParams;
		IAuthenticationFactory tokenAuthenticationFactory;		
		
		
		public TokenAuthenticationConfiguration(String loginPage, String mainFormPage, String emptyPage,
				String tokenAuthenticationPrefix, String tokenAuthenticationContext, IServerSettings serverSettings,
				ITokenAuthneticationCallBacksFactory tokenAuthenticationCallBackFactory,
				IRequestHelperFactory requestHelperFactory, ISessionDataFactory sessionDataFactory,
				TokenAuthenticationParams tokenAuthenticationParams, IAuthenticationFactory tokenAuthenticationFactory) {
			super();
			this.loginPage = loginPage;
			this.mainFormPage = mainFormPage;
			this.emptyPage = emptyPage;
			this.tokenAuthenticationPrefix = tokenAuthenticationPrefix;
			this.tokenAuthenticationContext = tokenAuthenticationContext;
			this.serverSettings = serverSettings;
			this.tokenAuthenticationCallBackFactory = tokenAuthenticationCallBackFactory;
			this.requestHelperFactory = requestHelperFactory;
			this.sessionDataFactory = sessionDataFactory;
			this.tokenAuthenticationParams = tokenAuthenticationParams;
			this.tokenAuthenticationFactory = tokenAuthenticationFactory;
		}

		
		@Override
		public IAuthenticationFactory getTokenAuthenticationFactory() {
			// TODO Auto-generated method stub
			return tokenAuthenticationFactory;
		}
		
		public String getLoginPage() {
			return loginPage;
		}
		
		public String getEmptyPage() {
			return emptyPage;
		}
		public String getTokenAuthenticationPrefix() {
			return tokenAuthenticationPrefix;
		}
		public String getTokenAuthenticationContext() {
			return tokenAuthenticationContext;
		}
		public IServerSettings getServerSettings() {
			return serverSettings;
		}
		public ITokenAuthneticationCallBacksFactory getTokenAuthenticationCallBackFactory() {
			return tokenAuthenticationCallBackFactory;
		}
		public IRequestHelperFactory getRequestHelperFactory() {
			return requestHelperFactory;
		}
		public ISessionDataFactory getSessionDataFactory() {
			return sessionDataFactory;
		}
		public TokenAuthenticationParams getTokenAuthenticationParams() {
			return tokenAuthenticationParams;
		}
		@Override
		public String getMainFormPage() {
			// TODO Auto-generated method stub
			return mainFormPage;
		}
		
		@Override
		public String getConfiguration() {
			
			// TODO Auto-generated method stub
			StringBuilder sb = new StringBuilder();
			sb.append("loginPage:" + loginPage); sb.append("\n");
			sb.append("mainFormPage:" + mainFormPage); sb.append("\n");
			sb.append("tokenAuthenticationPrefix:" + tokenAuthenticationPrefix); sb.append("\n");
			sb.append("tokenAuthenticationContext:" + tokenAuthenticationContext);sb.append("\n");
			sb.append("=====Token AuthenticationParams=======\n" + tokenAuthenticationParams.getConfiguration()); sb.append("\n");
			sb.append("=====Server Settings=====\n" + serverSettings.getConfiguration()); sb.append("\n");
			String s = sb.toString();
			return s;
		}
	}
}