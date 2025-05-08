$(document).ready(function () {
debugger
    function getUrlParameter(name) {
        var url = new URL(window.location.href);
        var params = new URLSearchParams(url.search);
        return params.get(name);
    }
    var productId  = getUrlParameter('id');
    console.log(id); 
    
    $.ajax({
        url: "http://localhost:8080//product/${productId}", 
        type: "GET",  // Sử dụng phương thức GET
        success: function(response) {
            // Xử lý kết quả trả về từ API
            console.log(response);

            if (response.code === "200") {
                console.log(response.code)
            } else {
                console.log('Không tìm thấy sản phẩm');
            }
        },
        error: function(xhr, status, error) {
            console.error("Đã có lỗi xảy ra: " + error);
        }
    });

});
