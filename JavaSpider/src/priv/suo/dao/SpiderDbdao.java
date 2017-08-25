package priv.suo.dao;
import priv.suo.tools.DbTools;

import java.sql.ResultSet;
import java.sql.SQLException;


public class SpiderDbdao {
	private DbTools dbtools;
	
	public SpiderDbdao(String dbName){
		this.dbtools = new DbTools();
	}
	
	//�ж��Ƿ��Ѵ���URL
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
	
	//�ж��Ƿ��Ѵ���KEY
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
	
	//���URL��webSite��
	public void addUrl(String urlString){
		dbtools.dbConn();
		String sql = "INSERT Website(url,wRank) VALUES('"+urlString+"',1)";
		int rs = dbtools.updateOperate(sql);
		if(rs>0){
			System.out.println("С֩���Ѿ��Ѵ�URL���뱦��...");
		}else{
			System.out.println("С֩����������ʧ��...");
		}
		dbtools.close();		
	}
	
	//���KEY��keyWord
	public void addKey(String keyword){
		dbtools.dbConn();
		String sql = "INSERT Keyword(keyword,kRank) VALUES('"+keyword+"',1)";
		int rs = dbtools.updateOperate(sql);
		if(rs>0){
			System.out.println("С֩���Ѿ��Ѵ�key���뱦��...");
		}else{
			System.out.println("С֩����������ʧ��...");
		}
		dbtools.close();
	}
	
	//���URL��key��webKey
	public void addWebKey(String keyword, String urlString) {
		dbtools.dbConn();
		String sql = 
				"INSERT WebKey(webId,keyId,wkRank) VALUES(" +
				"(SELECT id FROM Website WHERE url='" +
				urlString+"')," +
				"(SELECT id FROM Keyword WHERE keyword='" +
				keyword+"'),1)";
		int rs = dbtools.updateOperate(sql);
		if(rs>0){
			System.out.println("С֩���Ѿ���ȫ�����뱦��...");
		}else{
			System.out.println("С֩����������ʧ��...");
		}
		dbtools.close();		
	}
	//С֩��������վ�ȶ�
	public void upWrank(String urlString) {
		dbtools.dbConn();
		String sql = "UPDATE Website SET wRank = wRank+1 WHERE url='" +
				urlString+"'";
		int rs = dbtools.updateOperate(sql);
		if(rs>0){
			System.out.println("С֩��ż��������+["+urlString+"]������+1");
		}else{
			System.out.println("С֩����������+["+urlString+"]�������");
		}
		dbtools.close();	
	}
	//С֩��������վ������ʹ�����
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
			System.out.println("С֩��ż���������ǹ�����+1");
		}else{
			System.out.println("С֩�����������ǲ������");
		}
		dbtools.close();		
	}
	

	



	

	
}
