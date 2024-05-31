<?php
// Connexion à la base de données
$servername = "51.210.151.13";
$username = "bornegel";
$password_db = "Bornegel2024!";
$dbname = "borne_gel_2024";

// Vérification si les données POST sont présentes
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
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

            // Vérifier si l'ID de la borne existe déjà
            $stmt = $conn->prepare("SELECT COUNT(*) FROM borne WHERE IdBorne = :id");
            $stmt->bindParam(':id', $numeroESP);
            $stmt->execute();
            $count = $stmt->fetchColumn();

            if ($count > 0) {
                // Si l'ID existe, utiliser la méthode PATCH pour mettre à jour les données
                include 'Api-PATCH.php';
            } else {
                // Si l'ID n'existe pas, insérer de nouvelles données en utilisant la méthode POST
                include 'API_IOT.php.php';
            }

        } catch (PDOException $e) {
            // En cas d'erreur, réponse JSON avec le message d'erreur
            http_response_code(500);
            echo json_encode(array("message" => "Erreur lors de la vérification de l'ID de la borne : " . $e->getMessage()));
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
