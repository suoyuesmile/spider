package priv.suo.tools;

import java.io.*;


public class FileTools {
	public static void fileIn(String filename,String content){
		File file = new File(filename);
		System.out.println("�ҵ�ҳ�棬����ڴ�..."+filename+"�ļ���");
		try{
			//�����ļ������
			FileOutputStream writerStream = new FileOutputStream(file); 
			//������������������ļ����뵽���������ڴ棩
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writerStream, ConstTools.charSet));
			//���ַ�ͨ��������д���ļ���
			writer.write(content);
			//�ر�д��
			writer.close();  
		}catch(Exception e){
			System.out.print("�ļ�д��ʧ��");
		}
	}
}
