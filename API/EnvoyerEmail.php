<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;
use PHPMailer\PHPMailer\SMTP;

require '/home/bornegel2024/SmartGel/API/PHPMailer-master/src/Exception.php';
require '/home/bornegel2024/SmartGel/API/PHPMailer-master/src/PHPMailer.php';
require '/home/bornegel2024/SmartGel/API/PHPMailer-master/src/SMTP.php';

$response = array("success" => false);

// Vérifier si la requête est une requête POST
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Vérifier si l'email et le mot de passe sont présents dans la requête
    if (isset($_POST['email']) && isset($_POST['password'])) {
        // Récupérer l'email et le mot de passe depuis la requête
        $email = $_POST['email'];
        $password = $_POST['password'];

        // Créer une nouvelle instance de PHPMailer
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
            $mail->isHTML(true);
            $mail->Subject = 'Création de votre compte SmartGel';
            $mail->Body = 'Votre compte a été créé avec succès. Voici vos informations de connexion :<br>Email : ' . $email . '<br>Mot de passe : ' . $password;

            // Envoi de l'email
            $mail->send();

            $response['success'] = true;
        } catch (Exception $e) {
            $response['error'] = $mail->ErrorInfo;
        }
    } else {
        $response['error'] = "Paramètres email et/ou mot de passe manquants.";
    }
} else {
    $response['error'] = "Méthode de requête non autorisée.";
}

echo json_encode($response);
?>
