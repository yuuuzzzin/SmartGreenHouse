#include <DHT.h>
#include <ArduinoJson.h>

#define SOIL A0
#define DHTPIN 2
#define DHTTYPE DHT11

DHT dht(DHTPIN, DHTTYPE);

void setup() {
  Serial.begin(9600);
  dht.begin();
}

//함수로 받아온 데이터값을 Json으로 변환
void makeJson(float temp, float humi, int soil) 
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

  //변환 함수 사용
  makeJson(temp, humi, soil);
}
