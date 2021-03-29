<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="org.json.simple.*, java.sql.*, java.net.URLEncoder" %>

<%
//초기 선언
	JSONObject jsonMain = new JSONObject();
	JSONArray jArray = new JSONArray();
	JSONObject jObject = new JSONObject();
	

	String id = request.getParameter("ID");
	String water1 = request.getParameter("WATER1");
	String water2 = request.getParameter("WATER2");
	String water3 = request.getParameter("WATER3");
	String n = "";
	
	if(n.equals(id)) {
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
			int rs = stmt.executeUpdate("update device set water1 = '" + water1 + "', water2 = '" + water2 + "', water3 = '" + water3 + "' where number = '1';");
		
			if(rs != 0) {
				jsonMain.put("RESULT", "1");
				//JSONObject jsonID = new JSONObject();
				//String id = rs.getString(1);
				//jsonMain.put("ID", id);
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

%>