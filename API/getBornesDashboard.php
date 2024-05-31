<?php
header("Access-Control-Allow-Origin: *");

$servername = "51.210.151.13";
$username = "bornegel";
$password_db = "Bornegel2024!";
$dbname = "borne_gel_2024";

try {
    $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    $stmt = $conn->prepare("SELECT borne.IdBorne, etablissement.NomEtablissement
                            FROM borne
                            INNER JOIN etablissement ON borne.Id_Etablissement = etablissement.IdEtablissement");
    $stmt->execute();

    $bornes = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode($bornes);
}
catch(PDOException $e) {
    echo "Erreur : " . $e->getMessage();
}
?>
