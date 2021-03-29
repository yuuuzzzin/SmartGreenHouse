<%@ page contentType = "text/html; charset=utf-8" %>
<%@ page import = "java.sql.DriverManager" %>
<%@ page import = "java.sql.Connection" %>
<%@ page import = "java.sql.Statement" %>
<%@ page import = "java.sql.ResultSet" %>
<%@ page import = "java.sql.SQLException" %>
<%@ page import = "java.sql.PreparedStatement"%>



<html>
<head><title>장치기록</title></head>
<body>
     <h1>장치기록</h1> 
	<jsp:include page="DeviceSelect.jsp"/>
      <table width = "100%" border = "1">
      <tr>
            <td>number</td>
            <td>device</td>
            <td>state</td>
            <td>time</td>
            <td>id</td>
      </tr>
<%
	String devicename = request.getParameter("devicename");
	out.println(devicename);
      // 1. JDBC 드라이버 로딩
      Class.forName("com.mysql.jdbc.Driver");

      Connection conn = null; // DBMS와 Java연결객체
      Statement stmt = null; // SQL구문을 실행
      ResultSet rs = null; // SQL구문의 실행결과를 저장
      PreparedStatement pstmt = null;
      
      try
      {

            String query = "select * from DeviceRecord where device=?";
   
            // 2. 데이터베이스 커넥션 생성
            conn = DriverManager.getConnection("jdbc:mysql://database-1.cudf3zzu3npf.us-east-2.rds.amazonaws.com:3306/mydb", "jeongmin", "97shwjdals!");
   
            // 3. Statement 생성
            //stmt = conn.createStatement();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, devicename);
            // 4. 쿼리 실행
            rs = pstmt.executeQuery();
   
            // 5. 쿼리 실행 결과 출력
            while(rs.next())
            {
%>
      <tr>
            <td><%= rs.getString("number") %></td>
            <td><%= rs.getString("device") %></td>
            <td><%= rs.getString("state") %></td>
            <td><%= rs.getString("time") %></td>            
            <td><%= rs.getString("id") %></td>           
          
      </tr>
<%
            }
      }catch(SQLException ex){
            out.println(ex.getMessage());
            ex.printStackTrace();
      }finally{
            // 6. 사용한 Statement 종료
            if(rs != null) try { rs.close(); } catch(SQLException ex) {}
            if(pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
   
            // 7. 커넥션 종료
            if(conn != null) try { conn.close(); } catch(SQLException ex) {}
      }
%>
      </table>
</body>
</html>