<%@ page language="java" contentType="text/html; charset=UTF-8" 
 pageEncoding="UTF-8" import="org.json.simple.*, java.sql.*" %>

<%
//초기 선언
   JSONObject jsonMain = new JSONObject();
   JSONArray jArray = new JSONArray();
   JSONObject jObject = new JSONObject();
   String id = request.getParameter("id");
   int check = -1;
   if(id.equals("")) {
      jObject.put("RESULT", "0");
      // 0 송신
   } else {
      Connection conn = null;
      Statement stmt = null;
      try {
         String url = "jdbc:mysql://database-1.cudf3zzu3npf.us-east-2.rds.amazonaws.com:3306/mydb";
         String user = "jeongmin";
         String password = "97shwjdals!";            
         Class.forName("com.mysql.jdbc.Driver");
         conn = DriverManager.getConnection(url, user, password);
         
         if (conn == null) {
            jObject.put("RESULT", "0");
         }
         stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("select id from user where id = '" + id + "';");
         if(rs.next()) {
            check=1;      } 
         else {
            check=0;
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
      if(check==1)
      {
         %>
      <b><font color="red"> <%=id%> </font>는 이미 사용중인 아이디입니다.</b>
      <form name="checkForm" method="post" action="CheckID.jsp">
      <b>다른 아이디를 선택하세요. </b> <br/> <br/>
      <input type="text" name="id" />
      <input type="submit" value="ID중복확인"/>
      </form>
      <%
      }
      else
      {
      %><center>
      <b>입력하신 <font color="red"><%=id%></font>는 <br/>
      사용하실 수 있는 ID입니다.
   <input type="button" value="닫기" OnClick="setid()">
   </center>
    <%
      }
    %>
      <script language="javascript">
     
         function setid()
         {
		opener.document.CheckID.id.value="<%=id%>";
      		window.opener.close();
            
        		
         }
      </script>
   <%
   }
   

   jArray.add(0, jObject);

        // 최종적으로 배열을 하나로 묶음
   jsonMain.put("List", jArray);
   
%>