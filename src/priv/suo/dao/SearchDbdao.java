package priv.suo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import priv.suo.tools.DbTools;

public class SearchDbdao {
	private DbTools dbTools;
	public SearchDbdao(){
		this.dbTools = new DbTools(); 
	}
	public void search(String keyword) throws SQLException{
		ResultSet rs = null;
		int i = 0;
		String sql = 
				"SELECT Website.url,Keyword.keyword,WebKey.wkRank " + 
				"FROM Website,Keyword,WebKey " + "WHERE keyword like '%"+ keyword + 
				"%' AND WebKey.webId =Website.id " + "AND WebKey.keyId =Keyword.id " + 
				"ORDER BY wkRank DESC"; 
		dbTools.dbConn();
		rs = dbTools.selectOperate(sql);
		while(rs.next()){
			i++;
			System.out.println("第"+i+"名---------["+rs.getString(2)
					+"]---------["+rs.getString(1)+"]"
					+"---------[热度"+rs.getInt(3)+"]");
		}
		dbTools.close();
	} 
}