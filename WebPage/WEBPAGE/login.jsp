<%@ page pageEncoding = "utf-8" %>
<%@ page contentType = "text/html; charset = utf-8" %>
<head>
   <title>Login V18</title>
   <meta charset="UTF-8">
   <meta name="viewport" content="width=device-width, initial-scale=1">
<!--===============================================================================================-->   
   <link rel="icon" type="image/png" href="images/icons/favicon.ico"/>
<!--===============================================================================================-->
   <link rel="stylesheet" type="text/css" href="vendor/bootstrap/css/bootstrap.min.css">
<!--===============================================================================================-->
   <link rel="stylesheet" type="text/css" href="fonts/font-awesome-4.7.0/css/font-awesome.min.css">
<!--===============================================================================================-->
   <link rel="stylesheet" type="text/css" href="fonts/Linearicons-Free-v1.0.0/icon-font.min.css">
<!--===============================================================================================-->
   <link rel="stylesheet" type="text/css" href="vendor/animate/animate.css">
<!--===============================================================================================-->   
   <link rel="stylesheet" type="text/css" href="vendor/css-hamburgers/hamburgers.min.css">
<!--===============================================================================================-->
   <link rel="stylesheet" type="text/css" href="vendor/animsition/css/animsition.min.css">
<!--===============================================================================================-->
   <link rel="stylesheet" type="text/css" href="vendor/select2/select2.min.css">
<!--===============================================================================================-->   
   <link rel="stylesheet" type="text/css" href="vendor/daterangepicker/daterangepicker.css">
<!--===============================================================================================-->
   <link rel="stylesheet" type="text/css" href="css/util.css">
   <link rel="stylesheet" type="text/css" href="css/main.css">
<!--===============================================================================================-->
</head>
<body style="background-color: #666666;">
   
   <div class="limiter">
      <div class="container-login100">
         <div class="wrap-login100">
            <form action ="loginCheck.jsp" method="post" class="login100-form validate-form">
               <span class="login100-form-title p-b-43">
                     <a href="main.jsp"><h3>Greenery</h3></a>
               </span>        
               
           
               <div class="wrap-input100 validate-input" data-validate = "ID를 입력하세요.">
                  <input class="input100" type="text" name="id"> 
                  <span class="focus-input100"></span>
                  <span class="label-input100">ID</span>
               </div>
               <div class="wrap-input100 validate-input" data-validate="비밀번호를 입력하세요.">
                  <input class="input100" type="password" name="password">
                  <span class="focus-input100"></span>
                  <span class="label-input100">Password</span>
               </div>
              
               <div class="flex-sb-m w-full p-t-3 p-b-32">
                  <div class="contact100-form-checkbox">
                     <input class="input-checkbox100" id="ckb1" type="checkbox" name="remember-me">
                     <label class="label-checkbox100" for="ckb1">
                       로그인 상태 유지
                     </label>
                  </div>
                  <div>
                     <a href="adminLogin.jsp"  class="txt1"> 관리자 계정  |  </a>

                     <a href="#" class="txt1"> 비밀번호 찾기  |  </a>
              
                     <a href="signup.jsp" class="txt1">회원가입</a>
                  </div>
               </div>
               <div class="container-login100-form-btn">
                  <button class="login100-form-btn">
                     Login
                  </button>
               </div>       
            </form>

            <div class="login100-more" style="background-image: url('images/hero_1.jpg');">
            </div>
         </div>
      </div>
   </div>
   
   

   
   
<!--===============================================================================================-->
   <script src="vendor/jquery/jquery-3.2.1.min.js"></script>
<!--===============================================================================================-->
   <script src="vendor/animsition/js/animsition.min.js"></script>
<!--===============================================================================================-->
   <script src="vendor/bootstrap/js/popper.js"></script>
   <script src="vendor/bootstrap/js/bootstrap.min.js"></script>
<!--===============================================================================================-->
   <script src="vendor/select2/select2.min.js"></script>
<!--===============================================================================================-->
   <script src="vendor/daterangepicker/moment.min.js"></script>
   <script src="vendor/daterangepicker/daterangepicker.js"></script>
<!--===============================================================================================-->
   <script src="vendor/countdowntime/countdowntime.js"></script>
<!--===============================================================================================-->
   <script src="js/login.js"></script>

</body>
</html>