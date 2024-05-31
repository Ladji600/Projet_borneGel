<?php

header("Access-Control-Allow-Origin: *");

$servername = "51.210.151.13";
$username = "bornegel";
$password_db = "Bornegel2024!";
$dbname = "borne_gel_2024";

try {
    $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

// Récupérer l'établissement à partir des paramètres GET
    $etablissement = $_GET['etablissement'];
    /*
// Récupérer l'ID de l'utilisateur depuis les paramètres GET
    $userId = $_GET['userId'];
    */

// Préparer la requête en fonction de l'établissement
    // $stmt = $conn->prepare("SELECT IdBorne, Niveau_Gel, Niveau_Batterie, Salle, Heure, Date, Id_Etablissement FROM borne WHERE Id_Etablissement = :etablissement AND Id_Employes = :userId");
    $stmt = $conn->prepare("SELECT * FROM borne WHERE Id_Etablissement = :etablissement");
    $stmt->bindParam(':etablissement', $etablissement);
    /*$stmt->bindParam(':userId', $userId); */
    $stmt->execute();

    $bornes = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode($bornes);

}
catch(PDOException $e) {
    echo "Erreur : " . $e->getMessage();
}

?>
