# -*- coding: utf-8 -*-

from socket import *
from select import *
import sys
from time import ctime
import json
from collections import OrderedDict
import serial
import time
import os

HOST = '18.218.71.35' # EC2 서버 주소
PORT = 9999 
BUFSIZE = 1024
ADDR = (HOST,PORT)

if os.path.exists("/dev/ttyACM0") :
    tty = "/dev/ttyACM0"
elif os.path.exists("/dev/ttyACM1") :
    tty = "/dev/ttyACM1"
ArduinoSerial = serial.Serial(tty, 9600);

# 서버에 접속하기 위한 소켓 생성
clientSocket = socket(AF_INET, SOCK_STREAM)	
json_data = ""
try:
	# 서버에 접속 시도
	clientSocket.connect(ADDR)	
	print("success!")
	while True:
		f = open("SensorValue", 'r')
		while True:
			json_data = f.readline()
			if not json_data: break
			print(json_data)
			# 서버에 데이터 전송
			clientSocket.send(json_data)	
		f.close()
		#10초 간격으로서버로 전송 (DB에 저장됨)
		time.sleep(10)

		
except Exception as e:
    print('%s:%s'%ADDR)
    sys.exit()

print('connect is success')
