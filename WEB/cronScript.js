const { exec } = require('child_process');
const fs = require('fs');
const path = require('path');

// Chemin vers le script PHP
const phpScriptPath = '/home/bornegel2024/SmartGel/API/AlerteBorne.php';
const logFilePath = '/home/bornegel2024/SmartGel/API/cron.log';

// Fonction pour exécuter le script PHP
function runScript() {
    console.log('Running script at ' + new Date().toString());
    
    exec('php ${phpScriptPath}', (error, stdout, stderr) => {
        const timestamp = new Date().toISOString();
        if (error) {
            fs.appendFileSync(logFilePath, '${timestamp} - Error: ${error.message}\n');
            return;
        }
        if (stderr) {
            fs.appendFileSync(logFilePath, '${timestamp} - Stderr: ${stderr}\n');
            return;
        }
        fs.appendFileSync(logFilePath, '${timestamp} - Stdout: ${stdout}\n');
    });
}

// Planifier l'exécution toutes les 5 minutes (300000 millisecondes)
setInterval(runScript, 300000); 
// Planifier l'exécution toutes les 1 minutes (60000 millisecondes)
//setInterval(runScript, 60000);

// Exécuter immédiatement la première fois
runScript();

console.log('Script scheduled to run every 5 minutes');