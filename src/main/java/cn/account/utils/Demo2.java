package cn.account.utils;
  
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cn.account.bean.ReadilyShoot;
import cn.account.bean.UserBind;
import cn.sdk.util.DateUtil;
  
public class Demo2 {  
  
    static String sql = null;  
    static DBHelper db1 = null;  
    static ResultSet ret = null;  
    private static String insert_sql_wx = "INSERT INTO t_user_bind (open_id,id_card,mobile_number,is_bind,client_type) VALUES (?,?,?,?,?)";
    private static String insert_sql_t_user_bind_alipay = "INSERT INTO t_readily_shoot(add_date,illegal_time,illegal_sections,situation_statement,report_serial_number,password,illegal_img1,illegal_img2,illegal_img3) VALUES (?,?,?,?,?,?,?,?,?)"; 
  
    public static void main(String[] args) throws SQLException { 
    	dealZFB();
    	//dealWX();
    	
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
    	sql = "select * from t_szjj_sspreport_bak170627 where intime>='2017-05-05 00:00:00';";//SQL语句  
        db1 = new DBHelper(sql);//创建DBHelper对象  
        ret = db1.pst.executeQuery();//执行语句，得到结果集  
        Connection conn =  db1.conn;
        conn.setAutoCommit(false); // 设置手动提交  
        PreparedStatement psts = conn.prepareStatement(insert_sql_t_user_bind_alipay);
        List<ReadilyShoot> readilyShoots = new ArrayList<ReadilyShoot>();
        try {
            while (ret.next()) {
            	String add_date = ret.getString("intime");
                String illegal_time = ret.getString("wfsj");
                String illegal_sections = ret.getString("wfroad");
                String situation_statement = ret.getString("wfname");
                String report_serial_number = ret.getString("jbbh");
                String password = ret.getString("querypwd");
                String imagepath = ret.getString("imagepath");
                String[] sss = imagepath.split(",");
                String illegal_img1 = sss[0];
                String illegal_img2 = sss[1];
                String illegal_img3 = "";
                try {
                	illegal_img3 = sss[2];
				} catch (Exception e) {
					System.out.println("=============");
				}
                
                ReadilyShoot readilyShoot = new ReadilyShoot();
            	readilyShoot.setAddDate(DateUtil.getDateTimeFromStr(add_date));
            	readilyShoot.setIllegalTime(DateUtil.getDateTimeFromStr(illegal_time));
            	readilyShoot.setIllegalSections(illegal_sections);
            	readilyShoot.setSituationStatement(situation_statement);
            	readilyShoot.setReportSerialNumber(report_serial_number);
            	readilyShoot.setPassword(password);
            	readilyShoot.setIllegalImg1(illegal_img1);
            	readilyShoot.setIllegalImg2(illegal_img2);
            	readilyShoot.setIllegalImg3(illegal_img3);
            	readilyShoots.add(readilyShoot);
            }//显示数据  
            for(int i=0;i<readilyShoots.size();i++){
            	//psts.setDate(1, new Date(readilyShoots.get(i).getAddDate().getDate()));
            	//psts.setDate(2, new Date(readilyShoots.get(i).getIllegalTime().getDate()));
            	psts.setTimestamp(1, new java.sql.Timestamp(readilyShoots.get(i).getAddDate().getTime()));
            	psts.setTimestamp(2, new java.sql.Timestamp(readilyShoots.get(i).getIllegalTime().getTime()));
            	
            	psts.setString(3, readilyShoots.get(i).getIllegalSections());
            	psts.setString(4, readilyShoots.get(i).getSituationStatement());
            	psts.setString(5, readilyShoots.get(i).getReportSerialNumber());
            	psts.setString(6, readilyShoots.get(i).getPassword());
            	psts.setString(7, readilyShoots.get(i).getIllegalImg1());
            	psts.setString(8, readilyShoots.get(i).getIllegalImg2());
                psts.setString(9, readilyShoots.get(i).getIllegalImg3());
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