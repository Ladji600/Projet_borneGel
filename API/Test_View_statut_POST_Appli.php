<?php
// Informations de connexion à la base de données
$servername = "51.210.151.13";
$username = "bornegel";
$password_db = "Bornegel2024!";
$dbname = "borne_gel_2024";

// Création de la connexion à la base de données
$conn = new mysqli($servername, $username, $password_db, $dbname);

// Vérification de la connexion
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Vérification si la requête est de type POST
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Récupération des données POST
    $idMission = $_POST['idMission'];

    // Requête SQL pour mettre à jour le statut de la mission
    $sql = "UPDATE status_mission SET Type = 'fait' WHERE IdStatus_Mission = ?";

    // Préparation de la requête
    $stmt = $conn->prepare($sql);
    if ($stmt) {
        // Liaison des paramètres
        $stmt->bind_param("i", $idMission);

        // Exécution de la requête
        if ($stmt->execute()) {
            echo "Mise à jour réussie Dally";
        } else {
            echo "Erreur lors de la mise à jour : " . $stmt->error;
        }

        // Fermeture de la requête préparée
        $stmt->close();
    } else {
        echo "Erreur lors de la préparation de la requête : " . $conn->error;
    }
} else {
    // Formulaire HTML pour envoyer les données via la méthode POST
    echo '<form method="post" action="">';
    echo 'ID Mission: <input type="text" name="idMission">';
    echo '<input type="submit" value="Mettre à jour">';
    echo '</form>';
}

// Fermeture de la connexion à la base de données
$conn->close();
?>
