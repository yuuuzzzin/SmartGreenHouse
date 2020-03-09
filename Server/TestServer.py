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

data = clientSocekt.recv(BUFSIZE)

def recvData(data):
    try:
        curs = conn.cursor()
        print(data)
        #if (data[0] == '{'):
        json_data = json.loads(data)
        print(json_data)
        #temp = json_data['temp']
        #humi = json_data['humi']
        temp = 655 #테스트용
        humi = 50 #테스트용
        #soil1 = json_data['soil1']
        soil2 = json_data['soil2']
        soil3 = json_data['soil3']
        soil1 = 1000 #테스트용
        cds = json_data['cds']
        level = json_data['level']
        DeviceState = "json_data['DeviceState']"
        print(DeviceState)
        if DeviceState != "null":
            DeviceStateDB(DeviceState)

        currentdate = datetime.datetime.now()+timedelta(hours=9)
        date = currentdate.strftime('%Y-%m-%d %H:%M:%S')
        print(date)
        sql = """INSERT INTO sensor (temp, humi, soil1, soil2, soil3, cds, level, date) VALUES (%s,%s,%s,%s,%s,%s,%s,%s)"""
        curs.execute(sql, (temp, humi, soil1, soil2, soil3, cds, level, date))
        conn.commit()
    except:
        pass

def userCheck():
    try:
        curs = conn.cursor()
        sql = """select * from device where number = 1""" 
        curs.execute(sql)
        row = curs.fetchone()
        print(row)
        user_s1 = row[1]
        user_s2 = row[2]
        user_s3 = row[3]
        user_l = row[4]
        user_h = row[5]
        print(user_s1)
        global h, s1,s2,s3
        if (user_s1 == 'on'):
            s1 = "StartMotor1"
        elif (user_s2 == 'on'):
            s2 = "StartMotor2"
        elif (user_s3 == 'on'):
            s3 = "StartMotor3"
        elif (user_l == 'on'):
            led = "StartLed"
        elif (user_l == 'off'):
            led == "StopLed"
        elif (user_h == 'on'):
            h = "StartFan"
        print(h,s1,s2,s3)
    except:
        pass
    
def updateDB(h, s1, s2, s3, l):
        #DB에 작동 상황 저장
    if (h == "StartFan"):
        fan = "on"
    else:
        fan = "off"

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
        
    curs = conn.cursor()
    sql = """UPDATE device SET water1 = %s, water2 = %s, water3= %s, fan = %s WHERE number = '2'"""
    curs.execute(sql,(water1,water2,water3,fan))
    conn.commit()
    print(s1)
    clientSocekt.send(bytes('%s,%s,%s,%s,%s'%(s1,s2,s3,l,h),"UTF-8"))
    #print(h, s1, s2, s3, l,"send data success!!")
    
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
            return "StartMotor"
    elif watercycleCode == "053002":
        if soil > 800 : #임시값이므로 실험을 통한 수정 필요
            return "soilgood"
        else:
            return "StartMotor"
    elif watercycleCode == "053003":
        if soil > 600 : #임시값이므로 실험을 통한 수정 필요
            return "soilgood"
        else:
                            #print("soilbad3")
            return "StartMotor"
    elif watercycleCode == "053004":
        if soil > 400 : #임시값이므로 실험을 통한 수정 필요
            return "soilgood"
        else:
            return "StartMotor"

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
    curs = conn.cursor()
    if DeviceState == "water1FInish":
        sql = "UPDATE device SET water1 = %s LIMIT 2"

        curs.execute(sql, ('off'))
        conn.commit()

    elif DeviceState == "water2Finish":
        sql = "UPDATE device SET water2 = %s LIMIT 2"
        curs.execute(sql, ('off'))
        conn.commit()
    
    
sched = BackgroundScheduler()
sched.start()

sched.add_job(recvData,'interval', seconds=5, id="test_1", args=[data])
#sched.add_job(userCheck,'interval', seconds=3, id="test_2", args=[])

while True:
    try:
        conn = pymysql.connect(host='database-1.cudf3zzu3npf.us-east-2.rds.amazonaws.com', user='jeongmin', password='97shwjdals!', db='mydb',charset='utf8')
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

        currentdate = datetime.datetime.now()+timedelta(hours=9)
        month = currentdate.strftime('%m')
        h = Humi(humi)
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
        watercycleSprngCode = json_data["watercycleSprngCode"] #물주기 봄 코드
        watercycleSummerCode = json_data["watercycleSummerCode"] #물주기 여름 코드
        watercycleAutumnCode = json_data["watercycleAutumnCode"] #물주기 가을 코드
        watercycleWinterCode = json_data["watercycleWinterCode"] #물주기 겨울 코드
        # 053001 : 항상 흙을 축축하게 유지함(물에 잠김)
        # 053002 : 흙을 촉촉하게 유지함(물에 잠기지 않도록 주의)
        # 053003 : 토양 표면이 말랐을때 충분히 관수함
        # 053004 : 화분 흙 대부분 말랐을때 충분히 관수함
        watercycleCode = CurrentSeason(month)
        s1 = WaterCycle(watercycleCode, soil1) + '1'
        
        curs = conn.cursor()
        sql = "select * from userplant where position = %s"
        curs.execute(sql,id+'2')
        row = curs.fetchone()
        curs.close()
        json_data = json.loads(row[2])
        name = json_data["distbNm"] #이름
        hdCode= json_data["hdCode"] #습도 코드 083001 : 40% 미만, 083002 : 40 ~ 70%, 083003: 70% 이상
        watercycleSprngCode = json_data["watercycleSprngCode"] #물주기 봄 코드
        watercycleSummerCode = json_data["watercycleSummerCode"] #물주기 여름 코드
        watercycleAutumnCode = json_data["watercycleAutumnCode"] #물주기 가을 코드
        watercycleWinterCode = json_data["watercycleWinterCode"] #물주기 겨울 코드
        s2 = WaterCycle(watercycleCode, soil2) +'2'
        
        curs = conn.cursor()
        sql = "select * from userplant where position = %s"
        curs.execute(sql,id+'3')
        row = curs.fetchone()
        curs.close()
        json_data = json.loads(row[2])
        name = json_data["distbNm"] #이름
        hdCode= json_data["hdCode"] #습도 코드 083001 : 40% 미만, 083002 : 40 ~ 70%, 083003: 70% 이상
        watercycleSprngCode = json_data["watercycleSprngCode"] #물주기 봄 코드
        watercycleSummerCode = json_data["watercycleSummerCode"] #물주기 여름 코드
        watercycleAutumnCode = json_data["watercycleAutumnCode"] #물주기 가을 코드
        watercycleWinterCode = json_data["watercycleWinterCode"] #물주기 겨울 코드
        s3 = WaterCycle(watercycleCode, soil3) + '3'
        
        l = WaterLevel(level)
        userCheck()
        updateDB(h,s1,s2,s3,l)
        time.sleep(7)

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
