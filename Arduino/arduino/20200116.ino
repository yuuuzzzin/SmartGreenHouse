#include <DHT.h>
#include <ArduinoJson.h>

#define DHTPIN 2
#define DHTTYPE DHT11

DHT dht(DHTPIN, DHTTYPE);

void setup() {
  Serial.begin(9600);
  dht.begin();
}

float makeJson(float temp, float humi) { //함수로 받아온 데이터값을 Json으로 변환 시킵니다.
  StaticJsonDocument<200> doc;
  JsonObject root = doc.createNestedObject();

  root["온도"] = temp;
  root["습도"] = humi;

  Serial.print("Json data : ");
  serializeJson(doc, Serial);
  Serial.println();
}

void loop() {
  delay(2000);
  
  float t = dht.readTemperature();
  float h = dht.readHumidity();

  Serial.print("Temperature: ");
  Serial.print(t);
  Serial.print(" *C ");
  Serial.print("Humidity: ");
  Serial.print(h);
  Serial.println(" %\t");

  makeJson(t, h); //변환 함수 사용
}
