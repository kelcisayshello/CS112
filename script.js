var closebtns = document.getElementsByClassName("close");
var i;

for (i = 0; i < closebtns.length; i++) {
    closebtns[i].addEventListener("click", function() {
        this.style.display = 'none';
    });
}

function displayContent(event, problemset) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("content");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("menulinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(problemset).style.display = "block";
    event.currentTarget.className += " active";
}

$(document).ready(function(){
    $('#ps-bigo').load("problemsets/bigo.html");
    $('#ps2').load("problemsets/bigo.html");
    $('#ps3').load("problemsets/bigo.html");
});