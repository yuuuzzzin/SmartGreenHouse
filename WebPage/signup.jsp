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
            <form action ="signUp_process.jsp" method="post" class="login100-form validate-form">
               <span class="login100-form-title p-b-43">
                     <a href="main.jsp"><h3>Greenery</h3></a>
               </span>
                        
               <div class="wrap-input100 validate-input" data-validate = "ID를 입력하세요.">
                  <input class="input100" type="text" name="id">
                  <span class="focus-input100"></span>
                  <span class="label-input100">ID</span>
               </div>
               
               
               
               
               
               
               
               
               <input type="button" onClick="idCheck(this.form)" value="중복확인"/>
               
               
               
               
               
               
               
               <div class="wrap-input100 validate-input" data-validate="비밀번호를 입력하세요.">
                  <input class="input100" type="password" name="passwd">
                  <span class="focus-input100"></span>
                  <span class="label-input100">Password</span>
               </div>
               <div class="wrap-input100 validate-input" data-validate="비밀번호 확인이 다릅니다.">
                  <input class="input100" type="password" name="passwd2">
                  <span class="focus-input100"></span>
                  <span class="label-input100">Password 확인</span>
               </div>
               <div class="wrap-input100 validate-input" data-validate = "이름을 입력하세요.">
                  <input class="input100" type="text" name="name">
                  <span class="focus-input100"></span>
                  <span class="label-input100">이름</span>
               </div>
               <div class="wrap-input100 validate-input" data-validate = "전화번호를 입력하세요.">
                  <input class="input100" type="tel" name="phone">
                  <span class="focus-input100"></span>
                  <span class="label-input100">전화번호</span>
               </div>
               <div class="container-login100-form-btn">
                  <button class="login100-form-btn">
                        SIGN UP
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
   <script>
      function idCheck(inputid){
         if(inputid.id.value == ""){
            alert("아이디 입력 필요");
            return;
         }
         url = "CheckID.jsp?id="+inputid.id.value;
         open(url,"confirm","toolbar=no, location=no, status=no,menubar=ne,scrollbar=ne,resizable=no,width==300,height=180");
         
      }
      
   </script>
</body>
</html>