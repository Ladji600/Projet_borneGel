<?php
header("Content-Type: application/json");

if ($_SERVER["REQUEST_METHOD"] == "GET") {
    // Récupération des données envoyées dans la requête GET
    $email = $_GET['email'] ?? '';
    $password = $_GET['password'] ?? '';

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
        $stmt = $conn->prepare("SELECT employes.*, etablissement.NomEtablissement, etablissement.Address
        FROM employes
        INNER JOIN etablissement ON employes.Id_Etablissement = etablissement.IdEtablissement
        WHERE employes.Mail = :email AND employes.Mot_De_Passe = :password");
;
        $stmt->bindParam(':email', $email);
        $stmt->bindParam(':password', $password);
        $stmt->execute();

        $user = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($user) {
            // Récupérer les données de l'utilisateur
            $response_data = [
                "IdEmployes" => $user['IdEmployes'],
                "Nom" => $user['Nom'],
                "Prenom" => $user['Prenom'],
                "Mail" => $user['Mail'],
                "Mot_De_Passe" => $user['Mot_De_Passe'],
                "Id_Role" => $user['Id_Role'],
                "Id_Etablissement" => $user['Id_Etablissement'],
                "NomEtablissement"=>$user['NomEtablissement'],
                "Address"=> $user['Address']
            ];

            echo json_encode($response_data);
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
