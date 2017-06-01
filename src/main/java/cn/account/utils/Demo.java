package cn.account.utils;
  
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cn.account.bean.UserBind;
  
public class Demo {  
  
    static String sql = null;  
    static DBHelper db1 = null;  
    static ResultSet ret = null;  
    private static String insert_sql_wx = "INSERT INTO t_user_bind (open_id,id_card,mobile_number,is_bind,client_type) VALUES (?,?,?,?,?)";
    private static String insert_sql_t_user_bind_alipay = "INSERT INTO t_user_bind_alipay (user_id,id_card,mobile_number,is_bind,client_type) VALUES (?,?,?,?,?)"; 
  
    public static void main(String[] args) throws SQLException { 
    	//dealZFB();
    	//dealWX();
    	String zt = "HBU";
    	char[] cs = zt.toCharArray();
    	for(char c : cs){
    		System.out.println(c);
    	}
    }  
  
    //处理微信数据
    public static void dealWX() throws SQLException{
    	 sql = "select * from t_szjj_userbind";//SQL语句  
         db1 = new DBHelper(sql);//创建DBHelper对象  
         Connection conn =  db1.conn;
         conn.setAutoCommit(false); // 设置手动提交
         ret = db1.pst.executeQuery();//执行语句，得到结果集  
         PreparedStatement psts = conn.prepareStatement(insert_sql_wx);
         List<UserBind> userBinds = new ArrayList<UserBind>();
         try {  
             while (ret.next()) {
                 String idno = ret.getString("idno");
                 String phone = ret.getString("phone");
                 String openid = ret.getString("openid");
                 if(StringUtils.isNotBlank(openid)){
                 	UserBind userBind = new UserBind();
                 	userBind.setClientType("Z");
                 	userBind.setIdCard(idno);
                 	userBind.setIsBind(1);
                 	userBind.setMobileNumber(phone);
                 	userBind.setOpenId(openid);
                 	userBinds.add(userBind);
                 }
             }//显示数据  
             for(int i=0;i<userBinds.size();i++){
             	psts.setString(1, userBinds.get(i).getOpenId());
                 psts.setString(2, userBinds.get(i).getIdCard());
                 psts.setString(3, userBinds.get(i).getMobileNumber());
                 psts.setInt(4, 1);
                 psts.setString(5, "C");
                 psts.addBatch();
                 if(i%1000==0){
                 	 psts.executeBatch(); // 执行批量处理
                 }
             }
             psts.executeBatch(); // 执行批量处理  
             conn.commit();  // 提交  
             ret.close();  
             db1.close();//关闭连接  
         } catch (SQLException e) {  
             e.printStackTrace();  
         } 
    }
    //处理支付数据
    public static void dealZFB() throws SQLException{
    	sql = "select * from t_szjj_userbind";//SQL语句  
        db1 = new DBHelper(sql);//创建DBHelper对象  
        ret = db1.pst.executeQuery();//执行语句，得到结果集  
        Connection conn =  db1.conn;
        conn.setAutoCommit(false); // 设置手动提交  
        PreparedStatement psts = conn.prepareStatement(insert_sql_t_user_bind_alipay);
        List<UserBind> userBinds = new ArrayList<UserBind>();
        try {
            while (ret.next()) {
            	String idno = ret.getString("idno");
                String phone = ret.getString("phone");
                String userId = ret.getString("unionid");
                if(StringUtils.isNotBlank(userId)){
                	UserBind userBind = new UserBind();
                	userBind.setClientType("Z");
                	userBind.setIdCard(idno);
                	userBind.setIsBind(1);
                	userBind.setMobileNumber(phone);
                	userBind.setUserId(userId);
                	userBinds.add(userBind);
                }
            }//显示数据  
            for(int i=0;i<userBinds.size();i++){
            	psts.setString(1, userBinds.get(i).getUserId());
                psts.setString(2, userBinds.get(i).getIdCard());
                psts.setString(3, userBinds.get(i).getMobileNumber());
                psts.setInt(4, 1);
                psts.setString(5, "Z");
                psts.addBatch();
                if(i%1000==0){
                	 psts.executeBatch(); // 执行批量处理
                }
            }
            psts.executeBatch();
            conn.commit();  // 提交
            ret.close();  
            db1.close();//关闭连接  
        } catch (SQLException e) {  
            e.printStackTrace();  
        } 
    }
}  