package token;

import java.io.IOException;
import java.io.Serializable;

/***
 * The main token interface!!!
 * @author lubo
 * T is the data associated with the token
 */
public interface IToken extends Serializable {
	byte [] serialize() throws IOException;
	String getTokenId();
}
