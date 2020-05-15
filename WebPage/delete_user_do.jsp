<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR" import="org.json.simple.*, java.sql.*, java.text.SimpleDateFormat, java.util.Date" %>

<%
//초기 선언
	request.setCharacterEncoding("EUC-KR");
	JSONObject jsonMain = new JSONObject();
	JSONArray jArray = new JSONArray();
	JSONObject jObject = new JSONObject();
	String id = request.getParameter("ID");
	
	String nl = "";
	if(nl.equals(id)) {
		jObject.put("RESULT", "0");
		// 0 송신
	} else {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://database-1.cudf3zzu3npf.us-east-2.rds.amazonaws.com:3306/mydb", "jeongmin", "97shwjdals!");
			if (conn == null) {
				jObject.put("RESULT", "0");
			}
			
			stmt = conn.createStatement();
			int rs = stmt.executeUpdate("delete from user where id  = '" + id + "';");
			if(rs != 0) {
				jsonMain.put("RESULT", "1");
			}
			else {
				jsonMain.put("RESULT", "0");
			}
		}
		finally {
			try {
				stmt.close();
			} catch (Exception ignored) {
			}
			try {
				conn.close();
			} catch (Exception ignored) {
			}
		}
	}
	
        // 안드로이드에 보낼 데이터를 출력
	out.println(jsonMain.toJSONString());

	response.sendRedirect("UserManagement.jsp");

%>