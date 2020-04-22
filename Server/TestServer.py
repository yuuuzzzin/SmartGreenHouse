# -*- coding: utf-8 -*-
from socket import *
from select import *
import pymysql
import json
import datetime
from datetime import timedelta
import time
import schedule
from apscheduler.jobstores.base import JobLookupError
from apscheduler.schedulers.background import BackgroundScheduler
from socket import error as SocketError
import errno
import sys

HOST = ''
PORT = 10001
BUFSIZE = 1024
ADDR = (HOST, PORT)
s1='null'
s2='null'
s3='null'
l='null'
h='null'

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

def recvData():
    try:
        data = clientSocekt.recv(BUFSIZE)
        Ard_data = data.decode()
        json_data = json.loads(Ard_data)
        #print('json 데이터', json_data)
        DeviceState = json_data['DeviceState']
        print('디바이스 상태', DeviceState)
        if DeviceState != "null":
            DeviceStateDB(DeviceState)
    except:
        pass

def userCheck(): #사용자가 물줘라 하면 워터펌프 작동
    try:
        curs = conn.cursor()
        sql = """select * from device where number = 1""" 
        curs.execute(sql)
        row = curs.fetchone()
        #print(row)
        user_s1 = row[1]
        user_s2 = row[2]
        user_s3 = row[3]
        user_l = row[4]
        user_h = row[5]
        #print(user_s1)
        global s1,s2,s3,l,h
        if (user_s1 == 'on'):
            s1 = "StartMotor1"
        if (user_s2 == 'on'):
            s2 = "StartMotor2"
        if (user_s3 == 'on'):
            s3 = "StartMotor3"
        if (user_l == 'on'):
            l = "StartLed"
        if (user_l == 'off'):
            l == "StopLed"
        if (user_h == 'on'):
            h = "StartFan"
        if (user_h == 'off'):
            h = "StopFan"
        print(s1,s2,s3,l,h)

    except:
        pass
    
def updateDB(s1, s2, s3, l, h): #DB에 작동 상황 저장
    if (s1 == "StartMotor1"):
        water1 = "on"
    else:
        water1 = "off"

    if (s2 == "StartMotor2"):
        water2 = "on"
    else:
        water2 = "off"

    if (s3 == "StartMotor3"):
        water3 = "on"
    else:
        water3 = "off"
        
    if (h == "StartFan"):
        fan = "on"
    else:
        fan = "off"
        
    if (l == "StartLed"):
        led = "on"
    else:
        led = "off"
        
    curs = conn.cursor()
    sql = """UPDATE device SET water1 = %s, water2 = %s, water3= %s, led = %s, fan = %s WHERE number = '2'"""
    curs.execute(sql,(water1,water2,water3,led,fan))
    conn.commit()
    #print(s1, s2, s3, l, h)
    clientSocekt.send(bytes('%s,%s,%s,%s,%s'%(s2,s3,l,h,s1),"UTF-8")) #순서가 이상함
    #print(s1, s2, s3, l, h ,"send data success!!")
    
def CurrentSeason(month): #현재 달에 맞는 계절
    if month == '02' or month == '01' or month =='12':
        return watercycleWinterCode
    elif month == '03' or month == '04' or month == '05':
        return watercycleSprngCode
    elif month == '06' or month == '07' or month == '08':
        return watercycleSummerCode
    elif month == '09' or month == '10' or month == '11':
        return watercycleAutumnCode

def WaterCycle(watercycleCode, soil):
    if watercycleCode == "053001":
        if soil > 1000 : #임시값이므로 실험을 통한 수정 필요
            return "soilgood"
        else:
            return "StartMotor@@"
    elif watercycleCode == "053002":
        if soil > 800 : #임시값이므로 실험을 통한 수정 필요
            return "soilgood"
        else:
            return "StartMotor@@"
    elif watercycleCode == "053003":
        if soil > 600 : #임시값이므로 실험을 통한 수정 필요
            return "soilgood"
        else:
                            #print("soilbad3")
            return "StartMotor@@"
    elif watercycleCode == "053004":
        if soil > 400 : #임시값이므로 실험을 통한 수정 필요
            return "soilgood"
        else:
            return "StartMotor@@"

def Humi(humi):
    if humi >= 70 :
        return "humigood"
    else:
        return "StartFan"
    
def WaterLevel(level):
    if level < 100 : #임시값이므로 실험을 통한 수정 필요
        return "waterlevelbad"
    else:
        return "waterlevelgood"

def DeviceStateDB(DeviceState):
    global s1, s2, s3, l, h
    curs = conn.cursor()
    if DeviceState == "water1Finish":
        sql = "UPDATE device SET water1 = %s LIMIT 2"
        curs.execute(sql, ('off'))
        conn.commit()
        s1 = 'null'

    if DeviceState == "water2Finish":
        sql = "UPDATE device SET water2 = %s LIMIT 2"
        curs.execute(sql, ('off'))
        conn.commit()
        s2 = 'null'

    if DeviceState == "water3Finish":
        sql = "UPDATE device SET water3 = %s LIMIT 2"
        curs.execute(sql, ('off'))
        conn.commit()
        s3 = 'null'

    if DeviceState == "ledFInish":
        sql = "UPDATE device SET led = %s LIMIT 2"
        curs.execute(sql, ('off'))
        conn.commit()
        
    if DeviceState == "fanFInish":
        sql = "UPDATE device SET fan = %s LIMIT 2"
        curs.execute(sql, ('off'))
        conn.commit()
        
