<?php
header("Access-Control-Allow-Origin: *");
// Connexion à la base de données
$servername = "51.210.151.13";
$username = "bornegel";
$password_db = "Bornegel2024!";
$dbname = "borne_gel_2024";


$conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Vérifier si les champs requis sont vides
    if (empty($_POST['Borne']) || empty($_POST['Etablissement']) || empty($_POST['Salle'])) {
        echo "Veuillez remplir tous les champs du formulaire.";
    } else {
        // Récupération des données du formulaire
        $etablissement = $_POST['Etablissement'];
        $salle = $_POST['Salle'];
        $borne = $_POST['Borne'];

        // Préparation de la requête SQL pour la mise à jour
        $sql = "UPDATE borne SET Salle = :salle, Id_Etablissement = :etablissement WHERE IdBorne = :borne";
        $stmt = $conn->prepare($sql);

        // Liaison des paramètres
        $stmt->bindParam(':salle', $salle);
        $stmt->bindParam(':etablissement', $etablissement);
        $stmt->bindParam(':borne', $borne);

        // Exécution de la requête
        $stmt->execute();

        // Affichage d'un message de succès en JavaScript
        echo "Détails mis à jour avec succès.";
    }
} else {
    echo "Méthode de requête non supportée.";
}
?>
