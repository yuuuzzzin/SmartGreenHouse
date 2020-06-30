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
      <%
  	String userID = null;
  	if(session.getAttribute("id") != null){
  		userID = (String) session.getAttribute("id");
  	}
  %>


  <body data-spy="scroll" data-target=".site-navbar-target" data-offset="300">
  

    
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
	   <h1 class="mb-2">장치 기록</h1>
	<%out.println("사용자 아이디 : " + userID );%>
      <table width = "100%" border = "1">
      <tr>
            <td>number</td>
            <td>device</td>
            <td>state</td>
            <td>time</td>
            <td>mode</td>
            <td>id</td>
      </tr>
<%
	String devicename = request.getParameter("devicename");
	   // 1. JDBC 드라이버 로딩
      Class.forName("com.mysql.jdbc.Driver");

      Connection conn = null; // DBMS와 Java연결객체
      Statement stmt = null; // SQL구문을 실행
      ResultSet rs = null; // SQL구문의 실행결과를 저장
      PreparedStatement pstmt = null;
      
      try
      {

	String userID2 = null;
  	if(session.getAttribute("id") != null){
  		userID2 = (String) session.getAttribute("id");
  	}


            String query = "select * from DeviceRecord where id = ? order by number desc";
   
            // 2. 데이터베이스 커넥션 생성
            conn = DriverManager.getConnection("jdbc:mysql://database-1.cudf3zzu3npf.us-east-2.rds.amazonaws.com:3306/mydb", "jeongmin", "97shwjdals!");
            // 3. Statement 생성
            //stmt = conn.createStatement();
            pstmt = conn.prepareStatement(query);
          //  pstmt.setString(1, devicename);
	    pstmt.setString(1, userID2);
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
            <td><%= rs.getString("mode") %></td>                     
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
