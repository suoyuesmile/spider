package priv.suo.driver;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;


import priv.suo.dao.ManageDao;
import priv.suo.dao.SearchDbdao;
import priv.suo.dao.SpiderDbdao;
import priv.suo.dao.SpiderDao;
import priv.suo.tools.ConstTools;
import priv.suo.vo.WebKey;


public class Driver {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws IOException, SQLException{
		// TODO Auto-generated method stub
		//程序入口
		menu();
	}
	
	//主菜单的功能选择
	public static void menu() throws IOException, SQLException{
		menuView();
		int choice = 6;
		Scanner scan = new Scanner(System.in);		
		while(choice!=0){
			System.out.print("请选择(主菜单)：");
			choice = scan.nextInt();
			switch(choice){
			case 1:
				System.out.println("启动小蜘蛛...");
				System.out.print("选择目标(输入网站名)：");			
				String keyword = scan.next();
				System.out.print("选择目标(输入有效的网址)：");
				String urlString = scan.next();
				startSpider(urlString,keyword);
				break;
			case 2:
				System.out.println("搜索一下");
				String key="";
				while(!key.equals("0")){
					System.out.print("输入关键字(输入0返回)：");
					key = scan.next();
					fangSearch(key);
				}
				break;			
			case 3:
				manageSourse();
				break;
			default:
				System.out.println("bye bye ...");
				break;
			}
		}
		scan.close();
	}
	//主菜单视图
	public static void menuView(){		
		System.out.println("           **          **         ");
		System.out.println("             *  @  @  *           ");
		System.out.println("   **********[小蜘蛛菜单]********   ");
		System.out.println("***   ***   1.启动小蜘蛛          **   ***");
		System.out.println("   ******   2.蜘蛛搜索              ******   ");
		System.out.println("***   ***   3.管理资源              ***   ***");
		System.out.println("   ******   0.退出(返回)   ******   ");
		System.out.println("***      ****************      ***");
	}
	
	
	/*
	 * 开启小蜘蛛
	 * 小蜘蛛循环爬取流程
	 * 1.向web服务器发送httpget请求，得到请求资源
	 * 2.对资源进行处理，提取出，关键字，url，文档内容
	 * 3.将提取的不同类型的资源分别存入keyword和website集合对象中
	 * 4.对得到的url进行第二次爬取，将新得到的url也存入一个website集合中
	 * 5.将新得到的url与之前的对比，不同的则加入集合，相同的则关联度+1
	 * 6.将得到的爬虫资源存入数据库
	 */
	public static void startSpider(String urlString,String keyword) throws IOException, SQLException{
		System.out.println("小蜘蛛出发...捕猎...");
		System.out.println("获取目标："+keyword+".....目标网页"+urlString);
		SpiderDbdao dbdao = new SpiderDbdao(ConstTools.dbName);
		
		if(dbdao.isExistUrl(urlString)){
			//website中的wRank+1
			dbdao.upWrank(urlString);
			if(dbdao.isExistKey(keyword)){
				//WebKey中的wkRank+1
				dbdao.upWKrank(keyword,urlString);
			}else{
				//添加到数据库
				dbdao.addWebKey(keyword,urlString);
			}
			
		}else{
			if(dbdao.isExistKey(keyword)){
				//Keyword中的kRank+1
				dbdao.upWrank(keyword);
			}
			//添加到数据库
			dbdao.addWebKey(keyword,urlString);
			//获取网页内容
			String webcont = SpiderDao.spiderGet(urlString);
			//下载网页中的图片
			SpiderDao.spiderDloadImg(webcont,urlString);
			//抓取网页中链接
			ArrayList<WebKey> wbList = SpiderDao.spiderDraw(webcont);
			//循环进入
			for(int i=0; i<wbList.size(); i++){
				startSpider(wbList.get(i).getUrl(),wbList.get(i).getKey());
			}
		}
		System.out.println("小蜘蛛捕猎回家...");
	}
	
	/*
	 * 搜索引擎
	 * 1.根据关键字进行查找，找到所有满足条件的网页
	 * 2.对网页进行排名，根据关联度和网站自身的联系都来排名
	 * 3.加工网页信息，优化页面
	 * 4.打印网页的，标题，网址，等信息的列表
	 */
	public static void fangSearch(String keyword) throws SQLException{
		SearchDbdao searchdb = new SearchDbdao();
		searchdb.search(keyword);		
	}
	
	/*
	 * 管理小蜘蛛得到资源
	 * 1.查找出所有规定范围id的类的关联记录
	 * 2.对记录进行删除操作(删除不规范记录-关键词与站点不匹配)
	 * 3.修改排名(网站,关键字,关联字段的排名进行修改)
	 */
	public static void manageSourse() throws SQLException{
		Scanner scan = new Scanner(System.in);
		manageView();
		ManageDao mdao = new ManageDao(); 
		int choice = 6;	
		String content="";
		while(choice!=0){
			System.out.print("请选择(管理资源)：");
			choice = scan.nextInt();
			switch(choice){
			case 1:
				System.out.println("查找记录...");
				System.out.print("输入起始id：");			
				int startId = scan.nextInt();
				System.out.print("输入终止id：");
				int endId = scan.nextInt();
				//查找一定范围的站点关联字的记录
				mdao.findRecord(startId,endId);
				content = "[范围"+startId+"到"+endId+"的记录]";
				//操作加入日志中
				mdao.addLog("查找",content);
				break;
			case 2:
				System.out.println("修改记录");
				System.out.print("查找网址:");
				String url = scan.next();
				mdao.findThisWeb(url);
				System.out.print("输入需要修改排名的网站网址 :");
				System.out.print("您想将热度增加多少:");
				int addHot = scan.nextInt();
				System.out.println("支付金额："+addHot*38+"元");
				//增加网站的热度
				mdao.addWrank(url,addHot);
				content = "[增加"+url+"网站的排名"+addHot+"]";
				//操作加入日志
				mdao.addLog("修改",content);
				break;			
			case 3:
				System.out.println("删除记录");
				System.out.print("输入起始id：");			
				int startDelId = scan.nextInt();
				System.out.print("输入终止id：");
				int endDelId = scan.nextInt();
				//删除一定范围的热度
				mdao.delRecord(startDelId,endDelId);
				content = "[范围"+startDelId+"到"+endDelId+"的记录]";	
				//操作加入日志中
				mdao.addLog("删除",content);
				break;
			case 4:
				System.out.println("查看日志");
				mdao.checkLog();
			default:
				break;
			}
		}
	}
	//资源管理菜单
	public static void manageView(){
		System.out.println("************[管理资源]************");
		System.out.println("*******     1.查找记录             *******");
		System.out.println("*******     2.修改记录             *******");
		System.out.println("*******     3.删除记录             *******");
		System.out.println("*******     4.查看日志             *******");
		System.out.println("*******     0.返回                    *******");
		System.out.println("*******************************");
	}
	
	
}
