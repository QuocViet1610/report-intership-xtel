$(document).ready(function () {
    function getUrlParameter(name) {
        var url = new URL(window.location.href);
        var params = new URLSearchParams(url.search);
        return params.get(name);
    }
    var productId  = getUrlParameter('id');
    console.log(productId); 
    
    var categoryId = null;
    $.ajax({
        url: `http://localhost:8080/product/${productId}`, 
        type: "GET",  // Sử dụng phương thức GET
        success: function(response) {


            if (response.code === "200") {
                var product = response.data;
                console.log(product)
                renderProductImages(product);

                saveProduct(product);
                $('#nameProduct').text(product.productName); 
                $('#Price').text(product.productPrice.toLocaleString() + "đ"); 
                $('#PriceOld').text(product.productPrice.toLocaleString() + "đ"); 
                $('#ratting').text(`${product.productTotalRating}`)
                $('#sold').text(`| đã bán ${product.productTotalSold}`)
                $('#brandName').text(product.brandName || 'Không có thương hiệu');  
                $('#productCode').text(product.productCode || 'Không có thông tin'); 
                $('#createdAt').text(new Date(product.createdAt).toLocaleDateString() || 'Không có thông tin');               
                
                if (product.productDescription) {
                    $('#product-description').html(product.productDescription);
                } else {
                    $('#product-description').html('Mô tả sản phẩm chưa có.');
                }


                var totalStock = product.productVariants.reduce(function(total, variant) {
                        return total + variant.quantity;
                }, 0);
                $('#stock').text(`${totalStock}` || 'Không có thông tin');
          
                if (product.productAttributeValue && product.productAttributeValue.length > 0) {
                    renderProductAttributes(product);
                }                
                renderProductAttributes(product); 

var rating = product.productTotalRating; // Lấy giá trị đánh giá

// Số lượng sao đầy
var fullStars = Math.floor(rating);


// Render stars
$('.ratting li').each(function(index) {
    if (index < fullStars) {
        console.log(1)
        $(this).find('i').addClass('fill');  // Sao đầy
    }  else {
        $(this).find('i').removeClass('fill half-fill');  // Không có sao
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


                   
    var quantity = parseInt($("#qty-input").val());
    var stock = parseInt($("#stock").text());

    // Disable nút cộng nếu quantity đã bằng hoặc lớn hơn stock
    if (quantity >= stock) {
        $('#qty-right-plus').prop('disabled', true); // Disable nút cộng
        $('#qty-right-plus').css({ 
            'background-color': '#d3d3d3',  // Màu nền mờ
            'color': '#a1a1a1',  // Màu chữ mờ
            'cursor': 'not-allowed'  // Con trỏ chuột thành dạng không cho phép
        });
    }              



// Lấy toàn bộ URL
let url = window.location.href;

// Sử dụng URLSearchParams để lấy giá trị của cateId
let urlParams = new URLSearchParams(window.location.search);
let cateId = urlParams.get('cateId');  // Lấy giá trị của tham số cateId


    var requestData = {
        searchText: "",
        brandId: null,
        genderId: null,
        fullParentId: cateId, // id là biến toàn cục hoặc bạn cần gán giá trị cho nó
        categorySearch: parseInt(cateId), // id là biến toàn cục hoặc bạn cần gán giá trị cho nó
 
    };

$.ajax({
    url: "http://localhost:8080/product/search?page=0&size=5&sort=productTotalSold.desc" ,
    type: "POST", 
    contentType: "application/json",  
    data: JSON.stringify(requestData),  
    success: function(response) {
        if (response.data && response.data.content) {
            console.log("product");
            console.log(response.data.content); 

            // Lấy dữ liệu sản phẩm từ response
            var products = response.data.content;
            
            // Tạo HTML để render vào trang
            var productListHTML = '';

            products.forEach(function(product) {
           if (Number(productId) !== Number(product.productId)){
              var imageUrl = 'default-image.jpg'; // Giá trị mặc định nếu không tìm thấy hình ảnh chính
                if (product.productImages && product.productImages.length > 0) {
                    // Tìm hình ảnh có isPrimary = 1
                    var primaryImage = product.productImages.find(image => image.isPrimary === 1);
                    if (primaryImage) {
                        imageUrl = primaryImage.imageUrl;  // Lấy imageUrl của hình ảnh chính
                    } else {
                        imageUrl = product.productImages[0].imageUrl;  // Nếu không có hình ảnh chính, dùng hình ảnh đầu tiên
                    }
                }
                var productName = product.productName || 'Tên sản phẩm';
                var productPrice = product.productPrice || 'Giá chưa cập nhật';
                var productLink = 'product-left-thumbnail.html?id=' + product.productId +'&cateId='+ cateId;  // Tạo link chi tiết sản phẩm

                // Thêm sản phẩm vào HTML
                productListHTML += `
                    <li>
                        <div class="offer-product">
                            <a href="${productLink}" class="offer-image">
                                <img src="${imageUrl}" class="img-fluid blur-up lazyload" alt="">
                            </a>
                            <div class="offer-detail">
                                <div>
                                    <a href="${productLink}">
                                        <h6 class="name">${productName}</h6>
                                    </a>
                                                                
                                    <h6 class="price theme-color">${productPrice.toLocaleString() + "đ"}</h6>
                                    <span>Đã bán: ${product.productTotalSold}</span>
                                </div>
                            </div>
                        </div>
                    </li>
                `;
                   }
            });

            // Đưa danh sách sản phẩm vào phần HTML
            $('.product-list').html(productListHTML);
            
        }
    },
    error: function(xhr, status, error) {
        let errorMessage = "Lịch sử giao hàng đang trống";
        if (xhr.responseJSON && xhr.responseJSON.message) {
            errorMessage = xhr.responseJSON.message;
        }
        console.log(errorMessage);  
    }
});


// sản phầm cùng thể loại

// Định nghĩa function để tạo HTML cho mỗi sản phẩm
function createProductHTML(product) {
    var imageUrl = 'default-image.jpg'; // Giá trị mặc định nếu không tìm thấy hình ảnh chính
                if (product.productImages && product.productImages.length > 0) {
                    // Tìm hình ảnh có isPrimary = 1
                    var primaryImage = product.productImages.find(image => image.isPrimary === 1);
                    if (primaryImage) {
                        imageUrl = primaryImage.imageUrl;  // Lấy imageUrl của hình ảnh chính
                    } else {
                        imageUrl = product.productImages[0].imageUrl;  // Nếu không có hình ảnh chính, dùng hình ảnh đầu tiên
                    }
                }

    return `
    <div>
        <div class="product-box-3 wow fadeInUp">
            <div class="product-header">
                <div class="product-image">
                    <a href="product-left-thumbnail.html?id=${product.productId}&cateId=${cateId}">
                        <img src="${imageUrl}"
                            class="img-fluid blur-up lazyload" alt="${product.productName}">
                    </a>

                    <ul class="product-option">
                        <li data-bs-toggle="tooltip" data-bs-placement="top" title="View">
                            <a href="javascript:void(0)" data-bs-toggle="modal"
                                data-bs-target="#view" onclick="quickView(${product.productId})">
                                <i data-feather="eye"></i>
                            </a>
                        </li>



                        <li data-bs-toggle="tooltip" data-bs-placement="top" title="Wishlist">
                            <a href="javascript:void(0)" class="notifi-wishlist" onclick="addToWishlist(${product.productId})">
                                <i data-feather="heart"></i>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>

            <div class="product-footer">
                <div class="product-detail">
                    <a href="/product/detail?id=${product.id}">
                        <h5 class="name">${product.productName}</h5>
                    </a>
                    <div class="product-rating mt-2">
                        <ul class="rating">
                            ${getRatingStars(product.ratingAvg || 5)}
                        </ul>
                        <span>(${product.ratingAvg || 5}.0)</span>
                    </div>
                    <h6 class="unit">${product.unit || ''}</h6>
                    <h5 class="price"><span class="theme-color">${formatPrice(product.productPrice)}đ</span> 
                    </h5>
                    <div class="add-to-cart-box bg-white">
                        <div class="cart_qty qty-box">
                            <div class="input-group bg-white">
                                <button type="button" class="qty-left-minus bg-gray"
                                    data-type="minus" data-field="">
                                    <i class="fa fa-minus"></i>
                                </button>
                                <input class="form-control input-number qty-input" type="text"
                                    name="quantity" value="0">
                                <button type="button" class="qty-right-plus bg-gray"
                                    data-type="plus" data-field="">
                                    <i class="fa fa-plus"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    `;
}

// Hàm định dạng giá tiền
function formatPrice(price) {
    return price ? price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".") : '0';
}

// Hàm tạo các ngôi sao đánh giá
function getRatingStars(rating) {
    let stars = '';
    const fullStars = Math.floor(rating);
    
    for (let i = 0; i < 5; i++) {
        if (i < fullStars) {
            stars += '<li><i data-feather="star" class="fill"></i></li>';
        } else {
            stars += '<li><i data-feather="star"></i></li>';
        }
    }
    
    return stars;
}

// Hàm để lấy dữ liệu từ API và cập nhật giao diện
function loadRelatedProducts(categoryId) {
    // Tạo dữ liệu request
    var requestData = {
        searchText: "",
        brandId: null,
        genderId: null,
        fullParentId: cateId, // id là biến toàn cục hoặc bạn cần gán giá trị cho nó
        categorySearch: parseInt(cateId), // id là biến toàn cục hoặc bạn cần gán giá trị cho nó
 
    };
    
    $.ajax({
        url: "http://localhost:8080/product/search?page=0&size=10&sort=productName.desc",
        type: "POST", 
        contentType: "application/json",  
        data: JSON.stringify(requestData),  
        success: function(response) {
            if (response.data && response.data.content && response.data.content.length > 0) {
                console.log("Sản phẩm cùng thể loại:", response.data.content);
                
                // Xóa nội dung cũ và thêm sản phẩm mới
                const productContainer = $(".slider-6_1.product-wrapper");
                productContainer.empty();
                
                // Thêm mỗi sản phẩm vào container
                response.data.content.forEach(function(product) {
                    productContainer.append(createProductHTML(product));
                });
                
                // Khởi tạo lại Feather Icons sau khi thêm mới nội dung
                feather.replace();
                
                // Khởi tạo lại slick slider nếu cần
                initSlider();
            } else {
                console.log("Không có sản phẩm cùng thể loại");
                $(".product-list-section").hide();
            }
        },
        error: function(xhr, status, error) {
            let errorMessage = "Không tìm thấy sản phẩm cùng thể loại";
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }
            console.log(errorMessage);
            $(".product-list-section").hide();
        }
    });
}

// Hàm khởi tạo slider
function initSlider() {
    if ($('.slider-6_1').hasClass('slick-initialized')) {
        $('.slider-6_1').slick('unslick');
    }
    
    $('.slider-6_1').slick({
        dots: false,
        infinite: true,
        speed: 500,
        slidesToShow: 6,
        slidesToScroll: 1,
        responsive: [
            {
                breakpoint: 1399,
                settings: {
                    slidesToShow: 5,
                    slidesToScroll: 1,
                }
            },
            {
                breakpoint: 1199,
                settings: {
                    slidesToShow: 4,
                    slidesToScroll: 1
                }
            },
            {
                breakpoint: 991,
                settings: {
                    slidesToShow: 3,
                    slidesToScroll: 1
                }
            },
            {
                breakpoint: 767,
                settings: {
                    slidesToShow: 2,
                    slidesToScroll: 1
                }
            },
            {
                breakpoint: 476,
                settings: {
                    slidesToShow: 1,
                    slidesToScroll: 1
                }
            }
        ]
    });
}

// Gọi function để lấy dữ liệu khi trang đã load xong
$(document).ready(function() {
    // Lấy categoryId từ sản phẩm hiện tại (có thể thay đổi cách lấy tùy theo cấu trúc trang của bạn)
    const currentCategoryId = $('input[name="categoryId"]').val() || $('#categoryId').val();
    
    // Tải sản phẩm cùng thể loại
    loadRelatedProducts(currentCategoryId);
    
    // Thiết lập các sự kiện cho nút tăng/giảm số lượng
    $(document).on('click', '.qty-right-plus', function() {
        const input = $(this).siblings('.qty-input');
        const currentVal = parseInt(input.val());
        input.val(isNaN(currentVal) ? 1 : currentVal + 1);
    });
    
    $(document).on('click', '.qty-left-minus', function() {
        const input = $(this).siblings('.qty-input');
        const currentVal = parseInt(input.val());
        if (!isNaN(currentVal) && currentVal > 0) {
            input.val(currentVal - 1);
        }
    });
});

 $.ajax({
        url: `http://localhost:8080/product-rating/${productId}`,   
        method: 'GET', 
        success: function(response) {
  const reviews = response.data; // Giả sử response.data là một mảng các đánh giá
   var productAverageRating = reviews[0].productAverageRating; 
   console.log("productAverageRating")
   console.log(productAverageRating)
 console.log(reviews)
        // Cập nhật giá trị điểm trung bình vào h2
        $(".product-main-rating h2").html(`${productAverageRating} <i data-feather="star"></i>`); // Gắn giá trị vào h2
        // Xây dựng HTML cho reviews
        let reviewsHtml = '';
        
        reviews.forEach(review => {
            reviewsHtml += `
            <li>
                <div class="people-box">
                    <div>
                        <div class="people-image people-text">
                            <img alt="user" class="img-fluid" src="${review.avatar}">
                        </div>
                    </div>
                    <div class="people-comment">
                        <div class="people-name">
                            <a href="javascript:void(0)" class="name">${review.fullName}</a>
                            <div class="date-time">
                                <h6 class="text-content">${new Date(review.createdAt).toLocaleDateString()}</h6>
                                <div class="product-rating">
                                    <ul class="rating">
                                        ${renderStars(review.ratingValue)} <!-- Render sao đánh giá -->
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="reply">
                            <p>${review.ratingComment}</p>
                        </div>
                    </div>
                </div>
            </li>
            `;
        });

        // Chèn HTML vào trong phần tử có class 'review-list'
        $(".review-list").html(reviewsHtml);
        },
        error: function(xhr, status, error) {
            console.error("Error adding product to cart:", error);
            let errorMessage = "Có lỗi xảy ra khi thêm sản phẩm vào giỏ hàng.";
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }
            showError(errorMessage);  
        }
    });
function renderStars(rating) {
    let starsHtml = '';
    for (let i = 0; i < 5; i++) {
        if (i < rating) {
            starsHtml += `<li><i data-feather="star" class="fill"></i></li>`;
        } else {
            starsHtml += `<li><i data-feather="star"></i></li>`;
        }
    }
    return starsHtml;
}

// đánh giá sản phẩm 
  document.getElementById('submitReview').addEventListener('click', function () {
        // Lấy giá trị từ form
        const rating = document.querySelector('.rating-select').value;
        const comment = document.getElementById('content').value;
        const userId = 456;  
        const isActive = 1; 

    let token = localStorage.getItem('authToken');
        // Dữ liệu cần gửi đi
        const reviewData = {
            rating: rating,
            comment: comment,
            productId: productId,
            userId: userId,
            isActive: isActive
        };

        // Gửi yêu cầu AJAX đến server
        $.ajax({
            url: 'http://localhost:8080/product-rating',  // Đường dẫn endpoint, thay {{local}} bằng địa chỉ thực tế
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(reviewData),  // Chuyển đổi đối tượng thành chuỗi JSON
                        headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json',

            },
            success: function (response) {
                console.log("Đánh giá đã được gửi thành công:", response);
                showSuccess("Cảm ơn bạn đã đánh giá sản phẩm!");
                   setTimeout(function() {
            location.reload();  // Tải lại trang sau 1 giây
        }, 1000);  // 1000ms = 1 giây
            },
            error: function (xhr, status, error) {
            console.error("Error adding product to cart:", error);
            let errorMessage = "Có lỗi xảy ra.";
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }
            showError(errorMessage);  
            }
        });
    });

});
var productSave = {};
function saveProduct(product){
    productSave = product
}




$(document).ready(function() {



});
function renderProductImages(product) {


    // Ẩn ảnh phụ trước khi render
    $('#sidebar-images').hide();

    // Render ảnh chính (first image)
    const mainImage = product.productImages.find(image => image.isPrimary === 1); 
    if (mainImage) {
        $('#main-image').attr('src', mainImage.imageUrl);
        $('#main-image').attr('data-zoom-image', mainImage.imageUrl);
    }

    // Render ảnh phụ (sidebar images)
    let sidebarImagesHtml = '';
    product.productImages.forEach(function(image) {
        if (image.isPrimary === 0) {
            sidebarImagesHtml += `
                <div class="sidebar-image-container">
                    <div class="sidebar-image">
                        <img src="${image.imageUrl}" class="img-fluid blur-up lazyload" alt="" data-src="${image.imageUrl}">
                    </div>
                </div>
            `;
        }
    });

    // Đảm bảo ảnh chính được tải xong trước khi hiển thị ảnh phụ
    $('#main-image').on('load', function() {
        setTimeout(function() {
            $('#sidebar-images').html(sidebarImagesHtml); // Thêm ảnh phụ vào phần tử
            $('#sidebar-images').show(); // Hiển thị ảnh phụ

            // Kích hoạt lazyload ngay khi ảnh được thêm vào DOM
            // Đảm bảo lazy load được kích hoạt ngay khi trang tải
            $('.lazyload').each(function() {
                if ($(this).attr('data-src')) {
                    $(this).attr('src', $(this).attr('data-src')); // Thay đổi src để lazyload ảnh ngay lập tức
                }
            });

            // Sự kiện click vào ảnh con để thay đổi ảnh chính
            $('.sidebar-image img').off('click').on('click', function() { // Dùng .off() để tránh sự kiện bị thêm nhiều lần
                var selectedImageUrl = $(this).attr('data-src'); // Lấy URL ảnh con đã click
                $('#main-image').attr('src', selectedImageUrl);  // Thay đổi ảnh chính
                $('#main-image').attr('data-zoom-image', selectedImageUrl);  // Cập nhật ảnh zoom
            });
        }, 1000); // Trì hoãn 1 giây trước khi hiển thị ảnh phụ
    });

    // Kích hoạt tải ảnh khi trang tải xong
    $(window).on('load', function() {
        $('#main-image').trigger('load');
    });

}




var selectedAttributeIds = [];
var productVariantsArray = [];


function renderProductAttributes(products) {
    productVariantsArray = products.productVariants;
    product = products
    products = products.productAttributeValue;

    var attributesHtml = '';
    products.forEach(function(productAttributeValue) {
        // Tạo phần tử cho mỗi thuộc tính (Kích thước, Màu sắc, ...)
        var attributeHtml = `
            <div class="product-package" data-id="${productAttributeValue.attributeId}">
                <div class="product-title">
                    <h4>${productAttributeValue.attributeName}</h4>
                </div>
                <ul class="select-package">
                    ${productAttributeValue.attributeValues.map(function(value) {
                        return `<li><a href="javascript:void(0)" data-id="${value.id}" class="attribute-value">${value.name}</a></li>`;
                    }).join('')}
                </ul>
            </div>
        `;
        // Thêm phần tử vào container
        attributesHtml += attributeHtml;
    });

    // Chèn tất cả thuộc tính vào phần tử có id 'product-attributes'
    $('#product-attributes').html(attributesHtml);

    // Gắn sự kiện click cho các giá trị thuộc tính
    $('.attribute-value').on('click', function() {
        var $this = $(this);
        var $parentPackage = $this.closest('.product-package');
        var attributeId = $parentPackage.data('id'); // ID của thuộc tính (kích thước, màu sắc,...)

        // Reset màu sắc cho tất cả các thuộc tính
        $parentPackage.find('.attribute-value').css('background-color', '').css('color', '');

        // Chuyển màu xanh cho giá trị đã chọn
        $this.css('background-color', 'green').css('color', 'white');

        var valueId = $this.data('id');
        
        // Tìm và thay thế giá trị đã chọn trong mảng selectedAttributeIds
        var existingIndex = selectedAttributeIds.findIndex(function(item) {
            return item.attributeId === attributeId;
        });

        if (existingIndex !== -1) {
            selectedAttributeIds[existingIndex].valueId = valueId;  // Thay thế giá trị cũ
        } else {
            selectedAttributeIds.push({ attributeId: attributeId, valueId: valueId });  // Thêm mới
        }
        $("#qty-input").val(1);

        updateProductInfo();
    });

    // Chọn giá trị mặc định cho mỗi thuộc tính và đánh dấu là đã chọn
    products.forEach(function(productAttributeValue) {
        var firstValue = productAttributeValue.attributeValues[0];
        var $firstAttributeValue = $(`.product-package[data-id='${productAttributeValue.attributeId}'] .attribute-value[data-id='${firstValue.id}']`);

        // Đánh dấu giá trị đầu tiên là đã chọn
        $firstAttributeValue.css('background-color', 'green').css('color', 'white');

        // Thêm vào mảng selectedAttributeIds với attributeId và valueId
        if (!selectedAttributeIds.some(item => item.attributeId === productAttributeValue.attributeId)) {
            selectedAttributeIds.push({
                attributeId: productAttributeValue.attributeId,
                valueId: firstValue.id
            });
        }
    });

    updateProductInfo();
}

function updateProductInfo() {
    var selectedVariant = productVariantsArray.filter(function(variant) {
        var variantAttributes = variant.variantAttributes.map(function(attr) {
            return { attributeId: attr.attributeValueId };  // Lấy attributeId từ variantAttributes
        });

        // Kiểm tra xem mọi attribute đã chọn có khớp với các giá trị trong variant
        return selectedAttributeIds.every(function(selected) {
            return variantAttributes.some(function(variantAttribute) {
                return variantAttribute.attributeId === selected.valueId;
            });
        });
    });

    console.log(selectedVariant);

    if (selectedVariant.length > 0) {
        var variant = selectedVariant[0]; // Lấy sản phẩm variant đầu tiên khớp với các thuộc tính đã chọn
        $('#nameProduct').text(variant.name); 
        $('#Price').text(variant.price.toLocaleString() + "đ"); 
        $('#PriceOld').text(variant.price.toLocaleString() + "đ"); 
        $('#productCode').text(variant.code || 'Không có thông tin'); 
        $('#createdAt').text(variant.createdAt.toLocaleDateString() || 'Không có thông tin');   
        $('#stock').text(variant.quantity || 'Không có thông tin');
    } else {
        console.log("No matching product variant found.");
    }
}

function addToCart() {
   showError("vui lòng đăng nhập")
}

function addToCartBackend() {
    function getUrlParameter(name) {
        var url = new URL(window.location.href);
        var params = new URLSearchParams(url.search);
        return params.get(name);
    }
    var productId  = getUrlParameter('id');
    productSave.productVariants = [];
    var quantity = parseInt($("#qty-input").val()); //so luong
    if (isNaN(quantity) || quantity <= 0) {
        showError("Số lượng không hợp lệ");
        return;  
    }
    
    let productVariantId ;
    
    if (selectedAttributeIds && selectedAttributeIds.length > 0) {
        // Kiểm tra nếu có variant
        var selectedVariant = productVariantsArray.filter(function (variant) {
            var variantAttributes = variant.variantAttributes.map(function (attr) {
                return { attributeId: attr.attributeValueId };  // Lấy attributeId từ variantAttributes
            });

            // Kiểm tra xem mọi attribute đã chọn có khớp với các giá trị trong variant
            return selectedAttributeIds.every(function (selected) {
                return variantAttributes.some(function (variantAttribute) {
                    return variantAttribute.attributeId === selected.valueId;
                });
            });
        });
        productVariantId = selectedVariant[0].id;
    } else {
        productVariantId = null;
    }

    
    let token = localStorage.getItem('authToken');
    let data = {
        productId: productId,
        productVariantId: productVariantId,
        quantity: quantity
    };

    $.ajax({
        url: 'http://localhost:8080/cart',  
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json',
        },
        data: JSON.stringify(data),  
        success: function(response) {
            renderCartHeader();
            showSuccess("Thêm sản phẩm giỏ hàng thành công");
        },
        error: function(xhr, status, error) {
            console.error("Error adding product to cart:", error);
            let errorMessage = "Có lỗi xảy ra khi thêm sản phẩm vào giỏ hàng.";
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }
            showError(errorMessage);  
        }
    });

}

