package com.springBoot1.tool;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class IOTool
{
	/**
	 * 功能:删除文件夹中的文件,文件夹(或文件夹本身)
	 * dir:文件或文件夹
	 * delDir	true:删除目录下的文件和文件夹;false:只删除文件
	 * retainRootDir   true:保留目录本身   false:不保留
	 */
	public static boolean delFilesDirectories(String dir, boolean delDir, boolean retainRootDir)
	{
		boolean flag = removeFileDirectoryInDir(dir, delDir);//true:成功进行删除操作
		if (delDir) {
			if (flag && retainRootDir)
				if (dir.indexOf(".") == -1)//不是文件夹的话,创建该文件夹
				{
					File file = new File(dir);
					file.mkdir();
				}
		}
		return flag;
	}

	//中转方法
	public static boolean removeFileDirectoryInDir(String dir, boolean delDir)
	{
		boolean flag = true;
		File file = new File(dir);
		if (!file.exists()) {
			System.out.println(dir + "该文件不存在,删除文件失败");
			flag = false;
		} else {
			if (file.isFile())
				flag = removeFile(dir);
			else
				flag = removeDir(dir, delDir);
		}
		return flag;
	}

	//核心方法一:删除目录下的文件,if(delDir == true),连着目录一起删除
	public static boolean removeDir(String dir, boolean delDir)
	{
		boolean flag = true;

		File dirfile = new File(dir);
		if (!dir.endsWith(File.separator)) {
			dir = dir + File.separator;
		}
		if (!(dirfile.isDirectory()) || !(dirfile.exists())) {
			System.out.println("删除目录失败,该目录不存在......!");
			flag = false;
		} else {
			File[] files = dirfile.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					flag = removeFile(files[i].getAbsolutePath());
					if (!flag)
						break;
				}
				if (files[i].isDirectory()) {
					flag = removeDir(files[i].getAbsolutePath(), delDir);
					if (!flag)
						break;
				}
			}
			if (delDir) {
				if (dirfile.delete())
					System.out.println("删除" + dir + "成功!");
				else
					System.out.println("删除" + dir + "失败!");
			}
		}
		return flag;
	}

	//核心方法二:删除文件
	public static boolean removeFile(String dir)
	{
		boolean flag = true;
		File file = new File(dir);
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				System.out.println(dir + "删除文件成功!");
			} else {
				System.out.println(dir + "删除文件失败!");
				flag = false;
			}
		} else {
			System.out.println(dir + "该文件不存在,删除文件失败");
			flag = false;
		}
		return flag;
	}

	/**
	 * 路径目录不错存在的话,创建目录
	 */
	public static void createDir(String path)
	{
		if (!new File(path).exists()) {
			new File(path).mkdirs();
		}
	}

	/**
	 * 获取文件大小
	 */
	public static String getFileSize(String file, String decimal)
	{
		//1,申明输入输出参数
		long fileSize = 0;
		String filePath = "";//文件路径
		File fileTemp = null;//临时文件对象
		//2,如果文件不是以'/'开头,就添加'/'				
		try {
			//如果是绝对路径
			if (new File(file).exists()) {
				fileTemp = new File(file);
			}
			//如果是项目中的文件
			else {
				if (!file.startsWith("/"))
					file = "/" + file;
				filePath = IOTool.class.getResource(file).getPath();
				fileTemp = new File(filePath);
			}
			fileSize = fileTemp.length();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formatFileSize(fileSize, decimal);
	}

	/**
	 * 转换文件大小
	 * decimal如:#.00
	 */
	public static String formatFileSize(long fileSize, String decimal)
	{
		DecimalFormat df = new DecimalFormat(decimal);
		String fileSizeString = "";
		if (fileSize < 1024) {
			fileSizeString = df.format((double) fileSize) + "B";
		} else if (fileSize < 1048576) {
			fileSizeString = df.format((double) fileSize / 1024) + "K";
		} else if (fileSize < 1073741824) {
			fileSizeString = df.format((double) fileSize / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileSize / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 将数据保存到指定的文件中
	 * outFileDir:文件路径,如:"d:\\out.txt";
	 * dataList:数据源
	 * isTable:输出的数据间以一个table键为间隔单位
	 * isLine :输出的数据间以换行为间隔单位
	 * isTable,isLine都为false的话,以一个空格为间隔单位
	 */
	public static void saveListDataToFile(String outFileDir, List dataList, boolean isTable, boolean isLine)
	{
		OutputStream out = null;
		DataOutputStream dataOut = null;
		try {
			out = new FileOutputStream(outFileDir);
			dataOut = new DataOutputStream(out);
			for (int i = 0; i < dataList.size(); i++) {
				String s = "个" + String.valueOf(dataList.get(i));
				String str = new String(s.getBytes("gbk"), "ISO8859-1");
				System.out.println(s);
				if (isTable || (isTable && isLine)) {
					dataOut.writeBytes(str + "\t");
				} else if (isLine) {
					dataOut.writeBytes(str + "\n");
				} else if (!isTable && !isLine) {
					dataOut.writeBytes(str + " ");
				}
			}
			dataOut.close();
		} catch (IOException e) {
			LogTool.error(IOTool.class, e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (dataOut != null) {
					dataOut.close();
				}
			} catch (Exception e2) {
				LogTool.error(IOTool.class, e2);
			}
		}
	}

	/*
	 * 获取文件类型
	 */
	public static String getFileType(String filePath)
	{
		int pos = filePath.lastIndexOf(".");
		String fileType = filePath.substring(pos + 1);
		return fileType;
	}

	/*
	 * 获取文件名
	 */
	public static String getFileName(String filePath)
	{
		int pos = filePath.lastIndexOf("\\");
		return filePath.substring(pos + 1);
	}

	/*
	 * 获取文件夹下面的所有"文件"
	 */
	public static List getfilelist(String paths)
	{
		List fileList = new ArrayList();
		File d = new File(paths);

		File[] lists = d.listFiles();

		for (int i = 0; i < lists.length; i++) {
			if (lists[i].isFile()) {
				fileList.add(lists[i]);
			}
		}
		return fileList;
	}

	/*
	 * 获取文件夹下面的所有"文件夹"
	 */
	public static List getDirlist(String paths)
	{
		List directoryList = new ArrayList();
		File d = new File(paths);

		File lists[] = d.listFiles();

		for (int i = 0; i < lists.length; i++) {
			if (lists[i].isDirectory()) {
				directoryList.add(lists[i].getName().toString());
			}
		}
		return directoryList;
	}

	/*
	 * 删除文件夹下面的所有"文件"
	 */
	public static void deleteFile(String path)
	{
		File d = new File(path);
		File lists[] = d.listFiles();

		for (int i = 0; i < lists.length; i++) {
			if (lists[i].isFile()) {
				lists[i].delete();
			}
		}
	}

	/*
	 * 删除文件夹下面的文件名为:filename的"文件"
	 */
	public static void deleteFile(String dirPath, String filename)
	{
		File d = new File(dirPath);
		File lists[] = d.listFiles();
		for (int i = 0; i < lists.length; i++) {
			if (lists[i].getName().equals(filename))
				lists[i].delete();
		}
	}

	/*
	 * 从读取file.property文件的特定属性:propertyName	
	 */
	public static String getProperty(String file, String propertyName)
	{
		InputStream is = null;
		//1,申明输入输出参数
		String propertyValue = "";
		Properties prop = null;
		Map propertiesMap = new HashMap();
		//2,如果文件不是以'/'开头,就添加'/'
		if (!file.startsWith("/"))
			file = "/" + file;
		//3,读取文件入内存,如果文件已入内存就不读取,直接读取数据		
		if (propertiesMap.get(file) == null) {
			prop = new Properties();
			
			try {
				is = new FileInputStream(IOTool.class.getResource(file).getPath());
				prop.load(is);
			} catch (IOException e) {
				LogTool.error(IOTool.class, e);
			} finally {
				if (is!=null) {
					try {
						is.close();
					} catch (IOException e) {
						LogTool.error(IOTool.class, e);
					}
				}
			}
			propertiesMap.put(file, prop);
		}
		//4,读取数据
		propertyValue = ((Properties) propertiesMap.get(file)).getProperty(propertyName, "UTF-8");
		return propertyValue;
	}
	
	public static List readTxtByCharset(String path, String charset)
	{
		//输出参数:txt内容
		List contentList = new ArrayList();
		//临时变量:存放txt的部分内容
		String content = "";
		System.out.println("readContentByCharset==>" + charset);
		BufferedReader reader = null;

		try {
			if (charset == "") {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			} else {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), charset));
			}
			while ((content = reader.readLine()) != null) {
				contentList.add(content);
			}
		} catch (IOException e) {
			LogTool.error(IOTool.class, e);
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (Exception e) {
				LogTool.error(IOTool.class, e);
			}
		}
		return contentList;
	}

	/**
	 * 写入txt文件
	 * isNotOverwrite: 
	 *     true 接着最后一行继续写入
	 *     false 重新写入
	 */
	public static void writeIntoTxt(String file,String str,boolean isNotOverwrite,String charset)
	{
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(new File(file), isNotOverwrite);			
			try {
				//charset转码后写入
				outputStream.write((str + "\n").getBytes(charset));
			} catch (Exception e) {
				//如果转码不支持就直接写入,不转码
				outputStream.write((str + "\n").getBytes());
			}
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Exception e) {
				System.out.println("输出流异常:" + file);
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取文件夹下的所有文件fileType的文件路径
	 */
	public static List readAllFiles(String filepath, String fileType)
	{
		//输出参数
		List filePathList = new ArrayList(); //所有文件fileType的文件路径 集合
		try {
			File file = new File(filepath);
			if (!file.isDirectory()) {
				System.out.println("文件");
				System.out.println("path=" + file.getPath());
				System.out.println("absolutepath=" + file.getAbsolutePath());
				System.out.println("name=" + file.getName());
			} else if (file.isDirectory()) {
				System.out.println("文件夹");
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File readfile = new File(filepath + "\\" + filelist[i]);
					if (!readfile.isDirectory()) {
						String ext = readfile.getName().substring(readfile.getName().length() - 3, readfile.getName().length());
						System.out.println("ext:" + ext);
						if (fileType.indexOf(ext) > -1) {
							filePathList.add(readfile.getAbsolutePath());
							System.out.println("path=" + readfile.getPath());
							System.out.println("absolutepath=" + readfile.getAbsolutePath());
							System.out.println("name=" + readfile.getName());
						}
					} else if (readfile.isDirectory()) {
						filePathList.addAll(readAllFiles(filepath + "\\" + filelist[i], fileType));
					}
				}
			}
		} catch (Exception e) {
			System.out.println("readfile()   Exception:" + e.getMessage());
			e.printStackTrace();
		}
		return filePathList;
	}
	
	/**
	 * 转移oldFilDir下的所有fileType类型的文件到newFileDir
	 * @param oldFilDir
	 * @param newFileDir
	 * @param fileType
	 */
	public static void moveFilesToDir(String oldFilDir,String newFileDir,String fileType)
	{
		String[] types = fileType.split(",");
		List files = null;
		
		for (int i = 0; i < types.length; i++) {
			files = readAllFiles(oldFilDir, fileType);
		}
		
		for (int i = 0; i < files.size(); i++) {
			String newFileName = (String)files.get(i);
			newFileName = newFileName.substring(newFileName.lastIndexOf("\\")+1);
			new FileStreamTool().copyFile( (String)files.get(i), newFileDir+newFileName);
		}
	}

	public static void moveFileNamesToDir(String oldFilDir,String newFileDir,String fileNames)
	{
		List files = null;
		
		files = readAllFiles(oldFilDir, "pdf");
		List a = new ArrayList();
		String[] array = fileNames.split(",");
		for (int i = 0; i < files.size(); i++) {
			String newFileName = (String)files.get(i);
			newFileName = newFileName.substring(newFileName.lastIndexOf("\\")+1);
			for (int j = 0; j < array.length; j++) {
				String temp = array[j];
				if (newFileName.indexOf(temp)>-1) {
					new FileStreamTool().copyFile( (String)files.get(i), newFileDir+newFileName);
					a.add(newFileName);
				}
			}
		}
		
		
		List all = new ArrayList();
		for (int i = 0; i < array.length; i++) {
			all.add(array[i]);
		}
		
		List bbb = new ArrayList();
		for (int j = 0; j < array.length; j++) {
			String temp = (String)array[j];
			for (int k = 0; k < a.size(); k++) {
				String strTemp = (String)a.get(k);
				if (strTemp.indexOf(temp) > -1) {
					System.out.print(temp+"---------------------------------,");
					bbb.add(temp);
					
				}
				
			}
			
			
		}
		
		all.removeAll(bbb);
		
		System.out.println();
		System.out.println(all);
	
		
	}
	
	
	public static String getPayDate(String str){
		String[] array = str.split("-");
		StringBuffer sb = new StringBuffer();
		sb.append(array[0]+"-");
		sb.append(array[1]+"-");
		sb.append(array[2]+" ");
		sb.append(array[3]);
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	/**
	 * 读取文件内容字符串
	 * @param path 文件路径
	 * @param encode 编码
	 * @return 文件内容
	 */
	public static String readFile(String path,String encode){
		String content = "";
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(path));
			content = new String(FilesUtil.read(inputStream),encode);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			System.out.println("UnsupportedEncodingException encode:"+encode+"\n");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != inputStream) {
					inputStream.close();
				}
			} catch (Exception e2) {
				LogTool.error(IOTool.class, e2);
			}
		}
		
		return content;
	}
