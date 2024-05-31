<?php
$servername = "51.210.151.13";
$username = "bornegel";
$password_db = "Bornegel2024!";
$dbname = "borne_gel_2024";

// Vérifier si la méthode est POST
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Récupérer les données POST
    $typeMission = $_POST['type'];
    $idEmployes = $_POST['idEmployes'];
    $idBorne = $_POST['idBorne'];

    // Vérifier si toutes les données nécessaires sont présentes
    if (isset($typeMission) && isset($idEmployes) && isset($idBorne)) {
        try {
            // Créer la connexion à la base de données
            $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
            $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

            // Préparer la requête SQL pour l'insertion de la mission
            $sql = "INSERT INTO mission (Type, Id_Employes, Id_Borne) VALUES (:Type, :Id_Employes, :Id_Borne)";
            $stmt = $conn->prepare($sql);
            $stmt->bindParam(':Type', $typeMission, PDO::PARAM_STR);
            $stmt->bindParam(':Id_Employes', $idEmployes, PDO::PARAM_INT);
            $stmt->bindParam(':Id_Borne', $idBorne, PDO::PARAM_INT);
            $stmt->execute();

            // Envoyer une réponse pour indiquer le succès
            echo "Mission ajoutee avec succes";
        } catch (PDOException $e) {
            // Gérer les erreurs de connexion ou d'insertion dans la base de données
            echo "Erreur d'insertion dans la base de donnes: " . $e->getMessage();
        }
    } else {
        // Envoyer un message si des données sont manquantes
        echo "Donnees manquantes";
    }
} else {
    // Envoyer un message si une méthode incorrecte est utilisée
    echo "Methode non autorisee (POST attendue)";
}
?>
