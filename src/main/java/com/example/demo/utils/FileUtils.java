package com.example.demo.utils;

import java.io.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletResponse;

public class FileUtils extends org.apache.commons.io.FileUtils {

	private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

	/**
	 * 删除文件夹
	 * @param dir
	 */
	public static void deleteDir(File dir){
		try {
			if(dir.isDirectory()){
				File[] files = dir.listFiles();
				for(int i=0; i<files.length; i++) {
					deleteDir(files[i]);
				}
			}
			dir.delete();
		}catch (Exception e){
			logger.info("删除文件夹异常"+e.getMessage());
		}
	}

	/**
	 * 浏览器导出文件
	 * @param response
	 * @param path 文件路径
	 */
	public void fileExport(HttpServletResponse response, String path) {
		File localFile = new File(path);
		if(!localFile.exists()) {
			return;
		}
		InputStream stream = null;
		OutputStream os=null;
		/* 设置文件ContentType类型，这样设置，会自动判断下载文件类型 */
		response.setContentType("multipart/form-data");
		/* 设置文件头：最后一个参数是设置下载文件名 */
		response.setHeader("Content-Disposition", "attachment;filename="+localFile.getName());
		try{
			stream=new FileInputStream(localFile);
			os = response.getOutputStream();
			byte[] b = new byte[1024];
			int len;
			while((len = stream.read(b)) > 0){
				os.write(b,0,len);
			}
			os.flush();
		}catch (IOException ioe){
			ioe.printStackTrace();
		}finally {
			try {
				os.close();
				stream.close();
			} catch (IOException e) {
				logger.info("文件流关闭异常"+e.getMessage());
			}
		}

	}

