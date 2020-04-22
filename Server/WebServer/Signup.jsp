<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR" import="org.json.simple.*, java.sql.*, java.text.SimpleDateFormat, java.util.Date" %>

<%
//초기 선언
	request.setCharacterEncoding("EUC-KR");
	JSONObject jsonMain = new JSONObject();
	JSONArray jArray = new JSONArray();
	JSONObject jObject = new JSONObject();
	String id = request.getParameter("ID");
	String password = request.getParameter("PASSWORD");
	String name = request.getParameter("NAME");
	String phone = request.getParameter("PHONE");
	
	String nl = "";
	if(nl.equals(id) || nl.equals(password) || nl.equals(name) || nl.equals(phone)) {
		jObject.put("RESULT", "0");
		// 0 송신
	} 	else 
	{
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://database-1.cudf3zzu3npf.us-east-2.rds.amazonaws.com:3306/mydb", "jeongmin", "97shwjdals!");
			if (conn == null) {
				jObject.put("RESULT", "0");
			}
			stmt = conn.createStatement();
			//ResultSet rs = stmt.executeQuery("select id, password from user where id = '" + id + "' && password = '" + password + "';");
			
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
			String dateStr = date.format(new Date());
			String query = String.format("insert into user(id, password, name, phone, regdate) values ('%s', '%s', '%s', '%s', '%s');", id, password, name, phone, dateStr);
			int n = stmt.executeUpdate(query);
			jObject.put("RESULT", "1");
		}
		catch (Exception e) {
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