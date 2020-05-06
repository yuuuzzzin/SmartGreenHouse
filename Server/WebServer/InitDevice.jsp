<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="org.json.simple.*, java.sql.*, java.net.URLEncoder" %>

<%
//초기 선언
	JSONObject jsonMain = new JSONObject();
	JSONArray jArray = new JSONArray();
	JSONObject jObject = new JSONObject();
	

	String id = request.getParameter("ID");
	String device = request.getParameter("device");

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
			if(device.equals("FAN")){
				String queryFan = "Delete from fan where number = ('2')";
				int n2 = stmt.executeUpdate(queryFan);

         			if(n2 != 0) {
					jsonMain.put("RESULT", "1");
				}
				else {
					jsonMain.put("RESULT", "0");
				}
			}
			else if(device.equals("LED")){
				String queryLed = "Delete from led where number = ('2')";
				int n3 = stmt.executeUpdate(queryLed);
				if(n3 != 0) {
					jsonMain.put("RESULT", "1");
				}
				else {
					jsonMain.put("RESULT", "0");
				}
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
	out.println(jsonMain.toJSONString());
	
        // 안드로이드에 보낼 데이터를 출력

%>