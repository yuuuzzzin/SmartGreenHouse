<%@ page contentType = "text/html; charset=utf-8" %>
<%@ page import = "java.sql.DriverManager" %>
<%@ page import = "java.sql.Connection" %>
<%@ page import = "java.sql.Statement" %>
<%@ page import = "java.sql.ResultSet" %>
<%@ page import = "java.sql.SQLException" %>
<%@page import="java.util.Date" %>
<%@page import="java.util.Calendar" %>
<%@page import = "java.text.DateFormat" %>
<%@page import = "java.text.ParseException" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>
<%@page import="org.json.simple.JSONObject"%>
<%

    //커넥션 선언

    Connection con = null;



    try {

        //드라이버 호출, 커넥션 연결

        Class.forName("com.mysql.jdbc.Driver").newInstance();

        con = DriverManager.getConnection("jdbc:mysql://database-1.cudf3zzu3npf.us-east-2.rds.amazonaws.com:3306/mydb", "jeongmin", "97shwjdals!");

    	ResultSet rs = null;
    	
    	String userDate = "2020-05-15";
    	SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
    	      
     	Date form = transFormat.parse(userDate);
    	      
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(form);
    	cal.add(Calendar.DATE, -7);
    	String strDate = transFormat.format(cal.getTime());

        //DB에서 뽑아온 데이터(JSON) 을 담을 객체. 후에 responseObj에 담기는 값

        List templist = new LinkedList();
 

	//전체 데이터 

        //String query = "select datecreated as mdatecreated, pm10Value, pm25Value from dust_airkorea where gps_id='JongRo-Gu'";

        //String query = "select a.datecreated as mdatecreated, b.pm25Value as Drnpm25Value, a.pm10Value as pm10Value, a.pm25Value as pm25Value from dust_airkorea a, dust_drone b where b.gps_id='JongRo-Gu'";



	//시간대별  평균값을 산출

	String query = "select  substring(date,1,10), avg(temp), avg(humi), avg(level) from Sensor where date between '"+strDate+"'  and '"+userDate+"'  group by DATE_FORMAT(date, '%y%m%d')";

	PreparedStatement pstm = con.prepareStatement(query);

        rs = pstm.executeQuery(query);

        

        //ajax에 반환할 JSON 생성

        JSONObject responseObj = new JSONObject();

        JSONObject lineObj = null;

        

	DecimalFormat f1 = new DecimalFormat("");



    	while (rs.next()) {

            	//String mdatecreated = rs.getString("mdatecreated");

            	float temp = rs.getFloat("avg(temp)");
		
		float humi = rs.getFloat("avg(humi)");

		float level = rs.getFloat("avg(level)");

            	String date = rs.getString("substring(date,1,10)");

 	    	lineObj = new JSONObject();

            	lineObj.put("date", date);

            	lineObj.put("temp", (int)temp);

            	lineObj.put("humi", (int)humi);

		lineObj.put("level", (int)level);

            	templist.add(lineObj);

        } 



    responseObj.put("templist", templist);

        out.print(responseObj.toString());

 

    } catch (Exception e) {

        e.printStackTrace();

    } finally {

        if (con != null) {

            try {

                con.close();

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

 

    }

%>