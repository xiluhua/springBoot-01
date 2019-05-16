package com.springBoot1.boot;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 环境工具
 * @author xilh by 20160119
 *
 */
public class Env {
	
	// add by xiluhua 20190409
	public static String DATABASE 	= null;
	public static String ENV		= null;
	
	public static String localIp = null;
	public static boolean isLocal = false;
	public static boolean isUat = false;
	public static boolean isFormal = false;
	
	static{
		localIp = getLocalIp();
		isUat = localIp.startsWith("10.1") || localIp.startsWith("10.29");
		isFormal = localIp.startsWith("10.4");
		isLocal = !isUat && !isFormal;
	}
	
	/**
	 * 获取本机IP地址
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getLocalIp(){
		String localIp = "";
		Enumeration allNetInterfaces = null;
		try {
			allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		InetAddress ip = null;
		while (allNetInterfaces.hasMoreElements()) {
			NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
//			System.out.println(netInterface.getName());
			Enumeration addresses = netInterface.getInetAddresses();
			while (addresses.hasMoreElements()) {
				ip = (InetAddress) addresses.nextElement();
				
				if (ip != null && ip instanceof Inet4Address) {

//					System.out.println("local IP address = " + ip.getHostAddress());
					localIp = ip.getHostAddress();
					if (!localIp.equals("127.0.0.1")) {
						return localIp;
					}
					
				}
			}
		}
		return localIp;
	}
}
