package net.is_bg.ltfn.authenticate;

public interface IAuthenticationCallBack<OUT, IN> {
	public OUT callBack(IN in);
}
