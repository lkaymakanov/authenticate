package token;

import java.util.Hashtable;
import java.util.Map;

public class TokenManager {
	private  static Map<String, ITokenData> map = new Hashtable<String, ITokenData>();
	
	public static void addTokenData(String tokenId, ITokenData data){
		synchronized (TokenManager.class) {
			map.put(tokenId, data);
		}
	}
	
	public static ITokenData getTokeData(String tokenId){
		synchronized (TokenManager.class) {
			return map.get(tokenId);
		}
	}
}