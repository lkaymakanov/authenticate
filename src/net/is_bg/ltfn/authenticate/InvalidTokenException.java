package net.is_bg.ltfn.authenticate;

public class InvalidTokenException extends  RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7708759282720305047L;

	public InvalidTokenException(String ivalidTokenId) {
		// TODO Auto-generated constructor stub
		super(ivalidTokenId);
	}

}
