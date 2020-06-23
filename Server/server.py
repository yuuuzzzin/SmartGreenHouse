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
conn2 = pymysql.connect(host='database-1.cudf3zzu3npf.us-east-2.rds.amazonaws.com', user='jeongmin', password='97shwjdals!', db='mydb',charset='utf8')

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
        DeviceState = json_data['DeviceState']
        userid = json_data['id']
        print('디바이스 상태', DeviceState,userid)
        if DeviceState != "null":
            DeviceStateUpload(DeviceState,userid)
        return json_data
    except:
        pass

def userCheck(): #사용자 설정 확인 후 장치 작동
    try:
	#사용자 장치 설정 값
        curs = conn.cursor()
        sql = "select * from device where number = 1"
        curs.execute(sql)
        row = curs.fetchone()
        user_s1 = row[1]
        user_s2 = row[2]
        user_s3 = row[3]
        user_l = row[4]
        user_h = row[5]
        print("사용자 설정 값:",user_s1,user_s2,user_s3,user_l,user_h)
	#현재 장치 상태 값
        curs = conn.cursor()
        sql = "select * from device where number = 2"
        curs.execute(sql)
        row = curs.fetchone()
        cur_s1 = row[1]
        cur_s2 = row[2]
        cur_s3 = row[3]
        cur_l = row[4]
        cur_h = row[5]
        print("현재 상태 값:",cur_s1,cur_s2,cur_s3,cur_l,cur_h)        
	#LED, FAN 예약 확인, 사용자 예약이 없으면 기본값
        curs = conn.cursor()
        sql = "select * from led order by number desc limit 1"
        curs.execute(sql)
        row = curs.fetchone()
        Start_led = row[1]
        End_led = row[2]

        curs = conn.cursor()
        sql = "select * from fan order by number desc limit 1"
        curs.execute(sql)
        row = curs.fetchone()
        Start_fan = row[1]
        End_fan = row[2]

        Current = datetime.datetime.now()+timedelta(hours=9) #서버가 오하이오주라서 9시간
        CurrentTime = Current.strftime('%H:%M') #str형식으로 변환
        
        print(Start_led, End_led, Start_fan, End_fan, CurrentTime)
        global s1,s2,s3,l,h
        if (user_s1 == 'on'):
            s1 = "StartMotor1"
            curs = conn.cursor()
            sql = "update device set water1 = 'user' where number=3" 
            curs.execute(sql)
            conn.commit()
        if (user_s2 == 'on'):
            s2 = "StartMotor2"
            curs = conn.cursor()
            sql = "update device set water2 = 'user' where number=3" 
            curs.execute(sql)
            conn.commit()
        if (user_s3 == 'on'):
            s3 = "StartMotor3"
            curs = conn.cursor()
            sql = "update device set water3 = 'user' where number=3" 
            curs.execute(sql)
            conn.commit()
        if (user_l == 'on'):
            l = "StartLed"
            curs = conn.cursor()
            sql = "update device set led = 'user' where number=3" 
            curs.execute(sql)
            conn.commit()
        if (user_l == 'off'):
            l = "StopLed"
            curs = conn.cursor()
            sql = "update device set led = 'user' where number=3" 
            curs.execute(sql)
            conn.commit()
        if (user_h == 'on'):
            h = "StartFan"
            curs = conn.cursor()
            sql = "update device set fan = 'user' where number=3" 
            curs.execute(sql)
            conn.commit()
        if (user_h == 'off'):
            h = "StopFan"
            curs = conn.cursor()
            sql = "update device set fan = 'user' where number=3" 
            curs.execute(sql)
            conn.commit()
            
	#사용자 설정과 현재 작동상황이 같으면 명령x
        if (user_h == 'on' and cur_h =='on'):
            h = 'null'
        if (user_l == 'on' and cur_l == 'on'):
            l = 'null'
        if (user_h == 'off' and cur_h =='off'):
            h = 'null'
        if (user_l == 'off' and cur_l == 'off'):
            l = 'null'
        if (Start_led == CurrentTime):
            l = "StartLed"
            curs = conn.cursor()
            sql = "update device set led = 'on' where number=1" 
            curs.execute(sql)
            conn.commit()
            curs = conn.cursor()
            sql = "update device set led = 'reservation' where number=3" 
            curs.execute(sql)
            conn.commit()
        if (End_led == CurrentTime):
            l = "StopLed"
            curs = conn.cursor()
            sql = "update device set led = 'off' where number=1" 
            curs.execute(sql)
            conn.commit()
            curs = conn.cursor()
            sql = "update device set led = 'reservation' where number=3" 
            curs.execute(sql)
            conn.commit()
        if (Start_fan == CurrentTime):
            h = "StartFan"
            curs = conn.cursor()
            sql = "update device set fan = 'on' where number=1" 
            curs.execute(sql)
            conn.commit()
            curs = conn.cursor()
            sql = "update device set fan = 'reservation' where number=3" 
            curs.execute(sql)
            conn.commit()
        if (End_fan == CurrentTime):
            h = "StopFan"
            curs = conn.cursor()
            sql = "update device set fan = 'off' where number=1" 
            curs.execute(sql)
            conn.commit()
            curs = conn.cursor()
            sql = "update device set fan = 'reservation' where number=3" 
            curs.execute(sql)
            conn.commit()
        print(s1,s2,s3,l,h)
        
    except:
        pass

