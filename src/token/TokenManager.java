package token;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * A class that manages tokens & data associated with token ids !!!
 * @author lubo
 */
public class TokenManager {
	private  static Map<String, ITokenData> map = new Hashtable<String, ITokenData>();
	
	/***
	 * Add token data to token map! The keys is the token id!
	 * @param data token data
	 */
	public static void addTokenData(ITokenData data){
		synchronized (TokenManager.class) {
			map.put(data.getTokenId(), data);
		}
	}
	
	/**
	 * Returns token data based on token id!
	 * @param tokenId
	 * @return Data associated with token id
	 */
	@SuppressWarnings("unchecked")
	public static ITokenData getTokeData(String tokenId){
		synchronized (TokenManager.class) {
			return map.get(tokenId);
		}
	}
	
	/**
	 * Remove token from token map by token id!
	 * @param tokenId
	 */
	public static void removeToken(String tokenId){
		synchronized (TokenManager.class) {
			 map.remove(tokenId);
		}
	}
	
	/**
	 * Returns set containing token ids!
	 * @return token ids
	 */
	public static Set<String> getTokenids(){
		synchronized (TokenManager.class) {
			return map.keySet();
		}
	}
}