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


    function renderProducts(products) {
        var productListHtml = '';  // Biến lưu trữ mã HTML của sản phẩm
    
        products.forEach(function(product) {
            // Tìm hình ảnh có isPrimary = 1
            var primaryImage = product.productImages.find(image => image.isPrimary === 1);
            var imageUrl = primaryImage ? primaryImage.imageUrl : ''; // Nếu không tìm thấy, để trống
    
            var productHtml = `
                <div>
                    <div class="product-box-4 wow fadeInUp">
                        <div class="product-image product-image-2">
                            <a href="product-left-thumbnail.html?id=${product.productId}">
                                <img src="${imageUrl}" class="img-fluid blur-up lazyload" alt="" />
                            </a>
    
                            <ul class="option">
                                <li data-bs-toggle="tooltip" data-bs-placement="top" title="Xem nhanh">
                                    <a href="javascript:void(0)" data-bs-toggle="modal" data-bs-target="#view">
                                        <i class="fa-solid fa-eye"></i>
                                    </a>
                                </li>
                                <li data-bs-toggle="tooltip" data-bs-placement="top" title="Yêu thích">
                                    <a href="javascript:void(0)" class="notifi-wishlist">
                                        <i class="fa-regular fa-heart"></i>
                                    </a>
                                </li>
                            </ul>
                        </div>
    
                        <div class="product-detail">
                            <ul class="rating">
                                ${renderStars(product.productTotalRating)}  <!-- Chức năng render sao -->
                            </ul>
                            <a href="product-left-thumbnail.html?id=${product.productId}">
                                <h5 class="name text-title">${product.productName}</h5>
                            </a>
                            <h5 class="price theme-color">${product.productPrice.toLocaleString()}đ</h5>
                            <div class="addtocart_btn">
                                <button class="add-button addcart-button btn buy-button text-light">
                                    <i class="fa-solid fa-plus"></i>
                                </button>
                                <div class="qty-box cart_qty">
                                    <div class="input-group">
                                        <button type="button" class="btn qty-left-minus" data-type="minus" data-field="">
                                            <i class="fa fa-minus"></i>
                                        </button>
                                        <input class="form-control input-number qty-input" type="text" name="quantity" value="1">
                                        <button type="button" class="btn qty-right-plus" data-type="plus" data-field="">
                                            <i class="fa fa-plus"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            `;
    
            productListHtml += productHtml;  // Thêm sản phẩm vào HTML
        });
    
        // Thêm các sản phẩm vào phần tử có id 'product-list'
        document.getElementById('product-list').innerHTML = productListHtml;
    
        // Khởi tạo Feather Icons
        feather.replace();
    
        // Khởi tạo Bootstrap Tooltip
        var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });
    }
    
    // Hàm render sao (rating) dựa trên tổng số sao của sản phẩm
    function renderStars(totalRating) {
        var stars = '';
        for (var i = 0; i < 5; i++) {
            stars += `<li><i data-feather="star" class="${i < totalRating ? 'fill' : ''}"></i></li>`;
        }
        return stars;
    }
    
});