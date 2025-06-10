$(document).ready(function () {
    // Lấy giá trị id từ URL
    function getUrlParameter(name) {
        var url = new URL(window.location.href);
        var params = new URLSearchParams(url.search);
        return params.get(name);
    }
    
    // Lấy id từ URL
    var id = getUrlParameter('id');
    console.log(id); 
    console.log(typeof id); 
    var requestData = {
        searchText: "",
        fullParentId: id,
        categorySearch: parseInt(id)
    };
    
    $.ajax({
        url: "http://localhost:8080/product/search?page=0&size=10", 
        type: "POST", 
        contentType: "application/json",  
        data: JSON.stringify(requestData),  
        success: function(response) {
            if (response.data && response.data.content) {
            console.log(response.data.content); 
            renderProducts(response.data.content);
            }
        },
        error: function(xhr, status, error) {
            console.error("Đã có lỗi xảy ra: " + error);
        }
    
    
    });