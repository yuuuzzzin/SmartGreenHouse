<%@ page language="java" contentType="text/html; charset=euc-kr"
    pageEncoding="euc-kr" import="org.json.simple.*, java.sql.*, java.net.URLEncoder" %>
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
<html>

  <head>

    <meta charset="euc-kr">

    <title>Line_Controls_Chart</title>



    <meta http-equiv="refresh" content="300">

    <!-- jQuery -->

    <script src="https://code.jquery.com/jquery.min.js"></script>

    <!-- google charts -->

    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>

    <script src="https://www.google.com/jsapi"></script>

  <body>

    <div id="Line_Controls_Chart">

        <!-- 라인 차트 생성할 영역 -->

        <div id="lineChartArea" style="padding:0px 20px 0px 0px; width: 1000px; margin:auto"></div>

        <!-- 컨트롤바를 생성할 영역 -->

  	<div id="controlsArea" style="padding:0px 20px 0px 0px;width: 1000px; margin:auto"></div>

    </div>



  </body>



  <script>
	
  function printTime() {

    var clock = document.getElementById("clock"); // 출력할 장소 선택

    var now = new Date();                         // 현재시간

    var nowTime = now.getFullYear() + "." + (now.getMonth()+1) + "." + now.getDate() + " " + now.getHours() + ":" + now.getMinutes() + ":" + now.getSeconds();

    clock.innerHTML = nowTime;      // 현재시간을 출력

  }

  window.onload = function() {         // 페이지가 로딩되면 실행

    printTime();

  }

  </script>

  </head>
  <script>

  var chartDrowFun = {

    chartDrow : function(){

	var queryObject = "";

    	var queryObjectLen = "";
	//alert('!');
    	$.ajax({

           type : 'POST',

           url : 'getMonthlyChartData.jsp',
           dataType : 'json',

           success : function(data) {

               queryObject = eval('(' + JSON.stringify(data) + ')');

               queryObjectLen = queryObject.templist.length;

               //alert('Total lines : ' + queryObjectLen + 'EA');

           },

           error : function(xhr, type) {

	       //alert('server error occoured')

	      alert('server msg : ' + xhr.status)

           }

    	});



        //날짜형식 변경하고 싶으시면 이 부분 수정하세요.

        //var chartDateformat 	= 'yyyy년mm월dd일';

        var chartDateformat 	= 'mm월';

        //라인차트의 라인 수

        var chartLineCount    = 12;

        //컨트롤러 바 차트의 라인 수

        var controlLineCount	= 12;

	alert('data생성');
        function drawDashboard() {

          var data = new google.visualization.DataTable();

          data.addColumn('datetime', 'date');

          data.addColumn('number', 'temp');

          data.addColumn('number', 'humi');

          data.addColumn('number', 'level');

          //alert('data생성-------------------' + queryObjectLen + 'EA');

          for (var i = 0; i < queryObjectLen; i++) {

              var date = queryObject.templist[i].date;

              var temp = queryObject.templist[i].temp;

              var humi = queryObject.templist[i].humi;

              var level = queryObject.templist[i].level;

	      //alert(date + ' ' + temp);

              data.addRows([ [ new Date(date), temp, humi, level] ]);

          }



          var chart = new google.visualization.ChartWrapper({

              chartType   : 'LineChart',

              containerId : 'lineChartArea', //라인 차트 생성할 영역

              options     : {

                  isStacked   : 'percent',

                  focusTarget : 'category',

                  height      : 500,

                  width	      : '100%',

                  legend      : { position: "top", textStyle: {fontSize: 13}},

                  pointSize   : 5,

                  tooltip     : {textStyle : {fontSize:12}, showColorCode : true,trigger: 'both'},

                  hAxis	: {format: chartDateformat, gridlines:{count:chartLineCount,units: {

                      months: {format: ['MM월']}}

                      },textStyle: {fontSize:12}},

                 vAxis : {minValue: 50,viewWindow:{min:0},gridlines:{count:-1},textStyle:{fontSize:12}},

                 animation  : {startup: true,duration: 1000,easing: 'in' },

                 annotations: {pattern: chartDateformat,

                      textStyle: {

                      fontSize: 15,

                      bold: true,

                      italic: true,

                      color: '#871b47',

                      auraColor: '#d799ae',

                      opacity: 5,

                      pattern: chartDateformat

                      }

                  }

              }

          });



          var control = new google.visualization.ControlWrapper({

              controlType: 'ChartRangeFilter',

              containerId: 'controlsArea',  //control bar를 생성할 영역

              options: {

                  ui:{

                        chartType: 'LineChart',

                        chartOptions: {

                        chartArea: {'width': '60%','height' : 80},

                          hAxis: {'baselineColor': 'none', format: chartDateformat, textStyle: {fontSize:12},

                        gridlines:{count:controlLineCount,units: {

                             months: {format: ['MM월']}}

                        }}

                      }

                  },

                  filterColumnIndex: 0

              }

          });



          var date_formatter = new google.visualization.DateFormat({ pattern: chartDateformat});

          date_formatter.format(data, 0);



          var dashboard = new google.visualization.Dashboard(document.getElementById('Line_Controls_Chart'));

          window.addEventListener('resize', function() { dashboard.draw(data); }, false); //화면 크기에 따라 그래프 크기 변경

          dashboard.bind([control], [chart]);

          dashboard.draw(data);



        }

        google.charts.setOnLoadCallback(drawDashboard);



      }

    }



$(document).ready(function(){

    google.charts.load('current', {

       'packages':['line','controls']

    });

    chartDrowFun.chartDrow(); //chartDrow() 실행

});

  </script>

</html>