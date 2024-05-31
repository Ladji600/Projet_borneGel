<?php
// Connexion à la base de données
$servername = "51.210.151.13";
$username = "bornegel";
$password_db = "Bornegel2024!";
$dbname = "borne_gel_2024";

// Vérification si les données POST sont présentes
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Récupération des données POST au format JSON
    $postdata = file_get_contents("php://input");

    // Vérification si des données sont présentes
    if (isset($postdata) && !empty($postdata)) {
        // Conversion des données JSON en tableau associatif
        $request = json_decode($postdata);

        // Récupération des valeurs des capteurs
        $gel_percentage = $request->gel_percentage;
        $battery_percentage = $request->battery_percentage;
        $numeroESP = $request->numeroESP;

        try {
            // Connexion à la base de données
            $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
            $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

            // Requête SQL d'insertion ou de mise à jour des données dans la table "borne"
            $sql = "INSERT INTO borne (Niveau_Gel, Niveau_Batterie, IdBorne) VALUES (:gel, :battery, :id) 
                    ON DUPLICATE KEY UPDATE Niveau_Gel = VALUES(Niveau_Gel), Niveau_Batterie = VALUES(Niveau_Batterie)";
            $stmt = $conn->prepare($sql);

            // Liaison des paramètres de la requête avec les valeurs
            $stmt->bindParam(':gel', $gel_percentage);
            $stmt->bindParam(':battery', $battery_percentage);
            $stmt->bindParam(':id', $numeroESP);

            // Exécution de la requête
            $stmt->execute();

            // Réponse JSON pour confirmer l'insertion ou la mise à jour
            http_response_code(201);
            echo json_encode(array("message" => "Données insérées avec succès."));
        } catch (PDOException $e) {
            // En cas d'erreur, réponse JSON avec le message d'erreur
            http_response_code(500);
            echo json_encode(array("message" => "Erreur lors de l'insertion ou de la mise à jour des données : " . $e->getMessage()));
        }
    } else {
        // En cas de données POST vides, réponse JSON avec un message d'erreur
        http_response_code(400);
        echo json_encode(array("message" => "Aucune donnée reçue."));
    }
} else {
    // En cas de méthode de requête non autorisée, réponse JSON avec un message d'erreur
    http_response_code(405);
    echo json_encode(array("message" => "Méthode non autorisée."));
}
?>
s