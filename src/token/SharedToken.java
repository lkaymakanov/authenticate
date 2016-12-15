package token;

import java.io.IOException;
import java.util.Date;

/***
 * A token that is shared between applications!!!
 * @author lubo
 *
 */
public final class SharedToken implements IToken{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9148377726252252854L;
	private String id = null;
	private Date createTimeDate;
	private Date endTimeDate;
	private long ttl;
	
	private SharedToken(){
		this(null, 0);
	}
	
	private SharedToken(String id, long ttl){
		id = TokenUtils.generateTokenId();
		this.createTimeDate = new Date();
		this.ttl = ttl;
		this.endTimeDate = new Date(createTimeDate.getTime() + ttl);
	}

	@Override
	public byte[] serialize() throws IOException  {
		// TODO Auto-generated method stub
		return TokenUtils.serialize(this);
	}


	@Override
	public String getTokenId() {
		// TODO Auto-generated method stub
		return id;
	}
	
	public static SharedToken getInstance(String id){
		return new SharedToken(id, 0);
	}
	
	public static SharedToken getInstance(String id, long ttl){
		return new SharedToken(id, ttl);
	}
	
	public static SharedToken getInstance(long ttl){
		return new SharedToken(null, ttl);
	}


	public Date getCreateTimeDate() {
		return createTimeDate;
	}


	public Date getEndTimeDate() {
		return endTimeDate;
	}

	public long getTtl() {
		return ttl;
	}
	
}
