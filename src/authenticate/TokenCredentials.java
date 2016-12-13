package authenticate;

import token.IToken;

public class TokenCredentials  implements ICredentials{

	private IToken token;
	
	public TokenCredentials(IToken token){
		this.token = token;
	}
	
	public IToken getToken() {
		return token;
	}
}
