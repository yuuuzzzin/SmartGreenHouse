#include <DHT.h>
#include <ArduinoJson.h>

#define SOIL A0
#define DHTPIN 7
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);
 
int fan=2;
int motor1=3;
int motor2=4;
int motor3=5;

void setup() {
  Serial.begin(9600);
  dht.begin();

  pinMode(fan, OUTPUT);
  pinMode(motor1, OUTPUT);
  pinMode(motor2, OUTPUT);
  pinMode(motor3, OUTPUT);

  digitalWrite (fan, HIGH);
  digitalWrite (motor1, HIGH);
  digitalWrite (motor2, HIGH);
  digitalWrite (motor3, HIGH);
}

void operateMotor(char motor){
  if (motor == 'a'){
    digitalWrite(motor1, LOW);
    delay(3000);
    digitalWrite(motor1, HIGH);  
  }
  if (motor == 'b'){
    digitalWrite(motor2, LOW);
    delay(3000);
    digitalWrite(motor2, HIGH);  
  }
}

//함수로 받아온 데이터값을 Json으로 변환
float makeJson(float temp, float humi, int soil) 
{ 
  StaticJsonDocument<256> doc;
  JsonObject root = doc.to<JsonObject>();
  root["temp"] = temp;
  root["humi"] = humi;
  root["soil"] = soil;
  root["water"] = soil;
  root["lumi"] = soil;

  serializeJson(root, Serial);
  Serial.println();
  delay(2000);
}

void loop() {
  float temp = dht.readTemperature();
  float humi = dht.readHumidity();
  int soil = analogRead(SOIL);
  makeJson(temp, humi, soil);

  if(Serial.available())
  {
    String Data = Serial.readString();
    /*if(Data == "SensorValue")
    {
      float temp = dht.readTemperature();
      float humi = dht.readHumidity();
      int soil = analogRead(SOIL);
      //변환 함수 사용
      makeJson(temp, humi, soil);
    }*/
    if(Data == "StartMotor1")
    {
      operateMotor('a');
    }
    else if(Data == "StartMotor2")
    {
      operateMotor('b');
    }
  }
}
