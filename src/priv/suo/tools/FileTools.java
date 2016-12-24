package priv.suo.tools;

import java.io.*;


public class FileTools {
	public static void fileIn(String filename,String content){
		File file = new File(filename);
		System.out.println("找到页面，放入口袋..."+filename+"文件中");
		try{
			//创建文件输出流
			FileOutputStream writerStream = new FileOutputStream(file); 
			//创建输出缓冲区，将文件输入到缓冲区（内存）
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writerStream, ConstTools.charSet));
			//将字符通过缓冲区写入文件中
			writer.write(content);
			//关闭写入
			writer.close();  
		}catch(Exception e){
			System.out.print("文件写入失败");
		}
	}
}
