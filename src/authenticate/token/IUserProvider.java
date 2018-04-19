package authenticate.token;

/**
 * Interface that gets access to logged user!!
 * @author lubo
 *
 * @param <T>
 */
public interface IUserProvider<T>{
	T getUser();
}
