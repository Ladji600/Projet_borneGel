#include <ArduinoJson.h>
#include <WiFi.h>
#include <HTTPClient.h>

const char* ssid = "Professeurs";
const char* password = "&*%@0931584S";
const char* api_url = "http://51.210.151.13/btssnir/projets2024/bornegel2024/bornegel2024/SmartGel/API/API_IdBorne.php";

const int trig_pin = 18;
const int echo_pin = 5;
#define SOUND_SPEED 340
#define TRIG_PULSE_DURATION_US 10

const float MAX_HEIGHT = 9.93;
const float MAX_GEL_HEIGHT = 7.20;
long ultrason_duration;
float distance_cm;

const int BATTERYPIN = 34;
const float TensionMin = 1;  
const float TensionMax = 3.5;

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

  // Mesurer le gel et le niveau de batterie
  int gel_percentage = mesurerGel();
  int batteryLevel = getBattery();

  // Envoyer les données
  sendData(gel_percentage, batteryLevel);

  esp_sleep_enable_timer_wakeup(15000000); // 15 secondes en microsecondes
  esp_deep_sleep_start();
}

int mesurerGel() {
  digitalWrite(trig_pin, LOW);
  delayMicroseconds(2);
  digitalWrite(trig_pin, HIGH);
  delayMicroseconds(TRIG_PULSE_DURATION_US);
  digitalWrite(trig_pin, LOW);
  unsigned long ultrason_duration = pulseIn(echo_pin, HIGH);
  float distance_cm = (ultrason_duration * SOUND_SPEED * 0.0001) / 2;
  float gel_height = MAX_HEIGHT - distance_cm;
  if (gel_height < 0) {
    gel_height = 0;
  }
  if (gel_height > MAX_GEL_HEIGHT) {
    gel_height = MAX_GEL_HEIGHT;
  }
  return round((gel_height / MAX_GEL_HEIGHT) * 100);
}

int getBattery() {
  float bat = analogRead(BATTERYPIN);
  int minValue = (4095 * TensionMin) / 3;
  int maxValue = (4095 * TensionMax) / 3;
  bat = ((bat - minValue) / (maxValue - minValue)) * 100;
  return int(bat);
}

void sendData(int gel_percentage, int batteryLevel) {
  DynamicJsonDocument doc(204);
  doc["battery_percentage"] = batteryLevel;
  doc["gel_percentage"] = gel_percentage;
  doc["numeroESP"] = 1000;

  String postData;
  serializeJson(doc, postData);
  
  Serial.println(postData);

  HTTPClient http;
  http.begin(api_url);
  http.addHeader("Content-Type", "application/json");
  int httpResponseCode = http.POST(postData);

  http.end();
}