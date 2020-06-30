<%@ page pageEncoding = "utf-8" %>
<%@ page contentType = "text/html; charset = utf-8" %>
<%@ page import="java.sql.*" %>
<html>
<head>
<title>DB</title>
</head>
<body>

	<%
		Connection conn = null;
		
		String url = "jdbc:mysql://database-1.cudf3zzu3npf.us-east-2.rds.amazonaws.com:3306/mydb";
		String user = "jeongmin";
		String password = "97shwjdals!";
			
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(url, user, password);
		
	%>

</body>
</html>