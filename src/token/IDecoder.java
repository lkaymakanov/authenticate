package token;

public interface IDecoder {
	public byte [] decode(byte [] bytes);
	public byte [] decode(byte [] bytes, int len);
}
