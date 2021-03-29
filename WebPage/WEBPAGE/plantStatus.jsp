<%@ page contentType = "text/html; charset=euc-kr" %>
<%@ page import = "java.sql.DriverManager" %>
<%@ page import = "java.sql.Connection" %>
<%@ page import = "java.sql.Statement" %>
<%@ page import = "java.sql.ResultSet" %>
<%@ page import = "java.sql.SQLException" %>
<%@ page import = "java.sql.PreparedStatement"%>
<html lang="ko">
   <head>
      <meta charset="utf-8">
    <title>Greenery</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link href="https://fonts.googleapis.com/css?family=DM+Sans:300,400,700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="fonts/icomoon/style.css">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/animate.min.css">
    <link rel="stylesheet" href="css/jquery.fancybox.min.css">
    <link rel="stylesheet" href="css/owl.carousel.min.css">
    <link rel="stylesheet" href="css/owl.theme.default.min.css">
    <link rel="stylesheet" href="fonts/flaticon/font/flaticon.css">
    <link rel="stylesheet" href="css/aos.css">

    <!-- MAIN CSS -->
    <link rel="stylesheet" href="css/style.css">
  </head>
  <body data-spy="scroll" data-target=".site-navbar-target" data-offset="300">
  
 <%	
	String userID = null;
  	if(session.getAttribute("id") != null){
  		userID = (String) session.getAttribute("id");
  	}

 	String date = null, temp = null,  humi = null, soil1 = null, soil2 = null, soil3 = null, level = null;
      // 1. JDBC 드라이버 로딩
      Class.forName("com.mysql.jdbc.Driver");

      Connection conn = null; // DBMS와 Java연결객체
      Statement stmt = null; // SQL구문을 실행
      ResultSet rs = null; // SQL구문의 실행결과를 저장
 
      try
      {
		
   	    String query = "select date, humi, temp, soil1, soil2, soil3, cds, level from Sensor order by date desc limit 1";
   
            // 2. 데이터베이스 커넥션 생성
            conn = DriverManager.getConnection("jdbc:mysql://database-1.cudf3zzu3npf.us-east-2.rds.amazonaws.com:3306/mydb", "jeongmin", "97shwjdals!");
   
            // 3. Statement 생성
            stmt = conn.createStatement();
   
            // 4. 쿼리 실행
            rs = stmt.executeQuery(query);
   
            // 5. 쿼리 실행 결과 출력
            while(rs.next())
            {
            	date = rs.getString("date");
		temp = rs.getString("temp");
		humi = rs.getString("humi");
		soil1 = rs.getString("soil1");
		soil2 = rs.getString("soil2");
		soil3 = rs.getString("soil3");
		level = rs.getString("level");
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

	if(Integer.parseInt(soil1) < 100){
                            soil1 = "촉촉";
                        }else if (100 <= Integer.parseInt(soil1) && Integer.parseInt(soil1) <600){
                            soil1 = "적당함";
                        }else{
                            soil1 = "마름";
                        }
	if(Integer.parseInt(soil2) < 100){
                            soil2 = "촉촉";
                        }else if (100 <= Integer.parseInt(soil2) && Integer.parseInt(soil2) <600){
                            soil2 = "적당함";
                        }else{
                            soil2 = "마름";
                        }
              if(Integer.parseInt(soil3) < 100){
                            soil3 = "촉촉";
                        }else if (100 <= Integer.parseInt(soil3) && Integer.parseInt(soil3) <600){
                            soil3 = "적당함";
                        }else{
                            soil3 = "마름";
                        }

%>
  

    
    <div class="site-wrap" id="home-section">

      <div class="site-mobile-menu site-navbar-target">
        <div class="site-mobile-menu-header">
          <div class="site-mobile-menu-close mt-3">
            <span class="icon-close2 js-menu-toggle"></span>
          </div>
        </div>
        <div class="site-mobile-menu-body"></div>
      </div>
      <header class="site-navbar site-navbar-target" role="banner">
        <div class="container">
          <div class="row align-items-center position-relative">

            <div class="col-lg-4">
              <nav class="site-navigation text-right ml-auto " role="navigation">
                <ul class="site-menu main-menu js-clone-nav ml-auto d-none d-lg-block">
                  <li><a href="main.jsp" class="nav-link">Home</a></li>
                  <li><a href="plantStatus.jsp" class="nav-link">Plant Status</a></li>
                  <li><a href="gardenList.jsp" class="nav-link">Plant Info</a></li>
                </ul>
              </nav>
            </div>
            <div class="col-lg-4 text-center">
              <div class="bold"><h3>Greenery</h3></a>
              </div>
              <div class="ml-auto toggle-button d-inline-block d-lg-none"><a href="#" class="site-menu-toggle py-5 js-menu-toggle text-white"><span class="icon-menu h3 text-white"></span></a></div>
            </div>
            <div class="col-lg-4">
              <nav class="site-navigation text-left mr-auto " role="navigation">
                <ul class="site-menu main-menu js-clone-nav ml-auto d-none d-lg-block">
                  <li><a href="chart.jsp" class="nav-link">Graph</a></li>
                  <li><a href="deviceHistory.jsp" class="nav-link">Device History</a></li>
                  <%
                 	 if(userID == null) {
                  %>
                  <li><a href="login.jsp" class="nav-link">Login</a></li>
                  <% }else{ %>
                  <li><a href="logoutAction.jsp" class="nav-link">Logout</a></li>
                <% } %>                </ul>
              </nav>
            </div>
          </div>
        </div>
      </header>
    <div class="ftco-blocks-cover-1">
      <div class="ftco-cover-1 overlay" style="background-image: url('images/hero_1.jpg')">
        <div class="container">
          <div class="row align-items-center justify-content-center">
        
          </div>
        </div>
      </div>
    </div>    
    
    <div class="site-section">
      <div class="container">
        <div class="row">
          <div class="col-md-12 blog-content">
	   <h1 class="mb-2">현재 온실 상태</h1>
	    <p> <%=date%></p>
            <div class="card-deck">
            	<div class="card text-white bg-success mb-4">
                  <div class="card-header">온도</div>
                  <div class="card-body">
                     <h1 class="card-title"><td><%=temp%>℃</td> </h1>
                  </div>
            	</div>
            	
            	<div class="card text-white bg-success mb-4">
                  <div class="card-header">습도</div>
                  <div class="card-body">
                     <h1 class="card-title"><td><%=humi%>%</td> </h1>                  </div>
               </div>
               
            	<div class="card text-white bg-success mb-4">
                  <div class="card-header">토양 습도1</div>
                  <div class="card-body">
			<h1 class="card-title"><td><%=soil1%></td> </h1> </div>
               </div>
               
            	<div class="card text-white bg-success mb-4">
                  <div class="card-header">토양 습도2</div>
                  <div class="card-body">
                     <h1 class="card-title"><td><%=soil2%></td> </h1>                  </div>
            	</div>
            	
            	<div class="card text-white bg-success mb-4">
                  <div class="card-header">토양 습도3</div>
                  <div class="card-body">
                   <h1 class="card-title"><td><%=soil3%></td> </h1>                  </div>
               </div>
               
            	<div class="card text-white bg-success mb-4">
                  <div class="card-header">물 양</div>
                  <div class="card-body">
                     <h1 class="card-title"><td><%=level%>%</td> </h1>                  </div>
               </div>
               
           	</div>
         </div>
		</div>
      </div>       
    </div>

    <script src="js/jquery-3.3.1.min.js"></script>
    <script src="js/popper.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/owl.carousel.min.js"></script>
    <script src="js/jquery.sticky.js"></script>
    <script src="js/jquery.waypoints.min.js"></script>
    <script src="js/jquery.animateNumber.min.js"></script>
    <script src="js/jquery.fancybox.min.js"></script>
    <script src="js/jquery.easing.1.3.js"></script>
    <script src="js/aos.js"></script>

    <script src="js/main.js"></script>

  </body>

</html>
