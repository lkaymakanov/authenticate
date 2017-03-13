package net.is_bg.ltfn.authenticate;

import authenticate.AuthenticationException;
import authenticate.IAuthentication;
import authenticate.IAuthenticationFactory;
import authenticate.UserPassAuthentication;

/***
 * A user password AuthenticationFactory!!!
 * 
 * @author lubo
 *
 */
public class UserPassAuthenticationFactory implements IAuthenticationFactory {

    private UserPassAuthentication up;

    public UserPassAuthenticationFactory(String user,
					 String pass,
					 IAuthenticationCallBack callBack,
					 Object authenticationCallBackParam) {
	up = new UserPassAuthenticationInner(user, pass, callBack, authenticationCallBackParam);
    }

    @Override
    public IAuthentication getAuthentication() {
	return up;
    }

    static class UserPassAuthenticationInner extends UserPassAuthentication {

	private IAuthenticationCallBack callBack;
	private Object authenticationCallBackParam;

	public UserPassAuthenticationInner(String user,
					   String pass,
					   IAuthenticationCallBack callBack,
					   Object authenticationCallBackParam) {
	    super(user, pass);
	    this.callBack = callBack;
	    this.authenticationCallBackParam = authenticationCallBackParam;
	}

	@Override
	public Object authenticate() throws AuthenticationException {
	    // here check user pass
	    return callBack.callBack(authenticationCallBackParam);
	}
    }

}
