<%@ page contentType = "text/html; charset=utf-8" %>
<%@ page import = "java.sql.DriverManager" %>
<%@ page import = "java.sql.Connection" %>
<%@ page import = "java.sql.Statement" %>
<%@ page import = "java.sql.ResultSet" %>
<%@ page import = "java.sql.SQLException" %>

<html>
<head><title>장치기록</title></head>
<body>
	<form action="DeviceRecord.jsp" method="get">
    <span style="float: left; margin-right:20px;">
    장치이름 :     
        <select name="devicename" size="1">
            <option value="">선택하세요</option>
            <option value="water1">water1</option>
            <option value="water2">water2</option>
            <option value="water3">water3</option>
            <option value="fan">fan</option>
            <option value="led">led</option>
        </select>
    </span>
    <input type="submit" value="전송" style="float:right; margin-right:50px;">

	</form>

</body>
</html>