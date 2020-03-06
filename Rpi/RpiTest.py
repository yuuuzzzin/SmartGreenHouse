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
import schedule
from apscheduler.jobstores.base import JobLookupError
from apscheduler.schedulers.background import BackgroundScheduler
HOST = '18.218.71.35' # EC2 서버 주소
PORT = 10001 
BUFSIZE = 1024
ADDR = (HOST,PORT)

if os.path.exists("/dev/ttyACM0") :
    tty = "/dev/ttyACM0"
elif os.path.exists("/dev/ttyACM1") :
    tty = "/dev/ttyACM1"
ArduinoSerial = serial.Serial(tty, 9600);
#ArduinoSerial.flushInput();

s = socket(AF_INET, SOCK_STREAM)	
s.connect((HOST,PORT))
print("success!")

def sendSensorData():
    f = open("SensorValue", 'r')
    json_data = f.readline()
    if not json_data:
        print("empty sensor data")
    #print(json_data)
            # 서버에 데이터 전송
    s.send(bytes(json_data,"UTF-8"))
    f.close()

sched = BackgroundScheduler()
sched.start()

sched.add_job(sendSensorData,'interval', seconds=5, id="test_1", args=[])
#sched.add_job(recvData,'interval', seconds=8, id="test_2", args=[])

while True:
    try:
        buffer = s.recv(BUFSIZE)
        data = buffer.decode("UTF-8")

        DeviceValue = data.split(',')
        water1 = DeviceValue[0]
        water2 = DeviceValue[1]
        water3 = DeviceValue[2]
        led = DeviceValue[3]
        fan = DeviceValue[4]
            
        #print(buffer.decode())
        ArduinoSerial.flushInput();

        if (water1 == "StartMotor1"):
            ArduinoSerial.write(b'StartWater1')
            print("StartWater1")

        elif (water2 == "soil2soilbad"):
            ArduinoSerial.write(b'StartWater2')
            print("StartWater2")

        elif (water3 == "soil3soilbad"):
            ArduinoSerial.write(b'StartWater3')
            print("StartWater3")

        elif (led == "1"):
            ArduinoSerial.write(b'StartLed')
            print("StartLed")

        elif (fan == "humibad"):
            ArduinoSerial.write(b'StartFan')
            print("humibad -> StartFan")
        ArduinoSerial.flushOutput();
        time.sleep(5)

    except SocketError as e:
        print('에러 발생', e)
        s.close()
    except:
        sys.exit()
    