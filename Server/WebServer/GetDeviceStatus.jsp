<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="org.json.simple.*, java.sql.*, java.net.URLEncoder" %>

<%
//�ʱ� ����
	JSONObject jsonMain = new JSONObject();
	JSONArray jArray = new JSONArray();
	JSONObject jObject = new JSONObject();

	String id = request.getParameter("ID");
	String n = "";
	
	if(n.equals(id)) {
		jObject.put("RESULT", "0");
		// 0 �۽�
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
			ResultSet rs = stmt.executeQuery("select led, fan, water1, water2, water3 from device");
			if(rs.next()) {
				jObject.put("RESULT", "1");
				jObject.put("led", rs.getString("led"));
				jObject.put("fan", rs.getString("fan"));
				jObject.put("water1", rs.getString("water1"));
				jObject.put("water2", rs.getString("water2"));
				jObject.put("water3", rs.getString("water3"));


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

        // ���������� �迭�� �ϳ��� ����
	jsonMain.put("List", jArray);
	
        // �ȵ���̵忡 ���� �����͸� ���
	out.println(jsonMain.toJSONString());
%>
