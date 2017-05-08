package com.lqlsoftware.ChooseCourse.controller.entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.lqlsoftware.USTBChooseCourse.AlternativeCourses;
import com.lqlsoftware.ChooseCourse.controller.dao.DBManager;
import com.mysql.jdbc.Connection;

public class course extends AlternativeCourses {
	
	 public static Boolean findCourseById(String id, String uid) {
		 Connection conn = (Connection) DBManager.getConnection();
		 PreparedStatement ps = null;
		 ResultSet rs = null;
		 String sql = "SELECT * FROM course WHERE status=0 AND cid=? AND uid=?";
		 try {
			 ps = conn.prepareStatement(sql);
			 ps.setString(1, id);
			 ps.setString(2, uid);
			 rs = ps.executeQuery();
			 if (rs.next()) {
				 return true;
			 } else {
				 return false;
			 }
		 } catch (SQLException e) {
			 e.printStackTrace();
		 } finally {
			 DBManager.free(rs, ps);
			 DBManager.freeConnection(conn);
		 }
		 return false;
	 }
	 
	 private static course instance;
	 
	 public static course getInstance() {
		 if (instance!=null) 
			 return instance;
		 else 
			 return new course();
	 }

	public static Boolean addPlanSuccess(String id, String uid) {
		 Connection conn = (Connection) DBManager.getConnection();
		 PreparedStatement ps = null;
		 ResultSet rs = null;
		 String sql = "UPDATE course SET status=1 WHERE cid=? AND uid=?";
		 try {
			 ps = conn.prepareStatement(sql);
			 ps.setString(1, id);
			 ps.setString(2, uid);
			 ps.execute();
			 return true;
		 } catch (SQLException e) {
			 e.printStackTrace();
		 } finally {
			 DBManager.free(rs, ps);
			 DBManager.freeConnection(conn);
		 }
		 return false;
	 }

}
