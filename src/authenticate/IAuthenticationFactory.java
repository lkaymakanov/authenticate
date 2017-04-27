package authenticate;

/***
 * A factory providing a way of authentication!!!
 * @author lubo
 *
 * @param <R> the type of data returned by the authenticate method IAuthentication interface!!!
 */
public interface IAuthenticationFactory<R> {
	public IAuthentication<R>  getAuthentication();  
}
