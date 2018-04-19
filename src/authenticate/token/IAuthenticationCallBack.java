package authenticate.token;

public interface IAuthenticationCallBack<OUT, IN> {
	public OUT callBack(IN in);
}
