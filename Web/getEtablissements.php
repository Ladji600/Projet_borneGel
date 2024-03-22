<?php
$servername = "51.210.151.13";
$username = "bornegel";
$password_db = "Bornegel2024!";
$dbname = "borne_gel_2024";

try {
    $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    $stmt = $conn->prepare("SELECT IdEtablissement, NomEtablissement FROM etablissement");
    $stmt->execute();

    $etablissements = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode($etablissements);
}
catch(PDOException $e) {
    echo "Erreur : " . $e->getMessage();
}
?>