//	public static void main(String[] args)
//	{
		//		delFilesDirectories("D:\\out\\in\\outin", true,false);
		//		String path = "d:/a/admin/temp/";
		//		createDir(path);

		//		String outFileDir = "d:\\out.txt";
		//		List list = new ArrayList();
		//		for (int i = 0; i < 10; i++)
		//		{
		//			list.add(String.valueOf(i));
		//		}
//				saveListDataToFile(outFileDir, list, true, false);
//				String s2 = getProperty("common.properties", "url");
//			    System.out.println(s2);
//			    String path = "C:\\Users\\Administrator\\Desktop\\hhh.sql";
//			    //System.out.println(new IOTool().getCharset(path,500));
//			    List list = new IOTool().readContent(path);
//			    
//			    for (int i = 0; i < list.size(); i++) {
//			    	String strLine =  String.valueOf(list.get(i));
//			    	strLine = StringTool.removeAll(strLine, "/*", "--");	    	
//					System.out.println(strLine);
//				}
//			    String str = "000000000000000000000";
//				new IOTool().writeIntoTxt(path, str, false);
//		String path = "C:\\Users\\Administrator\\Desktop\\utf8.txt";//"common.properties";//
//		String path = "C:\\Users\\Administrator\\Desktop\\utf8.txt";
//
//		System.out.println(IOTool.getFileSize(path,"###.##"));
//		String path = "C:\\Users\\Administrator\\Desktop\\login.jsp";
//		System.out.println(IOTool.readTxtByCharset(path, "gbk"));
//		IOTool.getFileCharset(path, 500);
//
//		System.out.println(IOTool.getTxtContentInList(path,500));

	     
		/**
		 * 读取所有文件,并将文件里的logCount.jsp行去掉,用<jsp:include page=\"/iweb/include/feet.jsp\"></jsp:include> 替换
		 * @param args
		
		public static void main(String[] args)
		{
			String include = "<jsp:include page=\"/iweb/include/feet.jsp\"></jsp:include>";
			String temp = "";
			String path = "C:\\Users\\Administrator\\Desktop\\cc";
			
			List list = IOTool.readAllFiles(path,"jsp");
			Map pathAndContentMap = new HashMap();
			Map isNeedToHandleMap = new HashMap();
			for (int i = 0; i < list.size(); i++) {
				boolean isNeedToHandle = false;
				String pathString = (String)list.get(i);
				List list2 = IOTool.getTxtContentInList(pathString, 500);
				for (int j = 0; j < list2.size(); j++) {
					temp = list2.get(j).toString();
					if (temp.indexOf("feet.jsp") > -1 || temp.indexOf("logCount.jsp") > -1) {
						isNeedToHandle = true;
						list2.set(j, "");
					}
				}
				for (int j = 0; j < list2.size(); j++) {
					temp = list2.get(j).toString();
					if (temp.indexOf("</body>") > -1 && isNeedToHandle) {
						list2.set(j, include + "\n</body>");
					}
				}
				pathAndContentMap.put(pathString, list2);
				if (pathAndContentMap.get(pathString+"1") == null) {
					String string = String.valueOf(isNeedToHandle);
					isNeedToHandleMap.put(pathString+"1", string);
				}
			}				
			Iterator itOutputStream = pathAndContentMap.keySet().iterator();
			while (itOutputStream.hasNext()) {
				String filePathKey =  (String)itOutputStream.next();
				if (isNeedToHandleMap.get(filePathKey+"1").equals("true")) {
					List OutputStreamList = (List)pathAndContentMap.get(filePathKey);
					for (int k = 0; k < OutputStreamList.size(); k++) {
						String tempString = (String)OutputStreamList.get(k);
						if (k == 0) {
							IOTool.writeIntoTxt(filePathKey,tempString, false, "utf-8");	
						}else {
							IOTool.writeIntoTxt(filePathKey,tempString, true, "utf-8");
						}	
					}
				}		
			}
		}	
		 * @throws IOException 
		 * @throws SQLException 
		
		
		 */
		//--------------------------------END--------------------------------
	
	
