<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

// Connexion à la base de données
$servername = "51.210.151.13";
$username = "bornegel";
$password_db = "Bornegel2024!";
$dbname = "borne_gel_2024";

$conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

try {
    if ($_SERVER['REQUEST_METHOD'] === 'POST') {
        // Vérifier si les champs nécessaires sont bien définis
        if (empty($_POST['motif']) || empty($_POST['employer']) || empty($_POST['borne'])) {
            echo json_encode(["status" => "error", "message" => "Veuillez remplir tous les champs du formulaire."]);
            exit();
        }

        // Récupérer les valeurs du formulaire
        $selectedMotif = $_POST['motif'];
        $Id_Employes = $_POST['employer'];
        $Id_Borne = $_POST['borne'];

        // Préparation de la requête SQL
        $stmt = $conn->prepare("INSERT INTO mission (Type, Id_Employes, Id_Borne) VALUES (:Type, :Id_Employes, :Id_Borne)");

        // Liaison des paramètres
        $stmt->bindParam(':Type', $selectedMotif);
        $stmt->bindParam(':Id_Employes', $Id_Employes);
        $stmt->bindParam(':Id_Borne', $Id_Borne);

        // Exécution de la requête
        $stmt->execute();

        // Répondre avec un message de succès en JSON
        echo json_encode(["status" => "success", "message" => "Nouvelle mission créée avec succès."]);
    } else {
        // Afficher un message d'erreur si la requête n'est pas de type POST
        echo json_encode(["status" => "error", "message" => "Requête invalide."]);
    }
} catch (PDOException $e) {
    echo json_encode(["status" => "error", "message" => "Erreur : " . $e->getMessage()]);
}
?>
