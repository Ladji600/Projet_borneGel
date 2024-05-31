<?php
$servername = "51.210.151.13";
$username = "bornegel";
$password_db = "Bornegel2024!";
$dbname = "borne_gel_2024";

// Vérifier si l'ID de l'établissement est passé dans l'URL
if(isset($_GET['id_etablissement'])) {
    $id_etablissement = $_GET['id_etablissement'];

    try {
        $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
        $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        
       // Partie 1: Sélectionner les bornes non affectées avec un niveau faible de batterie ou de gel
       $sql1 = "SELECT b.IdBorne, 
       CAST(b.Niveau_Gel AS UNSIGNED) AS Niveau_Gel, 
       CAST(b.Niveau_Batterie AS UNSIGNED) AS Niveau_Batterie, 
       b.Salle, 
       b.Heure, 
       b.Date, 
       b.Id_Etablissement
FROM borne_gel_2024.borne AS b
LEFT JOIN borne_gel_2024.mission AS m 
ON b.IdBorne = m.Id_Borne 
WHERE b.Id_Etablissement = :id_etablissement 
AND m.Id_Borne IS NULL
AND (CAST(b.Niveau_Gel AS UNSIGNED) <= 10 OR CAST(b.Niveau_Batterie AS UNSIGNED) <= 10)
AND b.Date = CURDATE()";

// Partie 2: Sélectionner les bornes déjà affectées mais avec un niveau faible actuel
$sql2 = "SELECT b.IdBorne, 
       CAST(b.Niveau_Gel AS UNSIGNED) AS Niveau_Gel, 
       CAST(b.Niveau_Batterie AS UNSIGNED) AS Niveau_Batterie, 
       b.Salle, 
       b.Heure, 
       b.Date, 
       b.Id_Etablissement
FROM borne_gel_2024.borne AS b
INNER JOIN borne_gel_2024.mission AS m 
ON b.IdBorne = m.Id_Borne 
WHERE b.Id_Etablissement = :id_etablissement 
AND (CAST(b.Niveau_Gel AS UNSIGNED) <= 10 OR CAST(b.Niveau_Batterie AS UNSIGNED) <= 10)
AND b.Date = CURDATE()";

// Union des deux parties
$sql = "($sql1) UNION ($sql2)";




        // Préparer et exécuter la requête en utilisant un paramètre nommé pour l'ID de l'établissement
        $stmt = $conn->prepare($sql);
        $stmt->bindParam(':id_etablissement', $id_etablissement);
        $stmt->execute();

        // Récupérer les résultats
        $results = $stmt->fetchAll(PDO::FETCH_ASSOC);

        // Retourner les résultats sous forme de réponse JSON
        header('Content-Type: application/json');
        echo json_encode($results);
    } catch(PDOException $e) {
        // Gérer les erreurs de connexion à la base de données
        echo "Erreur de connexion à la base de données: " . $e->getMessage();
    }
} else {
    echo "L'ID de l'établissement n'est pas spécifié dans la requête.";
}
?>
