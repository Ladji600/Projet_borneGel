<?php
header("Access-Control-Allow-Origin: *");

$servername = "51.210.151.13";
$username = "bornegel";
$password_db = "Bornegel2024!";
$dbname = "borne_gel_2024";

// Vérifier si l'ID de l'employé est passé en tant que paramètre GET
if(isset($_GET['userId'])) {
    $userId = $_GET['userId'];

    try {
        $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
        $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

        // Préparer la requête en fonction de l'ID de l'employé
        $stmt = $conn->prepare("SELECT * FROM employes WHERE id = :userId");
        $stmt->bindParam(':userId', $userId);
        $stmt->execute();

        // Récupérer les informations de l'employé
        $employee = $stmt->fetch(PDO::FETCH_ASSOC);

        echo json_encode($employee);
    }
    catch(PDOException $e) {
        echo "Erreur : " . $e->getMessage();
    }
} else {
    echo "L'ID de l'utilisateur n'est pas spécifié.";
}
?>
