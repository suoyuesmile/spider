package priv.suo.tools;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
/**
 * 
 * @author suo
 *
 */
public class DbTools {
	private Connection con;
	private Statement stm;
	private ResultSet rs;
	
	/**
	 * �������ݿ�
	 * @param dbName
	 */
	//ʵ�������ļ��أ����ӵĽ��������Ĵ���
	public void dbConn(){
		try{
			//1.����������������������ֽ��м���
			String driverName="com.mysql.jdbc.Driver"; //����������֣�����.����
			Class.forName(driverName);
			
			//2.��������
			String dbUrl="jdbc:mysql://localhost:3306/"+ConstTools.dbName
					+ "?useUnicode=true&characterEncoding=utf-8";//Э��://��ַ:�˿�/��Դ��?����
			String userName=ConstTools.username;
			String password=ConstTools.password;
			con=DriverManager.getConnection
					(dbUrl,userName,password);
			
			//3.����������
			stm=con.createStatement();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	

	/**
	 * ���ݿ���ɾ�Ĳ���
	 * @param sql
	 * @return Ӱ���¼������
	 */
	public int updateOperate(String sql){
		int iRet=0;
		try{
			iRet=stm.executeUpdate(sql);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		return iRet;
	}
	
	
	/**
	 * ���ݿ���Ҳ���
	 * @param sql
	 * @return �鵽���Ľ����
	 */
	public ResultSet selectOperate(String sql){
		try{
			rs=stm.executeQuery(sql);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return rs;
	}
	
	/**
	 * �ر����ݿ�����
	 * 
	 */
	public void close(){
		try{
			if(rs!=null) rs.close();
			if(stm!=null) stm.close();
			if(con!=null) con.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	
	
	

}

