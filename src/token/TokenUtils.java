package token;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.UUID;




/***
 * Token utilities!!!
 * @author lubo
 *
 */
public class TokenUtils {
	
	public  final static String ALPHABET = "-_1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
	
	/***
	 * Generates a String token identifier!!!
	 * @return
	 */
	public static String generateTokenId(){
		return UUID.randomUUID().toString();
	}
	
	/***
	 * Encrypts token using encoder!!!
	 */
	public static  byte [] encryptToken(IToken token, IEncoderFactory encoderFactory) throws IOException{
		return encrypt(token.serialize(), encoderFactory);
	}
	
	/***
	 * Decrypts token using decoder!!!
	 * @throws ClassNotFoundException 
	 */
	public static  IToken decryptToken(byte [] token, IDecoderFactory deocderFactory) throws IOException, ClassNotFoundException{
		byte [] dec = decrypt(token, deocderFactory);
		return deserialize(dec, dec.length, IToken.class);
	}
	
	/***
	 * Encrypts a token id using encoder!!!
	 * @return
	 */
	public static byte[] encrypt(byte[] b, int len, IEncoderFactory encoderFactory){
		return encoderFactory.getEncoder().encode(b, len);
	}
	
	/***
	 * Decrypts a token using a decoder!!!
	 * @return
	 */
	public static byte[] decrypt(byte[] b, int len, IDecoderFactory decoderFactory){
		return decoderFactory.getDecoder().decode(b, len);
	}
	
	/***
	 * Encrypts a token id using encoder!!!
	 * @return
	 */
	public static byte[] encrypt(byte[] b,  IEncoderFactory encoderFactory){
		return encoderFactory.getEncoder().encode(b);
	}
	
	/***
	 * Decrypts a token using a decoder!!!
	 * @return
	 */
	public static byte[] decrypt(byte[] b,  IDecoderFactory decoderFactory){
		return decoderFactory.getDecoder().decode(b);
	}
	
	

	/**
     * Converts Object to bytes!
     * @param o Object to convert to bytes
     * @return byte array of object
     * @throws IOException 
     */
	 public static byte[] serialize(Object o) throws IOException  {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream os;
			os = new ObjectOutputStream(out);
			os.writeObject(o);
			return out.toByteArray();
	 }

     /**
     * Converts byte array to Object!
     * 
     * @param data
     *            Byte array to be converted to Object
     * @param size
     *            The size of array in bytes
     * @return Converted Object - Null if fails
     * @throws IOException 
     * @throws ClassNotFoundException 
     */
     @SuppressWarnings("unchecked")
	public static <T> T deserialize(byte[] data, int size, Class<T> objectClass) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data, 0, size);
		ObjectInputStream is;
		is = new ObjectInputStream(in);
		return (T) is.readObject();
     }
     
     static final String base = "D:\\Projects\\PROJECTS\\IUIS\\workspace\\LTFN\\target\\classes\\token\\";
     
     
     public static void main(String [] a) throws UnsupportedEncodingException{
    	//byte [] fc =  AppUtil.readFileContent(new File(base + "SimpleOffsetDecoderFactory.class"));
    	//saveByteArray(fc, null, 100, "token.SimpleOffsetDecoderFactory.class");
    	System.out.println(getClName(ByteArrays.encoderDecoder));
    	System.out.println(getClName(ByteArrays.encoderFactory));
    	System.out.println(getClName(ByteArrays.decoderFactory));
     }
     
     
     private static String getClName(byte [] b){
    	 int length = (((b[0] << 8 ) | b[1])) & 0x0000ffff;
    	 byte [] nb;
    	 nb = new byte[length];
    	 for(int i = 0; i < length; i++){
    		 nb[i] = b[i+2]; 
    	 }
    	 Arrays.copyOfRange(b, length+2, b.length);
    	 return new String(nb);
     }
     
     
     private static void saveByteArray(byte [] b,  int bytesPerRow, String className) throws UnsupportedEncodingException{
    	 if(bytesPerRow > b.length) bytesPerRow = b.length;
    	 String byteArrayPrexif = " byte []  " + className + " = new byte[]{ \n" ;
    	 StringBuilder bd = new StringBuilder();
    	 bd.append(byteArrayPrexif);
    	 //add class name bytes
    	 byte [] classnameBytes = className.getBytes("UTF-8");
    	 bd.append((classnameBytes.length &  0x0000ff00 )+ ", ");  //high byte 
    	 bd.append((classnameBytes.length &  0x000000ff )+ ", ");  //low byte
    	 getByteArrayAsMatrix(bd, classnameBytes, bytesPerRow);
    	 getByteArrayAsMatrix(bd, b, bytesPerRow).toString();
    	 bd.append("};");
    	 System.out.println(bd);
     }
     
     /**
      * Creates a comma separated rectangular matrix of byte array into the StringBuilder sb!!!
      * @param sb
      * @param b
      * @param bytesPerRow
      * @return
      */
     private static StringBuilder getByteArrayAsMatrix(StringBuilder sb, byte [] b, int bytesPerRow){
    	 int row = 0;
    	 int i = 0;
    	 StringBuilder bd = sb;
    	 for(; row * bytesPerRow < b.length; row++){
    		 i = row * bytesPerRow;
    		 int jEnd = i + bytesPerRow;
    		 for(; i < jEnd && i < b.length; i++){
    			 bd.append(b[i] + ", ");      //collect elements in each row
    		 }
    		 bd.append("\n");   //add new line
    	 }
    	 return bd;
     }
     
     /***
      * Creates a shared token !!!
      * @param ttl - time to live in milliseconds
      * @return
      */
     public static SharedToken getSharedToken(long ttl){
    	 return SharedToken.getInstance(ttl);
     }
     
 	/***
 	 * Encrypts token & return a UTF representation String of encryption token bytes!!!
 	 * @param token
 	 * @param key
 	 * @return
 	 * @throws IOException 
 	 *//*
 	public static  String encryptTokenAsUTF8(IToken token, String key) throws IOException{
 		return new String(encryptToken(token, key));
 	}*/
     
     
 	
