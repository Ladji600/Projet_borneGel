<?php
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
                // Rediriger vers VisualisationMission.html si le rôle est 1
                header("Location: VisualisationMission.html");
                exit();
            } else if ($role_id == 2 || $role_id == 3) {
                // Rediriger vers index.html si le rôle est 2 ou 3
                header("Location: index.html");
                exit();
            } else {
                // Rôle non reconnu, affichage d'un message d'erreur
                echo '<script>alert("Rôle non reconnu pour cet utilisateur.");</script>';
            }
        } else {
            // Utilisateur non trouvé, afficher un message d'erreur et rester sur la page de connexion
            echo '<script>alert("Adresse email ou mot de passe incorrect, veuillez réessayer."); window.location.href = "connexion.html";</script>';
            exit();
        }
    } catch(PDOException $e) {
        // Gestion des erreurs de connexion à la base de données
        echo "Erreur de connexion : " . $e->getMessage();
    }
}
?>