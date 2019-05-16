package com.springBoot1.tool;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 文件处理
 * @author lenovo
 *
 */
public class FilesUtil {

	public static Map pMap = new HashMap();

	/**
	 * 判断是哪个系统
	 * @return
	 */
	public static String getOS() {
		String system = "";
		int OS = System.getProperty("os.name").toUpperCase().indexOf("WINDOWS");
		if (OS != -1)
			system = "windows";
		else
			system = "linux";
		return system;
	}

	public static void exeCommand(String cmd) throws Exception {
		Runtime.getRuntime().exec(cmd);
	}

//	public static void main(String[] args) throws Exception {
//		getOS();
//	}

	public static String getProperty(String file, String propertyName) {
		if (pMap.get(file) == null) {
			Properties prop = new Properties();
			InputStream is = null;
			try {
				is = new FileInputStream(FilesUtil.class
						.getResource("/" + file).getPath());
				prop.load(is);
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			pMap.put(file, prop);
		}

		String propertyValue = ((Properties) pMap.get(file)).getProperty(
				propertyName, "UTF-8");
		return propertyValue;
	} 

	/**
	 * 文件转换成流，返回流
	 * @param xmlStream
	 * @return
	 * @throws Exception 
	 */
	public byte[] parseFileToStream(String path) throws Exception {
		FileInputStream in = new FileInputStream(path);
		byte buffer[] = read(in);
		return buffer;
	}

	public static byte[] read(InputStream in) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		if (in != null) {
			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
			out.close();
			in.close();
			return out.toByteArray();
		}
		return null;
	}

	/**
	 * 把byte转文件
	 * @param path
	 * @param b
	 * @throws Exception
	 */
	public void write(String path, byte[] b) throws Exception {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream outStream;
		outStream = new FileOutputStream(file);
		outStream.write(b);
		outStream.close();
	}

