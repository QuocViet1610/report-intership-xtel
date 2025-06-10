$(document).ready(function () {

    function getUrlParameter(name) {
        var url = new URL(window.location.href);
        var params = new URLSearchParams(url.search);
        return params.get(name);
    }
    var id = getUrlParameter('id');
    console.log(id); 
    
});
