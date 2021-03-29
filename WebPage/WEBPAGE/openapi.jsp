<html>
<head>
</head>
<body>
<script>
      $.ajax({          
            url: 'PublicData.do',
            type: 'get',
            dataType: 'json',
            success: function(msg){
                var myItem = data.response.body.items.item;                                //이 경로 내부에 데이터가 들어있음
                var output = '';
                    output += '<h3>'+ i + '번째 서울 축제 데이터' +'</h3>';
                    output += '<h4>'+myItem[i].cntntsSj+'</h4>';
                    output += '<h4>'+myItem[i].rntOrginlFileNm+'</h4>';
                    output += '<h4>'+myItem[i].tel+'</h4>';
                    document.body.innerHTML += output;
                    
                }
            }
        });
</script>
</body>
</html>
