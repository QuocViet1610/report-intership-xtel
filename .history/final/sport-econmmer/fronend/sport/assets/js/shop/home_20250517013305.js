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
        url: "http://localhost:8080/product/search?page=0&size=7", 
        type: "POST", 
        contentType: "application/json",  
        data: JSON.stringify(requestData),  
        success: function(response) {
            if (response.data && response.data.content) {
                console.log("product")
            console.log(response.data.content); 
            // renderProducts(response.data.content);
            renderRecommendedProducts(response.data.content);
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
    
            // Tạo mã HTML cho mỗi sản phẩm
            var productHtml = `
                <div class="col-12">
                    <div class="slider-7_1 arrow-slider img-slider">
                        <div>
                            <div class="product-box-4 wow fadeInUp">
                                <div class="product-image product-image-2">
                                    <a href="product-left-thumbnail.html?id=${product.productId}">
                                        <img src="${imageUrl}" class="img-fluid blur-up lazyload" alt="${product.productName}" />
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
                    </div>
                </div>
            `;
    
            productListHtml += productHtml;  // Thêm sản phẩm vào HTML
        });
    
        // Thêm các sản phẩm vào phần tử có id 'top-product'
        document.getElementById('top-product').innerHTML = productListHtml;
    
        // Khởi tạo Feather Icons
        feather.replace();
    
        // Khởi tạo Bootstrap Tooltip
        var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });
    }
    
    // Hàm render sao (rating) dựa trên tổng số sao của sản phẩm
    // function renderStars(totalRating) {
    //     var stars = '';
    //     for (var i = 0; i < 5; i++) {
    //         stars += `<li><i data-feather="star" class="${i < totalRating ? 'fill' : ''}"></i></li>`;
    //     }
    //     return stars;
    // }phẩm

function renderRecommendedProducts(products) {
        let productHtml = '';
        
        // Lặp qua từng sản phẩm và tạo HTML
        products.forEach(function(product) {
            // Tìm hình ảnh có isPrimary = 1 hoặc lấy hình ảnh đầu tiên nếu không có
            let imageUrl = '';
            if (product.productImages && product.productImages.length > 0) {
                const primaryImage = product.productImages.find(img => img.isPrimary === 1);
                imageUrl = primaryImage ? primaryImage.imageUrl : product.productImages[0].imageUrl;
            }

            // Tạo HTML cho sản phẩm
            productHtml += `
                <div>
                    <div class="product-box-4 wow fadeInUp">
                        <div class="product-image product-image-2">
                            <a href="product-left-thumbnail.html?id=${product.productId}">
                                <img src="${imageUrl}" class="img-fluid blur-up lazyload" alt="${product.productName}">
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
                                ${renderStars(product.productTotalRating)}
                            </ul>
                            <a href="product-left-thumbnail.html?id=${product.productId}">
                                <h5 class="name text-title">${product.productName}</h5>
                            </a>
                            <div style="display: inline-flex; ">
                                                        <h5 class="price theme-color">${product.productPrice.toLocaleString()}đ </h5>
<span> đã bán </span>
                            </div>

                            <div class="addtocart_btn">
                                <button class="add-button addcart-button btn buy-button text-light" data-product-id="${product.productId}">
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
        });

        // Thêm HTML vào slider
        $('.slider-7_1').html(productHtml);

        // Khởi tạo lại slider sau khi thêm dữ liệu
        initializeSlider();
        
        // Khởi tạo lại các sự kiện và plugin
        initializeEvents();
    }

    // Hàm khởi tạo slider
    function initializeSlider() {
        // Hủy slider hiện tại nếu có
        if ($('.slider-7_1').hasClass('slick-initialized')) {
            $('.slider-7_1').slick('unslick');
        }

        // Khởi tạo lại slider với các tùy chọn
        $('.slider-7_1').slick({
            arrows: true,
            infinite: true,
            slidesToShow: 5,
            slidesToScroll: 1,
            responsive: [
                {
                    breakpoint: 1400,
                    settings: {
                        slidesToShow: 4,
                        slidesToScroll: 1
                    }
                },
                {
                    breakpoint: 1200,
                    settings: {
                        slidesToShow: 3,
                        slidesToScroll: 1
                    }
                },
                {
                    breakpoint: 992,
                    settings: {
                        slidesToShow: 2,
                        slidesToScroll: 1
                    }
                },
                {
                    breakpoint: 475,
                    settings: {
                        slidesToShow: 1,
                        slidesToScroll: 1
                    }
                }
            ]
        });
    }

    // Hàm khởi tạo các sự kiện và plugin
    function initializeEvents() {
        // Khởi tạo tooltip
        $('[data-bs-toggle="tooltip"]').tooltip();

        // Khởi tạo feather icons
        if (typeof feather !== 'undefined') {
            feather.replace();
        }

        // Sự kiện cho nút thêm vào giỏ hàng
        $('.add-button').on('click', function() {
            const productId = $(this).data('product-id');
            const quantityInput = $(this).closest('.addtocart_btn').find('.qty-input');
            const quantity = parseInt(quantityInput.val()) || 1;
            
            // Thêm sản phẩm vào giỏ hàng (có thể gọi API hoặc xử lý logic tại đây)
            addToCart(productId, quantity);
        });

        // Sự kiện tăng/giảm số lượng
        $('.qty-right-plus').on('click', function() {
            const input = $(this).closest('.input-group').find('.qty-input');
            const currentValue = parseInt(input.val()) || 0;
            input.val(currentValue + 1);
        });

        $('.qty-left-minus').on('click', function() {
            const input = $(this).closest('.input-group').find('.qty-input');
            const currentValue = parseInt(input.val()) || 0;
            if (currentValue > 1) {
                input.val(currentValue - 1);
            }
        });
    }



function renderStars(rating) {
        let starsHtml = '';
        const fullStars = Math.floor(rating);
        const halfStar = (rating % 1 >= 0.5) ? 1 : 0;
        const emptyStars = 5 - fullStars - halfStar;

        // Render sao đầy
        for (let i = 0; i < fullStars; i++) {
            starsHtml += `
                <li>
                    <i data-feather="star" class="fill"></i>
                </li>
            `;
        }

        // Render sao nửa
        if (halfStar) {
            starsHtml += `
                <li>
                    <i data-feather="star" class="half-fill"></i>
                </li>
            `;
        }

        // Render sao rỗng
        for (let i = 0; i < emptyStars; i++) {
            starsHtml += `
                <li>
                    <i data-feather="star"></i>
                </li>
            `;
        }

        return starsHtml;
    }
    
});