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


}
