import serial
import os
import time

if os.path.exists("/dev/ttyACM0") :
    tty = "/dev/ttyACM0"
elif os.path.exists("/dev/ttyACM1") :
    tty = "/dev/ttyACM1"

ArduinoSerial = serial.Serial(tty, 9600);
ArduinoSerial.flushInput();

while 1:
    input = ArduinoSerial.readline();
    with open('SensorValue','w') as f:
        f.write(input);
    time.sleep(10) #10초에 한번 읽어서 반영
