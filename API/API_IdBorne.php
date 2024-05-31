<?php
// Connexion � la base de donn�es
$servername = "51.210.151.13";
$username = "bornegel";
$password_db = "Bornegel2024!";
$dbname = "borne_gel_2024";

// V�rification si les donn�es POST sont pr�sentes
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // R�cup�ration des donn�es POST au format JSON
    $postdata = file_get_contents("php://input");

    // V�rification si des donn�es sont pr�sentes
    if (isset($postdata) && !empty($postdata)) {
        // Conversion des donn�es JSON en tableau associatif
        $request = json_decode($postdata);

        // R�cup�ration des valeurs des capteurs
        $gel_percentage = $request->gel_percentage;
        $battery_percentage = $request->battery_percentage;
        $numeroESP = $request->numeroESP;

        try {
            // Connexion � la base de donn�es
            $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
            $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

            // Requ�te SQL d'insertion ou de mise � jour des donn�es dans la table "borne"
            $sql = "INSERT INTO borne (Niveau_Gel, Niveau_Batterie, IdBorne) VALUES (:gel, :battery, :id) 
                    ON DUPLICATE KEY UPDATE Niveau_Gel = VALUES(Niveau_Gel), Niveau_Batterie = VALUES(Niveau_Batterie)";
            $stmt = $conn->prepare($sql);

            // Liaison des param�tres de la requ�te avec les valeurs
            $stmt->bindParam(':gel', $gel_percentage);
            $stmt->bindParam(':battery', $battery_percentage);
            $stmt->bindParam(':id', $numeroESP);

            // Ex�cution de la requ�te
            $stmt->execute();

            // R�ponse JSON pour confirmer l'insertion ou la mise � jour
            http_response_code(201);
            echo json_encode(array("message" => "Donn�es ins�r�es avec succ�s."));
        } catch (PDOException $e) {
            // En cas d'erreur, r�ponse JSON avec le message d'erreur
            http_response_code(500);
            echo json_encode(array("message" => "Erreur lors de l'insertion ou de la mise � jour des donn�es : " . $e->getMessage()));
        }
    } else {
        // En cas de donn�es POST vides, r�ponse JSON avec un message d'erreur
        http_response_code(400);
        echo json_encode(array("message" => "Aucune donn�e re�ue."));
    }
} else {
    // En cas de m�thode de requ�te non autoris�e, r�ponse JSON avec un message d'erreur
    http_response_code(405);
    echo json_encode(array("message" => "M�thode non autoris�e."));
}
?>
s