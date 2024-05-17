const int trig_pin = 18;
const int echo_pin = 5;

// Vitesse du son dans l'air
#define SOUND_SPEED 340
#define TRIG_PULSE_DURATION_US 10

// Hauteur maximale du récipient (en cm)
const float MAX_HEIGHT = 9.93; // Mettez la hauteur maximale mesurée entre le capteur et le fond du récipient
// Hauteur maximale acceptable du gel (en cm)
const float MAX_GEL_HEIGHT = 7.20; // Mettez la hauteur maximale acceptable du gel

long ultrason_duration;
float distance_cm;

void setup() {
  Serial.begin(115200);
  pinMode(trig_pin, OUTPUT); // On configure le trig en output
  pinMode(echo_pin, INPUT); // On configure l'echo en input
}

void loop() {
  // Préparer le signal
  digitalWrite(trig_pin, LOW);
  delayMicroseconds(2);
  // Créer une impulsion de 10 µs
  digitalWrite(trig_pin, HIGH);
  delayMicroseconds(TRIG_PULSE_DURATION_US);
  digitalWrite(trig_pin, LOW);

  // Renvoyer le temps de propagation de l'onde (en µs)
  ultrason_duration = pulseIn(echo_pin, HIGH);

  // Calcul de la distance aller simple
  distance_cm = (ultrason_duration * SOUND_SPEED * 0.0001) / 2; // Division par 2 pour obtenir la distance aller simple

  // Calcul de la hauteur du gel (hauteur maximale moins la distance mesurée)
  float gel_height = MAX_HEIGHT - distance_cm;

  // Vérification pour éviter les valeurs négatives
  if (gel_height < 0) {
    gel_height = 0;
  }

  // Vérification pour éviter que le gel dépasse la limite maximale
  if (gel_height > MAX_GEL_HEIGHT) {
    gel_height = MAX_GEL_HEIGHT;
  }

  // Calcul du pourcentage de gel avec arrondi
  int gel_percentage = round((gel_height / MAX_GEL_HEIGHT) * 100);

  // Affichage du pourcentage de gel sur le port série
  Serial.print("Pourcentage de gel: ");
  Serial.print(gel_percentage);
  Serial.println("%");

  delay(1000);
}
