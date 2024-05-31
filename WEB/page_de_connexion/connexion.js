// Fonction pour mettre en vert ou en rouge le champ en fonction de sa valeur
function toggleFieldValidity(field) {
    var errorMessage = $(field).siblings('.error-message');
    
    // Vérifie si le champ a été modifié ou touché par l'utilisateur
    if (field.value.trim().length > 0 || field.hasAttribute('data-touched')) {
        // Champ non vide ou déjà touché par l'utilisateur
        if (field.value.trim().length > 0 && field.value !== "0") {
            // Champ valide
            field.style.borderColor = "green";
            errorMessage.hide();
        } else {
            // Champ invalide (vide)
            field.style.borderColor = "red";
            errorMessage.show();
        }
    }
}

// Fonction pour vérifier le formulaire
function verifierFormulaire() {
    var inputs = document.getElementsByTagName('input');

    for (let i = 0; i < inputs.length; i++) {
        // Validation des champs
        if (!inputs[i].value) {
            inputs[i].style.borderColor = "red";
        } else {
            inputs[i].style.borderColor = "green";
        }

        // Validation de l'email
        if (inputs[0].value.includes('@')) {
            inputs[0].style.borderColor = "green";
            document.getElementById('errorTextEmail').style.display = 'none';
        } else {
            inputs[0].style.borderColor = "red";
            document.getElementById('errorTextEmail').style.display = 'block';
        }

        // Validation du mot de passe
        if (inputs[1].value.length < 8) {
            inputs[1].style.borderColor = "red";
            document.getElementById('errorTextMDP').style.display = 'block';
        } else {
            inputs[1].style.borderColor = "green";
            document.getElementById('errorTextMDP').style.display = 'none';
        }
    }
}
    // Ajoutez des écouteurs d'événements sur les champs pour détecter les changements
    $('#connexionForm input').on('input', function () {
        toggleFieldValidity(this);

        // Marque le champ comme "touché" pour indiquer qu'il a été modifié par l'utilisateur
        $(this).attr('data-touched', true);
    });

    // Soumission du formulaire
    $('#connexionForm').submit(function(event) {
        event.preventDefault(); // Empêche le comportement par défaut du formulaire

        // Envoi des données via AJAX
        $.ajax({
            url: $(this).attr('action'), // Récupère l'URL d'action du formulaire
            type: $(this).attr('method'), // Récupère la méthode du formulaire (POST dans ce cas)
            data: $(this).serialize(), // Sérialise les données du formulaire
            success: function(response) {
                // Traitez la réponse si nécessaire
                //alert(response); // Affichez la réponse dans une alerte pour le débogage
            },
            error: function(jqXHR, textStatus, errorThrown) {
                // Gérez les erreurs
                console.log("Erreur AJAX : " + textStatus, errorThrown);
            }
        });

        // Après soumission, exécute la fonction de vérification du formulaire
        verifierFormulaire();
    });