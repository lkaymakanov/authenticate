package net.is_bg.ltfn.authenticate;

import java.lang.reflect.InvocationTargetException;

import token.IDecoderFactory;
import token.IEncoderFactory;

public class AuthenticationEncryptionUtils {
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Object getInstance(Class type,
					Class<?>[] argtypes,
					Object[] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException,  NoSuchMethodException, InvocationTargetException  {
	    if (argtypes == null || args == null)
		return type.newInstance();
	    return type.getConstructor(argtypes).newInstance(args);
	}


	public static IEncoderFactory getEncoderFactory(ClassLoader cl, String pass) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException{
		IEncoderFactory ef = (IEncoderFactory)getInstance(cl.loadClass("token.SimpleOffsetEncoderFactory"), new Class[]{String.class}, new Object[]{pass});
		return ef;
	}
	
	public static IDecoderFactory getDecoderFactory(ClassLoader cl, String pass) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException{
		IDecoderFactory def = (IDecoderFactory)getInstance(cl.loadClass("token.SimpleOffsetDecoderFactory"), new Class[]{String.class}, new Object[]{pass});
		return def;
	}
}