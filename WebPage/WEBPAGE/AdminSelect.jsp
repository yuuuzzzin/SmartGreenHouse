<%@ page contentType = "text/html; charset=euc-kr" %>
<%@ page import = "java.sql.DriverManager" %>
<%@ page import = "java.sql.Connection" %>
<%@ page import = "java.sql.Statement" %>
<%@ page import = "java.sql.ResultSet" %>
<%@ page import = "java.sql.SQLException" %>

<html>
<head><title>관리자모드</title></head>
<body>
    <span style="float: left; margin-right:20px;">
    <h1>관리자 페이지</h1>
         <p>
             <div>     
            <button type="button" onclick="location.href='UserManagement.jsp' ">사용자 관리</button>
            <button type="button" onclick="location.href='UserPlant.jsp' ">사용자 식물 목록</button>
            <button type="button" onclick="location.href='DeviceSelect.jsp' ">장치 기록</button>
            <button type="button" onclick="location.href='sensors.jsp' ">센서 데이터</button>
          </div>
                </p>
      <div>
            <a href="main.jsp"  class="txt1"> 메인으로 돌아가기    </a>
           </div>

    </span>
	</form>

</body>
</html>




