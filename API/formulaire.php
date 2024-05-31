<?php
header("Access-Control-Allow-Origin: *"); // Autoriser les requêtes depuis n'importe quelle origine
header("Content-Type: application/json; charset=UTF-8"); // Définir le type de contenu JSON

// Vérifier la méthode de la requête
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Récupérer les données envoyées via POST
    $prenom = $_POST['prenom'];
    $nom = $_POST['nom'];
    $email = $_POST['email'];
    $password = $_POST['password'];
    $password2 = $_POST['password2'];

    // Vérifier si les champs requis sont vides
    if (empty($prenom) || empty($nom) || empty($email) || empty($password) || empty($password2)) {
        http_response_code(400);
        echo json_encode(array("message" => "Veuillez remplir tous les champs du formulaire."));
        exit;
    }

    // Vérifier le format de l'email
    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        http_response_code(400);
        echo json_encode(array("message" => "Veuillez entrer une adresse email valide."));
        exit;
    }

    // Vérifier si les mots de passe correspondent
    if ($password !== $password2) {
        http_response_code(400);
        echo json_encode(array("message" => "Les mots de passe ne correspondent pas."));
        exit;
    }

    // Connexion à la base de données
    try {
        $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
        $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
        // Préparation de la requête SQL pour l'insertion
        $stmt = $conn->prepare("INSERT INTO employes (Prenom, Nom, Mail, Mot_De_Passe) VALUES (:prenom, :nom, :email, :password)");
        $stmt->bindParam(':prenom', $prenom);
        $stmt->bindParam(':nom', $nom);
        $stmt->bindParam(':email', $email);
        $hashed_password = password_hash($password, PASSWORD_DEFAULT); // Hachage du mot de passe
        $stmt->bindParam(':password', $hashed_password);
    
        // Exécution de la requête
        $stmt->execute();
    
        // Réponse JSON en cas de succès
        http_response_code(200);
        echo json_encode(array("message" => "Nouveau compte créé avec succès."));
    
    } catch(PDOException $e) {
        // En cas d'erreur de base de données
        http_response_code(500);
        echo json_encode(array("message" => "Erreur de base de données : " . $e->getMessage()));
    } catch(Exception $e) {
        // Autre type d'erreur
        http_response_code(500);
        echo json_encode(array("message" => "Erreur inattendue : " . $e->getMessage()));
    }

    // Fermer la connexion à la base de données
    $conn = null;

} else {
    // Méthode de requête non supportée
    http_response_code(405);
    echo json_encode(array("message" => "Méthode de requête non supportée."));
}

?>