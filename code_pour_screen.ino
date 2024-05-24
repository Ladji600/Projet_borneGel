
#include <ArduinoJson.h>

#include <WiFi.h>

#include <HTTPClient.h>



#define SOUND_SPEED 340
#define TRIG_PULSE_DURATION_US 10

long ultrason_duration;
float distance_cm;

const int trig_pin = 5;
const int echo_pin = 18;

const float MAX_HEIGHT = 9.93;
const float MAX_GEL_HEIGHT = 7.20;  


const int BATTERYPIN = 36;
const float TensionMin = 1;
const float TensionMax = 3.3;

const int BATTERYPIN = 36;
const float TensionMin = 1.5;
const float TensionMax = 3.3;

void setup() {
  Serial.begin(115200);
  pinMode(trig_pin, OUTPUT);
  pinMode(echo_pin, INPUT);

  WiFi.begin(ssid, password);
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connexion au réseau WiFi...");
  }

  Serial.println("Connecté au réseau WiFi !");
}


void loop() {
  int gel_percentage = mesurerGel();
  int batteryLevel = getBattery();


  sendData(gel_percentage, batteryLevel);


  esp_sleep_enable_timer_wakeup(300000000); // 5 minutes en microsecondes
  esp_deep_sleep_start();

  
}


void sendData(int gel_percentage, int batteryLevel) {  
  
  DynamicJsonDocument doc(204);
  doc["battery_percentage"] = batteryLevel;
  doc["gel_percentage"] = gel_percentage;
  doc["numeroESP"] = 1;

  String postData;
  serializeJson(doc, postData);
  
  Serial.println(postData);

  HTTPClient http;
  http.begin(api_url);
  http.addHeader("Content-Type", "application/json");
  int httpResponseCode = http.POST(postData);

  http.end();
}
