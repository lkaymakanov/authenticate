package net.is_bg.ltfn.authenticate;

/**
 * Interface that gets access to logged user!!
 * @author lubo
 *
 * @param <T>
 */
public interface IUserProvider<T>{
	T getUser();
}
