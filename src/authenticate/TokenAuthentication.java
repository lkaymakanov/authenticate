package authenticate;


public abstract class TokenAuthentication <T> implements IAuthentication<T>{
	protected TokenCredentials tokenCredentials;
	
	public TokenAuthentication(TokenCredentials tokenCredentials){
		this.tokenCredentials = tokenCredentials;
	}
	
	public TokenCredentials getTokentCredentials() {
		return tokenCredentials;
	}
}
