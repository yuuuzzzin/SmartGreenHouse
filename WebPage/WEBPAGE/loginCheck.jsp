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
   
   ResultSet rs = null;
   PreparedStatement pstmt = null;
   
   try{
      request.setCharacterEncoding("utf-8");
      String sql = "select * from user where id = ?";   

      String id = request.getParameter("id");
      String passwd = request.getParameter("password");
      
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,id);
      rs = pstmt.executeQuery();
      
      if(rs.next()){
         if(passwd.equals(rs.getString("password"))){
            session.setAttribute("id",id); // 세션에 "id"이름으로 id 등록
            out.println("로그인 성공");
            response.sendRedirect("main.jsp"); // 성공 후 메인페이지 이동
         }else{
            out.println("로그인 실패");
         }
      }else 
         out.println("회원가입 필요");   
      
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