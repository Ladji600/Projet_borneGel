function verifierFormulaire() {
        var select = document.getElementsByTagName('select');
        var inputs = document.getElementsByTagName('input');
        var textsArea = document.getElementsByTagName('textarea');
        var mdp = document.getElementsByClassName('text-danger invisible');
        var textArea = textsArea[0];

        for (let i = 0; i < select.length; i++) {
            // les selects :
            if (!select[i].value) {
                select[i].style.borderColor = "green";
            } else {
                select[i].style.borderColor = "red";
            }

            // select employer :
            if (select[0].value === "agent0" ) {
                select[0].style.borderColor = "red";
                document.getElementById('errorSelectAgent').style.display = 'block';
            }else{
                select[0].style.borderColor = "green";
                document.getElementById('errorSelectAgent').style.display = 'none';
            }

            // select borne :
            if (select[1].value === "borne0" ) {
                select[1].style.borderColor = "red";
                document.getElementById('errorSelectBorne').style.display = 'block';
            }else{
                select[1].style.borderColor = "green";
                document.getElementById('errorSelectBorne').style.display = 'none';
            }

            // select motif :
            if (select[2].value === "motif0" ) {
                select[2].style.borderColor = "red";
                document.getElementById('errorSelectMotif').style.display = 'block';
            }else{
                select[2].style.borderColor = "green";
                document.getElementById('errorSelectMotif').style.display = 'none';
            }
        }
    $(document).ready(function() {

        // Fonction pour mettre en vert ou en rouge le champ en fonction de sa valeur
        function toggleFieldValidity(field) {
            var errorMessage = $(field).siblings('.error-message'); // Sélectionne le message d'erreur associé au champ
            if (field.value !== "0") {
                field.style.borderColor = "green";
                errorMessage.hide(); // Cache le message d'erreur
            } else {
                field.style.borderColor = "red";
                errorMessage.show(); // Affiche le message d'erreur
            }
        }

        // Ajoutez un écouteur d'événement pour le champ de sélection "Fonction"
        $('select[name="motif"]').change(function() {
            toggleFieldValidity(this);
        });

        // Ajoutez un écouteur d'événement pour le champ de sélection "Fonction"
        $('select[name="employer"]').change(function() {
            toggleFieldValidity(this);
        });

        // Ajoutez un écouteur d'événement pour le champ de sélection "Fonction"
        $('select[name="borne"]').change(function() {
            toggleFieldValidity(this);
        });


        // Ajoutez des écouteurs d'événements sur les champs pour détecter les changements
        $('#affectationForm input, #affectationForm select').on('input change', function() {
            toggleFieldValidity(this);
        });


        // Vérification initiale de la validité des champs
        $('#affectationForm input, #affectationForm select').each(function() {
            toggleFieldValidity(this);
        });
    });
}
