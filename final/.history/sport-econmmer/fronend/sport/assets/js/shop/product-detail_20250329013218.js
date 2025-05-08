$(document).ready(function () {

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
                // Hiển thị thông tin sản phẩm (ví dụ: tên sản phẩm, giá cả, mô tả)
                var product = response.data;
                var productHtml = `
                    <h1>${product.productName}</h1>
                    <p>${product.productDescription}</p>
                    <h3>${product.productPrice.toLocaleString()}đ</h3>
                    <img src="${product.productImages[0].imageUrl}" alt="${product.productName}" class="img-fluid">
                `;

                // Thêm thông tin sản phẩm vào trong phần tử với id 'product-details'
                $('#product-details').html(productHtml);
            } else {
                console.log('Không tìm thấy sản phẩm');
            }
        },
        error: function(xhr, status, error) {
            console.error("Đã có lỗi xảy ra: " + error);
        }
    });

});
