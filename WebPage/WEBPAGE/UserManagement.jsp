<%@ page contentType = "text/html; charset=utf-8" %>
<%@ page import = "java.sql.DriverManager" %>
<%@ page import = "java.sql.Connection" %>
<%@ page import = "java.sql.Statement" %>
<%@ page import = "java.sql.ResultSet" %>
<%@ page import = "java.sql.SQLException" %>
<%@ page import = "java.sql.PreparedStatement"%>

<html>
<head>
<title>회원 목록</title></head>
<body>
      <h1>회원관리</h1>
      <table width = "100%" border = "1">
      <tr>
            <td>id</td>
            <td>password</td>
            <td>name</td>
            <td>phone</td>
            <td>regdate</td>
            <td>비고</td>
      </tr>
 
<%
      // 1. JDBC 드라이버 로딩
      Class.forName("com.mysql.jdbc.Driver");

      Connection conn = null; // DBMS와 Java연결객체
      Statement stmt = null; // SQL구문을 실행
      ResultSet rs = null; // SQL구문의 실행결과를 저장
  
      try
      {

   
            String query = "select * from user order by regdate";
   
            // 2. 데이터베이스 커넥션 생성
            conn = DriverManager.getConnection("jdbc:mysql://database-1.cudf3zzu3npf.us-east-2.rds.amazonaws.com:3306/mydb", "jeongmin", "97shwjdals!");
   
            // 3. Statement 생성
            stmt = conn.createStatement();
   
            // 4. 쿼리 실행
            rs = stmt.executeQuery(query);
   
            // 5. 쿼리 실행 결과 출력
            while(rs.next())
            {
%>
      <tr>
            <td><%= rs.getString("id") %></td>
            <td><%= rs.getString("password") %></td>
            <td><%= rs.getString("name") %></td>
            <td><%= rs.getString("phone") %></td>            
            <td><%= rs.getString("regdate") %></td>
            <td><a href="delete_user_do.jsp?ID=<%=rs.getString("id")%>">삭제</a></td>       

      </tr>
<%
            }
      }catch(SQLException ex){
            out.println(ex.getMessage());
            ex.printStackTrace();
      }finally{
            // 6. 사용한 Statement 종료
            if(rs != null) try { rs.close(); } catch(SQLException ex) {}
            if(stmt != null) try { stmt.close(); } catch(SQLException ex) {}
   
            // 7. 커넥션 종료
            if(conn != null) try { conn.close(); } catch(SQLException ex) {}
      }
%>
      </table>
</body>
</html>