//	public static void main(String[] args) throws IOException
//	{
//		//输出参数:txt内容
//		List contentList = new ArrayList();
//		//临时变量:存放txt的部分内容
//		String content = "";
//		BufferedReader reader = null;
//		String path = "I:/DESK-TOP/datetool.txt";
//		String writeIntoTxt_path = "I:/DESK-TOP/datetool2.txt";
//		if (!new File(writeIntoTxt_path).exists()) {
//			new File(writeIntoTxt_path).createNewFile();
//		}
//		try {
//			reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
//			while ((content = reader.readLine()) != null) {
//				String temp = StringTool.replaceBlank(content.trim());
//				if (!RegExpTool.vaidate(StringTool.replaceBlank(content.trim()), RegExpTool.allNumRegex)) {
//					IOTool.writeIntoTxt(writeIntoTxt_path,content, true, "utf-8");
//				}
//				
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (reader != null)
//					reader.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}		
//	}

//	public static void main(String[] args)
//	{
//		String oldFilDir = "C://Documents and Settings//Administrator//桌面//guangxi-policy";
//		String newFileDir = "C://Documents and Settings//Administrator//桌面//policy//";
//		
//		String newest = "C://Documents and Settings//Administrator//桌面//policy-newest//";
//		
//		IOTool.moveFilesToDir(oldFilDir,newFileDir,"pdf");
//		String pdfNames = "001057226369327,001057226476327,001057353763327,001057732608327,001057763644327,001057763751327,001057763858327,001057763965327,001057764072327,001057764179327,001057764286327,001057764393327,001057764403327,001057764510327,001057764617327,001057764724327,001057764831327,001057764938327,001057765045327,001057765152327,001057765259327,001057765366327,001057765473327,001057765580327,001057765687327,001057765794327,001057765804327,001057765911327,001057766018327,001057766125327,001057766232327,001057766339327,001057766446327,001057766553327,001057766660327,001057766767327,001057766874327,001057766981327,001057767088327,001057767195327,001057767205327,001057767312327,001057767419327,001057767526327,001057767633327,001057892225327,001057892332327,001057892439327,001057892546327,001057892653327,001057892760327,001057892867327,001057892974327,001057893081327,001057893188327,001057893295327,001057893305327,001057893412327,001057893519327,001057893626327,001057893733327,001057893840327,001057893947327,001057894054327,001057894161327,001057894268327,001057894375327,001057894482327,001057894589327,001057894696327,001058178263327,001058178370327,001058178477327,001058178584327,001058178691327,001058178798327,001058178808327,001058178915327,001058179022327,001058179129327,001058179236327,001058179343327,001058179450327,001058179557327,001058179664327,001058179771327,001058179878327,001058179985327,001058180092327,001058180102327,001058180209327,001058180316327,001058180423327,001058180530327,001058180637327,001058180744327,001058180851327,001058180958327,001058181065327,001058181172327,001058181279327,001058181386327,001058181493327,001058181503327,001058181610327,001058181717327,001058181824327,001058181931327,001058182038327,001058182145327,001058182252327,001058182359327,001058182466327,001058182573327,001058182680327,001058276236327,001058278065327,001058307272327,001058307379327,001058307486327,001058307593327,001058307603327,001058307710327,001058307817327,001058307924327,001058308031327,001058308138327,001058308245327,001058308352327,001058308459327,001058308566327,001058308673327,001058308780327,001058308887327,001058308994327,001058309004327";
//		IOTool.moveFileNamesToDir(newFileDir,newest,pdfNames);
//		String c = "001057354084327, 001057354308327, 001057354415327, 001057354522327, 001057354629327, 001057354736327, 001057354843327, 001057762457327, 001057762564327, 001057762671327, 001057762778327, 001057762885327, 001057762992327, 001057763002327, 001057763109327, 001057763216327, 001057763323327, 001057763430327, 001057763537327, 001057354191327, 001057354298327";
//		String[] arr = c.split(",");
//		System.out.println(arr.length);
//	}
	
