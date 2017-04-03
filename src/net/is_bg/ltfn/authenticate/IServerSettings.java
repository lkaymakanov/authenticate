package net.is_bg.ltfn.authenticate;


public interface IServerSettings {
	
	public String getKeystoreFile();
	public String getKeystorePass();
	public String getKeyAlias() ;
	public String getKeyPass();
	public String getSocketProtocol();
	public String getStoreType();
	public String getApplication();
	public int    getReadTimeOut();
	public String getContext() ;
	public String getIp() ;
	public String getPort();
	public String getProtocol() ;
	public String toClientConfigurationName();
	public boolean isSecure();
	public String toEndPoint();
	String getConfiguration();
	
	public static class TokenServerSettingsBuilder{
		
		private String ip = "10.240.110.181";
		private String port = "8080";
		private String protocol = "http";
		private String context;
		private String keystoreFile;
		private String keystorePass;
		private String keyAlias;
		private String keyPass;
		private String socketProtocol;
		private String storeType;
		private String application;
		private int readTimeOut = 600;
		
		public TokenServerSettingsBuilder setIp(String ip) {
			this.ip = ip;
			return this;
		}
		public TokenServerSettingsBuilder setPort(String port) {
			this.port = port;
			return this;
		}
		public TokenServerSettingsBuilder setProtocol(String protocol) {
			this.protocol = protocol;
			return this;
		}
		
		public TokenServerSettingsBuilder setContext(String context) {
			this.context = context;
			return this;
		}
		public TokenServerSettingsBuilder setKeystoreFile(String keystoreFile) {
			this.keystoreFile = keystoreFile;
			return this;
		}
		public TokenServerSettingsBuilder setKeystorePass(String keystorePass) {
			this.keystorePass = keystorePass;
			return this;
		}
		public TokenServerSettingsBuilder setKeyAlias(String keyAlias) {
			this.keyAlias = keyAlias;
			return this;
		}
		public TokenServerSettingsBuilder setKeyPass(String keyPass) {
			this.keyPass = keyPass;
			return this;
		}
		public TokenServerSettingsBuilder setSocketProtocol(String socketProtocol) {
			this.socketProtocol = socketProtocol;
			return this;
		}
		public TokenServerSettingsBuilder setStoreType(String storeType) {
			this.storeType = storeType;
			return this;
		}
		public TokenServerSettingsBuilder setApplication(String application) {
			this.application = application;
			return this;
		}
		public TokenServerSettingsBuilder setReadTimeOut(int readTimeOut) {
			this.readTimeOut = readTimeOut;
			return this;
		}
		
		public TokenServerSettingsBuilder fillFromString(String settings){
			String [] a = settings.split(":");
			protocol = a[0].toLowerCase(); ip = a[1]; port = a[2];
			return this;
		}

		public IServerSettings build(){
			return new ServerSettings(ip, port, protocol,
					context,keystoreFile, keystorePass,
					keyAlias, keyPass,
					socketProtocol,
					storeType,application, readTimeOut);
		}
		
	}
	
	
	static class ServerSettings implements IServerSettings{

		private String ip = "10.240.110.181";
		private String port = "8080";
		private String protocol = "http";
		private String context;
		private String keystoreFile;
		private String keystorePass;
		private String keyAlias;
		private String keyPass;
		private String socketProtocol;
		private String storeType;
		private String application;
		private int readTimeOut = 600;
		
		
		ServerSettings(String ip, String port, String protocol,
			    String context, String keystoreFile,
				String keystorePass, String keyAlias, String keyPass,
				String socketProtocol, String storeType, String application,
				int readTimeOut) {
			super();
			this.ip = ip;
			this.port = port;
			this.protocol = protocol;
			this.context = context;
			this.keystoreFile = keystoreFile;
			this.keystorePass = keystorePass;
			this.keyAlias = keyAlias;
			this.keyPass = keyPass;
			this.socketProtocol = socketProtocol;
			this.storeType = storeType;
			this.application = application;
			this.readTimeOut = readTimeOut;
		}
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		public String getPort() {
			return port;
		}
		public void setPort(String port) {
			this.port = port;
		}
		public String getProtocol() {
			return protocol;
		}
		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}
		public boolean isSecure() {
			return protocol.equals("https");
		}
		
		public String getContext() {
			return context;
		}
		public void setContext(String context) {
			this.context = context;
		}
		public String getKeystoreFile() {
			return keystoreFile;
		}
		public void setKeystoreFile(String keystoreFile) {
			this.keystoreFile = keystoreFile;
		}
		public String getKeystorePass() {
			return keystorePass;
		}
		public void setKeystorePass(String keystorePass) {
			this.keystorePass = keystorePass;
		}
		public String getKeyAlias() {
			return keyAlias;
		}
		public void setKeyAlias(String keyAlias) {
			this.keyAlias = keyAlias;
		}
		public String getKeyPass() {
			return keyPass;
		}
		public void setKeyPass(String keyPass) {
			this.keyPass = keyPass;
		}
		public String getSocketProtocol() {
			return socketProtocol;
		}
		public void setSocketProtocol(String socketProtocol) {
			this.socketProtocol = socketProtocol;
		}
		public String getStoreType() {
			return storeType;
		}
		public void setStoreType(String storeType) {
			this.storeType = storeType;
		}
		public String getApplication() {
			return application;
		}
		public void setApplication(String application) {
			this.application = application;
		}
		public int getReadTimeOut() {
			return readTimeOut;
		}
		public void setReadTimeOut(int readTimeOut) {
			this.readTimeOut = readTimeOut;
		}
		/**
		 * Returns protocol:ip:port - suitable for configuration name!!!! 
		 * @return
		 */
		public String toClientConfigurationName(){
			return protocol + ":" + ip + ":" + port;
		}
		
		/***
		 * Gets server end point by ServerSettings properties!!!
		 * @return
		 */
		public String toEndPoint(){
			return protocol+"://" + ip + ":" + port + "/" + context;
		}
		
		@Override
		public String getConfiguration() {
			// TODO Auto-generated method stub
			StringBuilder sb = new StringBuilder();
			sb.append("ip:" + ip); sb.append("\n");
			sb.append("port:" + port); sb.append("\n");
			sb.append("protocol:" + protocol); sb.append("\n");
			sb.append("secure:" + isSecure());sb.append("\n");
			sb.append("context:" + context); sb.append("\n");
			sb.append("keystoreFile:" + keystoreFile); sb.append("\n");
			sb.append("keystorePass:" + keystorePass); sb.append("\n");
			sb.append("keyAlias:" + keyAlias); sb.append("\n");
			sb.append("keyPass:" + keyPass); sb.append("\n");
			sb.append("socketProtocol:" + socketProtocol); sb.append("\n");
			sb.append("storeType:" + storeType); sb.append("\n");
			sb.append("application:" + application); sb.append("\n");
			sb.append("readTimeOut:" + readTimeOut); sb.append("\n");
			String s = sb.toString();
			return s;
		}
		
	}
	
	
	
	/*static SOCKET_PROTOCOL socketProtocolFromString(String socketProtocol){
		return socketProtocol ==null ? null :  socketProtocol.equals("ssl") ?  SOCKET_PROTOCOL.SSL : socketProtocol.equals("tls") ?  SOCKET_PROTOCOL.TLS : null;
	}
	
	static STORE_TYPE storeTypeFromString(String storeType){
		return  storeType ==null ? null :   storeType.equals("pkcs12") ?  STORE_TYPE.PKCS12 : storeType.equals("jks") ?  STORE_TYPE.JKS : null;
	}*/
	
	
	
	
}
