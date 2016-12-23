package priv.suo.tools;
/**
 * 常量配置
 * 根据需要更改配置
 * [常量]    =  [值]  
 */
public class ConstTools {
	public static int     byteMax     =    10240         ;
	public static int     byteImgMax  =    102400        ;
	public static String  charSet     =    "utf8"        ;
	public static String  tempHtmlDir =    "html/"       ;
	public static String  tempImgDir  =    "image/"      ;
	public static String  dbName      =    "webspider"   ;
	public static String  username    =    "root"        ;
	public static String  password    =    "root"        ;
	public static void showAll(){
		System.out.println("byteMax:"+byteMax);
		System.out.println("charSet:"+charSet);
	}
}
