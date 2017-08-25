package priv.suo.dao;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import priv.suo.tools.ConstTools;
import priv.suo.tools.FileTools;
import priv.suo.vo.WebKey;

public class SpiderDao {
	/*
	 * С֩�뿪ʼ����
	 * 1.��ȡ��ҳ������
	 * 2.����ȡ������д���ĵ�
	 * 3.���ػ�ȡ������
	 */
	public static String spiderGet(String urlString) throws ClientProtocolException, IOException{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(urlString);
		//���ö�ȡ��ʱ(60��)�����ӳ�ʱ(60��)
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
		httpget.setConfig(requestConfig);
		//�����������Ӧ
		CloseableHttpResponse response = httpclient.execute(httpget);
		String content="";
		System.out.println("��ʼ����...");
		try {
			//����Ӧ���ݴ����ַ�����
		    HttpEntity entity = response.getEntity();
		    content = EntityUtils.toString(entity);
		    Date date = new Date();
            String filename = ConstTools.tempHtmlDir+date.getTime()+(1+Math.random()*1000);
            //����ȡ�����ݴ����ļ��У��ļ�����������ã���֤������
            FileTools.fileIn(filename+".html",content);
		} finally {
			//�ر���Ӧ
		    response.close();
		}
		return content;
	}
	
	/*
	 * С֩����ȡ����
	 * 1.ʹ������a��ǩ������ȡ��
	 * 2.��a��ǩ��Ĺؼ��ֺ�url��ȡ����
	 * 3.��ȡ���������ݷ�װ��website��������
	 */
	public static ArrayList<WebKey> spiderDraw(String content){
		ArrayList<WebKey> webList = new  ArrayList<WebKey>();
		System.out.println("С֩����ץȡ��...");
		String aTag = "";
		String url ="";
		String keyWord="";
		String aRule="[a-zA-z]+://[^\\s]*</a>";
		int i =0;
		//ƥ���a��ǩ������
		Matcher mat = Pattern.compile(aRule).matcher(content);
		while(mat.find()){
			i++;
			aTag = mat.group();			
			try{
			//��ȡ��URL�͹ؼ���
			url = aTag.substring(0,aTag.indexOf("\""));
			keyWord = aTag.substring(aTag.indexOf("\"")+2,aTag.indexOf("<"));
			}catch(Exception e){
				//�����쳣����
				System.out.println("С֩��ץ��һ���쳣���ӣ���������ץȡ...");
				continue;
			}
			WebKey wbt = new WebKey(keyWord,url,1);
			//�ҵ������Ӷ�����뵽������
			webList.add(wbt);
			System.out.println("��ȡ"+i+"������..."+"�ؼ��֣�"+keyWord+"     �����ӣ�"+url);
		}		
		return webList;
	}
	
	/*
	 * С֩����ȡͼƬ��Դ
	 * 1.ͨ������ƥ������е�ͼƬ����
	 * 2.ͨ��ͼƬ�����ҵ�ͼƬ��Դ
	 * 3.��ͼƬ��Դ���ص����ص��ļ�����
	 */
	public static void spiderDloadImg(String content,String urlString) throws ClientProtocolException, IOException{
		System.out.println("С֩��ץȡͼƬ��...");
		String ImgUrl="";
		String rule="<img src=\"[^\\s]*";
		//ƥ��ͼƬ����
		Matcher mat = Pattern.compile(rule).matcher(content);
		while(mat.find()){
			ImgUrl = mat.group();
			//ƴװͼƬ���ӣ��е�ͼƬ����ͷ���еĲ����ڣ���ͷ�Ĳ��ӣ�ûͷ�ļ���ͷ
			if(!ImgUrl.matches(urlString)){
				ImgUrl = urlString+ImgUrl.substring(10,ImgUrl.length()-1);
			}else{
				ImgUrl = ImgUrl.substring(10,ImgUrl.length()-1);
			}
			spiderGetImg(ImgUrl);
		}
				
	}
	
	/*
	 * С֩��������ͼƬ
	 * 1.���������ȡͼƬ���ֽ���
	 * 2.��ͼƬ�ֽ���д���ļ�֮��
	 */
	public static void spiderGetImg(String ImgUrl) throws IOException{	
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(ImgUrl);
		//���ö�ȡ��ʱ�����ӳ�ʱ
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
		httpget.setConfig(requestConfig);
		//���շ��͵���Ӧ
		CloseableHttpResponse response = httpclient.execute(httpget);
	
		try{			
			HttpEntity entity = response.getEntity();
		   if (entity != null) {
			   //����ͼƬ���ֽڣ������ֽ�����������
		        InputStream instream = entity.getContent();		        
		        try {
		        	//ƴװͼƬ����
		        	String imgType = ImgUrl.substring(ImgUrl.length()-4);
		        	Date date = new Date();
		        	String filename = ConstTools.tempImgDir+date.getTime()+(1+Math.random()*1000)+imgType;
		        	FileOutputStream fileOutputStream = new FileOutputStream(filename);
		            byte[] buff = new byte[ConstTools.byteImgMax];
		            int len = instream.read(buff);
		            //���ֽ���д���ļ�
		            fileOutputStream.write(buff, 0, len);
		        	System.out.println("С֩�����ͼƬ��..."+filename+"�ļ���");
		        }
		        catch(Exception e){
		        	System.out.println("С֩��װ��ͼƬʧ��...");
		        }
		        finally {
		        	//�ر��ֽ�������
		            instream.close();
		        }
		    }
		}
		catch(Exception e){
			System.out.println("С֩��û�еõ��ظ�...");
		}
		finally {
			//�ر���Ӧ
		    response.close();
		}	
	}
}