	/**
	 * 获取文件中内容（行）
	 * @param path
	 * @return
	 */
	public static List<String> getFileContent(String path){
		FileInputStream fis=null;
		try {
			List mPaths = new ArrayList<>();
			File file = new File( path);
			if(!file.exists()){
				return null;
			}
			fis= new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
			String line;
			while ((line = br.readLine()) != null) {
				mPaths.add(line);
			}
			return mPaths;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * copy文件夹下所有内容
	 * @param path
	 * @param copyPath
	 * @throws IOException
	 */
	public static void copy(String path, String copyPath) throws IOException {
		File filePath = new File(path);
		if(filePath.isDirectory()){
			File[] list = filePath.listFiles();
			for(int i=0; i<list.length; i++){
				String newPath = path + File.separator + list[i].getName();
				String newCopyPath = copyPath + File.separator + list[i].getName();
				File newFile = new File(copyPath);
				if(!newFile.exists()){
					newFile.mkdirs();
				}
				copy(newPath, newCopyPath);
			}
		}else if(filePath.isFile()){
			org.apache.commons.io.FileUtils.copyFile(filePath,new File(copyPath));
		}
	}

	/**
	 * 复制某个文件
	 * @param srcPathStr 文件路径
	 * @param desPathStr 移动的文件夹名称(无需包含文件名)
	 */
	public  void copyfile(String srcPathStr, String desPathStr){
		//获取源文件的名称
		String newFileName = srcPathStr.substring(srcPathStr.lastIndexOf("\\")+1); //目标文件地址
		System.out.println("源文件:"+newFileName);
		desPathStr = desPathStr + File.separator + newFileName; //源文件地址
		System.out.println("目标文件地址:"+desPathStr);
		try{
			FileInputStream fis = new FileInputStream(srcPathStr);//创建输入流对象
			FileOutputStream fos = new FileOutputStream(desPathStr); //创建输出流对象
			byte datas[] = new byte[1024*8];//创建搬运工具
			int len = 0;//创建长度
			//循环读取数据
			while((len = fis.read(datas))!=-1){
				fos.write(datas,0,len);
			}
			fis.close();//释放资源
			fis.close();//释放资源
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * zip压缩
	 * @param srcDir
	 * @param out
	 * @param KeepDirStructure
	 * @throws RuntimeException
	 */
	public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure)
			throws RuntimeException{

		long start = System.currentTimeMillis();
		ZipOutputStream zos = null ;
		try {
			zos = new ZipOutputStream(out);
			File sourceFile = new File(srcDir);
			compress(sourceFile,zos,sourceFile.getName(),KeepDirStructure);
			long end = System.currentTimeMillis();
			logger.info("压缩完成，耗时：" + (end - start) +" ms");
		} catch (Exception e) {
			throw new RuntimeException("zip error from ZipUtils",e);
		}finally{
			if(zos != null){
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 递归压缩方法
	 * @param sourceFile 源文件
	 * @param zos		 zip输出流
	 * @param name		 压缩后的名称
	 * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构;
	 * 							false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 * @throws Exception
	 */
	private static void compress(File sourceFile, ZipOutputStream zos, String name,
								 boolean KeepDirStructure) throws Exception{
		byte[] buf = new byte[1024];
		if(sourceFile.isFile()){
			// 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
			zos.putNextEntry(new ZipEntry(name));
			// copy文件到zip输出流中
			int len;
			FileInputStream in = new FileInputStream(sourceFile);
			while ((len = in.read(buf)) != -1){
				zos.write(buf, 0, len);
			}
			// Complete the entry
			zos.closeEntry();
			in.close();
		} else {
			File[] listFiles = sourceFile.listFiles();
			if(listFiles == null || listFiles.length == 0){
				// 需要保留原来的文件结构时,需要对空文件夹进行处理
				if(KeepDirStructure){
					// 空文件夹的处理
					zos.putNextEntry(new ZipEntry(name + "/"));
					// 没有文件，不需要文件的copy
					zos.closeEntry();
				}

			}else {
				for (File file : listFiles) {
					// 判断是否需要保留原来的文件结构
					if (KeepDirStructure) {
						// 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
						// 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
						compress(file, zos, name + "/" + file.getName(),KeepDirStructure);
					} else {
						compress(file, zos, file.getName(),KeepDirStructure);
					}

				}
			}
		}
	}

	/**
	 * zip文件解压
	 * @param sourceZip
	 * @param destDir
	 * @throws Exception
	 */
	public static void unzip(String sourceZip, String destDir) throws Exception{
		try{
			Project p = new Project();
			Expand e = new Expand();
			e.setProject(p);
			e.setSrc(new File(sourceZip));
			e.setOverwrite(false);
			e.setDest(new File(destDir));
			e.setEncoding("gbk");
			e.execute();
		}catch(Exception e){
			throw e;
		}
	}

	/*
	 * 读取txt文件的内容
	 * @param  path 路径
	 * @return 返回文件内容
	 */
	public static String txtToString(File file){
		StringBuilder result = new StringBuilder();
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
			String s = null;
			while((s = br.readLine())!=null){//使用readLine方法，一次读一行
				result.append(System.lineSeparator()+s);
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return result.toString();
	}

	/**
	 * 读取word文件的内容
	 * @param file file
	 * @return 返回文件内容
	 */
	public static String readWordToString(File file) {
		String wordContent = "";
		if(file!=null && file.exists()){
			String path = file.getAbsolutePath();
			if(StringDateUtils.isNotBlank(path)){
				try {
					if (path.endsWith(".doc")) {
						InputStream is = new FileInputStream(file);
						WordExtractor ex = new WordExtractor(is);
						wordContent = ex.getText();
						ex.close();
					} else if (path.endsWith("docx")) {
						OPCPackage opcPackage = POIXMLDocument.openPackage(path);
						POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
						wordContent = extractor.getText();
						extractor.close();
					} else {
						System.out.println("此文件不是word文件！");
					}
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		}
		return wordContent;
	}

	/**
	 * 将list数据写入文件
	 * @param fileInfo
	 * @param path
	 */
	public void writeObject(List<String> fileInfo,String path) {
		OutputStreamWriter osw =null;
		try {
			File file=new File(path);
			if(file.exists()) {
				file.delete();
			}
			//参数true:覆盖文件中内容，反之
			FileOutputStream fos = new FileOutputStream(path,true);
			//将信息写入文件之后出现乱码情况需要配置字体编码
			osw = new OutputStreamWriter(fos, "UTF-8");
			StringBuffer infoValue=new StringBuffer();
			if(fileInfo!=null&&!fileInfo.isEmpty()) {
				for(String info:fileInfo){
					infoValue.append(info+"\r\n");//  '\r\n' 是用换行使用
				}
			}
			osw.write(infoValue.toString());
			osw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				osw.close();
			} catch (IOException e) {
				logger.info("文件流关闭异常"+e.getMessage());
			}
		}

	}

	/**
	 * 字节转成 MB GB
	 * @param size
	 * @return
	 */
	public static String readableFileSize(long size) {
		if (size <= 0) return "0";
		final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	/**
	 * 将base64字符串转为图片
	 * @param imgStr base64字符
	 * @param imgFilePath 图片存储路径
	 * @return
	 */
	public static boolean base64ToImage(String imgStr, String imgFilePath) {
		if (imgStr == null) {
			return false;
		}
		byte[] b;
		try {
			b = new BASE64Decoder().decodeBuffer(imgStr);
			for (int i = 0; i < b.length; i++) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
