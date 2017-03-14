package net.is_bg.ltfn.authenticate;

import net.is_bg.ltfn.authenticate.TokenAuthenticationFactory.ServerData;
import net.is_bg.ltfn.authenticate.TokenAuthenticationFactory.TokenDataWrapper;

/**
 * The exposed  structure of data that is coming from Authentication server!!!
 * @author lubo
 *
 */
public  class ServerDataEx extends ServerData{
	public ServerDataEx(TokenDataWrapper tokenData, String userKey, String defDbCon) {
		super(tokenData, userKey, defDbCon);
	}
	ServerDataEx() {
		// TODO Auto-generated constructor stub
	}
	
}