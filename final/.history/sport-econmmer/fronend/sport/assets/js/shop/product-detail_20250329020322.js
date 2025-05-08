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
                var product = response.data;
                console.log(product)
                renderProductImages(product);
                $('#nameProduct').text(product.productName); // Cập nhật tên sản phẩm
                $('#Price').text(product.productPrice.toLocaleString() + "đ"); // Cập nhật giá sản phẩm
                $('#PriceOld').text(product.productPrice.toLocaleString() + "đ"); // Cập nhật giá cũ

                // Cập nhật đánh giá sản phẩm
                $('#review').text(`${product.productTotalRating}`)
   // Cập nhật sao cho đánh giá
   var rating = Math.round(product.productTotalRating); // Làm tròn đánh giá (nếu có)
   $('.rating li').each(function(index) {
       if (index < rating) {
           $(this).find('i').addClass('fill'); // Đánh dấu sao đã được đánh giá
       } else {
           $(this).find('i').removeClass('fill'); // Đánh dấu sao chưa được đánh giá
       }
   });
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
    var mainImage = product.productImages.find(image => image.isPrimary === 1); 
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
