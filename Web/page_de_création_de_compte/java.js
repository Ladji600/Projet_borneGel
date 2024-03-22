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

        // select fonction :
        if (select[0].value === "role0") {
            select[0].style.borderColor = "red";
            document.getElementById('errorSelectRole').style.display = 'block';
        } else {
            select[0].style.borderColor = "green";
            document.getElementById('errorSelectRole').style.display = 'none';
        }

        // select etablissement :
        if (select[1].value === "site0") {
            select[1].style.borderColor = "red";
            document.getElementById('errorSelectEtabli').style.display = 'block';
        } else {
            select[1].style.borderColor = "green";
            document.getElementById('errorSelectEtabli').style.display = 'none';
        }
    }

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

    return formulaireValide;
}

function verifierFormulaire() {
    var formulaireValide = validerFormulaire();

    if (formulaireValide) {
        // Soumettre le formulaire
        document.querySelector('form').submit();
    }

    return formulaireValide;
}
