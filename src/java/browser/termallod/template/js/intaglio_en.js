var countries = ["afdekmethode",
"afdekvernis",
"afslaan",
"agent",
"american_roulette",
"applying_resin_by_hand",
"aquatint",
"aquatint",
"aquatint_box",
"armoury",
"art_of_etching",
"asfaltvernis",
"asphalt_varnish",
"bevel",
"bite",
"biting",
"bourgondische_pek",
"bruineerstaal",
"brush_etching",
"burgundy_pitch",
"burijn",
"burin",
"burnisher",
"chalk_lithograph",
"chalk_manner",
"chemical_manual_intaglio_printmaking",
"chemisch_manuele_diepdrukvorm",
"covered_biting_method",
"craquelé",
"craquelé_effect",
"crayon_etching",
"crayon_manner",
"crayonets",
"crayonmanier",
"creeping_bite",
"creeping_bite",
"crevé",
"crevé",
"dabber",
"damarhars",
"damascene",
"damasceren",
"dammar_resin",
"deep_etch",
"diepdrukgrafiek",
"diepets",
"dragging",
"droge_naald",
"drukvorm",
"drypoint",
"dust_box",
"dust_ground",
"dutch_bath",
"dutch_bath",
"dutch_mordant",
"dutch_mordant",
"edition",
"engraved_line",
"enkelvoudige_bijting",
"etching",
"etching_bath",
"etching_tray",
"etsbad",
"etsbak",
"etsdruk",
"etsgaatje",
"etsing",
"etskunst",
"etslijn",
"etsmiddel",
"etsoven",
"etswater",
"facetteren",
"free-line_etching",
"gietgrein",
"graveerijzer",
"graver",
"gravurelijn",
"groenspaan",
"haden’s_method",
"hand_wipe",
"hard_etching_ground",
"heliogravure",
"heliogravure",
"hole",
"hotplate",
"hydrofiel_gaas",
"hydrophilic_gauze",
"impression",
"ininkten",
"ink",
"intaglio_printmaking",
"kaliumchloraat",
"korrelaquatint",
"krijtlitho",
"lavis",
"lift-ground",
"lift-ground_etching",
"lift-ground_process",
"lift_ground",
"liftground",
"lijndifferentiatie",
"lijnets",
"lijnvoering",
"line",
"line_etching",
"liquid_ground",
"lithografisch_krijt",
"lithographic_chalk",
"luitbouwer",
"lutemaker",
"maagdenwas",
"mace-head",
"manual_intaglio_printmaking",
"manual_mechanical_intaglio_printmaking",
"manuele_diepdruk",
"mattoir",
"mattoir",
"mechanisch_manuele_diepdruk",
"mengmethode",
"methode_van_haden",
"mezzotint",
"mixed_method",
"monotype",
"monotypie",
"mordant",
"moulette",
"open_etching",
"open_tekening",
"oplage",
"opsnijden",
"opstrijken",
"overflow",
"overlaat",
"pastel_manner",
"pastelmanier",
"penseelets",
"plaattoon",
"plate",
"polijstpoeder",
"polijststaal",
"polishing_powder",
"ponsgravure",
"potassium_chlorate",
"proefdruk",
"proof",
"pumping",
"punch_engraving",
"punched_print",
"punteermanier",
"rebiting_ground",
"recarve",
"remarque",
"remarque",
"repoussage",
"repoussage",
"reservage",
"reservagetechniek",
"resin_dust",
"retroussage",
"retroussage",
"roeten",
"roulette-ets",
"roulette_mordue",
"salt_grain",
"sausen",
"schraapstaal",
"scraper",
"single_biting",
"smoking",
"soft-ground_etching",
"soft_etching_ground",
"spaans_groen",
"spijkolie",
"spike_oil",
"spirit_ground",
"spit_bite",
"spit_bite",
"staat",
"state",
"stippelgravure",
"stipple_engraving",
"stopgrond",
"stopping-out_varnish",
"strooigrein",
"stuifgrein",
"stuifkast",
"sugar-lift_aquatint",
"sugar-lift_soft_ground_etching",
"sugar_aquatint",
"suiker_vernis_mou",
"suikeraquatint",
"surface_tone",
"tampon",
"tea_lead",
"theelood",
"tonal_etching",
"tonal_process",
"tone",
"tone_variation",
"toonets",
"toonprocédé",
"toonverloop",
"toonvlak",
"uitbijten",
"variety_of_line",
"verdigris",
"vernis_dur",
"vernis_dur",
"vernis_mol",
"vernis_mol",
"vernis_mou",
"vernis_à_remordre",
"virgin_wax",
"wapensmederij",
"water_tone",
"watertoon",
"zoutgrein",
"zwarte_kunst",
"z"];
window.termUrls = new Map();
termUrls.set("c", "browser_en_C_D_1.html");
termUrls.set("e", "browser_en_E_F_1.html");
termUrls.set("g", "browser_en_G_H_1.html");


