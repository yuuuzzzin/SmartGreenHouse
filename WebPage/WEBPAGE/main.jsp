<%@ page pageEncoding = "euc-kr" %>
<%@ page contentType = "text/html; charset = euc-kr" %>
<html>

  <head>
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
          <div class="col-md-8 blog-content">
           <div class="comment-body">
					<div class="bold"><h3>스마트 온실 - Greenery</h3></a>
             					</div>

                        <div class="meta">나만의 작은 온실</div>
			<div align="center">
           				<img src="images/system.png" alt="gimage" width="700" height="500" align="center">
			</div>
			<p></p>
                        <p>앱을 통해 온실을 모니터링하고 환경을 제어할 수 있습니다. 온/습도 상태를 체크해 장치를 제어합니다. 물탱크에 물이 부족할 경우 사용자에게 알려줍니다. 카메라를 통해 실시간으로 온실 확인 및 촬영이 가능합니다.</p>
                        <p>작물 별 최적의 생장 환경에 따른 원격 제어를 지원합니다. 다양한 식물 품종의 생장 환경에 대한 정보를 제공하고 그를 토대로 자동 급수, 환풍, 광량을 조절합니다.</p>
                      </div>
          </div>
          </div>
          </div>
    </div>
    
    <footer class="site-footer">
      <div class="container">
        <div class="row">
          <div class="col-md-6">
            <div class="row">
              <div class="col-md-7">
                <h2 class="footer-heading mb-4">About Us</h2>
              </div>
              <div class="col-md-4 ml-auto">
                <h2 class="footer-heading mb-4">Features</h2>
                <ul class="list-unstyled">
                  <li><a href="#">About Us</a></li>
                  <li><a href="#">Testimonials</a></li>
                  <li><a href="#">Terms of Service</a></li>
                  <li><a href="#">Privacy</a></li>
                  <li><a href="#">Contact Us</a></li>
                </ul>
              </div>

            </div>
          </div>
          <div class="col-md-4 ml-auto">

            <div class="mb-5">
              <h2 class="footer-heading mb-4">Subscribe to Newsletter</h2>
              <form action="#" method="post" class="footer-suscribe-form">
                <div class="input-group mb-3">
                  <input type="text" class="form-control border-secondary text-white bg-transparent" placeholder="Enter Email" aria-label="Enter Email" aria-describedby="button-addon2">
                  <div class="input-group-append">
                    <button class="btn btn-primary text-white" type="button" id="button-addon2">Subscribe</button>
                  </div>
                </div>
            </div>


            <h2 class="footer-heading mb-4">Follow Us</h2>
            <a href="#about-section" class="smoothscroll pl-0 pr-3"><span class="icon-facebook"></span></a>
            <a href="#" class="pl-3 pr-3"><span class="icon-twitter"></span></a>
            <a href="#" class="pl-3 pr-3"><span class="icon-instagram"></span></a>
            <a href="#" class="pl-3 pr-3"><span class="icon-linkedin"></span></a>
            </form>
          </div>
        </div>
        <div class="row pt-5 mt-5 text-center">
          <div class="col-md-12">
            <div class="border-top pt-5">
              <p>
            <!-- Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. -->
            Copyright &copy;<script>document.write(new Date().getFullYear());</script> All rights reserved | This template is made with <i class="icon-heart text-danger" aria-hidden="true"></i> by <a href="https://colorlib.com" target="_blank" >Colorlib</a>
            <!-- Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. -->
            </p>
            </div>
          </div>

        </div>
      </div>
    </footer>

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