	/**
	 * 读取文件名.txt内容
	 * @param path:文件路径
	 * @return:每行的文件内容组成的List集合
	 */
	public List readContent(String path) {
		//输入,isSystemWindows:是否是windows系统;
		boolean isSystemWindows = getOS().equals("windows");
		//txtPath:路径指向的.txt文件
		String txtPath = path.substring(0, path.length() - 3) + "txt";
		System.out.println(isSystemWindows + "==new===>" + txtPath);
		//contentArray:解析后的文件内容
		String[] contentArray = null;
		//输出
		List list = new ArrayList();
		FileInputStream fis = null;
		try {
			File textFile = new File(txtPath);
			fis = new FileInputStream(textFile);
			byte[] buf = new byte[fis.available()];
			fis.read(buf);
			String strContent = "";

			String str = new String(buf);
			boolean isOpenedByNotePad = !str.startsWith("REQUEST");
			String str1 = new String(buf, "unicode");
			//在linux下,GB2312需要转成UTF-8,是UTF-8需要转成GB2312,但是打开方式是记事本的话,UTF-8还是UTF-8
//			String charset = this.getCharset(txtPath, 500);
			String charset = this.get_charset(textFile);
			if (isOpenedByNotePad && !isSystemWindows) {
				if (charset.equals("GB2312")) {
					charset = "UTF-8";
				}
			}
			System.out.println("charset ==>" + charset);
			
			if (charset.equals("GB2312") || charset.equals("GBK")|| charset.equals("ANSI")) {
				if (isSystemWindows) {
					contentArray = str.split("\n");
					for (int i = 0; i < contentArray.length; i++) {
						list.add(contentArray[i]);
					}
				} else {
					list = this.readContentByCharset(path, "GBK");
				}
			} else if (charset.equals("UTF-8")) { //UTF-8编码
				//System.out.println("charset ==>"+charset+"\n" + str2);				
				list = this.readContentByCharset(path, "UTF-8");
			} else if (charset.equals("UNICODE")) { //UNICODE编码
				//System.out.println("charset ==>"+charset+"\n" + str1); 
				strContent = str1;
				if (isSystemWindows) {
					contentArray = strContent.split("\n");
				} else {
					contentArray = strContent.split("\r\n");
				}
				for (int i = 0; i < contentArray.length; i++) {
					list.add(contentArray[i]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				LogTool.error(this.getClass(), e);
			}
		}
		return list;
	}

	public void delFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 复制文件
	 * @param oldPath 要复制的文件的绝对路径
	 * @param newPath 新文件的绝对路径
	 * @throws Exception 
	 */
	public void copyFile(String oldPath, String newPath) {
		FileInputStream in = null;
		try {
			in = new FileInputStream(oldPath);
			byte buffer[] = read(in);
			write(newPath, buffer);
		} catch (Exception e) {
			LogTool.error(this.getClass(), e);
		}
		finally{
			try{
				if(in != null)				
					in.close();
			}catch (Exception e) {
				LogTool.error(this.getClass(), e);
			}
		}
		
	}

	public void writeText(String path, String value) throws IOException {
		File f = new File(path);
		FileOutputStream fos = new FileOutputStream(f, true);
		fos.write(value.getBytes("gbk"));
		fos.close();
	}

	public List readContentByCharset(String path, String charset) {
		String content = "";
		ArrayList list = new ArrayList();
		System.out.println("readContentByCharset==>" + charset);
		BufferedReader reader = null;
		try {
			if (charset == "") {
				reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(path)));
			} else {
				reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(path), charset));
			}
			while ((content = reader.readLine()) != null) {
				list.add(content);
			}
		} catch (IOException e) {
			LogTool.error(this.getClass(), e);
		} finally{
			try {
				if (reader!=null) {
					reader.close();
				}
			} catch (IOException e) {
				LogTool.error(this.getClass(), e);
			}
		}
		return list;
	}

	/*
	 * return:获取文件编码方式
	 * path:文件路径
	 * readLength:读取文件长度
	 */
	public String getCharset(String path, int readLength) {
		FileInputStream fis = null;
		//输出
		String charset = "";
		//输入,charsetArray:字符集数组
		String[] charsetArray = new String[] { "GB2312", "GBK", "UNICODE",
				"UNICODE BIG ENDIAN", "ISO-8859-1", "UTF-8", "ANSI" };
		try {
			fis = new FileInputStream(new File(path));
			//文件没有给到长度长,就直接读取文件全部
			if (fis.available() < readLength)
				readLength = fis.available();
			byte[] buf = new byte[readLength];
			fis.read(buf);

			String str = new String(buf);
			String strUNICODE = new String(buf, "UNICODE");

			for (int i = 0; i < charsetArray.length; i++) {
				if (str.equals(new String(str.getBytes(charsetArray[i]),
						charsetArray[i]))) {
					charset = charsetArray[i];
					break;
				}
			}
			//如果用UNICODE,字符串没有长度,就可以判断是UTF-8
			if (charset.equals("UNICODE") && strUNICODE.length() == 0) {
				charset = "UTF-8";
			}
		} catch (Exception e) {
			LogTool.error(this.getClass(), e);
			charset = "ANSI";//无法parse编码的话,直接认为是ANSI
		}finally{
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				LogTool.error(this.getClass(), e);
			}
		}
		System.out.println("getCharset1==>" + charset);
		//linux系统的话,GB2312和UTF-8正好相反,所以需要转换
		if (!getOS().equals("windows")) {
			if (charset.equals("UTF-8"))
				charset = "GB2312";
			else if (charset.equals("GB2312") || charset.equals("gbk")
					|| charset.equals("ANSI"))
				charset = "UTF-8";
		}
		System.out.println("getCharset2==>" + charset);
		
		return charset;
	}


	/**
	 * 根据文本的前两个字节来定义其编码格式的。定义如下：
	 *  ANSI：　　　　　　　　无格式定义；
	 *Unicode： 　　　　　　前两个字节为FFFE；
  	 *Unicode big endian：　前两字节为FEFF；　 
  	 *UTF-8：　 　　　　　　前两字节为EFBB；
	 * @param file
	 * @return
	 */
	public String get_charset( File file ) {   
        String charset = "GBK";   
        byte[] first3Bytes = new byte[3];  
        BufferedInputStream bis = null;
        try {   
            boolean checked = false;   
            bis = new BufferedInputStream( new FileInputStream( file ) );   
            bis.mark( 0 );   
            int read = bis.read( first3Bytes, 0, 3 );   
            if ( read == -1 ) return charset;   
            if ( first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE ) {   
                charset = "UTF-16LE";   
                checked = true;   
            }   
            else if ( first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF ) {   
                charset = "UTF-16BE";   
                checked = true;   
            }   
            else if ( first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF ) {   
                charset = "UTF-8";   
                checked = true;   
            }   
            bis.reset();   
            if ( !checked ) {   
            //    int len = 0;   
                int loc = 0;   
  
                while ( (read = bis.read()) != -1 ) {   
                    loc++;   
                    if ( read >= 0xF0 ) break;   
                    if ( 0x80 <= read && read <= 0xBF ) // 单独出现BF以下的，也算是GBK   
                    break;   
                    if ( 0xC0 <= read && read <= 0xDF ) {   
                        read = bis.read();   
                        if ( 0x80 <= read && read <= 0xBF ) // 双字节 (0xC0 - 0xDF) (0x80   
                                                                        // - 0xBF),也可能在GB编码内   
                        continue;   
                        else break;   
                    }   
                    else if ( 0xE0 <= read && read <= 0xEF ) {// 也有可能出错，但是几率较小   
                        read = bis.read();   
                        if ( 0x80 <= read && read <= 0xBF ) {   
                            read = bis.read();   
                            if ( 0x80 <= read && read <= 0xBF ) {   
                                charset = "UTF-8";   
                                break;   
                            }   
                            else break;   
                        }   
                        else break;   
                    }   
                }   
                //System.out.println( loc + " " + Integer.toHexString( read ) );   
            }   
  
            bis.close();   
        } catch ( Exception e ) {   
        	LogTool.error(this.getClass(), e);
        } finally {
        	try {
	        	if (bis != null) {
					bis.close();
				}
        	} catch (IOException e) {
        		LogTool.error(this.getClass(), e);
        	}
        } 
  
        return charset;   
    }   
	
	
	
	public static void main(String[] args) throws Exception
	 {
	   String path = "I:\\DESK-TOP\\TEMP\\test-太平无忧私家车意外保障.xml";
//	 //new AdminBatFileService().readTxt("C:\\Users\\Administrator\\Desktop\\request1.csv");
//	 //new AdminBatFileService().readContent("C:\\Users\\Administrator\\Desktop\\request1.csv");
//	   System.out.println(FilesUtil.get_charset(new File(path)));
//	   System.out.println(new FilesUtil().readContent(path).get(2));
	   InputStream inputStream = new FileInputStream(new File(path));
	   String str = new String(new FilesUtil().read(inputStream),"gbk");
	   System.out.println(str);
	 }

}
