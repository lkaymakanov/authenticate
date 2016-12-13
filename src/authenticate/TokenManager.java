package authenticate;

import java.util.Hashtable;
import java.util.Map;

public class TokenManager {
	private  static Map<String, TokenData> map = new Hashtable<String, TokenData>();
	
	public static void addTokenData(String tokenId, TokenData data){
		synchronized (TokenManager.class) {
			map.put(tokenId, data);
		}
	}
	
	public static TokenData getTokeData(String tokenId){
		synchronized (TokenManager.class) {
			return map.get(tokenId);
		}
	}
}