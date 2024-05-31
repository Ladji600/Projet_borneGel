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
            echo "Mission ajoutée avec succès";
        } catch (PDOException $e) {
            // Gérer les erreurs de connexion ou d'insertion dans la base de données
            echo "Erreur d'insertion dans la base de données: " . $e->getMessage();
        }
    } else {
        // Envoyer un message si des données sont manquantes
        echo "Données manquantes";
    }
} else {
    // Envoyer un message si une méthode incorrecte est utilisée
    echo "Méthode non autorisée (POST attendue)";
}
?>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Affectation de mission</title>
</head>
<body>
    <h1>Affectation de mission</h1>
    <form action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" method="post">
        <label for="typeMission">Type de mission :</label>
        <input type="text" id="typeMission" name="type" required><br><br>

        <label for="idEmployes">ID de l'employé :</label>
        <input type="number" id="idEmployes" name="idEmployes" required><br><br>

        <label for="idBorne">ID de la borne :</label>
        <input type="number" id="idBorne" name="idBorne" required><br><br>

        <input type="submit" value="Ajouter mission">
    </form>
</body>
</html>
