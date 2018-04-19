package authenticate.token;

/***
 * The main authentication interface!!!
 * @author lubo
 */
public interface IAuthentication<R>{
	public R authenticate() throws TokenAuthenticationException;
}
