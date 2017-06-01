package cn.account.utils;  
  
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.SQLException;  
  
public class DBHelper { 
	//相对Oracle的批量处理，MySQL需要JDBC参数显式开启,并且对于JDBC驱动的版本也有要求
	//参数useServerPrepStmts=false，如果不开启(useServerPrepStmts=false)，
	//使用com.mysql.jdbc.PreparedStatement进行本地SQL拼装，最后送到db上就是已经替换了?后的最终SQL
    public static final String url = "jdbc:mysql://192.168.1.121/service?useServerPrepStmts=false&rewriteBatchedStatements=true";  
    public static final String name = "com.mysql.jdbc.Driver";  
    public static final String user = "root";  
    public static final String password = "123456";  
  
    public Connection conn = null;  
    public PreparedStatement pst = null;  
  
    public DBHelper(String sql) {  
        try {  
            Class.forName(name);//指定连接类型  
            conn = DriverManager.getConnection(url, user, password);//获取连接  
            pst = conn.prepareStatement(sql);//准备执行语句  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    public void close() {  
        try {  
            this.conn.close();  
            this.pst.close();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  
}  