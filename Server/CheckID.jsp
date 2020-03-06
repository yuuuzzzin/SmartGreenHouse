<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="org.json.simple.*, java.sql.*" %>

<%
//초기 선언
	JSONObject jsonMain = new JSONObject();
	JSONArray jArray = new JSONArray();
	JSONObject jObject = new JSONObject();
	String id = request.getParameter("ID");
	if(id.equals("")) {
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
			ResultSet rs = stmt.executeQuery("select id from user where id = '" + id + "';");
			if(rs.next()) {
				jObject.put("RESULT", "1");			}
			else {
				jObject.put("RESULT", "0");
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
	jArray.add(0, jObject);
        // 최종적으로 배열을 하나로 묶음
	jsonMain.put("List", jArray);
	
        // 안드로이드에 보낼 데이터를 출력
	out.println(jsonMain.toJSONString());
%>