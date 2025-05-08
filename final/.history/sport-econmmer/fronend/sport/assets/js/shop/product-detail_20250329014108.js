$(document).ready(function () {
    function getUrlParameter(name) {
        var url = new URL(window.location.href);
        var params = new URLSearchParams(url.search);
        return params.get(name);
    }
    var productId  = getUrlParameter('id');
    console.log(productId); 
    
    $.ajax({
        url: `http://localhost:8080/product/${productId}`, 
        type: "GET",  // Sử dụng phương thức GET
        success: function(response) {


            if (response.code === "200") {
                renderProductImages(product);
            } else {
                console.log('Không tìm thấy sản phẩm');
            }
        },
        error: function(xhr, status, error) {
            console.error("Đã có lỗi xảy ra: " + error);
        }
    });

});

function renderProductImages(product) {
    // Render ảnh chính (first image)
    var mainImage = product.productImages.find(image => image.isPrimary === 1); // Lấy ảnh chính
    if (mainImage) {
        $('#main-image').attr('src', mainImage.imageUrl);
        $('#main-image').attr('data-zoom-image', mainImage.imageUrl);
    }

    // Render ảnh phụ (sidebar images)
    var sidebarImagesHtml = '';
    product.productImages.forEach(function(image) {
        if (image.isPrimary === 0) {
            sidebarImagesHtml += `
                <div>
                    <div class="sidebar-image">
                        <img src="${image.imageUrl}" class="img-fluid blur-up lazyload" alt="">
                    </div>
                </div>
            `;
        }
    });
    $('#sidebar-images').html(sidebarImagesHtml); // Thêm ảnh phụ vào phần tử
}
