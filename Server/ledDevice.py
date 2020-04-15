#!/usr/local/bin/python3
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

# 소켓 생성
serverSocket = socket(AF_INET, SOCK_STREAM)

# 소켓 주소 정보 할당
serverSocket.bind(ADDR)
print('bind')

# 연결 수신 대기 상태
serverSocket.listen(100)
print('listen')

# 연결 수락
clientSocket, addr_info = serverSocket.accept()
print('accept')
print('--client information--')

conn = pymysql.connect(host='database-1.cudf3zzu3npf.us-east-2.rds.amazonaws.com', user='jeongmin', password='97shwjdals!', db='mydb',charset='utf8')
curs = conn.cursor()
sql = """select * from led order by id desc limit 1"""
curs.execute(sql) # DB의 마지막 한줄만 가져옴 
row = curs.fetchone()

startTime = row[1] #시작 시간
endTime = row[2] #종료 시간

startHour = int(startTime.strftime('%H'))
startMin = int(startTime.strftime('%M'))
endHour = int(endTime.strftime('%H'))
endMin = int(endTime.strftime('%M'))
intHour = endHour - startHour
intMin = endMin - startMin
interval = str((intHour * 3600) + (intMin * 60)) #'종료시간-시작시간'을 초단위로 변경
print(interval)

#현재시간에서 시&분 뽑아내기
currentdate = datetime.datetime.now()+timedelta(hours=9)
currentHour = currentdate.hour #현재 시
currentMin = currentdate.minute #현재 분
	

if (startHour == currentHour and startMin == currentMin):
    led = "ledbad"
    DBled = "on"
      #라즈베리파이에 'ledbad'문자열과 작동시간 보내기
    clientSocket.send(bytes('%s %s'%(led,interval),"UTF-8"))
    sql = """UPDATE device SET led = %s where number = '1'"""
    curs.execute(sql,(DBled))
    conn.commit()
    time.sleep(10)

conn.close()

# 소켓 종료
clientSocket.close()
serverSocket.close()
print('close')