/* 	*//***
 	 * Decrypts token from token encrypted token bytes using  a key!!!
 	 * @param tokenBytes
 	 * @param key
 	 * @return
 	 *//*
 	public static  <T extends Serializable>  byte [] decryptToken(byte [] tokenBytes, String key){
 		//SimpleOffsetEncoderDecoder encdec = getEncoderDecoder(key);
 		return null;
 	}
 	
 	*//***
 	 * Decrypts token from token encrypted token bytes using user id hash & user pass hash!!!
 	 * @param tokenBytes
 	 * @param userIdhash
 	 * @param userHashedPass
 	 * @return
 	 *//*
 	public static  <T extends Serializable>  byte [] decryptToken(byte [] tokenBytes, String userIdhash, String userHashedPass){
 		return decryptToken(tokenBytes, generateEncryptionKey(userIdhash, userHashedPass));
 	}
 	

 	
 	*//**
 	 * Encrypts a token id using user hashed id & user hashed pass!!!
 	 * @param tokenid
 	 * @param userIdhash
 	 * @param userPass
 	 * @return
 	 *//*
 	public static byte[] encryptTokenId(byte[] tokenid,  String userIdhash, String userHashedPass){
 		IEncoder encdec = getEncoder(generateEncryptionKey(userIdhash, userHashedPass));
 		return encdec.encode(tokenid);
 	}*/
     
/*     *//***
 	 * Generates encryption key for user id hash & pass hash!!!
 	 * @param userId
 	 * @param userPass
 	 * @return
 	 *//*
 	public static String generateEncryptionKey(String userIdhash, String userHashedPass){
 		return new Sha512().digest(userIdhash + userHashedPass);
 	}*/
     
/*     private final static String ALPHABET = "-_1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
 	
 	*//***
 	 * Returns encoder using alphabet & key!!!
 	 * @param key
 	 * @return
 	 *//*
 	private static IEncoder getEncoder(String key){
 		return  new SimpleOffsetEncoderDecoder(ALPHABET, key);
 	}
 	
 	
 	*//***
 	 * Returns encoder using alphabet & key!!!
 	 * @param key
 	 * @return
 	 *//*
 	private static IDecoder getDecoder(String key){
 		return  new SimpleOffsetEncoderDecoder(ALPHABET, key);
 	}
 	*/
	
}
