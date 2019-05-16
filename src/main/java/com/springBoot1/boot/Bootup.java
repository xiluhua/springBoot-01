package com.springBoot1.boot;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.springBoot1.tool.IOTool;
import com.springBoot1.tool.LogTool;


/**
 * 启动
 * @author xilh
 * @since 20190409
 * @param args
 * @return
 */
public class Bootup {
	
	public final static String PATH							 	= "file:///home/loginuser/core/conf/";
	public final static String APPLICATION_CONTEXT_XML 			= "applicationContext.xml";
	public final static String APPLICATION_CONTEXT_XML_ORIGIN 	= "applicationContextOrigin.xml";
	public final static String PATH_APPLICATION_CONTEXT		 	= PATH + APPLICATION_CONTEXT_XML;
	/**
	 * 启动前
	 * @author xilh
	 * @since 20190409
	 * @param args
	 * @return
	 */
	public String[] aopConfig(String[] args) {
		if (args == null || args.length == 0) {
			try {
				AopConfig[] arr = new AopConfig[] { 
				};
				
				String[]  pkgs     = new String[] { "com.taiping" };
				boolean flag = this.createAopConfigByAnnotation(Arrays.asList(arr), pkgs);
				if ( !flag ) {
					return new String[]{"2"};
				}
				args = new String[]{"1"};

			} catch (Exception e) {
				e.printStackTrace();
				return new String[]{"2"};
			}
		}
		return args;
	}
	
	/**
	 * 启动前
	 * @author xilh
	 * @since 20190409
	 * @param args
	 * @return
	 */
	public String[] envAndAopConfig(String[] args) {
		
		Thread thread = Thread.currentThread();
		System.out.println(thread.getName());
		// 环境
		Env.ENV = Cons.DEV;
		if (args.length > 0 && args[0].toLowerCase().indexOf(Cons.PROD) > -1) {
			Env.ENV = Cons.PROD;
		}
		
		// LOCAL
		if (args == null || args.length == 0) {
			LogTool.debug(this.getClass(), "LOCAL ...");
			if (thread.getName().equals("main")) {
				return args;
			}
		}
		
		// UAT & PROD
		if (args.length == 1) {
			LogTool.debug(this.getClass(), "UAT ...");
			args[0] = args[0];
		}
		
		try {
			AopConfig[] arr = new AopConfig[] { 
			};
			
			
			String[] pkgs	= new String[] { "com.taiping" };
			this.createAopConfigByAnnotation(Arrays.asList(arr), pkgs);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return args;
	}
	
	public boolean createAopConfigByAnnotation(List<AopConfig> aopConfigs, String ... pkgs) throws UnsupportedEncodingException, Exception {
		
		return true;
	}
	
}

