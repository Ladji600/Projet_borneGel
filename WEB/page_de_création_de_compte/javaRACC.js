function validerFormulaire() {
    var inputs = document.getElementsByTagName('input');
    var textsArea = document.getElementsByTagName('textarea');
    var formulaireValide = true; // Variable pour indiquer si le formulaire est valide

    for (let i = 0; i < inputs.length; i++) {

        // les inputs = Name, First name et Email :
        if (!inputs[i].value) {
            inputs[i].style.borderColor = "red";
            formulaireValide = false;
        } else {
            inputs[i].style.borderColor = "green";
        }

        // Prenom :
        if (inputs[0].value.trim().length > 0) {
            inputs[0].style.borderColor = "green";
            document.getElementById('errorTextPrenom').style.display = 'none';
        } else {
            inputs[0].style.borderColor = "red";
            document.getElementById('errorTextPrenom').style.display = 'block';
        }

        // Nom :
        if (inputs[1].value.trim().length > 0) {
            inputs[1].style.borderColor = "green";
            document.getElementById('errorTextNom').style.display = 'none';
        } else {
            inputs[1].style.borderColor = "red";
            document.getElementById('errorTextNom').style.display = 'block';
        }

        // Email :
        if (inputs[2].value.includes('@')) {
            inputs[2].style.borderColor = "green";
            document.getElementById('errorTextEmail').style.display = 'none';
        } else {
            inputs[2].style.borderColor = "red";
            document.getElementById('errorTextEmail').style.display = 'block';
        }

        // le mot de passe :
        if (inputs[3].value.length < 8) {
            inputs[3].style.borderColor = "red";
            document.getElementById('errorTextMDP').style.display = 'block';
        } else {
            inputs[3].style.borderColor = "green";
            document.getElementById('errorTextMDP').style.display = 'none';
        }

        // la confirmation du mot de passe :
        if (inputs[4].value === inputs[3].value && inputs[4].value !== "") {
            inputs[4].style.borderColor = "green";
            document.getElementById('errorTextMDPverif').style.display = 'none';
        } else if (inputs[4].value === "") {
            inputs[4].style.borderColor = "red";
            document.getElementById('errorTextMDPverif').style.display = 'block';
            document.getElementById('errorTextMDPverif').innerText = "Ce champ ne peut pas être vide";
        } else {
            inputs[4].style.borderColor = "red";
            document.getElementById('errorTextMDPverif').style.display = 'block';
            document.getElementById('errorTextMDPverif').innerText = "Les mots de passe ne correspondent pas";
        }
    }

    $(document).ready(function() {

        // Fonction pour mettre en vert ou en rouge le champ en fonction de sa valeur
        function toggleFieldValidity(field) {
            var errorMessage = $(field).siblings('.error-message'); // Sélectionne le message d'erreur associé au champ
            if (field.value.trim().length > 0 && field.value !== "0") {
                field.style.borderColor = "green";
                errorMessage.hide(); // Cache le message d'erreur
            } else {
                field.style.borderColor = "red";
                errorMessage.show(); // Affiche le message d'erreur
            }
        }

        // Ajoutez un écouteur d'événement pour le champ de sélection "Fonction"
        $('select[name="Fonction"]').change(function() {
            toggleFieldValidity(this);
        });


        // Ajoutez des écouteurs d'événements sur les champs pour détecter les changements
        $('#creationCompteForm input, #creationCompteForm select').on('input change', function() {
            toggleFieldValidity(this);
        });

        // Soumission du formulaire
        $('#creationCompteForm').submit(function(event) {
            event.preventDefault(); // Empêche le comportement par défaut du formulaire
        });

        // Vérification initiale de la validité des champs
        $('#creationCompteForm input, #creationCompteForm select').each(function() {
            toggleFieldValidity(this);
        });
    });

}