def SendMessage(s1, s2, s3, l, h): #Rpi에 명령어 전송
    clientSocekt.send(bytes('%s,%s,%s,%s,%s'%(s2,s3,l,h,s1),"UTF-8")) #순서 이대로 보내야 Rpi에서 s1부터 받아짐
    #print(s1, s2, s3, l, h ,"send data success!!")
    
def WaterCycle(watercycleCode, soil):
    if watercycleCode == "053001":
        if soil < 550 :
            return "soilgood"
        else:
            return "StartMotor"
    elif watercycleCode == "053002":
        if soil < 750 : 
            return "soilgood"
        else:
            return "StartMotor"
    elif watercycleCode == "053003":
        if soil < 900 : 
            return "soilgood"
        else:
            return "StartMotor"
    elif watercycleCode == "053004":
        if soil < 1000 : 
            return "soilgood"
        else:
            return "StartMotor"

def Humi(humi):
    if humi <=50 :
        return "humigood"
    else:
        return "StartFan"
    
def WaterLevel(level):
    if level <= 2 : 
        return "waterlevelbad"
    else:
        return "waterlevelgood"

def DeviceStateUpload(DeviceState,userid):
    global s1, s2, s3, l, h
    time = datetime.datetime.now()+timedelta(hours=9) #서버가 오하이오주라서 9시간
    curs = conn.cursor()
    if DeviceState == "water1Finish":
        sql = "UPDATE device SET water1 = %s LIMIT 2"
        curs.execute(sql, ('off'))
        conn.commit()
        s1 = 'null'
        sql = "INSERT INTO DeviceRecord (device, state, time, mode,id) VALUES(%s,%s,%s,(SELECT water1 FROM device WHERE number=3),%s)"
        curs.execute(sql, ("water1", "finish", time, userid))
        conn.commit()

    if DeviceState == "water2Finish":
        sql = "UPDATE device SET water2 = %s LIMIT 2"
        curs.execute(sql, ('off'))
        conn.commit()
        s2 = 'null'
        sql = "INSERT INTO DeviceRecord (device, state, time, mode,id) VALUES(%s,%s,%s,(SELECT water2 FROM device WHERE number=3),%s)"
        curs.execute(sql, ("water2", "finish", time, userid))
        conn.commit()
        
    if DeviceState == "water3Finish":
        sql = "UPDATE device SET water3 = %s LIMIT 2"
        curs.execute(sql, ('off'))
        conn.commit()
        s3 = 'null'
        sql = "INSERT INTO DeviceRecord (device, state, time, mode,id) VALUES(%s,%s,%s,(SELECT water3 FROM device WHERE number=3),%s)"
        curs.execute(sql, ("water3", "finish", time, userid))
        conn.commit()
        
    if DeviceState == "ledFinish":
        sql = "UPDATE device SET led = %s WHERE number = 2"
        curs.execute(sql, ('off'))
        conn.commit()
        l = 'null'
        sql = "INSERT INTO DeviceRecord (device, state, time, mode,id) VALUES(%s,%s,%s,(SELECT led FROM device WHERE number=3),%s)"
        curs.execute(sql, ("led", "finish", time, userid))
        conn.commit()

    if DeviceState == "ledOperate":
        sql = "UPDATE device SET led = %s WHERE number = 2"
        curs.execute(sql, ('on'))
        conn.commit()
        l = 'null'
        sql = "INSERT INTO DeviceRecord (device, state, time, mode,id) VALUES(%s,%s,%s,(SELECT led FROM device WHERE number=3),%s)"
        curs.execute(sql, ("led", "operate", time, userid))
        conn.commit()
        
    if DeviceState == "fanFinish":
        sql = "UPDATE device SET fan = %s WHERE number = 2"
        curs.execute(sql, ('off'))
        conn.commit()
        h = 'null'
        sql = "INSERT INTO DeviceRecord (device, state, time, mode,id) VALUES(%s,%s,%s,(SELECT fan FROM device WHERE number=3),%s)"
        curs.execute(sql, ("fan", "finish", time, userid))
        conn.commit()
    if DeviceState == "fanOperate":
        sql = "UPDATE device SET fan = %s WHERE number = 2"
        curs.execute(sql, ('on'))
        conn.commit()
        h = 'null'
        sql = "INSERT INTO DeviceRecord (device, state, time, mode,id) VALUES(%s,%s,%s,(SELECT fan FROM device WHERE number=3),%s)"
        curs.execute(sql, ("fan", "operate", time, userid))
        conn.commit()
        
