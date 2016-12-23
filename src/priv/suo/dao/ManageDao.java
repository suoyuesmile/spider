package priv.suo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import priv.suo.tools.DbTools;

public class ManageDao {
	private DbTools dbtools;
	public ManageDao(){
		this.dbtools = new DbTools();
	}
	//按照规定范围找到资源
	public void findRecord(int startId, int endId) throws SQLException {
		ResultSet rs = null;
		dbtools.dbConn();
		String sql = 
				"SELECT Website.url,Keyword.keyword,WebKey.wkRank,WebKey.id " + 
				"FROM Website,Keyword,WebKey " + 
				"WHERE WebKey.id>=" +
				+startId+" AND WebKey.id<=" +
				+endId+	" AND WebKey.webId =Website.id " + 
				"AND WebKey.keyId =Keyword.id " + 
				"ORDER BY WebKey.id ASC"; 
		dbtools.dbConn();
		rs = dbtools.selectOperate(sql);
		while(rs.next()){
			System.out.println("第"+rs.getInt(4)+"条---------["+rs.getString(2)
					+"]---------["+rs.getString(1)+"]"
					+"---------[热度"+rs.getInt(3)+"]");
		}
		dbtools.close();
	}
	//找到这个网站的记录
	public void findThisWeb(String url) throws SQLException {
		ResultSet rs = null;
		dbtools.dbConn();
		String sql = "SELECT * FROM Website WHERE url='" +
				url+"'";
		dbtools.dbConn();
		rs = dbtools.selectOperate(sql);
		if(rs.next()){
			System.out.println("---------["+rs.getInt(1)
					+"]---------["+rs.getString(2)+"]"
					+"---------[热度"+rs.getInt(3)+"]");
		}
		dbtools.close();
		
	}
	//整加网站的热度
	public void addWrank(String url, int addHot) {
		dbtools.dbConn();
		String sql = "UPDATE Website SET wRank =wRank+" +
				addHot+" WHERE url ='" +
						url+"'";
		int rs = dbtools.updateOperate(sql);
		if(rs>0){
			System.out.println("成功增加网站热度");
		}else{
			System.out.println("增加失败...");
		}
		dbtools.close();		
	}

	public void delRecord(int startDelId, int endDelId) {
		dbtools.dbConn();
		String sql = "DELECT FROM WebKey WHERE id>=" +
				startDelId+" AND id<=" +
				endDelId;
		int rs = dbtools.updateOperate(sql);
		if(rs>0){
			System.out.println("成功删除记录");
		}else{
			System.out.println("删除失败...");
		}
		dbtools.close();
	}
	//将管理操作的日志加入数据库
	public void addLog(String opera, String content) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String time =  formatter.format(date);
		dbtools.dbConn();
		String sql = "INSERT manageLog(opera,content,time) VALUES('" +
				opera+"','" +
						content+"','" +
								time+"')";
		int rs = dbtools.updateOperate(sql);
		if(rs>0){
			System.out.println("此次操作已加入日志...");
		}else{
			System.out.println("收入日志失败...");
		}
		dbtools.close();
	}
	//从数据库中查看所有日志
	public void checkLog() throws SQLException {
		ResultSet rs = null;
		int i = 0;
		String sql = "SELECT * FROM manageLog ORDER BY id DESC";
		dbtools.dbConn();
		rs = dbtools.selectOperate(sql);
		while(rs.next()){
			i++;
			System.out.println("第"+i+"条---------["+rs.getString(2)
					+"]---------"+rs.getString(3)
					+"---------["+rs.getString(4)+"]");
		}
		dbtools.close();
	}

}