let arr = Array.from(termUrls.keys());
window.valueOfTextField = "";
window.text = "";
document.getElementById("myInput").style.borderColor = "blue";
 
autocomplete(document.getElementById("myInput"), arr);


function autocomplete(inp, arr) {
    /*the autocomplete function takes two arguments,
     the text field element and an array of possible autocompleted values:*/
    var currentFocus;
    /*execute a function when someone writes in the text field:*/
    inp.addEventListener("input", function (e) {
        var a, b, i, val = this.value;
        /*close any already open lists of autocompleted values*/
        closeAllLists();
        if (!val) {
            return false;
        }
        currentFocus = -1;
        /*create a DIV element that will contain the items (values):*/
        a = document.createElement("DIV");
        a.setAttribute("id", this.id + "autocomplete-list");
        a.setAttribute("class", "autocomplete-items");
        /*append the DIV element as a child of the autocomplete container:*/
        this.parentNode.appendChild(a);
        /*for each item in the array...*/
        for (i = 0; i < arr.length; i++) {
            window.text = arr[i];
            /*check if the item starts with the same letters as the text field value:*/
            if (arr[i].substr(0, val.length).toUpperCase() == val.toUpperCase()) {
                /*create a DIV element for each matching element:*/
                b = document.createElement("DIV");
                /*make the matching letters bold:*/
                b.innerHTML = "<strong>" + arr[i].substr(0, val.length) + "</strong>";
                b.innerHTML += arr[i].substr(val.length);
                /*insert a input field that will hold the current array item's value:*/
                b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";
                /*execute a function when someone clicks on the item value (DIV element):*/
                b.addEventListener("click", function (e) {
                    /*insert the value for the autocomplete text field:*/
                    inp.value = this.getElementsByTagName("input")[0].value;
                    window.text = this.getElementsByTagName("input")[0].value;

                    /*close the list of autocompleted values,
                     (or any other open lists of autocompleted values:*/
                    closeAllLists();
                });
                a.appendChild(b);
            }
        }
    });
    /*execute a function presses a key on the keyboard:*/
    inp.addEventListener("keydown", function (e) {
        var x = document.getElementById(this.id + "autocomplete-list");
        if (x)
            x = x.getElementsByTagName("div");
        if (e.keyCode == 40) {
            /*If the arrow DOWN key is pressed,
             increase the currentFocus variable:*/
            currentFocus++;
            /*and and make the current item more visible:*/
            addActive(x);
        } else if (e.keyCode == 38) { //up
            /*If the arrow UP key is pressed,
             decrease the currentFocus variable:*/
            currentFocus--;
            /*and and make the current item more visible:*/
            addActive(x);
        } else if (e.keyCode == 13) {
            /*If the ENTER key is pressed, prevent the form from being submitted,*/
            e.preventDefault();
            if (currentFocus > -1) {
                /*and simulate a click on the "active" item:*/
                if (x)
                    x[currentFocus].click();
            }
        }
    });
    function addActive(x) {
        /*a function to classify an item as "active":*/
        if (!x)
            return false;
        /*start by removing the "active" class on all items:*/
        removeActive(x);
        if (currentFocus >= x.length)
            currentFocus = 0;
        if (currentFocus < 0)
            currentFocus = (x.length - 1);
        /*add class "autocomplete-active":*/
        x[currentFocus].classList.add("autocomplete-active");
    }
    function removeActive(x) {
        /*a function to remove the "active" class from all autocomplete items:*/
        for (var i = 0; i < x.length; i++) {
            x[i].classList.remove("autocomplete-active");
        }
    }
    function closeAllLists(elmnt) {
        /*close all autocomplete lists in the document,
         except the one passed as an argument:*/
        var x = document.getElementsByClassName("autocomplete-items");
        for (var i = 0; i < x.length; i++) {
            if (elmnt != x[i] && elmnt != inp) {
                x[i].parentNode.removeChild(x[i]);
            }
        }
    }
    document.addEventListener("click", function (e) {
        //document.getElementById("myInput").value=window.valueOfTextField;
        //document.getElementById('form_id').action = window.location.href = window.valueOfTextField; //Will set it
        closeAllLists(e.target);
    });

    document.getElementById("form_id").addEventListener("submit", myFunction);
    function myFunction() {
        window.valueOfTextField = window.termUrls.get(window.text);
        if(document.getElementById("myInput").value == ""){
           alert("search box is empty")
          }
        else{
        document.getElementById('form_id').action = window.location.href = window.valueOfTextField; //Will set it
        }
     }
    
    
}
