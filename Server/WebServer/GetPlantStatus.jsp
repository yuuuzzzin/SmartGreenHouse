<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="org.json.simple.*, java.sql.*, java.net.URLEncoder" %>

<%
//초기 선언
	JSONObject jsonMain = new JSONObject();
	JSONArray jArray = new JSONArray();
	JSONObject jObject = new JSONObject();

	String id = request.getParameter("id");
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
			ResultSet rs = stmt.executeQuery("select date, humi, temp, soil1, soil2, soil3, cds, level from sensor");
			if(rs.next()) {
				jObject.put("RESULT", "1");
				jObject.put("date", rs.getString("date"));
				jObject.put("humi", rs.getString("humi"));
				jObject.put("temp", rs.getString("temp"));
				jObject.put("soil1", rs.getString("soil1"));
				jObject.put("soil2", rs.getString("soil2"));
				jObject.put("soil3", rs.getString("soil3"));
				jObject.put("cds", rs.getString("cds"));
				jObject.put("level", rs.getString("level"));
				            	
            			jArray.add(jObject);
				
			}
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
