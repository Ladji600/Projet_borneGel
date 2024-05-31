<?php
$servername = "51.210.151.13";
$username = "bornegel";
$password_db = "Bornegel2024!";
$dbname = "borne_gel_2024";

try {
    $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password_db);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Vérifier si le déclencheur existe déjà
    $stmt_check_trigger = $conn->query("SELECT * FROM information_schema.triggers WHERE trigger_schema = '$dbname' AND trigger_name = 'add_status_mission'");
    $trigger_exists = $stmt_check_trigger->fetch(PDO::FETCH_ASSOC);

    // Si le déclencheur existe, on ne le recrée pas
    if(!$trigger_exists) {
        // Créer le déclencheur
        $sql_trigger = "
            CREATE TRIGGER add_status_mission AFTER INSERT ON mission
            FOR EACH ROW
            BEGIN
                INSERT INTO status_mission (Id_Mission, Type)
                VALUES (NEW.IdMission, 'pas fait');
            END;
        ";

        $conn->exec($sql_trigger);
    }

    // Vérifier si l'ID de l'employé est passé dans l'URL
    if(isset($_GET['id_employe'])) {
        $id_employe = $_GET['id_employe'];

        // Récupérer la date actuelle au format YYYY-MM-DD
        $date = date("Y-m-d");

        // Créer la requête SQL pour récupérer les missions de l'employé pour la date actuelle
        $sql = "SELECT mission.IdMission, mission.Type, mission.Heure, mission.Date, mission.Id_Employes, mission.Id_Borne, borne.Salle, borne.Niveau_Gel, borne.Niveau_Batterie, status_mission.IdStatus_Mission
                FROM mission 
                INNER JOIN borne ON mission.Id_Borne = borne.IdBorne 
                INNER JOIN status_mission ON mission.IdMission = status_mission.Id_Mission
                WHERE mission.Id_Employes = :id_employe 
                AND mission.Date = :date
                AND status_mission.Type != 'fait'";

        // Préparer et exécuter la requête en utilisant des paramètres nommés pour l'ID de l'employé et la date actuelle
        $stmt = $conn->prepare($sql);
        $stmt->bindParam(':id_employe', $id_employe);
        $stmt->bindParam(':date', $date);
        $stmt->execute();

        // Récupérer les résultats
        $results = $stmt->fetchAll(PDO::FETCH_ASSOC);

        // Retourner les résultats sous forme de réponse JSON
        header('Content-Type: application/json');
        echo json_encode($results);
    } else {
        echo "L'ID de l'employé n'est pas spécifié dans la requête.";
    }

} catch(PDOException $e) {
    // Gérer les erreurs de connexion à la base de données
    echo "Erreur de connexion à la base de données: " . $e->getMessage();
}
?>
