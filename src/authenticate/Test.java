package authenticate;

import token.ITokenData;
import token.TokenData.TokenBuilder;

public class Test {

	public static void main(String [] ar){
		TokenBuilder<String> s  = new TokenBuilder<String>();
		s.setAdditionalData("add");
		s.setTokenId("tokenId");
		ITokenData<String> tdata =  s.build();
	}
}
