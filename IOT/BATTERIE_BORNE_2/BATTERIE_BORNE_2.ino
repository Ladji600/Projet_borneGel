const int BATTERYPIN = 34;
const float TensionMin = 1;  // Tension minimale de la batterie en volts
const float TensionMax = 3.5;  // Tension maximale de la batterie en volts

void setup() {
  Serial.begin(115200);
  pinMode(BATTERYPIN, INPUT);
}

void loop() {
  int battery_percentage = getBattery();
  Serial.print("Pourcentage de batterie: ");
  Serial.print(battery_percentage);
  Serial.println("%");
  delay(1000);  // Attendre 1 seconde entre chaque mesure
}

int getBattery() {
  float bat = analogRead(BATTERYPIN);  // Lecture de la valeur analogique
  int minValue = (4095 * TensionMin) / 3;  // Conversion de la tension minimale en valeur analogique
  int maxValue = (4095 * TensionMax) / 3;  // Conversion de la tension maximale en valeur analogique
  bat = ((bat - minValue) / (maxValue - minValue)) * 100;  // Calcul du pourcentage de batterie
  return int(bat);  // Retourne le pourcentage de batterie en entier
}
