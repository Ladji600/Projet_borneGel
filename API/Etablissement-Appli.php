<?php
$servername = "51.210.151.13";
$username = "bornegel";
$password_db = "Bornegel2024!";
$dbname = "borne_gel_2024";

// Vérifier si l'ID de l'employé est passé dans l'URL
if(isset($_GET['Id_Employes'])) {
    $id_employe_connecte = $_GET['Id_Employes'];

    try {
        $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
        $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

        // Vérifier l'existence de la colonne 'Adresse'
        $query = $conn->query("SELECT * FROM etablissement LIMIT 1");
        $column_exists = $query !== false;

        if ($column_exists) {
            // Créer la requête SQL en utilisant le paramètre de l'ID de l'employé connecté
            $sql = "SELECT IdEtablissement, NomEtablissement, Address
                    FROM etablissement 
                    JOIN employes  ON etablissement.Id_Employes = employes.IdEmployes
                    WHERE employes.IdEmployes = :id_employe";
    

            // Préparer et exécuter la requête en utilisant un paramètre nommé pour l'ID de l'employé
            $stmt = $conn->prepare($sql);
            $stmt->bindParam(':id_employe', $id_employe_connecte);
            $stmt->execute();

            // Récupérer les résultats
            $results = $stmt->fetchAll(PDO::FETCH_ASSOC);

            // Retourner les résultats sous forme de réponse JSON
            header('Content-Type: application/json');
            echo json_encode($results);
        } else {
            echo "La colonne 'Adresse' n'existe pas dans la table 'etablissement'. Veuillez vérifier votre structure de base de données.";
        }
    } catch(PDOException $e) {
        // Gérer les erreurs de connexion à la base de données
        echo "Erreur de connexion à la base de données: " . $e->getMessage();
    }
} else {
    echo "L'ID de l'employé n'est pas spécifié dans la requête.";
}
?>
