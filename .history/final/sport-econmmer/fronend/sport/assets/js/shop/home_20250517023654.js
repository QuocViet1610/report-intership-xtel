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
        url: "http://localhost:8080/product/search?page=0&size=7&sort=productTotalSold.desc", 
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



        $.ajax({
        url: "http://localhost:8080/product/search?page=0&size=7&sort=productId.desc", 
        type: "POST", 
        contentType: "application/json",  
        data: JSON.stringify(requestData),  
        success: function(response) {
            if (response.data && response.data.content) {
                console.log("product")
            console.log(response.data.content); 
            // renderProducts(response.data.content);
            renderRecommendedProducts2(response.data.content);
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
                            <a href="javascript:void(0)" 
                            class="notifi-wishlist" 
                            data-name="${product.productName}" 
                            data-image="${imageUrl}" 
                            data-article="product-left-thumbnail.html?id=${product.productId}" 
                            data-token="${token}">
                                <i class="fa-regular fa-heart"></i>
                            </a>

                            </ul>
                        </div>

                        <div class="product-detail">
                            <ul class="rating">
                                ${renderStars(product.productTotalRating)}
                            </ul>
                            <a href="product-left-thumbnail.html?id=${product.productId}">
                                <h5 class="name text-title">${product.productName}</h5>
                            </a>
                    <div style="display: flex; justify-content: space-between; width: 100%;">
                    <h5 class="price theme-color">${product.productPrice.toLocaleString()}đ</h5>
                    <h5  style="padding-right:40px;">Đã bán: ${product.productTotalSold}</h5>
                    </div>

                            <div class="addtocart_btn">
                                <button class="add-button addcart-button btn buy-button text-light" data-product-id="${product.productId}"  onclick="location.href = 'product-left-thumbnail.html?id=${product.productId}';">
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
        $('.slider-7_1.arrow-slider.img-slider').html(productHtml);

        // Khởi tạo lại slider sau khi thêm dữ liệu
        initializeSlider();
        
        // Khởi tạo lại các sự kiện và plugin
        initializeEvents();
    }

$(document).on('click', '.notifi-wishlist', function(e) {
    e.preventDefault(); // Ngăn chặn hành vi mặc định của thẻ <a>
    
    // Lấy element <a> được click
    const $this = $(this);
    
    // Lấy dữ liệu từ thuộc tính data-*
    // Sử dụng cả hai phương thức để đảm bảo lấy được dữ liệu
    const name = $this.data('name') || $this.attr('data-name');
    const image = $this.data('image') || $this.attr('data-image');
    const article = $this.data('article') || $this.attr('data-article');
    const token = $this.data('token') || $this.attr('data-token');
    
    // Log để debug
    console.log("Clicked wishlist button");
    console.log("Element:", $this[0]);
    console.log("Full data attributes:", {
        name: name,
        image: image,
        article: article,
        token: token
    });
    
    // Kiểm tra dữ liệu
    if (!name || !image) {
        console.error("Missing product data. Data received:", { 
            name: name, 
            image: image,
            htmlElement: $this.prop('outerHTML')
        });
        
        // Thử lấy thông tin từ các phần tử gần kề
        const productBox = $this.closest('.product-box-4');
        const altName = productBox.find('.name').text();
        const altImage = productBox.find('img').attr('src');
        
        if (altName && altImage) {
            console.log("Recovered data from nearby elements:", { altName, altImage });
            // Sử dụng dữ liệu thay thế
            saveFavoriteProduct(altName, altImage, article || "", token || "");
        } else {
            alert("Không thể lưu sản phẩm do thiếu thông tin!");
        }
        return;
    }
    
    // Lưu sản phẩm vào danh sách yêu thích
    saveFavoriteProduct(name, image, article, token);
});

// Tách logic lưu sản phẩm yêu thích thành hàm riêng
function saveFavoriteProduct(name, image, article, token) {
    const favoriteProduct = { 
        name: name, 
        image: image, 
        article: article, 
        token: token,
        dateAdded: new Date().toISOString() 
    };
    
    console.log("Saving product to favorites:", favoriteProduct);
    
    // Lấy dữ liệu từ localStorage
    let favorites = [];
    try {
        favorites = JSON.parse(localStorage.getItem('favorites')) || [];
    } catch (error) {
        console.error("Error parsing favorites from localStorage:", error);
        favorites = [];
    }
    
    // Kiểm tra xem sản phẩm đã tồn tại trong danh sách yêu thích chưa
    const existingIndex = favorites.findIndex(item => 
        item.name === name && item.article === article
    );
    
    if (existingIndex >= 0) {
        // Nếu sản phẩm đã tồn tại, thông báo cho người dùng
        alert(`Sản phẩm "${name}" đã có trong danh sách yêu thích!`);
    } else {
        // Thêm sản phẩm mới vào danh sách
        favorites.push(favoriteProduct);
        localStorage.setItem('favorites', JSON.stringify(favorites));
        
        // Hiển thị thông báo thành công
        alert(`Đã thêm sản phẩm "${name}" vào danh sách yêu thích!`);
        
        // Tùy chọn: Cập nhật biểu tượng trái tim hoặc số lượng sản phẩm yêu thích
        updateWishlistUI();
    }
}

// Cập nhật giao diện khi thêm sản phẩm yêu thích
function updateWishlistUI() {
    // Đếm số lượng sản phẩm yêu thích
    const favorites = JSON.parse(localStorage.getItem('favorites')) || [];
    const count = favorites.length;
    
    // Cập nhật số hiển thị trên biểu tượng giỏ hàng (nếu có)
    const wishlistCountElement = $('.wishlist-count');
    if (wishlistCountElement.length) {
        wishlistCountElement.text(count);
    }
    
    // Thêm class 'active' cho nút yêu thích đã được click
    $('.notifi-wishlist').each(function() {
        const $this = $(this);
        const productName = $this.data('name') || $this.attr('data-name');
        const productArticle = $this.data('article') || $this.attr('data-article');
        
        // Kiểm tra xem sản phẩm có trong danh sách yêu thích không
        const isInWishlist = favorites.some(item => 
            item.name === productName && item.article === productArticle
        );
        
        // Cập nhật biểu tượng trái tim
        if (isInWishlist) {
            $this.find('i').removeClass('fa-regular').addClass('fa-solid');
        }
    });
}

    function renderRecommendedProducts2(products) {
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
<div style="display: flex; justify-content: space-between; width: 100%;">
  <h5 class="price theme-color">${product.productPrice.toLocaleString()}đ</h5>
  <h5  style="padding-right:40px;">Đã bán: ${product.productTotalSold}</h5>
</div>

                            <div class="addtocart_btn">
                                <button class="add-button addcart-button btn buy-button text-light" data-product-id="${product.productId} " onclick="location.href = 'product-left-thumbnail.html?id=${product.productId}';">
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
        $('.slider-7_1.arrow-slide.img-slider').html(productHtml);

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

$(document).ready(function () {
    // Gọi hàm để tải và hiển thị bài viết blog
    fetchAndRenderBlogs();

    // Hàm để tải dữ liệu blog từ API và hiển thị
    function fetchAndRenderBlogs() {
        // Tạo đối tượng request để gửi lên API
        let searchRequest = {
            searchText: null,
            categoryId: null
        };

        // Gọi API để lấy dữ liệu blog
        $.ajax({
            url: `http://localhost:8080/blog/search?page=0&size=3`,
            method: "POST",
            dataType: "json",
            data: JSON.stringify(searchRequest),
            contentType: "application/json",
            headers: {
                "Accept": "application/json"
            },
            success: function(response) {
                if (response.code === "200") {
                    console.log("Blog data loaded successfully");
                    renderBlogs(response.data.content);
                    initializeBlogSlider();
                } else {
                    showError("Lỗi khi tải dữ liệu: " + response.message);
                }
            },
            error: function(xhr) {
                let errorMessage = xhr.responseJSON && xhr.responseJSON.message
                    ? xhr.responseJSON.message
                    : "Có lỗi xảy ra! Mã lỗi: " + xhr.status;
                showError(errorMessage);
            }
        });
    }

    // Hàm để hiển thị thông báo lỗi
    function showError(message) {
        console.error(message);
        // Bạn có thể thêm logic hiển thị thông báo lỗi cho người dùng ở đây
        // Ví dụ: alert(message) hoặc hiển thị một div chứa thông báo lỗi
    }

    // Hàm để render các bài blog từ dữ liệu API
    function renderBlogs(blogs) {
        // Kiểm tra nếu có dữ liệu blog
        if (!blogs || blogs.length === 0) {
            console.log("Không có dữ liệu blog");
            return;
        }

        // Tạo container để chứa các blog item
        let blogContainer = $('.slider-3-blog');
        
        // Xóa nội dung hiện tại của container
        blogContainer.empty();

        // Duyệt qua mỗi blog và tạo HTML
        blogs.forEach(function(blog) {
            // Định dạng ngày tháng
            let date = new Date(blog.createdAt);
            let formattedDate = formatDate(date);

            // Tạo nội dung tóm tắt từ nội dung đầy đủ
            let summary = createSummary(blog.content);

            // Tạo HTML cho mỗi blog item
            let blogItemHtml = `
                <div>
                    <div class="blog-box ratio_45">
                        <div class="blog-box-image">
                            <a href="blog-detail.html?id=${blog.id}">
                                <img src="${blog.image}" class="blur-up lazyload bg-img" alt="${blog.title}">
                            </a>
                        </div>

                        <div class="blog-detail">
                            <label>${blog.title}</label>
                            <a href="blog-detail.html?id=${blog.id}">
                                <h3>${summary}</h3>
                            </a>
                            <h5 class="text-content">${formattedDate}</h5>
                        </div>
                    </div>
                </div>
            `;

            // Thêm blog item vào container
            blogContainer.append(blogItemHtml);
        });
    }

    // Hàm để tạo tóm tắt từ nội dung blog
    function createSummary(content) {
        // Tạo một container tạm thời để loại bỏ các thẻ HTML
        let tempDiv = document.createElement("div");
        tempDiv.innerHTML = content;
        let textContent = tempDiv.textContent || tempDiv.innerText || "";
        
        // Cắt nội dung thành tóm tắt ngắn
        let maxLength = 120; // Số ký tự tối đa cho tóm tắt
        if (textContent.length > maxLength) {
            return textContent.substring(0, maxLength) + "...";
        }
        return textContent;
    }

    // Hàm để định dạng ngày tháng
    function formatDate(date) {
        if (!(date instanceof Date) || isNaN(date)) {
            return "Không có ngày";
        }
        
        let day = date.getDate();
        let month = date.getMonth() + 1; // Tháng bắt đầu từ 0
        let year = date.getFullYear();
        
        return `${day}/${month}/${year}`;
    }

    // Hàm khởi tạo slider cho blog
    function initializeBlogSlider() {
        // Kiểm tra xem slider đã được khởi tạo chưa
        if ($('.slider-3-blog').hasClass('slick-initialized')) {
            // Nếu đã khởi tạo, hủy bỏ slider hiện tại
            $('.slider-3-blog').slick('unslick');
        }
        
        // Khởi tạo slider mới
        $('.slider-3-blog').slick({
            dots: false,
            infinite: true,
            speed: 500,
            arrows: true,
            slidesToShow: 3,
            slidesToScroll: 1,
            responsive: [
                {
                    breakpoint: 1200,
                    settings: {
                        slidesToShow: 2,
                        slidesToScroll: 1
                    }
                },
                {
                    breakpoint: 768,
                    settings: {
                        slidesToShow: 1,
                        slidesToScroll: 1
                    }
                }
            ]
        });
    }
});