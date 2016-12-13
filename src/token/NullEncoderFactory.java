package token;

import java.util.Arrays;

public class NullEncoderFactory  implements IEncoderFactory{
	
	private IEncoder nullEncoder = new IEncoder() {
		@Override
		public byte[] encode(byte[] bytes, int len) {
			// TODO Auto-generated method stub
			return Arrays.copyOf(bytes, len);
		}
		
		@Override
		public byte[] encode(byte[] bytes) {
			// TODO Auto-generated method stub
			return bytes;
		}
	};

	@Override
	public IEncoder getEncoder() {
		// TODO Auto-generated method stub
		return nullEncoder;
	}

}
