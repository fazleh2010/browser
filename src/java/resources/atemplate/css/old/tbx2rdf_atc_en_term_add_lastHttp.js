
function getURL(termPageUrl) {
    termPageUrl = termPageUrl.replace(".html", "");
    termPageUrl = termPageUrl.concat("_add.html");
    //alert("The termPageUrl of this page is: " + termPageUrl);

    var array = termPageUrl.split('/');
    termPageUrl = array[array.length - 1];
    
    document.getElementById('form_id').action = window.location.href = termPageUrl;

    //document.getElementById('form_id').action = window.location.href = termPageUrl;

    //var termPageUrl = 0;
    //document.getElementById('form_id').action = window.location.href = "tbx2rdf_atc_en_term_add.html"; 
}



