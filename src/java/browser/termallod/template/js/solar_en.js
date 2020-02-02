var countries = ["addington,_d.m.",
"alsema,_e._alsema,_e.",
"amorf_silicium",
"amorfe_halfgeleider",
"amorphous_semiconductor",
"amorphous_silicon",
"anckaert,_k._anckaert,_k.",
"anderson,_b.l.",
"archer,_m.d.",
"bailey,_r.a.",
"belg.be",
"biederman,_h.",
"bigfrogmountain",
"boeykens,_s.",
"bosmans,_an",
"boër,_k.w.",
"bremer,_r.",
"brouwer,_f.",
"bube,_r.h.",
"bussel,_l.m._van",
"campenhout,_j._van",
"capper,_p.",
"cassidy,_d.",
"chaure,_n.b.",
"clason,_w.e.",
"climate_action",
"crop,_f.",
"csa",
"dhaeseleer,_w._.",
"defect_door_licht_geïnduceerd",
"detavernier,_christophe",
"dictionary",
"dik_kristallijn_materiaal",
"dobbeleir,_m.",
"dope",
"doperen",
"dorpe,_p._van",
"doteren",
"dunne_film",
"ecn",
"ecn2007",
"ecolis",
"electron_vacancy",
"euroinvester",
"fom",
"gat",
"geebelen,_k.",
"genoe,_j.",
"gerven,_p._van",
"gibilisco,_s.",
"girard,_j.e.",
"glover,_i.a.",
"golio,_m.",
"google",
"google_books",
"google_scholar",
"green,_m.a.",
"hambley,_a.r.",
"hawkes,_p.w.",
"hnatek,_e.r.",
"hole",
"holterman,_j.",
"holzhauer,_r.w.",
"houben,_m.",
"houghton,_j.t.",
"houweling,_z.s.",
"hyder,_a.k.",
"iate",
"interconnect",
"johansson,_t.b.",
"john,_v.",
"johnson,_g.",
"jvc_jvc",
"kaltschmitt,_w.",
"kamp,_j.",
"kane,_t.s.",
"kanicki,_j.",
"kapon,_e.",
"keizer,_c._de",
"kelly,_m.j.",
"kennislink",
"kimman,_j.",
"kooijman,_c.s.",
"kreith,_f.",
"kruijssen,_f.j.c._van_der",
"kubbinga,_h._kubbinga,_h.",
"lacres,_p.",
"ldce",
"levinshtein,_m.e.",
"light-induced_defect",
"louagie,_filip",
"lowet,_t.",
"luque,_a.",
"löve,_e.h._.",
"maccomb,_g.",
"maesen,_m.",
"mannaert,_h.",
"marent,_katrien",
"markus,_j.",
"markvart,_t.",
"marshall,_j.m.",
"marton,_c.",
"marton,_l.",
"massobrio,_g.",
"mckeown,_n.b.",
"menkveld,_m.",
"mierlo,_b.c._van",
"minges,_m.l.",
"mobilyz",
"mols,_y.",
"motsnyi,_v.",
"müller,_g.",
"n-dopering",
"n-doping",
"n-dotering",
"n-halfgeleider",
"n-type_doping",
"n-type_halfgeleider",
"n-type_semiconductor",
"nalwa,_h.s.",
"ngô,_c.",
"nndb",
"orton,_j.w.",
"p-dopering",
"p-doping",
"p-dotering",
"p-n_junction",
"p-type_doping",
"p-type_halfgeleider",
"p-type_semiconductor",
"pawley,_j.b",
"physics",
"plak",
"pn-junctie",
"pn-overgang",
"pollefliet,_j.",
"poly-elektronica",
"raymond,_c.f.",
"redfield,_d.",
"renkema,_j.",
"rickerby,_d.g.",
"rittal",
"roger,_a.m.",
"ryckevelde",
"sato,_n.",
"schakelingen",
"schijf",
"schottky_barrier",
"schottky_junction",
"schottkybarrière",
"schrijber,_r.a.",
"schuurmans_stekhoven,_g.",
"scribd",
"seeger,_k.",
"sei",
"sellmyer,_d.j.",
"sharma,_s.",
"sharp",
"shepherd,_d.w.",
"siaw,_t.l.",
"singh,_jai",
"sinke,_w.c.1",
"sinke,_w.c.2",
"siricomindia",
"slice",
"soete,_w.",
"sonnenenergie",
"spectrum",
"spiro,_t.g.",
"staal,_j.f.",
"sterrett,_f.s.",
"stesmans,_andre",
"street,_r.a.",
"sun,_s.s.",
"sun-nrg",
"taaltelefoon",
"ten_bosch?",
"tentamen",
"terminology",
"thick_crystalline_material",
"thin_film",
"thuisexperimenteren",
"vallins,_g.h.",
"van_dale",
"van_dale_e-n",
"verbong,_g.",
"verhoeve,_c.w.g.",
"wafer",
"wafer",
"websters",
"weitering,_h.h.",
"werkenindeindustrie",
"whitaker,_j.c.",
"white,_r.e.",
"wikipedia_en",
"wikipedia_nl",
"willardson,_r.k.1",
"willardson,_r.k.2",
"willemen,_j.a._willemen,_j.a.",
"wissenburgh,_c.",
"witte,_h._de",
"woordenlijst",
"yacobi,_b.g._yacobi,_b.g.",
"young,_e.c.",
"zonnepanelen_gd",
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
