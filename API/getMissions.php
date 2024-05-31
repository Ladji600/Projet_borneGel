<?php
header("Access-Control-Allow-Origin: *");

$servername = "51.210.151.13";
$username = "bornegel";
$password_db = "Bornegel2024!";
$dbname = "borne_gel_2024";

// Vérifier si l'ID de l'utilisateur est passé en paramètre
if(isset($_GET['userId'])) {
    $userId = $_GET['userId'];

    try {
        $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
        $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

        // Requête SQL pour récupérer les missions de l'utilisateur spécifié
        $stmt = $conn->prepare("SELECT IdMission, Type , Heure , Date, Id_Employes, Id_Borne FROM mission WHERE Id_Employes = :userId");
        $stmt->bindParam(':userId', $userId);
        $stmt->execute();

        $missions = $stmt->fetchAll(PDO::FETCH_ASSOC);

        echo json_encode($missions);
    }
    catch(PDOException $e) {
        echo "Erreur : " . $e->getMessage();
    }
} else {
    echo "L'ID de l'utilisateur n'a pas été fourni.";
}
?>
