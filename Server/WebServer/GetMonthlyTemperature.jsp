<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="org.json.simple.*, java.sql.*, java.net.URLEncoder" %>
<%@page import="java.util.Date" %>
<%@page import="java.util.Calendar" %>
<%@page import = "java.text.DateFormat" %>
<%@page import = "java.text.ParseException" %>
<%@page import="java.text.SimpleDateFormat" %>

<%
//�ʱ� ����
	JSONObject jsonMain = new JSONObject();
	JSONArray jArray = new JSONArray();
	JSONObject jObject = new JSONObject();

	String id = request.getParameter("id");
	String date = request.getParameter("date");
	String n = "";
	
	SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM");
		
	Date form = transFormat.parse(date);
		
	Calendar cal = Calendar.getInstance();
	cal.setTime(form);
	cal.add(Calendar.MONTH, -12);
	String strDate = transFormat.format(cal.getTime());
	



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
			ResultSet rs = stmt.executeQuery("select  substring(date,6,2), avg(temp) from sensor where date between '"+strDate+"' and  '"+date+"' group by DATE_FORMAT(date, '%y%m')");
			for(int i=1; i<13; i++){
				if(rs.next()) {
					jObject.put("RESULT", "1");
					jObject.put("temp"+i, rs.getString("avg(temp)"));
					jObject.put("date"+i, rs.getString("substring(date,6,2)"));
					jArray.add(jObject);

				}
				else{
					jObject.put("temp"+i, "0");
					jObject.put("date"+i, "0");

				}
				//jObject.put("date", rs.getString("date"));
							            	
            							
			}
			if(n.equals(rs)) {
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
