<?php
header("Access-Control-Allow-Origin: *");

$servername = "51.210.151.13";
$username = "bornegel";
$password_db = "Bornegel2024!";
$dbname = "borne_gel_2024";

try {
    $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Préparer la requête pour récupérer toutes les bornes
    $stmt = $conn->prepare("SELECT * FROM borne");
    $stmt->execute();

    // Récupérer toutes les bornes sous forme de tableau associatif
    $bornes = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode($bornes);
} catch(PDOException $e) {
    echo "Erreur : " . $e->getMessage();
}
?>