def SensorUpload():
    data = clientSocekt.recv(BUFSIZE)
    Ard_data = data.decode()
    json_data = json.loads(Ard_data)
    #temp = json_data['temp']
    #humi = json_data['humi']
    temp = 655 #테스트용
    humi = 50 #테스트용
    #soil1 = json_data['soil1']
    #soil2 = json_data['soil2']
    #soil3 = json_data['soil3']
    soil1 = 1000 #테스트용
    soil2 = 1000 #테스트용
    soil3 = 1000 #테스트용
    cds = json_data['cds']
    level = json_data['level']
    currentdate = datetime.datetime.now()+timedelta(hours=9)
    date = currentdate.strftime('%Y-%m-%d %H:%M:%S')
    #print(date)
    sql = """INSERT INTO sensor (temp, humi, soil1, soil2, soil3, cds, level, date) VALUES (%s,%s,%s,%s,%s,%s,%s,%s)"""
    curs.execute(sql, (temp, humi, soil1, soil2, soil3, cds, level, date))
    conn.commit()

def TTTT():
    curs = conn.cursor()
    sql = "select * from sensor order by id desc limit 1" # DB의 마지막 한줄만 가져옴
    curs.execute(sql)
    row = curs.fetchone()
    curs.close()
    temp = int(row[1])
    humi = int(row[2])
    soil1 = int(row[3])
    soil2 = int(row[4])
    soil3 = int(row[5])
    cds = int(row[6])
    level = int(row[7])
    h = Humi(humi)
    currentdate = datetime.datetime.now()+timedelta(hours=9)
    month = currentdate.strftime('%m')
    
    curs = conn.cursor()
    sql = "select * from userplant LIMIT 1"
    curs.execute(sql)
    row = curs.fetchone()
    curs.close()
    id = row[0]
    
    curs = conn.cursor()        
    sql = "select * from userplant where position = %s"
    curs.execute(sql,id+'1')
    row = curs.fetchone()
    curs.close()
    json_data = json.loads(row[2])
    name = json_data["distbNm"] #이름
    hdCode= json_data["hdCode"] #습도 코드 083001 : 40% 미만, 083002 : 40 ~ 70%, 083003: 70% 이상
    if month == '02' or month == '01' or month =='12':
        watercycleCode = json_data["watercycleSprngCode"]
    elif month == '03' or month == '04' or month == '05':
        watercycleCode = json_data["watercycleSummerCode"]
    elif month == '06' or month == '07' or month == '08':
        watercycleCode = json_data["watercycleAutumnCode"]
    elif month == '09' or month == '10' or month == '11':
        watercycleCode = json_data["watercycleWinterCode"]
    s1 = WaterCycle(watercycleCode, soil1) + '1'
    
    curs = conn.cursor()
    sql = "select * from userplant where position = %s"
    curs.execute(sql,id+'2')
    row = curs.fetchone()
    curs.close()
    json_data = json.loads(row[2])
    name = json_data["distbNm"] #이름
    hdCode= json_data["hdCode"] #습도 코드 083001 : 40% 미만, 083002 : 40 ~ 70%, 083003: 70% 이상
    if month == '02' or month == '01' or month =='12':
        watercycleCode = json_data["watercycleSprngCode"]
    elif month == '03' or month == '04' or month == '05':
        watercycleCode = json_data["watercycleSummerCode"]
    elif month == '06' or month == '07' or month == '08':
        watercycleCode = json_data["watercycleAutumnCode"]
    elif month == '09' or month == '10' or month == '11':
        watercycleCode = json_data["watercycleWinterCode"]
    s2 = WaterCycle(watercycleCode, soil2) +'2'
    
    curs = conn.cursor()
    sql = "select * from userplant where position = %s"
    curs.execute(sql,id+'3')
    row = curs.fetchone()
    curs.close()
    json_data = json.loads(row[2])
    name = json_data["distbNm"] #이름
    hdCode= json_data["hdCode"] #습도 코드 083001 : 40% 미만, 083002 : 40 ~ 70%, 083003: 70% 이상
    if month == '02' or month == '01' or month =='12':
        watercycleCode = json_data["watercycleSprngCode"]
    elif month == '03' or month == '04' or month == '05':
        watercycleCode = json_data["watercycleSummerCode"]
    elif month == '06' or month == '07' or month == '08':
        watercycleCode = json_data["watercycleAutumnCode"]
    elif month == '09' or month == '10' or month == '11':
        watercycleCode = json_data["watercycleWinterCode"]
    s3 = WaterCycle(watercycleCode, soil3) + '3'
    
sched = BackgroundScheduler()
sched.start()

#sched.add_job(recvData,'interval', seconds=5, id="test_1", args=[]) #5초에 한번
sched.add_job(TTTT,'cron', second='1', id="test_1", args=[])
sched.add_job(SensorUpload,'cron', second='30', id="test_2", args=[]) #1분에 한번

while True:
    try:
        conn = pymysql.connect(host='database-1.cudf3zzu3npf.us-east-2.rds.amazonaws.com', user='jeongmin', password='97shwjdals!', db='mydb',charset='utf8')
        recvData()
        userCheck()
        updateDB(h,s1,s2,s3,l)
        time.sleep(3)

    except SocketError as e:
        print('에러 발생', e)
        conn.close()
        clientSocekt.close()
        serverSocket.close()
        print('close')
        
    except KeyboardInterrupt:
        print ("Shutdown requested...exiting")
        sys.exit()
        
    finally:
        conn.commit()
        conn.close()
