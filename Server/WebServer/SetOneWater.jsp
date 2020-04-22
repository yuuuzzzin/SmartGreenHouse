<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="org.json.simple.*, java.sql.*, java.net.URLEncoder" %>

<%
//초기 선언
	JSONObject jsonMain = new JSONObject();
	JSONArray jArray = new JSONArray();
	JSONObject jObject = new JSONObject();
	

	String id = request.getParameter("ID");
	String water = request.getParameter("WATER");
	String position = request.getParameter("position");
	String n = "";
	
	if(n.equals(id) || n.equals(position)) {
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
						
			if(position.equals("1")){
				int rs1 = stmt.executeUpdate("update device set water1 = '" + water + "' where number = '1';");
				if(rs1 != 0) {
					
					jsonMain.put("RESULT", "1");
					//JSONObject jsonID = new JSONObject();
					//String id = rs.getString(1);
					//jsonMain.put("ID", id);
				}else {
					jsonMain.put("RESULT", "0");
				}
			}
			if(position.equals("2")){
				int rs2 = stmt.executeUpdate("update device set water2 = '" + water + "' where number = '1';");

				if(rs2 != 0) {
				
					jsonMain.put("RESULT", "1");
					//JSONObject jsonID = new JSONObject();
					//String id = rs.getString(1);
					//jsonMain.put("ID", id);
				}else {
					jsonMain.put("RESULT", "0");
				}
			}
			if(position.equals("3")){
				int rs3 = stmt.executeUpdate("update device set water3 = '" + water + "' where number = '1';");

				if(rs3 != 0) {
					jsonMain.put("RESULT", "1");
					//JSONObject jsonID = new JSONObject();
					//String id = rs.getString(1);
					//jsonMain.put("ID", id);
				}else {
					jsonMain.put("RESULT", "0");
				}
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