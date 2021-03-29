<%@ page contentType = "text/html; charset=euc-kr" %>
<%@ page import = "java.sql.DriverManager" %>
<%@ page import = "java.sql.Connection" %>
<%@ page import = "java.sql.Statement" %>
<%@ page import = "java.sql.ResultSet" %>
<%@ page import = "java.sql.SQLException" %>
<%@ page import = "java.sql.PreparedStatement"%>



<html>
<head><title>����� �½�</title></head>
<body>
     <h1>����� �Ĺ� ���</h1> 
      <table width = "100%" border = "1">
      <tr>
            <td>id</td>
            <td>position</td>
            <td>info</td>
            <td>nickname</td>
            <td>date</td>
      </tr>
<%
      // 1. JDBC ����̹� �ε�
      Class.forName("com.mysql.jdbc.Driver");

      Connection conn = null; // DBMS�� Java���ᰴü
      Statement stmt = null; // SQL������ ����
      ResultSet rs = null; // SQL������ �������� ����
 
      try
      {

   
            String query = "select * from userplant order by id";
   
            // 2. �����ͺ��̽� Ŀ�ؼ� ����
            conn = DriverManager.getConnection("jdbc:mysql://database-1.cudf3zzu3npf.us-east-2.rds.amazonaws.com:3306/mydb", "jeongmin", "97shwjdals!");
   
            // 3. Statement ����
            stmt = conn.createStatement();
   
            // 4. ���� ����
            rs = stmt.executeQuery(query);
   
            // 5. ���� ���� ��� ���
            while(rs.next())
            {
%>
      <tr>
            <td><%= rs.getString("id") %></td>
            <td><%= rs.getString("position") %></td>
            <td><%= rs.getString("info") %></td>
            <td><%= rs.getString("nickname") %></td>            
            <td><%= rs.getString("date") %></td>
          
          
      </tr>
<%
            }
      }catch(SQLException ex){
            out.println(ex.getMessage());
            ex.printStackTrace();
      }finally{
            // 6. ����� Statement ����
            if(rs != null) try { rs.close(); } catch(SQLException ex) {}
            if(stmt != null) try { stmt.close(); } catch(SQLException ex) {}
   
            // 7. Ŀ�ؼ� ����
            if(conn != null) try { conn.close(); } catch(SQLException ex) {}
      }
%>
      </table>
</body>
</html>