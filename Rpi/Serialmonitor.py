import serial
import os

if os.path.exists("/dev/ttyACM0") :
    tty = "/dev/ttyACM0"
elif os.path.exists("/dev/ttyACM1") :
    tty = "/dev/ttyACM1"

ArduinoSerial = serial.Serial(tty, 9600);
ArduinoSerial.flushInput();

while 1:
    try:            
        input = ArduinoSerial.readline();
        input = str(input)
        with open('SensorValue','w') as f:
            f.write(input);
    except:
        pass
