#! /usr/bin/python3
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
from socket import error as SocketError
import errno

HOST = '18.218.71.35' # EC2 서버 주소
PORT = 10001 
BUFSIZE = 1024
ADDR = (HOST,PORT)

if os.path.exists("/dev/ttyACM0") :
    tty = "/dev/ttyACM0"
elif os.path.exists("/dev/ttyACM1") :
    tty = "/dev/ttyACM1"
ArduinoSerial = serial.Serial(tty, 9600);
ArduinoSerial.flushInput();

s = socket(AF_INET, SOCK_STREAM)	
s.connect((HOST,PORT))
print("success!")
while True:
    try:

    # 서버에 접속 시도
        f = open("SensorValue", 'r')
        json_data = f.readline()
        if not json_data: break
        print(json_data)
                # 서버에 데이터 전송
        s.send(bytes(json_data,"UTF-8"))
        f.close()
        time.sleep(5)
        print("====================")
        buffer = s.recv(BUFSIZE)
        buffer_str = buffer.decode("UTF-8")
        print(buffer.decode())
        time.sleep(5)
    except SocketError as e:
        print('에러 발생', e)
        if e.errno != errno.ECONNRESET:
            raise # Not error we are looking for
        pass # Handle error here.
