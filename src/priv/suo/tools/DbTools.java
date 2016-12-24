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
	 * 连接数据库
	 * @param dbName
	 */
	//实现驱动的加载，连接的建立，语句的创建
	public void dbConn(){
		try{
			//1.加载驱动：按驱动类的名字进行加载
			String driverName="com.mysql.jdbc.Driver"; //驱动类的名字：包名.类名
			Class.forName(driverName);
			
			//2.建立连接
			String dbUrl="jdbc:mysql://localhost:3306/"+ConstTools.dbName
					+ "?useUnicode=true&characterEncoding=utf-8";//协议://地址:端口/资源名?参数
			String userName=ConstTools.username;
			String password=ConstTools.password;
			con=DriverManager.getConnection
					(dbUrl,userName,password);
			
			//3.创建语句对象
			stm=con.createStatement();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	

	/**
	 * 数据库增删改操作
	 * @param sql
	 * @return 影响记录的行数
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
	 * 数据库查找操作
	 * @param sql
	 * @return 查到到的结果集
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
	 * 关闭数据库连接
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

