package com.lqlsoftware.ChooseCourse.controller.dao;

import java.sql.Connection;  
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.alibaba.druid.pool.DruidDataSource;

public class DBManager { 
      
    // 鏁版嵁搴撻┍鍔�
    private static String driver = "com.mysql.jdbc.Driver";  
    
    // 鏁版嵁搴搖rl
    private static String url = "jdbc:mysql://localhost:3306/mm?useUnicode=true&characterEncoding=UTF-8";
    
	// 鏁版嵁搴撶敤鎴峰悕 root
	private static String userName = "root";
	
	// 鏁版嵁搴撶殑鐢ㄦ埛瀵嗙爜
	private static String userPassword = "Lqlluyuli123";
	
	// 姹犲惎鍔ㄦ椂鍒涘缓鐨勮繛鎺ユ暟閲�
	private static int InitialSize = 1;
	
	// 姹犻噷涓嶄細琚噴鏀剧殑鏈�皯绌洪棽杩炴帴鏁伴噺 0琛ㄧず鏃犻檺鍒�
	private static int MinIdle = 1;
	
	// 鍚屼竴鏃堕棿鍙互浠庢睜鍒嗛厤鐨勬渶澶氳繛鎺ユ暟閲�0琛ㄧず鏃犻檺鍒�
	private static int MaxActive = 100;
	
	// 鏈�ぇ绛夊緟鏃堕棿 ms
	private static int MaxWait = 60000;
	
	// 涓�釜杩炴帴鍦ㄦ睜涓渶灏忕敓瀛樼殑鏃堕棿 ms
	private static long TimeBetweenEvictionRunsMillis = 60000;
	
	// 绌洪棽杩炴帴鑰楀敖 瓒呮椂鐨勬湭鍏抽棴鐨勮繛鎺ヨ閲婃斁
	private static boolean RemoveAbandoned = true;
	
	// 绌洪棽杩炴帴鑰楀敖 瓒呰繃姝ゆ椂闂寸殑鏈叧闂殑杩炴帴灏嗚閲婃斁
	private static long RemoveAbandonedTimeoutMillis = 6000;
	
    // 鏁版嵁婧� 
    private static DruidDataSource DruidDS = null;  
    
    
    public static String getDriver() {
		return driver;
	}

	public static void setDriver(String driver) {
		DBManager.driver = driver;
	}

	public static String getUrl() {
		return url;
	}

	public static void setUrl(String url) {
		DBManager.url = url;
	}

	public static String getUserName() {
		return userName;
	}

	public static void setUserName(String userName) {
		DBManager.userName = userName;
	}

	public static String getUserPassword() {
		return userPassword;
	}

	public static void setUserPassword(String userPassword) {
		DBManager.userPassword = userPassword;
	}

	public static int getInitialSize() {
		return InitialSize;
	}

	public static void setInitialSize(int initialSize) {
		InitialSize = initialSize;
	}

	public static int getMinIdle() {
		return MinIdle;
	}

	public static void setMinIdle(int minIdle) {
		MinIdle = minIdle;
	}

	public static int getMaxActive() {
		return MaxActive;
	}

	public static void setMaxActive(int maxActive) {
		MaxActive = maxActive;
	}

	public static int getMaxWait() {
		return MaxWait;
	}

	public static void setMaxWait(int maxWait) {
		MaxWait = maxWait;
	}

	public static long getTimeBetweenEvictionRunsMillis() {
		return TimeBetweenEvictionRunsMillis;
	}

	public static void setTimeBetweenEvictionRunsMillis(
			long timeBetweenEvictionRunsMillis) {
		TimeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public static boolean isRemoveAbandoned() {
		return RemoveAbandoned;
	}

	public static void setRemoveAbandoned(boolean removeAbandoned) {
		RemoveAbandoned = removeAbandoned;
	}

	public static long getRemoveAbandonedTimeoutMillis() {
		return RemoveAbandonedTimeoutMillis;
	}

	public static void setRemoveAbandonedTimeoutMillis(
			long removeAbandonedTimeoutMillis) {
		RemoveAbandonedTimeoutMillis = removeAbandonedTimeoutMillis;
	}

	public static DruidDataSource getDruidDS() {
		return DruidDS;
	}

	static {
        DruidDS = initDataSource();
    }
    
	protected static DruidDataSource initDataSource() {
    	
    	try {
	        DruidDataSource DruidDS = new DruidDataSource();
	        
	        DruidDS.setDriverClassName(driver);
	        DruidDS.setUrl(url);
	        DruidDS.setUsername(userName);
	        DruidDS.setPassword(userPassword);
	        
	        DruidDS.setInitialSize(InitialSize);
	        DruidDS.setMinIdle(MinIdle);
	        DruidDS.setMaxActive(MaxActive);
	        DruidDS.setMaxWait(MaxWait);
	        DruidDS.setTimeBetweenEvictionRunsMillis(TimeBetweenEvictionRunsMillis);
	        
	        DruidDS.setRemoveAbandoned(RemoveAbandoned);
	        DruidDS.setRemoveAbandonedTimeoutMillis(RemoveAbandonedTimeoutMillis);
	        
	        return DruidDS;
    	} catch(Exception e){
        	e.printStackTrace();
            return null;
        }
	}
    
	protected static void destoryDataSource() {
		
    	try{
        	DruidDS.close();
            DruidDS = null;
        }
        catch(Exception e){
        	e.printStackTrace();  
        }
	}
    
	public synchronized static Connection getConnection() {
		
    	try{
        	if (DruidDS != null) {
        		Connection conn = DruidDS.getConnection().getMetaData().getConnection();
        		return conn;
        	} else {
        		return null;
        	}
        }
        catch(Exception e){
        	e.printStackTrace();
            return null;
        }
	}
    
    public static void freeConnection(Connection conn) {
    	
    	try{
    		if(conn!=null){
    			conn.close();
    		}
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public static void free(ResultSet rs, PreparedStatement ps) {
    
    	try{
    		if(rs!=null){
    			rs.close();
    		}
    		if(ps!=null){
    			ps.close();
    		}
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }
}