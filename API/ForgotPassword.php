<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;
use PHPMailer\PHPMailer\SMTP;

require '/home/bornegel2024/SmartGel/API/PHPMailer-master/src/Exception.php';
require '/home/bornegel2024/SmartGel/API/PHPMailer-master/src/PHPMailer.php';
require '/home/bornegel2024/SmartGel/API/PHPMailer-master/src/SMTP.php';

// Vérifier si la requête est une requête POST
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Vérifier si l'email est présent dans la requête
    if (isset($_POST['email'])) {
        // Récupérer l'email depuis la requête
        $email = $_POST['email'];
        $token = bin2hex(random_bytes(16)); // Générer un token sécurisé
        $expiry = date("Y-m-d H:i:s", strtotime('+1 hour')); // Définir une expiration de 1 heure

        // Connexion à la base de données
        $servername = "51.210.151.13";
        $username = "bornegel";
        $password_db = "Bornegel2024!";
        $dbname = "borne_gel_2024";

        try {
            $conn = new PDO("mysql:host=$servername;dbname=$dbname;charset=utf8mb4", $username, $password_db);
            $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

            // Insérer le token et l'email dans la table password_resets
            $stmt = $conn->prepare("INSERT INTO password_resets (email, token, expiry) VALUES (:email, :token, :expiry)");
            $stmt->bindParam(':email', $email);
            $stmt->bindParam(':token', $token);
            $stmt->bindParam(':expiry', $expiry);
            $stmt->execute();

            // Créer une nouvelle instance de PHPMailer
            $mail = new PHPMailer(true);

            try {
                // Configuration du serveur SMTP
                $mail->isSMTP();
                $mail->Host = 'smtp.gmail.com';
                $mail->SMTPAuth = true;
                $mail->Username = 'noreply.smartgel@gmail.com';
                $mail->Password = 'pktngbjzxjvnfqkd';
                $mail->SMTPSecure = PHPMailer::ENCRYPTION_STARTTLS;
                $mail->Port = 587;

                // Configuration de l'expéditeur et du destinataire
                $mail->setFrom('noreply.smartgel@gmail.com', 'SmartGel');
                $mail->addAddress($email);

                // Contenu de l'email
                $mail->CharSet = 'UTF-8';
                $mail->isHTML(true);
                $mail->Subject = 'Réinitialisation de mot de passe';
                $mail->Body    = 'Vous avez demandé une réinitialisation de mot de passe. Cliquez sur le lien suivant pour procéder à la réinitialisation : <a href="http://51.210.151.13/btssnir/projets2024/bornegel2024/bornegel2024/SmartGel/WEB/ResetPassword.html?token=' . $token . '">Réinitialiser le mot de passe</a>';

                // Envoi de l'email
                $mail->send();
                
                // Réponse JSON indiquant le succès
                echo json_encode(array("success" => true));

            } catch (Exception $e) {
                // Réponse JSON en cas d'erreur
                echo json_encode(array("success" => false, "error" => $mail->ErrorInfo));
            }
        } catch(PDOException $e) {
            error_log("Erreur de connexion : " . $e->getMessage());
            echo json_encode(["error" => "Erreur lors de la connexion à la base de données."]);
        }
    } else {
        // Réponse JSON si l'email n'est pas présent dans la requête
        echo json_encode(array("success" => false, "error" => "L'email n'est pas défini."));
    }
} else {
    // Réponse JSON si la méthode de requête n'est pas POST
    echo json_encode(array("success" => false, "error" => "Méthode de requête non autorisée."));
}
?>
