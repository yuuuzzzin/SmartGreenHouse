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

s = socket(AF_INET, SOCK_STREAM)	
s.connect((HOST,PORT))
print("success!")

def sendSensorData():
    f = open("SensorValue", 'r')
    json_data = f.readline()
    if not json_data:
        print("empty sensor data")
    # 서버에 데이터 전송
    s.send(json_data.encode())
    f.close()

sched = BackgroundScheduler()
sched.start()

sched.add_job(sendSensorData,'interval', seconds=5, id="test_1", args=[])

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
            
        print(water1, water2, water3, led, fan)
        ArduinoSerial.flushInput();

        if (water1 == "StartMotor1"):
            ArduinoSerial.write(b'StartWater1')
            print("StartWater1")
        elif (water2 == "StartMotor2"):
            ArduinoSerial.write(b'StartWater2')
            print("StartWater2")
        elif (water3 == "StartMotor3"):
            ArduinoSerial.write(b'StartWater3')
            print("StartWater3")
        elif (led == "StartLed"):
            ArduinoSerial.write(b'StartLed')
            print("StartLed")
        elif (led == "StopLed"):
            ArduinoSerial.write(b'StopLed')
            print("StopLed")
        elif (fan == "StartFan"):
            ArduinoSerial.write(b'StartFan')
            print("StartFan")
        elif (fan == "StopFan"):
            ArduinoSerial.write(b'StopFan')
            print("StopFan")
        else:
            ArduinoSerial.write(b'null')
            print("Nonoperating")
            
        ArduinoSerial.flushOutput();
        time.sleep(4)
    except SocketError as e:
        print('에러 발생', e)
        s.close()
    except:
        sys.exit()
    
