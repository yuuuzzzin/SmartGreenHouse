# -*- coding: utf-8 -*-
from socket import *
from select import *
import pymysql
import json
import datetime
from datetime import timedelta
import time

HOST = ''
PORT = 10000
BUFSIZE = 1024
ADDR = (HOST, PORT)

conn = pymysql.connect(host='database-1.cudf3zzu3npf.us-east-2.rds.amazonaws.com', user='jeongmin', password='97shwjdals!', db='mydb',charset='utf8')

# 소켓 생성
serverSocket = socket(AF_INET, SOCK_STREAM)

# 소켓 주소 정보 할당
serverSocket.bind(ADDR)
print('bind')

# 연결 수신 대기 상태
serverSocket.listen(100)
print('listen')

# 연결 수락
clientSocekt, addr_info = serverSocket.accept()
print('accept')
print('--client information--')
print(clientSocekt)
data=""
while True:
# 클라이언트로부터 메시지를 가져옴
	data = clientSocekt.recv(65535)
	#json_data = json.dumps(data)
	#json_data = json.loads(data)
	#print(json_data1)
	print(data)
	if (data[0] == '{'):
		json_data = json.loads(data)
		print(json_data)
		temp = json_data['temp']
		humi = json_data['humi']
		soil1 = json_data['soil1']	
		soil2 = json_data['soil2']
		soil3 = json_data['soil3']
		cds = json_data['cds']
		level = json_data['level']
		currentdate = datetime.datetime.now()+timedelta(hours=9)
		date = currentdate.strftime('%Y-%m-%d %H:%M:%S')
		curs = conn.cursor()
		sql = """insert into sensor(temp, humi, soil1, soil2, soil3, cds, level, date) values(%s,%s,%s,%s,%s,%s,%s,%s)"""
		curs.execute(sql,(temp, humi, soil1, soil2, soil3, cds, level, date))
		conn.commit()
	if not data:
		break

conn.close()

# 소켓 종료
clientSocekt.close()
serverSocket.close()
print('close')