def SensorUpload(table):
    curs = conn2.cursor()
    data = clientSocekt.recv(BUFSIZE)
    Ard_data = data.decode()
    json_data = json.loads(Ard_data)
    temp = json_data['temp']
    humi = json_data['humi']
    soil1 = json_data['soil1']
    soil2 = json_data['soil2']
    soil3 = json_data['soil3']
    cds = json_data['cds']
    level = json_data['level']
    currentdate = datetime.datetime.now()+timedelta(hours=9)
    date = currentdate.strftime('%Y-%m-%d %H:%M:%S')
    #print(date)
    sql = "INSERT INTO " + table + " (temp, humi, soil1, soil2, soil3, cds, level, date) VALUES (%s,%s,%s,%s,%s,%s,%s,%s)"
    curs.execute(sql, (temp, humi, soil1, soil2, soil3, cds, level, date))
    conn2.commit()
    print(temp, humi, soil1, soil2, soil3, cds, level, date)

def PlantInfo(json_data):
    currentdate = datetime.datetime.now()+timedelta(hours=9)
    month = currentdate.strftime('%m')
    temp = json_data['temp']
    humi = json_data['humi']
    soil1 = json_data['soil1']
    soil2 = json_data['soil2']
    soil3 = json_data['soil3']
    cds = json_data['cds']
    level = json_data['level']
    userid = json_data['id']
    curs = conn.cursor()        
    sql = "select * from userplant where id = %s"
    curs.execute(sql,userid)
    rows = curs.fetchall()
    curs.close()
    if rows[0] != None:
        row = rows[0]
        plantdata = json.loads(row[2])
        hdCode= plantdata["hdCode"] 
        if month == '02' or month == '01' or month =='12':
            watercycleCode = plantdata["watercycleWinterCode"]
        elif month == '03' or month == '04' or month == '05':
            watercycleCode = plantdata["watercycleSprngCode"]
        elif month == '06' or month == '07' or month == '08':
            watercycleCode = plantdata["watercycleSummerCode"]
        elif month == '09' or month == '10' or month == '11':
            watercycleCode = plantdata["watercycleAutumnCode"]
        s1 = WaterCycle(watercycleCode, soil1) + '1'
        if s1 == 'StartMotor1':
            curs = conn.cursor()
            sql = "update device set water1='auto' where number=3" 
            curs.execute(sql)
            conn.commit()

    if rows[1] == None:
        pass
    else:
        row = rows[1]
        plantdata = json.loads(row[2])
        hdCode= plantdata["hdCode"] 
        if month == '02' or month == '01' or month =='12':
            watercycleCode = plantdata["watercycleWinterCode"]
        elif month == '03' or month == '04' or month == '05':
            watercycleCode = plantdata["watercycleSprngCode"]
        elif month == '06' or month == '07' or month == '08':
            watercycleCode = plantdata["watercycleSummerCode"]
        elif month == '09' or month == '10' or month == '11':
            watercycleCode = plantdata["watercycleAutumnCode"]
        s2 = WaterCycle(watercycleCode, soil2) + '2'
        if s2 == 'StartMotor2':
            curs = conn.cursor()
            sql = "update device set water2='auto' where number=3" 
            curs.execute(sql)
            conn.commit()
        
    if rows[2] == None:
        pass
    else:
        row = rows[0]
        plantdata = json.loads(row[2])
        hdCode= plantdata["hdCode"] 
        if month == '02' or month == '01' or month =='12':
            watercycleCode = plantdata["watercycleWinterCode"]
        elif month == '03' or month == '04' or month == '05':
            watercycleCode = plantdata["watercycleSprngCode"]
        elif month == '06' or month == '07' or month == '08':
            watercycleCode = plantdata["watercycleSummerCode"]
        elif month == '09' or month == '10' or month == '11':
            watercycleCode = plantdata["watercycleAutumnCode"]
        s3 = WaterCycle(watercycleCode, soil3) + '3'
        if s3 == 'StartMotor3':
            curs = conn.cursor()
            sql = "update device set water3='auto' where number=3" 
            curs.execute(sql)
            conn.commit()
            SendMessage(h,s1,s2,s3,l)

#############################################################################
sched = BackgroundScheduler()
sched.start()

sched.add_job(PlantInfo,'cron', second='1', id="test_1", args=[recvData()]) #1분에 한번
sched.add_job(SensorUpload,'cron', second='31', id="test_2", args=['Sensor']) #1분에 한번
#sched.add_job(SensorUpload,'cron', minute="00", second='00', id="test_3", args=['Graph']) #1시간에 한번
#############################################################################
while True:
    try:
        conn = pymysql.connect(host='database-1.cudf3zzu3npf.us-east-2.rds.amazonaws.com', user='jeongmin', password='97shwjdals!', db='mydb',charset='utf8')
        recvData()
        userCheck()
        SendMessage(h,s1,s2,s3,l)
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

