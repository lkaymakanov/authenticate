package token;

public interface IEncoder {
	public byte [] encode(byte [] bytes);
	public byte [] encode(byte [] bytes, int len);
}
