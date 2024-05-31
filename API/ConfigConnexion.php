<?php
header("Access-Control-Allow-Origin: *");

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $email = $_POST['email'];
    $password = $_POST['password'];

    // Connexion à la base de données
    $servername = "51.210.151.13";
    $username = "bornegel";
    $password_db = "Bornegel2024!";
    $dbname = "borne_gel_2024";

    try {
        $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
        $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

        $stmt = $conn->prepare("SELECT IdEmployes, Id_Role FROM employes WHERE Mail = :email AND Mot_De_Passe = :password");

        $stmt->bindParam(':email', $email);
        $stmt->bindParam(':password', $password);
        $stmt->execute();

        $user = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($user) {
            // Stocker l'IdEmployes dans un cookie
            setcookie("IdEmployes", $user['IdEmployes'], time() + (86400 * 30), "/"); // Expire dans 30 jours

            // Redirection en fonction du rôle de l'utilisateur
            $role_id = $user['Id_Role'];
            if ($role_id == 1) {
                // Rediriger vers la visualisation des missions agent.
                echo json_encode(["redirect" => "http://51.210.151.13/btssnir/projets2024/bornegel2024/bornegel2024/SmartGel/WEB/A_VisualisationMission.html"]);
                exit;

            } else if ($role_id == 3) {
                // Rediriger vers le dashboard responsable technique.
                echo json_encode(["redirect" => "http://51.210.151.13/btssnir/projets2024/bornegel2024/bornegel2024/SmartGel/WEB/RT_Dashboard.html"]);
                exit;

            } else if ($role_id == 2 ){
                 // Rediriger vers le dashboard responsable agent .
                 echo json_encode(["redirect" => "http://51.210.151.13/btssnir/projets2024/bornegel2024/bornegel2024/SmartGel/WEB/RA_Dashboard.html"]);
                 exit;

            }else {
                echo json_encode(["error" => "Rôle non reconnu pour cet utilisateur."]);
            }
        } else {
            echo json_encode(["error" => "Adresse email ou mot de passe incorrect, veuillez réessayer."]);
        }
    } catch(PDOException $e) {
        error_log("Erreur de connexion : " . $e->getMessage());
        echo json_encode(["error" => "Erreur lors de la connexion à la base de données."]);
    }
}
?>