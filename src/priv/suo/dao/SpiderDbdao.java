package priv.suo.dao;
import priv.suo.tools.DbTools;

import java.sql.ResultSet;
import java.sql.SQLException;


public class SpiderDbdao {
	private DbTools dbtools;
	
	public SpiderDbdao(String dbName){
		this.dbtools = new DbTools();
	}
	
	//判断是否已存在URL
	public boolean isExistUrl(String urlString) throws SQLException {
		boolean isExist;
		dbtools.dbConn();
		String sql = "SELECT * FROM Website WHERE url ='"
				+ urlString + "';" ;
		ResultSet res = dbtools.selectOperate(sql);
		if(res.next()){
			isExist = true;
		}else{
			isExist = false;
		}
		dbtools.close();
		return isExist;
		
	}
	
	//判断是否已存在KEY
	public boolean isExistKey(String keyword) throws SQLException {
		boolean isExist;
		dbtools.dbConn();
		String sql = "SELECT * FROM Keyword WHERE keyword ='"
				+ keyword + "'" ;
		ResultSet res = dbtools.selectOperate(sql);
		if(res.next()){
			isExist = true;
		}else{
			isExist = false;
		}
		dbtools.close();
		return isExist;
	}
	
	//添加URL到webSite表
	public void addUrl(String urlString){
		dbtools.dbConn();
		String sql = "INSERT Website(url,wRank) VALUES('"+urlString+"',1)";
		int rs = dbtools.updateOperate(sql);
		if(rs>0){
			System.out.println("小蜘蛛已经把此URL收入宝箱...");
		}else{
			System.out.println("小蜘蛛收入囊中失败...");
		}
		dbtools.close();		
	}
	
	//添加KEY到keyWord
	public void addKey(String keyword){
		dbtools.dbConn();
		String sql = "INSERT Keyword(keyword,kRank) VALUES('"+keyword+"',1)";
		int rs = dbtools.updateOperate(sql);
		if(rs>0){
			System.out.println("小蜘蛛已经把此key收入宝箱...");
		}else{
			System.out.println("小蜘蛛收入囊中失败...");
		}
		dbtools.close();
	}
	
	//添加URL和key到webKey
	public void addWebKey(String keyword, String urlString) {
		addUrl(urlString);
		addKey(keyword);
		dbtools.dbConn();
		String sql = 
				"INSERT WebKey(webId,keyId,wkRank) VALUES(" +
				"(SELECT id FROM Website WHERE url='" +
				urlString+"')," +
				"(SELECT id FROM Keyword WHERE keyword='" +
				keyword+"'),1)";
		int rs = dbtools.updateOperate(sql);
		if(rs>0){
			System.out.println("小蜘蛛已经把全部收入宝箱...");
		}else{
			System.out.println("小蜘蛛收入囊中失败...");
		}
		dbtools.close();		
	}
	//小蜘蛛增加网站热度
	public void upWrank(String urlString) {
		dbtools.dbConn();
		String sql = "UPDATE Website SET wRank = wRank+1 WHERE url='" +
				urlString+"'";
		int rs = dbtools.updateOperate(sql);
		if(rs>0){
			System.out.println("小蜘蛛偶遇老朋友+["+urlString+"]关联度+1");
		}else{
			System.out.println("小蜘蛛与老朋友+["+urlString+"]擦肩而过");
		}
		dbtools.close();	
	}
	//小蜘蛛增加网站与关联词关联度
	public void upWKrank(String keyword, String urlString) {
		dbtools.dbConn();
		String sql = "UPDATE WebKey SET wkRank = wkRank+1 " +
				"WHERE webId =" +
				"(SELECT id FROM Website WHERE url='" +
				urlString+"') AND keyId =" +
				"(SELECT id FROM Keyword WHERE keyword='" +
				keyword+"')";
		int rs = dbtools.updateOperate(sql);
		if(rs>0){
			System.out.println("小蜘蛛偶遇老朋友们关联度+1");
		}else{
			System.out.println("小蜘蛛与老朋友们擦肩而过");
		}
		dbtools.close();		
	}
	

	



	

	
}