//	public static void main(String[] args) throws IOException, SQLException
//	{
//		//输出参数:txt内容
//		List contentList = new ArrayList();
//		//临时变量:存放txt的部分内容
//		String content = "";
//		BufferedReader reader = null;
//		List saveModelList = new ArrayList<IOToolModel>();
//		List sqlList = new ArrayList<String>();
//		String path = "I:/DESK-TOP/TEMP/5.8-5.10订单汇总2.csv";
//		String writeIntoTxt_path = "I:/DESK-TOP/TEMP/5.8-5.10订单汇总3.csv";
//		if (!new File(writeIntoTxt_path).exists()) {
//			new File(writeIntoTxt_path).createNewFile();
//		}
//		try {
//			
//			reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "GBK"));
//			while ((content = reader.readLine()) != null) {
//				content = StringTool.replaceBlank(content);
//				System.out.println(content);
//				String[] array = content.split(",");
//				IOToolModel ioToolModel = new IOToolModel();
//				ioToolModel.setTaobao_trans_id(array[0]);
//				ioToolModel.setTaobao_buyer_name(array[1]);
//				ioToolModel.setAlipay_username(array[2]);
//				ioToolModel.setActual_paid_amount(new Double(array[3]));
//				String pay_date = getPayDate(array[4]);
//				ioToolModel.setPay_date(pay_date);
//				ioToolModel.setBaobei_name(array[5]);
//				ioToolModel.setBaobei_num(new Integer(array[6]));
//				saveModelList.add(ioToolModel);
//			}
//			
//			BaseDB db = new BaseDB();
//			Connection conn = db.getConnection();
//			for (int i = 0; i < saveModelList.size(); i++) {
//			
//				IOToolModel ioToolModel = (IOToolModel)saveModelList.get(i);
//				String sql = "insert into test_taobao_inf values('"+ioToolModel.getTaobao_trans_id()+"','"+ioToolModel.getTaobao_buyer_name()+"','"+ioToolModel.getAlipay_username()+"',"+ioToolModel.getActual_paid_amount()+",to_date('"+ioToolModel.getPay_date()+"','yyyy-mm-dd HH24:MI:SS'),'"+ioToolModel.getBaobei_name()+"',"+ioToolModel.getBaobei_num()+")";
//				System.out.println(sql);
//				conn.createStatement().execute(sql);
//				if (i%100 == 0) {
//					conn.close();
//					db = new BaseDB();
//					conn= db.getConnection();
//				}
//				
//		
//			}
//			
//			conn.createStatement().close();
//			conn.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (reader != null)
//					reader.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}		
//		
//		
////		Date date = getPayDate("2013-5-10-23:58:01");
////		System.out.println(DateTool.getDateStrByFormat(date, "yyyy-MM-dd HH:mm:ss"));
//	}
	
	public static void main(String[] args) throws IOException, SQLException
	{
		
	}
}
