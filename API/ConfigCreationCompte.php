<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;
use PHPMailer\PHPMailer\SMTP;

require '/home/bornegel2024/SmartGel/API/PHPMailer-master/src/Exception.php';
require '/home/bornegel2024/SmartGel/API/PHPMailer-master/src/PHPMailer.php';
require '/home/bornegel2024/SmartGel/API/PHPMailer-master/src/SMTP.php';

// Connexion à la base de données
$servername = "51.210.151.13";
$username = "bornegel";
$password_db = "Bornegel2024!";
$dbname = "borne_gel_2024";

$conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Vérifier si les champs requis sont vides
    if (empty($_POST['Fonction']) || empty($_POST['Etablissement']) || empty($_POST['prenom']) || empty($_POST['nom']) || empty($_POST['email']) || empty($_POST['password']) || empty($_POST['password2'])) {
        echo json_encode(["error" => "Veuillez remplir tous les champs du formulaire."]);
        exit;
    }

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
        echo json_encode(["error" => "Les mots de passe ne correspondent pas."]);
        exit;
    }

    // Vérification de l'existence de l'email
    $stmt = $conn->prepare("SELECT COUNT(*) FROM employes WHERE Mail = :email");
    $stmt->bindParam(':email', $email);
    $stmt->execute();
    $email_exists = $stmt->fetchColumn();

    if ($email_exists) {
        echo json_encode(["error" => "Un compte avec cet email existe déjà."]);
        exit;
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
    try {
        $stmt->execute();

        // Envoi de l'email
        $mail = new PHPMailer(true);
        try {
            // Configuration du serveur SMTP
            $mail->isSMTP();
            $mail->Host = 'smtp.gmail.com';
            $mail->SMTPAuth = true;
            $mail->Username = 'noreply.smartgel@gmail.com';
            $mail->Password = 'pktngbjzxjvnfqkd';
            $mail->SMTPSecure = 'tls';
            $mail->Port = 587;

            // Configuration de l'expéditeur et du destinataire
            $mail->setFrom('noreply.smartgel@gmail.com', 'SmartGel');
            $mail->addAddress($email);

            // Contenu de l'email
            $mail->CharSet = 'UTF-8';
            $mail->isHTML(true);
            $mail->Subject = 'Votre compte SmartGel a été créé';
            $mail->Body = 'Bonjour ' . htmlspecialchars($prenom) . ' ' . htmlspecialchars($nom) . ',<br><br>Votre compte SmartGel a été créé avec succès.<br><br>Voici vos informations de connexion:<br>Email: ' . htmlspecialchars($email) . '<br>Mot de passe: ' . htmlspecialchars($password) . '<br><br>Merci de ne pas partager ces informations.<br><br>Cordialement,<br>L\'équipe SmartGel';

            // Envoi de l'email
            $mail->send();

            // Réponse JSON indiquant le succès
            echo json_encode(["success" => "Nouveau compte créé avec succès et email envoyé."]);
        } catch (Exception $e) {
            // Réponse JSON en cas d'erreur d'envoi de mail
            echo json_encode(["success" => "Le compte a été créé, mais l'email n'a pas pu être envoyé.", "error" => $mail->ErrorInfo]);
        }
    } catch (PDOException $e) {
        // Réponse JSON en cas d'erreur d'insertion
        echo json_encode(["error" => "Erreur lors de la création du compte: " . $e->getMessage()]);
    }
} else {
    echo json_encode(["error" => "Méthode de requête non supportée."]);
}
?>