// Gắn sự kiện cho nút "Thêm vào giỏ hàng"
document.querySelector('#cart-button').addEventListener('click', function() {
    let token = localStorage.getItem('authToken');
    if(token){
        addToCartBackend();

       
    }else{
          addToCart();
    }


});
/// so luong
document.querySelector('#qty-right-plus').addEventListener('click', function() {
    var quantity = parseInt($("#qty-input").val());
    var stock = parseInt($("#stock").text());
    
    var qtyInput = document.querySelector('#qty-input');
    var currentValue = parseInt(qtyInput.value, 10); 


    if (!isNaN(currentValue)) {
        qtyInput.value = currentValue + 1;
    } else {
        qtyInput.value = 1; 
    }
    if (parseInt(qtyInput.value) >= stock) {
        $('#qty-right-plus').prop('disabled', true);
        $('#qty-right-plus').css({ 
            'background-color': '#d3d3d3',  // Màu nền mờ
            'color': '#a1a1a1',  // Màu chữ mờ
          
        });
    }
});

document.querySelector('#qty-left-minus').addEventListener('click', function() {
    var quantity = parseInt($("#qty-input").val());
    var stock = parseInt($("#stock").text());
    var qtyInput = document.querySelector('#qty-input');
    var currentValue = parseInt(qtyInput.value, 10); // Chuyển giá trị của input thành số

    // Kiểm tra nếu giá trị là số hợp lệ và giảm nó đi 1, không cho giá trị âm
    if (!isNaN(currentValue) && currentValue > 1) {
        qtyInput.value = currentValue - 1;
    } else {
        qtyInput.value = 1; // Đảm bảo giá trị không trở thành âm
    }
    if (parseInt(qtyInput.value) < stock) {
        $('#qty-right-plus').prop('disabled', false); // Enable nút cộng
        $('#qty-right-plus').css({ 
            'background-color': '',  // Khôi phục màu nền mặc định
            'color': '',  // Khôi phục màu chữ mặc định
            'cursor': ''  // Khôi phục con trỏ chuột mặc định
        });
    }
});

document.querySelector('#qty-input').addEventListener('input', function(e) {
    var value = e.target.value;

    if (!/^[-]?\d*$/.test(value)) {  
        e.target.setCustomValidity('Please enter a valid number.');
    } else {
        // Nếu giá trị là "0", thay đổi thành "1"
        if (value === '0') {
            e.target.value = '1';
        }
        e.target.setCustomValidity(''); // Không có lỗi
    }
});



