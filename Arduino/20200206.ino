#include <DHT.h>
#include <ArduinoJson.h>

#define LED 9 //식물생장용 led

#define SOIL1 A0 //토양수분 1,2,3
#define SOIL2 A1
#define SOIL3 A2

#define LEVEL A3 //수위
#define CDS A4 //조도

#define DHTPIN 2 //온습도
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);

int FAN=3;
int motor1=4;
int motor2=5;
int motor3=6;

void setup() {
  Serial.begin(9600);
  dht.begin();

  pinMode(FAN, OUTPUT); //쿨링팬
  pinMode(motor1, OUTPUT); //워터펌프1
  pinMode(motor2, OUTPUT); //워터펌프2
  pinMode(motor3, OUTPUT); //워터펌프3
  pinMode(LED, OUTPUT); //워터펌프3

  digitalWrite (FAN, HIGH);
  digitalWrite (motor1, HIGH);
  digitalWrite (motor2, HIGH);
  digitalWrite (motor3, HIGH);
  digitalWrite (LED, HIGH);

}
void operateLED(int operatetime) {
  digitalWrite(LED, LOW);
  delay(operatetime);
  digitalWrite(LED, HIGH);  
}
void operateFan(int operatetime){
  digitalWrite(FAN, LOW);
  delay(operatetime);
  digitalWrite(FAN, HIGH);  
}
void operateMotor(char motor){
  if (motor == 'a'){ //
    digitalWrite(motor1, LOW);
    delay(3000);
    digitalWrite(motor1, HIGH);  
  }
  if (motor == 'b'){ //motor1
    digitalWrite(motor2, LOW);
    delay(3000);
    digitalWrite(motor2, HIGH);  
  }
  if (motor == 'c'){ //motor2
    digitalWrite(motor3, LOW);
    delay(3000);
    digitalWrite(motor3, HIGH);  
  }
}

//함수로 받아온 데이터값을 Json으로 변환
void makeJson(float temp, float humi, int soil1, int soil2, int soil3, int cds, int level) 
{ 
  StaticJsonDocument<256> doc;
  JsonObject root = doc.to<JsonObject>();
  root["temp"] = temp;
  root["humi"] = humi;
  root["soil1"] = soil1;
  root["soil2"] = soil2;
  root["soil3"] = soil3;
  root["cds"] = cds;
  root["level"] = level;

  serializeJson(root, Serial);
  Serial.println();
}

void loop() {
  float temp = dht.readTemperature();
  float humi = dht.readHumidity();
  int soil1 = analogRead(SOIL1);
  int soil2 = analogRead(SOIL2);
  int soil3 = analogRead(SOIL3);
  int cds = analogRead(CDS);
  int level = analogRead(LEVEL);
  
  if(Serial.available())
  {
    String Data = Serial.readString();
    if(Data == "StartFan")
    {
      operateMotor('a');
    }
    if(Data == "StartMotor1") //워터펌프1 작동
    {
      operateMotor('a');
    }
    else if(Data == "StartMotor2") //워터펌프2 작동
    {
      operateMotor('b');
    }
    else if(Data == "StartMotor3") //워터펌프3 작동
    {
      operateMotor('c');
    }
  }
  makeJson(temp, humi, soil1, soil2, soil3, cds, level);
  delay(100);
}
