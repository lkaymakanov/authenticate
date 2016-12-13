package token;

import java.util.Arrays;

public class NullDecoderFactory implements IDecoderFactory {

	IDecoder nullDecoder = new IDecoder() {
		@Override
		public byte[] decode(byte[] bytes, int len) {
			// TODO Auto-generated method stub
			return Arrays.copyOf(bytes, len);
		}
		
		@Override
		public byte[] decode(byte[] bytes) {
			// TODO Auto-generated method stub
			return bytes;
		}
	};
	
	@Override
	public IDecoder getDecoder() {
		// TODO Auto-generated method stub
		return nullDecoder;
	}

}
