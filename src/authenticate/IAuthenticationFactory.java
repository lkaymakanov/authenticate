package authenticate;

public interface IAuthenticationFactory<R> {
	public IAuthentication<R>  getAuthentication();  
}
