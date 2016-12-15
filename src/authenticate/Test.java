package authenticate;

import token.ITokenData;
import token.TokenData.TokenDataBuilder;

public class Test {

	public static void main(String [] ar){
		TokenDataBuilder<String> s  = new TokenDataBuilder<String>();
		s.setAdditionalData("add");
		s.setTokenId("tokenId");
		ITokenData<String> tdata =  s.build();
	}
}
