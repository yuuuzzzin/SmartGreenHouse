<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="org.json.simple.*, java.sql.*, java.net.URLEncoder" %>

<%
//�ʱ� ����
	JSONObject jsonMain = new JSONObject();
	JSONArray jArray = new JSONArray();
	JSONObject jObject = new JSONObject();

	String id = request.getParameter("id");
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
			ResultSet rs1 = stmt.executeQuery("select nickname, position from userplant where id = '" + id + "' ");
			
			int i = 0;
			while(rs1.next()) {
				JSONObject innerjObject = new JSONObject();
				innerjObject.put("RESULT", "1");
				innerjObject.put("nickname", rs1.getString("nickname"));
				innerjObject.put("position", rs1.getString("position"));				            	
            			jArray.add(i, innerjObject);
				i++;
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

	//jArray.add(0, jObject);

        // ���������� �迭�� �ϳ��� ����
	jsonMain.put("List", jArray);
	
        // �ȵ���̵忡 ���� �����͸� ���
	out.println(jsonMain.toJSONString());
%>
