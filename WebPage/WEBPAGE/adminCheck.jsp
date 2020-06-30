<%@ page pageEncoding = "euc-kr" %>
<%@ page contentType = "text/html; charset = utf-8" %>
<%@ page import="java.sql.*" %>
<html>
<head>
<title>DB</title>
</head>
<body>

   <%@ include file = "dbconn.jsp" %>
   <%
   
   ResultSet rs = null;
   PreparedStatement pstmt = null;
   
   try{
      request.setCharacterEncoding("utf-8");
      String sql = "select * from administrator where id = ?";   

      String id = request.getParameter("id");
      String passwd = request.getParameter("password");
      
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,id);
      rs = pstmt.executeQuery();
      
      if(rs.next()){
         if(passwd.equals(rs.getString("password"))){
            session.setAttribute("id",id); // 세션에 "id"이름으로 id 등록
            out.println("관리자 계정으로 로그인 성공");
            response.sendRedirect("AdminSelect.jsp"); // 성공 후 관리자 페이지 이동
         }else{
            out.println("로그인 실패");
         }
      }  
      
   }catch (SQLException ex){
      out.println("SQLException: " +ex.getMessage());
   }finally{
      if (rs !=null)
         rs.close();
      if(pstmt != null)
         pstmt.close();
      if(conn != null)
         conn.close();
   }

   %>
   
</body>
</html>