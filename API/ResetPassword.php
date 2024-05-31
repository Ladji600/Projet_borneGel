<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $token = $_POST['token'];
    $newPassword = $_POST['newPassword']; // Hash du nouveau mot de passe

    // Connexion à la base de données
    $servername = "51.210.151.13";
    $username = "bornegel";
    $password_db = "Bornegel2024!";
    $dbname = "borne_gel_2024";

    try {
        $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
        $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

        $stmt = $conn->prepare("SELECT email FROM password_resets WHERE token = :token AND expiry > NOW()");
        $stmt->bindParam(':token', $token);
        $stmt->execute();

        $resetRequest = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($resetRequest) {
            $email = $resetRequest['email'];

            $stmt = $conn->prepare("UPDATE employes SET Mot_De_Passe = :newPassword WHERE Mail = :email");
            $stmt->bindParam(':newPassword', $newPassword);
            $stmt->bindParam(':email', $email);
            $stmt->execute();

            $stmt = $conn->prepare("DELETE FROM password_resets WHERE email = :email");
            $stmt->bindParam(':email', $email);
            $stmt->execute();

            echo json_encode(["success" => "Mot de passe réinitialisé avec succès."]);
        } else {
            echo json_encode(["error" => "Token invalide ou expiré."]);
        }
    } catch(PDOException $e) {
        error_log("Erreur de connexion : " . $e->getMessage());
        echo json_encode(["error" => "Erreur lors de la connexion à la base de données."]);
    }
}
?>
