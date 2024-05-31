function validerFormulaire() {
    var select = document.getElementsByTagName('select');
    var inputs = document.getElementsByTagName('input');
    var textsArea = document.getElementsByTagName('textarea');
    var formulaireValide = true; // Variable pour indiquer si le formulaire est valide

    for (let i = 0; i < select.length; i++) {
        // les selects :
        if (!select[i].value) {
            select[i].style.borderColor = "green";
        } else {
            select[i].style.borderColor = "red";
            formulaireValide = false;
        }

        // select borne :
        if (select[0].value === "0") {
            select[0].style.borderColor = "red";
            document.getElementById('errorSelectBorne').style.display = 'block';
        } else {
            select[0].style.borderColor = "green";
            document.getElementById('errorSelectBorne').style.display = 'none';
        }

        // select etablissement :
        if (select[1].value === "0") {
            select[1].style.borderColor = "red";
            document.getElementById('errorSelectEtabli').style.display = 'block';
        } else {
            select[1].style.borderColor = "green";
            document.getElementById('errorSelectEtabli').style.display = 'none';
        }
    }

    for (let i = 0; i < inputs.length; i++) {

        // les inputs :
        if (!inputs[i].value) {
            inputs[i].style.borderColor = "red";
            formulaireValide = false;
        } else {
            inputs[i].style.borderColor = "green";
        }

        // Salle :
        if (inputs[0].value.trim().length > 0) {
            inputs[0].style.borderColor = "green";
            document.getElementById('errorTextSalle').style.display = 'none';
        } else {
            inputs[0].style.borderColor = "red";
            document.getElementById('errorTextSalle').style.display = 'block';
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

        // Ajoutez un écouteur d'événement pour le champ de sélection "Borne"
        $('select[name="Borne"]').change(function() {
            toggleFieldValidity(this);
        });

        // Ajoutez un écouteur d'événement pour le champ de sélection "Borne"
        $('select[name="Etablissement"]').change(function() {
            toggleFieldValidity(this);
        });

        // Ajoutez un écouteur d'événement pour le champ de sélection "Borne"
        $('input[name="Salle"]').change(function() {
            toggleFieldValidity(this);
        });


        // Ajoutez des écouteurs d'événements sur les champs pour détecter les changements
        $('#ajouterDetails input, #ajouterDetails select').on('input change', function() {
            toggleFieldValidity(this);
        });

        // Soumission du formulaire
        $('#ajouterDetails').submit(function(event) {
            event.preventDefault(); // Empêche le comportement par défaut du formulaire

            // Envoi des données via AJAX
            $.ajax({
                url: $(this).attr('action'), // Récupère l'URL d'action du formulaire
                type: $(this).attr('method'), // Récupère la méthode du formulaire (POST dans ce cas)
                data: $(this).serialize(), // Sérialise les données du formulaire
                success: function(response) {
                    // Traitez la réponse si nécessaire
                    alert(response); // Affichez la réponse dans une alerte pour le débogage
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    // Gérez les erreurs
                    console.log("Erreur AJAX : " + textStatus, errorThrown);
                }
            });
        });

        // Vérification initiale de la validité des champs
        $('#ajouterDetails input, #ajouterDetails select').each(function() {
            toggleFieldValidity(this);
        });
    });

}



