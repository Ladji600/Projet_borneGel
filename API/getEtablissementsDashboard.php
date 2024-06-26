<?php
header("Access-Control-Allow-Origin: *");

$servername = "51.210.151.13";
$username = "bornegel";
$password_db = "Bornegel2024!";
$dbname = "borne_gel_2024";

// Récupérer l'ID de l'employé depuis les paramètres GET
$userId = $_GET['userId'];

try {
    $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Préparer la requête en fonction de l'ID de l'employé
    $stmt = $conn->prepare("SELECT IdEtablissement, NomEtablissement FROM etablissement WHERE Id_Employes = :userId");
    $stmt->bindParam(':userId', $userId);
    $stmt->execute();

    $etablissements = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode($etablissements);
}
catch(PDOException $e) {
    echo "Erreur : " . $e->getMessage();
}
?>
