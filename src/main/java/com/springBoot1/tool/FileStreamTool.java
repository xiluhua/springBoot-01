package com.springBoot1.tool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileStreamTool {
	
	/**
	 * 复制文件
	 * @param oldPath 要复制的文件的绝对路径
	 * @param newPath 新文件的绝对路径
	 * @throws Exception 
	 */
	public void copyFile(String oldPath,String newPath){
		try {
			FileInputStream in = new FileInputStream(oldPath);
			byte  buffer[] = read(in);
			write(newPath, buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	/**
	 * 文件转换成流，返回流
	 * @param xmlStream
	 * @return
	 * @throws Exception 
	 */
	public byte[] parseFileToStream(String path) throws Exception{
		FileInputStream in = new FileInputStream(path);
		byte  buffer[] = read(in);
		return buffer;
	}
	
	/**
	 * 使用ByteArrayOutputStream将流变成字节
	 * @param in InputStream,输入流
	 * @return 字节数组
	 * @throws Exception
	 */
	public static byte[] read(InputStream in) {  
		ByteArrayOutputStream out = new ByteArrayOutputStream();  
		try {
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
		} catch (Exception e) {
			LogTool.error(FileStreamTool.class, e);
		} finally {
			try {
				if (in!=null) {
					in.close();
				}
				if (out!=null) {
					out.close();
				}
			} catch (Exception e2) {
				LogTool.error(FileStreamTool.class, e2);
			}
		}
		
		return null;  
	} 
	/**
	 * 把byte转文件
	 * @param path
	 * @param b
	 * @throws Exception
	 */
    public static void write(String path,byte[] b) throws Exception {  
		File file = new File(path);
		if(file.exists()){
			file.delete();
		}
		FileOutputStream outStream;
		outStream = new FileOutputStream(file);
		outStream.write(b);
		outStream.close();
	 }
	
	

}
