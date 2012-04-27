package nativeLib;

/**
 * 
 * @author ricardoandrade
 *
 */
public class Zbar {
	static {
		System.loadLibrary("zbar");
	}
	
	/**
	 * 
	 * @param width
	 * @param height
	 * @param data
	 * @return
	 */
	public native String decode(int width, int height, byte[]data);
	
}
