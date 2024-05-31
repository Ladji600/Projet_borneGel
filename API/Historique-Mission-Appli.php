<?php
$servername = "51.210.151.13";
$username = "bornegel";
$password_db = "Bornegel2024!";
$dbname = "borne_gel_2024";

// Vérifier si l'ID de l'établissement est passé dans l'URL
if(isset($_GET['Id_Etablissement'])) {
    $id_etablissement = $_GET['Id_Etablissement'];

    try {
        $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
        $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

        // Créer la requête SQL pour récupérer les missions des employés dans le même établissement
        $sql = "SELECT mission.*, borne.Salle, borne.Id_Etablissement
                FROM mission
                INNER JOIN borne ON mission.Id_Borne = borne.IdBorne
                WHERE borne.Id_Etablissement = :Id_Etablissement
                AND mission.Date <= CURDATE()";
                 //DATE_SUB(CURDATE(), INTERVAL 1 DAY)";

        // Préparer et exécuter la requête en utilisant un paramètre nommé pour l'ID de l'établissement
        $stmt = $conn->prepare($sql);
        $stmt->bindParam(':Id_Etablissement', $id_etablissement);
        $stmt->execute();

        // Récupérer les résultats
        $results = $stmt->fetchAll(PDO::FETCH_ASSOC);

        // Retourner les résultats sous forme de réponse JSON
        header('Content-Type: application/json');
        echo json_encode($results);
    } catch(PDOException $e) {
        // Gérer les erreurs de connexion à la base de données
        echo "Erreur de connexion à la base de données: " . $e->getMessage();
    }
} else {
    echo "L'ID de l'établissement n'est pas spécifié dans la requête.";
}
?>
