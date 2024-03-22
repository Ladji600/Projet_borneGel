<?php
// Connexion à la base de données
$servername = "51.210.151.13";
$username = "bornegel";
$password_db = "Bornegel2024!";
$dbname = "borne_gel_2024";

$conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Récupération des données du formulaire
    $fonction = $_POST['Fonction'];
    $etablissement = $_POST['Etablissement'];
    $prenom = $_POST['prenom'];
    $nom = $_POST['nom'];
    $email = $_POST['email'];
    $password = $_POST['password'];
    $password2 = $_POST['password2'];

    // Vérifier si les mots de passe correspondent
    if ($password !== $password2) {
        // Gérer l'erreur ici
        exit('Les mots de passe ne correspondent pas.');
    }

    // Préparation de la requête SQL
    $stmt = $conn->prepare("INSERT INTO employes (Nom, Prenom, Mail, Mot_De_Passe, Id_Role, Id_Etablissement) VALUES (:nom, :prenom, :email, :password, :fonction, :etablissement)");

    // Liaison des paramètres
    $stmt->bindParam(':nom', $nom);
    $stmt->bindParam(':prenom', $prenom);
    $stmt->bindParam(':email', $email);
    $stmt->bindParam(':password', $password);
    $stmt->bindParam(':fonction', $fonction);
    $stmt->bindParam(':etablissement', $etablissement);

    // Exécution de la requête
    $stmt->execute();

    // Réponse en cas de succès
    echo "Nouveau compte créé avec succès.";
} else {
    echo "Méthode de requête non supportée.";
}
?>
