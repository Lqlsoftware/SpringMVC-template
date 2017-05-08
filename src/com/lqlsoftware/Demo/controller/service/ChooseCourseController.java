package com.lqlsoftware.ChooseCourse.controller.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lqlsoftware.USTBChooseCourse.AlternativeCourses;
import com.lqlsoftware.USTBChooseCourse.ChooseCourse;
import com.lqlsoftware.ChooseCourse.controller.dao.DBManager;
import com.lqlsoftware.ChooseCourse.controller.entity.course;
import com.mysql.jdbc.Connection;

@Controller
public class ChooseCourseController {
	
	@RequestMapping(value="/ChooseCourse")
	public String ChooseCourse (HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return "/ChooseCourse";
	}
	
	@RequestMapping(value="/ChooseCourse.login")
	public String ChooseCourseLogin (HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 字符编码转换 解决页面中文数据传递
		request.setCharacterEncoding("UTF-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		
		// 获取用户请求中的参数
		String UserName = request.getParameter("loginName");
		String Password = request.getParameter("loginPwd");
		
		ChooseCourse cc = new ChooseCourse();
		cc.setUsername(UserName);
		cc.setPassword(Password);
		if (cc.getLogin()){
			ArrayList<AlternativeCourses> AC = cc.getAlternativeCourses();
			ArrayList<AlternativeCourses> Course = new ArrayList<AlternativeCourses>(); 
			for (AlternativeCourses ac : AC) {
				if (ac.getSKRS()<ac.getKRL()) {
					String JSM = ac.getJSM();
					ac.setJSM(JSM.substring(JSM.indexOf("JSM")+6, JSM.length()-3));
					Course.add(ac);
				}
			}
			request.getSession().setAttribute("UserName", UserName);
			request.getSession().setAttribute("Password", Password);
			request.setAttribute("Course", Course);
			return "/ChooseCourseList";
		} else {
			request.setAttribute("Error", "登陆失败");
			return "/ChooseCourse";
		}
	}
	
	@RequestMapping(value="/ChooseCourse.logout")
	public String ChooseCourseLogout (HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return "/ChooseCourse";
	}
	
	@RequestMapping(value="/courseList")
	public String courseList (HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ChooseCourse cc = new ChooseCourse();
		cc.setUsername((String) request.getSession().getAttribute("UserName"));
		cc.setPassword((String) request.getSession().getAttribute("Password"));
		if (cc.getLogin()){
			ArrayList<AlternativeCourses> AC = cc.getAlternativeCourses();
			ArrayList<AlternativeCourses> Course = new ArrayList<AlternativeCourses>(); 
			for (AlternativeCourses ac : AC) {
				if (ac.getSKRS()<ac.getKRL()) {
					if (course.findCourseById(ac.getID(),(String) request.getSession().getAttribute("UserName"))) {
						if (cc.addCourses(ac)) {
							System.out.println(ac.getKCM() + " 选课成功");
							course.addPlanSuccess(ac.getID(),(String) request.getSession().getAttribute("UserName"));
						} else {
							System.out.println(ac.getKCM() + " 选课失败");
						}
					}
					String JSM = ac.getJSM();
					ac.setJSM(JSM.substring(JSM.indexOf("JSM")+6, JSM.length()-3));
					Course.add(ac);
				}
			}
			request.setAttribute("Course", Course);
			return "/ChooseCourseList";
		} else {
			request.setAttribute("Error", "登陆失败");
			return "/ChooseCourse";
		}
	}
	
	@RequestMapping(value="/addCourse")
	public String addCourse (HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 字符编码转换 解决页面中文数据传递
		request.setCharacterEncoding("UTF-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		String id = request.getParameter("id");
		ChooseCourse cc = new ChooseCourse();
		cc.setUsername((String) request.getSession().getAttribute("UserName"));
		cc.setPassword((String) request.getSession().getAttribute("Password"));
		if (cc.getLogin()){
			ArrayList<AlternativeCourses> AC = cc.getAlternativeCourses();
			for (AlternativeCourses ac : AC) {
				if (ac.getSKRS()<ac.getKRL()) {
					if (ac.getID().equals(id)){
						if (cc.addCourses(ac)) {
							request.setAttribute("name", "选课成功！");
							request.setAttribute("url", "courseList");
							return "/frame/success";
						}
					}
				}
			}
		} else {
			request.setAttribute("name", "选课失败！");
			request.setAttribute("url", "courseList");
			return "/frame/success";
		}
		request.setAttribute("name", "选课失败！");
		request.setAttribute("url", "courseList");
		return "/frame/success";
	}
	
	@RequestMapping(value="/addCoursePlan")
	public String addCoursePlan (HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 字符编码转换 解决页面中文数据传递
		request.setCharacterEncoding("UTF-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		String id = request.getParameter("id");
		ChooseCourse cc = new ChooseCourse();
		cc.setUsername((String) request.getSession().getAttribute("UserName"));
		cc.setPassword((String) request.getSession().getAttribute("Password"));
		if (cc.getLogin()){
			ArrayList<AlternativeCourses> AC = cc.getAlternativeCourses();
			for (AlternativeCourses ac : AC) {
					if (ac.getID().equals(id)){
						String sql = "insert into course (cid,name,uid,status)values(?,?,?,0)";
						// 获取与数据库的连接
						Connection conn = (Connection) DBManager.getConnection();
						PreparedStatement ps = null;
						ResultSet rs = null;
						try {
							ps = conn.prepareStatement(sql);
							ps.setString(1, id);
							ps.setString(2, ac.getKCM());
							ps.setString(3, (String) request.getSession().getAttribute("UserName"));
							ps.execute();
						} catch (SQLException e) {
							e.printStackTrace();
						} finally {
							DBManager.free(rs, ps);
							DBManager.freeConnection(conn);
						}
						request.setAttribute("name", "加入待选列表成功！");
						request.setAttribute("url", "courseList");
						return "/frame/success";
					}
				}
			}
		else {
			request.setAttribute("name", "选课失败！");
			request.setAttribute("url", "courseList");
			return "/frame/success";
		}
		return id;
	}
}
