<%@ page contentType = "text/html; charset=euc-kr" %>
<%@ page import = "java.sql.DriverManager" %>
<%@ page import = "java.sql.Connection" %>
<%@ page import = "java.sql.Statement" %>
<%@ page import = "java.sql.ResultSet" %>
<%@ page import = "java.sql.SQLException" %>

<html>
<head><title>������</title></head>
<body>
      <h1><b>���� ������</b></h1>
      <table width = "100%" border = "1">
      <tr>
            <td>date</td>
            <td>humi</td>
            <td>temp</td>
            <td>soil1</td>
            <td>soil2</td>
            <td>soil3</td>
            <td>cds</td>
            <td>level</td>
      </tr>
 
<%
      // 1. JDBC ����̹� �ε�
      Class.forName("com.mysql.jdbc.Driver");

      Connection conn = null; // DBMS�� Java���ᰴü
      Statement stmt = null; // SQL������ ����
      ResultSet rs = null; // SQL������ �������� ����
 
      try
      {

   
            String query = "select * from Sensor order by date";
   
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
            <td><%= rs.getString("date") %></td>
            <td><%= rs.getString("humi") %></td>
            <td><%= rs.getString("temp") %></td>
            <td><%= rs.getString("soil1") %></td>       
            <td><%= rs.getString("soil2") %></td>
            <td><%= rs.getString("soil3") %></td>  
            <td><%= rs.getString("cds") %></td>   
            <td><%= rs.getString("level") %></td>
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