package net.is_bg.ltfn.authenticate;

import token.ITokenData;
import net.is_bg.ltfn.authenticate.TokenAuthenticationFactory.ServerData;
import net.is_bg.ltfn.authenticate.TokenAuthenticationFactory.TokenDataWrapper;

/**
 * The exposed  structure of data that is coming from Authentication server!!!
 * @author lubo
 *
 */
public  class TokenDataUserData extends ServerData{
	
	TokenDataUserData() {
		// TODO Auto-generated constructor stub
	}
	
	TokenDataUserData(ITokenData tokenData, String userKey, String defDbCon) {
		// TODO Auto-generated constructor stub
		super((TokenDataWrapper)tokenData, userKey, defDbCon);
	}
}