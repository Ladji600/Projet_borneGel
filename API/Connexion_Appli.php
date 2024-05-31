<?php
header("Content-Type: application/json");

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Récupération des données envoyées depuis l'application
    $data = json_decode(file_get_contents("php://input"), true);

    // Récupération de l'email et du mot de passe
    $email = $data['email'];
    $password = $data['password'];

    // Connexion à la base de données
    $servername = "51.210.151.13";
    $username = "bornegel";
    $password_db = "Bornegel2024!";
    $dbname = "borne_gel_2024";

    try {
        $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
        // Configuration de PDO pour afficher les erreurs
        $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

        // Préparation de la requête SQL
        $stmt = $conn->prepare("SELECT * FROM employes WHERE Mail = :email AND Mot_De_Passe = :password");
        $stmt->bindParam(':email', $email);
        $stmt->bindParam(':password', $password);
        $stmt->execute();

        $user = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($user) {
            // Vérifier le rôle de l'utilisateur
            $role_id = $user['Id_Role'];
            if ($role_id == 1) {
                // Renvoyer une réponse JSON avec le chemin de redirection pour le rôle 1
                echo json_encode(["redirect" => "VisualisationMission.html"]);
            } else if ($role_id == 2 || $role_id == 3) {
                // Renvoyer une réponse JSON avec le chemin de redirection pour les rôles 2 ou 3
                echo json_encode(["redirect" => "index.html"]);
            } else {
                // Rôle non reconnu, renvoyer un message d'erreur
                echo json_encode(["error" => "Rôle non reconnu pour cet utilisateur."]);
            }
        } else {
            // Utilisateur non trouvé, renvoyer un message d'erreur
            echo json_encode(["error" => "Adresse email ou mot de passe incorrect, veuillez réessayer."]);
        }
    } catch(PDOException $e) {
        // Gestion des erreurs de connexion à la base de données
        echo json_encode(["error" => "Erreur de connexion : " . $e->getMessage()]);
    }
}
?>