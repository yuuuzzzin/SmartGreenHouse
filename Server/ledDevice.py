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

# ���� ����
serverSocket = socket(AF_INET, SOCK_STREAM)

# ���� �ּ� ���� �Ҵ�
serverSocket.bind(ADDR)
print('bind')

# ���� ���� ��� ����
serverSocket.listen(100)
print('listen')

# ���� ����
clientSocket, addr_info = serverSocket.accept()
print('accept')
print('--client information--')

conn = pymysql.connect(host='database-1.cudf3zzu3npf.us-east-2.rds.amazonaws.com', user='jeongmin', password='97shwjdals!', db='mydb',charset='utf8')
curs = conn.cursor()
sql = """select * from led order by id desc limit 1"""
curs.execute(sql) # DB�� ������ ���ٸ� ������ 
row = curs.fetchone()

startTime = row[1] #���� �ð�
endTime = row[2] #���� �ð�

startHour = int(startTime.strftime('%H'))
startMin = int(startTime.strftime('%M'))
endHour = int(endTime.strftime('%H'))
endMin = int(endTime.strftime('%M'))
intHour = endHour - startHour
intMin = endMin - startMin
interval = str((intHour * 3600) + (intMin * 60)) #'����ð�-���۽ð�'�� �ʴ����� ����
print(interval)

#����ð����� ��&�� �̾Ƴ���
currentdate = datetime.datetime.now()+timedelta(hours=9)
currentHour = currentdate.hour #���� ��
currentMin = currentdate.minute #���� ��
	

if (startHour == currentHour and startMin == currentMin):
    led = "ledbad"
    DBled = "on"
      #��������̿� 'ledbad'���ڿ��� �۵��ð� ������
    clientSocket.send(bytes('%s %s'%(led,interval),"UTF-8"))
    sql = """UPDATE device SET led = %s where number = '1'"""
    curs.execute(sql,(DBled))
    conn.commit()
    time.sleep(10)

conn.close()

# ���� ����
clientSocket.close()
serverSocket.close()
print('close')

