const borneCardsContainer = document.getElementById('borne-cards');

// Fonction pour afficher les messages d'erreur
function displayError(message) {
    const errorElement = document.createElement('div');
    errorElement.classList.add('alert', 'alert-danger');
    errorElement.textContent = message;
    borneCardsContainer.appendChild(errorElement);
}

// Fonction pour formater la date
function formatDate(date) {
    const options = {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric',
        second: 'numeric',
    };
    return date.toLocaleDateString('fr-FR', options);
}

// Fonction pour créer une carte de borne
function createBorneCard(borne) {
    const card = document.createElement('div');
    card.classList.add('borne-card');

    // En-tête de la carte avec l'ID et la date de dernière mise à jour
    const header = document.createElement('h3');
    header.textContent = `Borne ${borne.id}`;
    const updateText = document.createElement('span');
    updateText.classList.add('text-muted');
    updateText.textContent = ` (Mise à jour le ${formatDate(new Date(borne.updatedAt))})`;
    header.appendChild(updateText);
    card.appendChild(header);

    // Contenu de la carte avec les informations de la borne
    const content = document.createElement('div');
    const batteryLevelElement = document.createElement('p');
    batteryLevelElement.textContent = `Niveau de batterie: ${borne.batteryLevel}%`;
    content.appendChild(batteryLevelElement);

    const gelLevelElement = document.createElement('p');
    gelLevelElement.textContent = `Niveau de gel: ${borne.gelLevel}%`;
    content.appendChild(gelLevelElement);

    // Ajout d'une jauge pour le niveau de batterie
    const batteryGauge = document.createElement('div');
    batteryGauge.classList.add('progress');
    const batteryBar = document.createElement('div');
    batteryBar.classList.add('progress-bar');
    batteryBar.setAttribute('aria-valuenow', borne.batteryLevel);
    batteryBar.setAttribute('aria-valuemin', '0');
    batteryBar.setAttribute('aria-valuemax', '100');
    batteryBar.style.width = `${borne.batteryLevel}%`;
    batteryGauge.appendChild(batteryBar);
    content.appendChild(batteryGauge);

    // Ajout d'une jauge pour le niveau de gel
    const gelGauge = document.createElement('div');
    gelGauge.classList.add('progress');
    const gelBar = document.createElement('div');
    gelBar.classList.add('progress-bar');
    gelBar.setAttribute('aria-valuenow', borne.gelLevel);
    gelBar.setAttribute('aria-valuemin', '0');
    gelBar.setAttribute('aria-valuemax', '100');
    gelBar.style.width = `${borne.gelLevel}%`;
    gelGauge.appendChild(gelBar);
    content.appendChild(gelGauge);

    card.appendChild(content);

    return card;
}

// Fonction pour récupérer les données des bornes
function fetchBorneData() {
    // Remplacez l'URL par l'endpoint de votre API
    fetch('https://your-api-endpoint')
        .then(response => response.json())
        .then(data => {
            borneCardsContainer.innerHTML = ''; // Effacer les cartes existantes
            if (data.length === 0) {
                displayError('Aucune borne trouvée.');
                return;
            }
            data.forEach(borne => {
                const card = createBorneCard(borne);
                borneCardsContainer.appendChild(card);
            });
        })
        .catch(error => {
            console.error('Error fetching data:', error);
            displayError('Une erreur est survenue lors de la récupération des données.');
        });
}

// Appel initial de la fonction pour afficher les données
fetchBorneData();

// Actualisation des données toutes les 5 secondes
setInterval(fetchBorneData, 5000);
