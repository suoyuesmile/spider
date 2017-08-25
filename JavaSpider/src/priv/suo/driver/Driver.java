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
		//�������
		menu();
	}
	
	//���˵��Ĺ���ѡ��
	public static void menu() throws IOException, SQLException{
		menuView();
		int choice = 6;
		Scanner scan = new Scanner(System.in);		
		while(choice!=0){
			System.out.print("��ѡ��(���˵�)��");
			choice = scan.nextInt();
			switch(choice){
			case 1:
				System.out.println("����С֩��...");
				System.out.print("ѡ��Ŀ��(������վ��)��");			
				String keyword = scan.next();
				System.out.print("ѡ��Ŀ��(������Ч����ַ)��");
				String urlString = scan.next();
				//����С֩��
				startSpider(urlString,keyword);
				break;
			case 2:
				System.out.println("����һ��");
				String key="";
				while(!key.equals("0")){
					System.out.print("����ؼ���(����0����)��");
					key = scan.next();
					//ͨ���ؼ�������һ��
					fangSearch(key);
				}
				break;			
			case 3:
				//������ȡ������Դ
				manageSourse();
				break;
			default:
				System.out.println("bye bye ...");
				break;
			}
		}
		scan.close();
	}
	//���˵���ͼ
	public static void menuView(){		
		System.out.println("           **          **         ");
		System.out.println("             *  @  @  *           ");
		System.out.println("   **********[С֩��˵�]********   ");
		System.out.println("***   ***   1.����С֩��          **   ***");
		System.out.println("   ******   2.֩������              ******   ");
		System.out.println("***   ***   3.������Դ              ***   ***");
		System.out.println("   ******   0.�˳�(����)   ******   ");
		System.out.println("***      ****************      ***");
	}
	
	
	/*
	 * ����С֩��
	 * С֩��ѭ����ȡ����
	 * 1.��web����������httpget���󣬵õ�������Դ
	 * 2.����Դ���д�����ȡ�����ؼ��֣�url���ĵ�����
	 * 3.����ȡ�Ĳ�ͬ���͵���Դ�ֱ����keyword��website���ݿ�
	 * 4.�Եõ���url���еڶ�����ȡ�����µõ���urlҲ����һ��website������
	 * 5.���µõ���url��֮ǰ�ĶԱȣ���ͬ����������ݿ⣬��ͬ���������+1
	 * 6.���õ���������Դ�������ݿ�
	 */
	public static void startSpider(String urlString,String keyword) throws IOException, SQLException{
		System.out.println("С֩�����...����...");
		System.out.println("��ȡĿ�꣺"+keyword+".....Ŀ����ҳ"+urlString);
		//SpiderDbdao dbdao = new SpiderDbdao(ConstTools.dbName);
		
		/*if(dbdao.isExistUrl(urlString)){
			//website�е�wRank+1
			dbdao.upWrank(urlString);
			if(dbdao.isExistKey(keyword)){
				//WebKey�е�wkRank+1
				dbdao.upWKrank(keyword,urlString);
			}else{
				//���µ�key�ӵ����ݿ�
				dbdao.addKey(keyword);
				//�ѹ���url��key�Ĺ�����ӵ����ݿ�
				dbdao.addWebKey(keyword,urlString);
			}
			
		}else{
			//���µ�url�ӵ����ݿ�
			dbdao.addUrl(urlString);
			if(dbdao.isExistKey(keyword)){
				//Keyword�е�kRank+1
				dbdao.upWrank(keyword);
			}else{
				//���µ�key�ӵ����ݿ�
				dbdao.addKey(keyword);
			}
			//��ӵ����ݿ�
			dbdao.addWebKey(keyword,urlString);*/
			//��ȡ��ҳ����
			String webcont = SpiderDao.spiderGet(urlString);
			//������ҳ�е�ͼƬ
			SpiderDao.spiderDloadImg(webcont,urlString);
			//ץȡ��ҳ������
			ArrayList<WebKey> wbList = SpiderDao.spiderDraw(webcont);
			//ʹ�øո�ץȡ�������ӣ�����ѭ��
			for(int i=0; i<wbList.size(); i++){
				startSpider(wbList.get(i).getUrl(),wbList.get(i).getKey());
			}
		//}
		System.out.println("С֩�벶�Իؼ�...");
	}
	
	/*
	 * ��������
	 * 1.���ݹؼ��ֽ��в��ң��ҵ�����������������ҳ
	 * 2.����ҳ�������������ݹ����Ⱥ���վ�������ϵ��������
	 * 3.�ӹ���ҳ��Ϣ���Ż�ҳ��
	 * 4.��ӡ��ҳ�ģ����⣬��ַ������Ϣ���б�
	 */
	public static void fangSearch(String keyword) throws SQLException{
		SearchDbdao searchdb = new SearchDbdao();
		//���ݹؼ��ֲ�����ҳ��Ϣ
		searchdb.search(keyword);		
	}
	
	/*
	 * ����С֩��õ���Դ
	 * 1.���ҳ����й涨��Χid����Ĺ�����¼
	 * 2.�Լ�¼����ɾ������(ɾ�����淶��¼-�ؼ�����վ�㲻ƥ��)
	 * 3.�޸�����(��վ,�ؼ���,�����ֶε����������޸�)
	 */
	public static void manageSourse() throws SQLException{
		Scanner scan = new Scanner(System.in);
		//������Դ�˵���ͼ
		manageView();
		ManageDao mdao = new ManageDao(); 
		int choice = 6;	
		String content="";
		while(choice!=0){
			System.out.print("��ѡ��(������Դ)��");
			choice = scan.nextInt();
			switch(choice){
			case 1:
				System.out.println("���Ҽ�¼...");
				System.out.print("������ʼid��");			
				int startId = scan.nextInt();
				System.out.print("������ֹid��");
				int endId = scan.nextInt();
				//����һ����Χ��վ������ֵļ�¼
				mdao.findRecord(startId,endId);
				content = "[��Χ"+startId+"��"+endId+"�ļ�¼]";
				//����������־��
				mdao.addLog("����",content);
				break;
			case 2:
				System.out.println("�޸ļ�¼");
				System.out.print("������ַ:");
				String url = scan.next();
				mdao.findThisWeb(url);
				System.out.print("������Ҫ�޸���������վ��ַ :");
				System.out.print("���뽫�ȶ����Ӷ���:");
				int addHot = scan.nextInt();
				System.out.println("֧����"+addHot*38+"Ԫ");
				//������վ���ȶ�
				mdao.addWrank(url,addHot);
				content = "[����"+url+"��վ������"+addHot+"]";
				//����������־
				mdao.addLog("�޸�",content);
				break;			
			case 3:
				System.out.println("ɾ����¼");
				System.out.print("������ʼid��");			
				int startDelId = scan.nextInt();
				System.out.print("������ֹid��");
				int endDelId = scan.nextInt();
				//ɾ��һ����Χ���ȶ�
				mdao.delRecord(startDelId,endDelId);
				content = "[��Χ"+startDelId+"��"+endDelId+"�ļ�¼]";	
				//����������־��
				mdao.addLog("ɾ��",content);
				break;
			case 4:
				System.out.println("�鿴��־");
				mdao.checkLog();
			default:
				break;
			}
		}
	}
	//��Դ����˵�
	public static void manageView(){
		System.out.println("************[������Դ]************");
		System.out.println("*******     1.���Ҽ�¼             *******");
		System.out.println("*******     2.�޸ļ�¼             *******");
		System.out.println("*******     3.ɾ����¼             *******");
		System.out.println("*******     4.�鿴��־             *******");
		System.out.println("*******     0.����                    *******");
		System.out.println("*******************************");
	}
	
	
}
