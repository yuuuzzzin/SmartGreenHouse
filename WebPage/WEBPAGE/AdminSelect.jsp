<%@ page contentType = "text/html; charset=euc-kr" %>
<%@ page import = "java.sql.DriverManager" %>
<%@ page import = "java.sql.Connection" %>
<%@ page import = "java.sql.Statement" %>
<%@ page import = "java.sql.ResultSet" %>
<%@ page import = "java.sql.SQLException" %>

<html>
<head><title>�����ڸ��</title></head>
<body>
    <span style="float: left; margin-right:20px;">
    <h1>������ ������</h1>
         <p>
             <div>     
            <button type="button" onclick="location.href='UserManagement.jsp' ">����� ����</button>
            <button type="button" onclick="location.href='UserPlant.jsp' ">����� �Ĺ� ���</button>
            <button type="button" onclick="location.href='DeviceSelect.jsp' ">��ġ ���</button>
            <button type="button" onclick="location.href='sensors.jsp' ">���� ������</button>
          </div>
                </p>
      <div>
            <a href="main.jsp"  class="txt1"> �������� ���ư���    </a>
           </div>

    </span>
	</form>

</body>
</html>




