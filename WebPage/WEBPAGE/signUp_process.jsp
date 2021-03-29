<%@ page pageEncoding = "utf-8" %>
<%@ page contentType = "text/html; charset = utf-8" %>
<%@ page import="java.sql.*" %>
<html>
<head>
<title>DB</title>
</head>
<body>

	<%@ include file = "dbconn.jsp" %>
	
	<%
	request.setCharacterEncoding("utf-8");
	PreparedStatement pstmt = null;
	
	try{
		String sql = "insert into user (id, password, name, phone) values(?,?,?,?)";
		
		String id = request.getParameter("id");
		String passwd = request.getParameter("passwd");
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
	
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1,id);
		pstmt.setString(2,passwd);
		pstmt.setString(3,name);
		pstmt.setString(4,phone);
		pstmt.executeUpdate();
		out.println("테이블 삽입이 성공했습니다.");
		response.sendRedirect("login.jsp");
		
	}catch (SQLException ex){
		out.println(" 테이블 삽입 실패 <br>");
		out.println("SQLException: " +ex.getMessage());
		
		
	}finally{
		if(pstmt != null)
			pstmt.close();
		if(conn != null)
			conn.close();
		
	}
	
	%>
	
</body>
</html>