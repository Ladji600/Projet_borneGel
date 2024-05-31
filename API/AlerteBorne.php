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

// Configuration de la base de données
$servername = "51.210.151.13";
$username = "bornegel";
$password_db = "Bornegel2024!";
$dbname = "borne_gel_2024";

try {
    $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Récupérer les bornes avec un niveau de gel ou de batterie inférieur à 10
    $stmt = $conn->prepare("SELECT IdBorne, Niveau_Gel, Niveau_Batterie, Id_Etablissement FROM borne WHERE Niveau_Gel < 10 OR Niveau_Batterie < 10");
    $stmt->execute();
    $bornes = $stmt->fetchAll(PDO::FETCH_ASSOC);

    foreach ($bornes as $borne) {
        $etablissementId = $borne['Id_Etablissement'];

        // Récupérer les informations de l'établissement
        $stmt = $conn->prepare("SELECT Nom_Etablissement FROM etablissements WHERE Id_Etablissement = :etablissementId");
        $stmt->bindParam(':etablissementId', $etablissementId);
        $stmt->execute();
        $etablissement = $stmt->fetch(PDO::FETCH_ASSOC);
        $nomEtablissement = $etablissement['Nom_Etablissement'];

        // Récupérer les responsables de l'établissement
        $stmt = $conn->prepare("SELECT Nom, Prenom, Mail FROM employes WHERE Id_Etablissement = :etablissementId AND Id_Role IN (3, 2)");
        $stmt->bindParam(':etablissementId', $etablissementId);
        $stmt->execute();
        $responsables = $stmt->fetchAll(PDO::FETCH_ASSOC);

        // Envoyer un email à chaque responsable
        foreach ($responsables as $responsable) {
            $mail = new PHPMailer(true);
            try {
                $mail->isSMTP();
                $mail->Host = 'smtp.gmail.com';
                $mail->SMTPAuth = true;
                $mail->Username = 'noreply.smartgel@gmail.com';
                $mail->Password = 'pktngbjzxjvnfqkd';
                $mail->SMTPSecure = 'tls';
                $mail->Port = 587;

                $mail->setFrom('noreply.smartgel@gmail.com', 'SmartGel');
                $mail->addAddress($responsable['Mail']);

                $mail->isHTML(true);
                $mail->Subject = 'Alerte niveau bas de gel ou de batterie';
                $mail->Body = 'Bonjour ' . $responsable['Prenom'] . ' ' . $responsable['Nom'] . ',<br><br>' .
                              'La borne n°' . $borne['IdBorne'] . ' dans votre établissement (' . $nomEtablissement . ') a un niveau de gel ou de batterie inférieur à 10.<br>' .
                              'Niveau de gel: ' . $borne['Niveau_Gel'] . '<br>' .
                              'Niveau de batterie: ' . $borne['Niveau_Batterie'] . '<br><br>' .
                              'Cordialement,<br>L\'équipe SmartGel';

                $mail->send();
            } catch (Exception $e) {
                echo json_encode(['success' => false, 'error' => 'Erreur lors de l\'envoi de l\'email: ' . $mail->ErrorInfo]);
                exit;
            }
        }
    }

    echo json_encode(['success' => true, 'message' => 'Emails envoyés avec succès']);
} catch (PDOException $e) {
    echo json_encode(['success' => false, 'error' => 'Erreur de connexion à la base de données: ' . $e->getMessage()]);
}

?>
