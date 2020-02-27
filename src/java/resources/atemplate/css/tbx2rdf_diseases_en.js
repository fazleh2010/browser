window.termUrls = new Map();
termUrls.set("aneurism","http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_diseases/data/diseases/aneurism-EN");
termUrls.set("aneurysm","http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_diseases/data/diseases/aneurysm-EN");
termUrls.set("apical","http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_diseases/data/diseases/apical-EN");
termUrls.set("ataxia","http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_diseases/data/diseases/ataxia-EN");
termUrls.set("ataxy","http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_diseases/data/diseases/ataxy-EN");
termUrls.set("clubbing","http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_diseases/data/diseases/clubbing-EN");
termUrls.set("congenital","http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_diseases/data/diseases/congenital-EN");
termUrls.set("cyanosis","http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_diseases/data/diseases/cyanosis-EN");
termUrls.set("diaphoresis","http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_diseases/data/diseases/diaphoresis-EN");
termUrls.set("distension","http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_diseases/data/diseases/distension-EN");
termUrls.set("distention","http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_diseases/data/diseases/distention-EN");
termUrls.set("dyspnea","http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_diseases/data/diseases/dyspnea-EN");
termUrls.set("dyspnoea","http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_diseases/data/diseases/dyspnoea-EN");
termUrls.set("edema","browser_en_E_F_1_term_0.html");
termUrls.set("erythema","browser_en_E_F_1_term_1.html");
termUrls.set("failure to thrive","browser_en_E_F_1_term_2.html");
termUrls.set("fibroma","browser_en_E_F_1_term_3.html");
termUrls.set("hepatomegaly","browser_en_G_H_1_term_0.html");
termUrls.set("macular","http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_diseases/data/diseases/macular-EN");
termUrls.set("mucopurulent","http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_diseases/data/diseases/mucopurulent-EN");
termUrls.set("murmur","http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_diseases/data/diseases/murmur-EN");
termUrls.set("nodule","http://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_diseases/data/diseases/nodule-EN");
termUrls.set("oedema","browser_en_O_P_1_term_0.html");
termUrls.set("onset","browser_en_O_P_1_term_1.html");
termUrls.set("proptosis","browser_en_O_P_1_term_2.html");
termUrls.set("scoliosis","browser_en_S_T_1_term_0.html");
termUrls.set("systolic","browser_en_S_T_1_term_1.html");
termUrls.set("tachycardia","browser_en_S_T_1_term_2.html");
termUrls.set("tachypnea","browser_en_S_T_1_term_3.html");
termUrls.set("tachypnoea","browser_en_S_T_1_term_4.html");
termUrls.set("thyroid","browser_en_S_T_1_term_5.html");
termUrls.set("thyroid gland","browser_en_S_T_1_term_6.html");

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
