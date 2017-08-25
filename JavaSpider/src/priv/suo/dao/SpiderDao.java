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
	 * 小蜘蛛开始爬行
	 * 1.获取网页数据流
	 * 2.将获取的数据写入文档
	 * 3.返回获取的数据
	 */
	public static String spiderGet(String urlString) throws ClientProtocolException, IOException{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(urlString);
		//设置读取超时(60秒)和连接超时(60秒)
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
		httpget.setConfig(requestConfig);
		//接收请求的响应
		CloseableHttpResponse response = httpclient.execute(httpget);
		String content="";
		System.out.println("开始爬行...");
		try {
			//将响应数据存入字符串中
		    HttpEntity entity = response.getEntity();
		    content = EntityUtils.toString(entity);
		    Date date = new Date();
            String filename = ConstTools.tempHtmlDir+date.getTime()+(1+Math.random()*1000);
            //将获取的数据存入文件中，文件名随机数设置，保证不覆盖
            FileTools.fileIn(filename+".html",content);
		} finally {
			//关闭响应
		    response.close();
		}
		return content;
	}
	
	/*
	 * 小蜘蛛提取数据
	 * 1.使用正则将a标签整个获取到
	 * 2.将a标签里的关键字和url提取出来
	 * 3.提取出来的数据封装到website对象里面
	 */
	public static ArrayList<WebKey> spiderDraw(String content){
		ArrayList<WebKey> webList = new  ArrayList<WebKey>();
		System.out.println("小蜘蛛疯狂抓取中...");
		String aTag = "";
		String url ="";
		String keyWord="";
		String aRule="[a-zA-z]+://[^\\s]*</a>";
		int i =0;
		//匹配出a标签的内容
		Matcher mat = Pattern.compile(aRule).matcher(content);
		while(mat.find()){
			i++;
			aTag = mat.group();			
			try{
			//提取出URL和关键字
			url = aTag.substring(0,aTag.indexOf("\""));
			keyWord = aTag.substring(aTag.indexOf("\"")+2,aTag.indexOf("<"));
			}catch(Exception e){
				//跳过异常链接
				System.out.println("小蜘蛛抓到一个异常链接，跳过继续抓取...");
				continue;
			}
			WebKey wbt = new WebKey(keyWord,url,1);
			//找到的链接对象加入到链表中
			webList.add(wbt);
			System.out.println("提取"+i+"个链接..."+"关键字："+keyWord+"     超链接："+url);
		}		
		return webList;
	}
	
	/*
	 * 小蜘蛛提取图片资源
	 * 1.通过正则匹配出所有的图片链接
	 * 2.通过图片链接找到图片资源
	 * 3.将图片资源下载到本地的文件夹中
	 */
	public static void spiderDloadImg(String content,String urlString) throws ClientProtocolException, IOException{
		System.out.println("小蜘蛛抓取图片中...");
		String ImgUrl="";
		String rule="<img src=\"[^\\s]*";
		//匹配图片链接
		Matcher mat = Pattern.compile(rule).matcher(content);
		while(mat.find()){
			ImgUrl = mat.group();
			//拼装图片链接，有的图片存在头，有的不存在，有头的不加，没头的加上头
			if(!ImgUrl.matches(urlString)){
				ImgUrl = urlString+ImgUrl.substring(10,ImgUrl.length()-1);
			}else{
				ImgUrl = ImgUrl.substring(10,ImgUrl.length()-1);
			}
			spiderGetImg(ImgUrl);
		}
				
	}
	
	/*
	 * 小蜘蛛上网找图片
	 * 1.发送请求获取图片的字节流
	 * 2.将图片字节流写入文件之中
	 */
	public static void spiderGetImg(String ImgUrl) throws IOException{	
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(ImgUrl);
		//设置读取超时和连接超时
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
		httpget.setConfig(requestConfig);
		//接收发送的响应
		CloseableHttpResponse response = httpclient.execute(httpget);
	
		try{			
			HttpEntity entity = response.getEntity();
		   if (entity != null) {
			   //由于图片是字节，采用字节流接收数据
		        InputStream instream = entity.getContent();		        
		        try {
		        	//拼装图片名字
		        	String imgType = ImgUrl.substring(ImgUrl.length()-4);
		        	Date date = new Date();
		        	String filename = ConstTools.tempImgDir+date.getTime()+(1+Math.random()*1000)+imgType;
		        	FileOutputStream fileOutputStream = new FileOutputStream(filename);
		            byte[] buff = new byte[ConstTools.byteImgMax];
		            int len = instream.read(buff);
		            //将字节流写入文件
		            fileOutputStream.write(buff, 0, len);
		        	System.out.println("小蜘蛛放入图片至..."+filename+"文件中");
		        }
		        catch(Exception e){
		        	System.out.println("小蜘蛛装入图片失败...");
		        }
		        finally {
		        	//关闭字节输入流
		            instream.close();
		        }
		    }
		}
		catch(Exception e){
			System.out.println("小蜘蛛没有得到回复...");
		}
		finally {
			//关闭响应
		    response.close();
		}	
	}
}
