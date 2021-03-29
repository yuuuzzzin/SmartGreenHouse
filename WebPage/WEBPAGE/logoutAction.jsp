<%@ page pageEncoding = "utf-8" %>
<%@ page contentType = "text/html; charset = utf-8" %>
<%@ page import="java.sql.*" %>
<html>
<head>
<title>Logout</title>
</head>
<body>
	<%
		session.invalidate();
	%>
	<script>
		location.href = 'main.jsp';
	</script>	
</body>
</html>