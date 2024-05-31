#include <ArduinoJson.h>
#include <WiFi.h>
#include <HTTPClient.h>

// Informations de connexion WiFi
const char* ssid = "Professeurs";
const char* password = "&*%@0931584S";

// URL de l'API pour envoyer les donn�es
const char* api_url = "http://51.210.151.13/btssnir/projets2024/bornegel2024/bornegel2024/SmartGel/API/API_IOT.php";

// D�finition des broches pour le capteur ultrason
const int trig_pin = 5;
const int echo_pin = 18;

// D�finition de la vitesse du son en m/s
#define SOUND_SPEED 340
// Dur�e de l'impulsion du trigger en microsecondes
#define TRIG_PULSE_DURATION_US 10

// Hauteurs maximales pour le calcul du pourcentage de gel
const float MAX_HEIGHT = 9.93; // Hauteur maximale mesur�e par le capteur (en cm)
const float MAX_GEL_HEIGHT = 7.20; // Hauteur maximale du gel dans le r�servoir (en cm)

long ultrason_duration; // Dur�e de l'echo ultrason
float distance_cm; // Distance mesur�e par le capteur ultrason

// Broche pour mesurer le niveau de batterie
const int BATTERYPIN = 36;
const float TensionMin = 1.5; // Tension minimale de la batterie (en V)
const float TensionMax = 3.3; // Tension maximale de la batterie (en V)

// Broche pour la LED d'indication
const int LED_PIN = 23; 

void setup() {
  Serial.begin(115200); // Initialisation de la communication s�rie
  pinMode(trig_pin, OUTPUT); // D�finir la broche trig_pin comme sortie
  pinMode(echo_pin, INPUT); // D�finir la broche echo_pin comme entr�e
  pinMode(LED_PIN, OUTPUT); // D�finir la broche LED_PIN comme sortie
  digitalWrite(LED_PIN, LOW); // S'assurer que la LED est atteinte au d�marrage

  // Connexion au r�seau WiFi
  WiFi.begin(ssid, password);
  
  // Attendre jusqu'� ce que la connexion soit �tablie
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connexion au r�seau WiFi...");
  }

  Serial.println("Connect�e au r�seau WiFi !");
}

void loop() {
  // Activer la LED au r�veil de l'ESP32
  digitalWrite(LED_PIN, HIGH);

  // Mesurer le niveau de gel et de batterie
  int gel_percentage = mesurerGel();
  int batteryLevel = getBattery();

  // Envoyer les donn�es � l'API
  sendData(gel_percentage, batteryLevel);

  // D�sactiver la LED apr�s l'envoi des donn�es
  digitalWrite(LED_PIN, LOW);

  // Mettre l'ESP32 en mode sommeil profond pendant 5 minutes
  esp_sleep_enable_timer_wakeup(300000000); // 5 minutes en microsecondes
  esp_deep_sleep_start();
}

int mesurerGel() {
  // Envoi d'une impulsion pour d�clencher le capteur ultrason
  digitalWrite(trig_pin, LOW);
  delayMicroseconds(2);
  digitalWrite(trig_pin, HIGH);
  delayMicroseconds(TRIG_PULSE_DURATION_US);
  digitalWrite(trig_pin, LOW);
  
  // Mesure de la dur�e de l'�cho
  unsigned long ultrason_duration = pulseIn(echo_pin, HIGH);
  
  // Calcul de la distance en cm
  float distance_cm = (ultrason_duration * SOUND_SPEED * 0.0001) / 2;
  
  // Calcul de la hauteur du gel
  float gel_height = MAX_HEIGHT - distance_cm;
  if (gel_height < 0) {
    gel_height = 0;
  }
  if (gel_height > MAX_GEL_HEIGHT) {
    gel_height = MAX_GEL_HEIGHT;
  }
  
  // Retourne le pourcentage de gel restant
  return round((gel_height / MAX_GEL_HEIGHT) * 100);
}

int getBattery() {
  // Lire la valeur analogique de la batterie
  float bat = analogRead(BATTERYPIN);
  
  // Convertir la lecture en pourcentage
  int minValue = (4095 * TensionMin) / 3;
  int maxValue = (4095 * TensionMax) / 3;
  bat = ((bat - minValue) / (maxValue - minValue)) * 100;
  
  // Retourne le pourcentage de batterie
  return int(bat);
} 

void sendData(int gel_percentage, int batteryLevel) {
  // Cr�ation d'un document JSON pour envoyer les donn�es
  DynamicJsonDocument doc(204);
  doc["battery_percentage"] = batteryLevel;
  doc["gel_percentage"] = gel_percentage;
  doc["numeroESP"] = 1; 

  // S�rialisation du document JSON
  String postData;
  serializeJson(doc, postData);
  
  // Affichage des donn�es JSON 
  Serial.println(postData);

  // Initialisation de la requ�te HTTP
  HTTPClient http;
  http.begin(api_url);
  http.addHeader("Content-Type", "application/json");
  
  // Envoi de la requ�te POST avec les donn�es JSON
  int httpResponseCode = http.POST(postData);

  // Fin de la requ�te HTTP
  http.end();
}
