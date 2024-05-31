<?php
header("Access-Control-Allow-Origin: *");

$servername = "51.210.151.13";
$username = "bornegel";
$password_db = "Bornegel2024!";
$dbname = "borne_gel_2024";

try {
    $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Récupérer l'établissement de l'utilisateur
    $userId = $_GET['userId'];
    $stmt = $conn->prepare("SELECT Id_Etablissement FROM employes WHERE IdEmployes = :userId");
    $stmt->bindParam(':userId', $userId);
    $stmt->execute();
    $result = $stmt->fetch(PDO::FETCH_ASSOC);
    $etablissementId = $result['Id_Etablissement'];

    // Sélectionner les agents associés à l'établissement de l'utilisateur avec le nom de l'établissement
    $stmt = $conn->prepare("SELECT e.IdEmployes, e.Nom, e.Prenom, et.NomEtablissement FROM employes e JOIN etablissement et ON e.Id_Etablissement = et.IdEtablissement WHERE e.Id_Role = 1 AND e.Id_Etablissement = :etablissementId");
    $stmt->bindParam(':etablissementId', $etablissementId);
    $stmt->execute();

    $agents = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode($agents);
}
catch(PDOException $e) {
    echo "Erreur : " . $e->getMessage();
}
?>