<?php
// Connexion à la base de données
$servername = "51.210.151.13";
$username = "bornegel";
$password_db = "Bornegel2024!";
$dbname = "borne_gel_2024";

// Fonction pour envoyer des notifications par e-mail
function envoyerNotification($donnees) {
    $to_email = $donnees['Mail'];
    $subject = "Notification - Niveau de batterie ou de gel bas";
    $message = "Le niveau de gel est : {$donnees['Niveau_Gel']} % et le niveau de batterie est : {$donnees['Niveau_Batterie']} %. Il faudrait changer la batterie ou recharger.";
    $headers = "From:ladjiibrahimdiarra@gmail.com"; // Remplacez par votre adresse e-mail

    if (mail($to_email, $subject, $message, $headers)) {
        return "Notification envoyée à $to_email avec succès.";
    } else {
        return "Échec de l'envoi de la notification à $to_email.";
    }
}

// Vérifier si des données ont été envoyées en POST (ou traitement d'une requête d'API)
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Récupérer les données envoyées par l'API (simuler avec $_POST pour les tests)
    $niveauGel = $_POST['Niveau_Gel']; // Niveau de gel
    $niveauBatterie = $_POST['Niveau_Batterie']; // Niveau de batterie

    try {
        // Connexion à la base de données
        $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
        $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

        // Requête SQL pour sélectionner les bornes avec un niveau de gel ou de batterie inférieur ou égal à 15
        $stmt = $conn->prepare("SELECT * FROM borne WHERE Niveau_Gel <= :niveauGel OR Niveau_Batterie <= :niveauBatterie");
        $stmt->bindParam(':niveauGel', $niveauGel, PDO::PARAM_INT);
        $stmt->bindParam(':niveauBatterie', $niveauBatterie, PDO::PARAM_INT);
        $stmt->execute();

        // Récupérer les résultats
        $bornes = $stmt->fetchAll(PDO::FETCH_ASSOC);

        // Envoyer des notifications aux employés concernés (rôle 3)
        foreach ($bornes as $borne) {
            $stmt = $conn->prepare("SELECT Mail FROM employes WHERE Id_Role = 3");
            $stmt->execute();
            $recipients = $stmt->fetchAll(PDO::FETCH_ASSOC);

            foreach ($recipients as $recipient) {
                echo envoyerNotification($recipient); // Envoyer la notification par e-mail
            }
        }

    } catch (PDOException $e) {
        // Gestion des erreurs PDO
        echo "Erreur de connexion à la base de données : " . $e->getMessage();
    }
} else {
    // Répondre avec un message d'erreur si aucune donnée n'a été envoyée en POST
    echo "Aucune donnée envoyée en POST.";
}
?>