<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="org.json.simple.*, java.sql.*, java.text.SimpleDateFormat, java.util.Date" %>

<%
//초기 선언
   request.setCharacterEncoding("UTF-8");
   JSONObject jsonMain = new JSONObject();
   JSONArray jArray = new JSONArray();
   JSONObject jObject = new JSONObject();
   String id = request.getParameter("id");
   String position = "";
   String info = request.getParameter("info");
   String nickname = request.getParameter("nickname");
   String n = "";
   if(n.equals(id) || n.equals(info) || n.equals(nickname)){
      jObject.put("RESULT", "0");
      // 0 송신
   } else 
   {
      request.setCharacterEncoding("UTF-8");
      Connection conn = null;
      Statement stmt = null;
      try {
         Class.forName("com.mysql.jdbc.Driver");
         conn = DriverManager.getConnection("jdbc:mysql://database-1.cudf3zzu3npf.us-east-2.rds.amazonaws.com:3306/mydb?useUnicode=true&characterEncoding=utf8", "jeongmin", "97shwjdals!");
         if (conn == null) {
            jObject.put("RESULT", "0");
         }
         stmt = conn.createStatement();
               
         SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
         String dateStr = date.format(new Date());

         ResultSet rs = stmt.executeQuery("select position from userplant where id = '" + id + "' order by position DESC limit 1;");
         if(rs.next()) {
            jObject.put("RESULT", "1");
            if (rs.getString("position").equals(id + "1")) {
               position ="2";
            }
            else if (rs.getString("position").equals(id + "2")){
               position ="3";
            }
            else {
               jObject.put("RESULT", "0");
               position="1";
            }
         }else {
            //jObject.put("RESULT", "0");
            position ="1";

         }         
         String query = String.format("insert into userplant(id, position, info, nickname, date) values ('%s','%s', '%s', '%s', '%s');", id, id+position, info, nickname, dateStr);
         int n2 = stmt.executeUpdate(query);
         jObject.put("RESULT", "1");

         
      }
      catch (Exception e) {
         jObject.put("RESULT", "0");
         out.println(e.toString());
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
   }
   jArray.add(0, jObject);
        // 최종적으로 배열을 하나로 묶음
   jsonMain.put("List", jArray);
       
   
        // 안드로이드에 보낼 데이터를 출력
   out.println(jsonMain.toJSONString());
%>