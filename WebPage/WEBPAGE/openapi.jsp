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
                var myItem = data.response.body.items.item;                                //�� ��� ���ο� �����Ͱ� �������
                var output = '';
                    output += '<h3>'+ i + '��° ���� ���� ������' +'</h3>';
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
