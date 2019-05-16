package com.springBoot1.tool;

/**
 * 
 * @author xiluhua by 20160119
 *
 */
public class LogTool {
	//-------------------------------------------------------------------------
	public static <T> void debug(Class<T> clazz, String logStr) {
		System.out.println(logStr);
	}

	public static <T> void info(Class<T> clazz, String logStr) {
		System.out.println(logStr);
	}
	
	public static <T> void warn(Class<T> clazz, String logStr) {
		
		System.out.println(logStr);
	}
	
	public static <T> void error(Class<T> clazz, String logStr) {
		System.out.println(logStr);
	}
	
	public static <T> void error(Class<T> clazz, Exception e) {
		System.out.println(e.getMessage());
	}
}