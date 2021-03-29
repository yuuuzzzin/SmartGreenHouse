<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="org.json.simple.*, java.sql.*, java.net.URLEncoder" %>

<%
//초기 선언
	JSONObject jsonMain = new JSONObject();
	JSONArray jArray = new JSONArray();
	JSONObject jObject = new JSONObject();
	

	String id = request.getParameter("id");
	String led = request.getParameter("led");
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
			String query = String.format("update device set led = "+ led +" where number = '1';");

			int n2 = stmt.executeUpdate(query);

			jObject.put("RESULT", "1");

		}catch (Exception e) {
			jObject.put("RESULT", "0");
			out.println(e.toString());
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