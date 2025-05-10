function getUrlParameter(name) {
    var url = new URL(window.location.href);
    var params = new URLSearchParams(url.search);
    return params.get(name);
}

// Lấy id từ URL
var id = getUrlParameter('id');
console.log(id); 
console.log(typeof id); 

$(document).ready(function () {
// Lấy giá trị id từ URL

    initBrands();
    initClothesSize();
    initShoesSize();
    initColors();
    
    // Tải dữ liệu ban đầu
    fetchDataApi(null, null);
});

// Hàm khởi tạo dữ liệu thương hiệu
function initBrands() {
    var requestDataBrand = {
        searchText: "",
    };

    $.ajax({
        url: "http://localhost:8080/brand/search?page=0&size=20&sort=id.asc", 
        type: "POST", 
        contentType: "application/json",  
        data: JSON.stringify(requestDataBrand),  
        success: function(response) {
            if (response.data && response.data.content) {
                let brandList = response.data.content;
                
                let brandListHTML = `
                    <li>
                        <div class="form-check ps-0 m-0 category-list-box">
                            <input class="form-check-input" type="radio" name="brand" id="flexRadioDefaultAll" data-brand-id="null" checked>
                            <label class="form-check-label" for="flexRadioDefaultAll">
                                <span class="name">Tất cả</span>
                            </label>
                        </div>
                    </li>
                `;

                brandList.forEach(function(brand) {
                    brandListHTML += `
                        <li>
                            <div class="form-check ps-0 m-0 category-list-box">
                                <input class="form-check-input" type="radio" name="brand" id="flexRadioDefault${brand.id}" data-brand-id="${brand.id}">
                                <label class="form-check-label" for="flexRadioDefault${brand.id}">
                                    <span class="name">${brand.name}</span>
                                </label>
                            </div>
                        </li>
                    `;
                });

                // Thêm danh sách thương hiệu 
                $('.brandlisst').html(brandListHTML);
            }
        },
        error: function(xhr, status, error) {
            let errorMessage = "Không thể tải dữ liệu thương hiệu";
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }
            showError(errorMessage);  
        }
    });
}

// Hàm khởi tạo cho kích thước quần áo
function initClothesSize() {
    $.ajax({
        url: `http://localhost:8080/attribute/find-value/3`,
        type: "GET", 
        contentType: "application/json",  
        success: function(response) {
            if (response.data) {
                let sizes = response.data; 
                let htmlContent = '';

                sizes.forEach(function(size) {
                    htmlContent += `
                        <li>
                            <div class="form-check ps-0 m-0 category-list-box">
                                <input class="checkbox_animated attribute-checkbox" type="checkbox" 
                                       id="size_clothes_${size.id}" 
                                       data-attribute-id="3" 
                                       data-value-id="${size.id}" 
                                       data-name="${size.name}">
                                <label class="form-check-label" for="size_clothes_${size.id}">
                                    <span class="name">${size.name}</span>                                                        
                                </label>
                            </div>
                        </li>
                    `;
                });

                $('#sizeClothes').html(htmlContent);
            }
        },
        error: function(xhr, status, error) {
            let errorMessage = "Không thể tải kích thước quần áo";
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }
            showError(errorMessage);  
        }
    });
}

// Hàm khởi tạo cho kích thước giày
function initShoesSize() {
    $.ajax({
        url: `http://localhost:8080/attribute/find-value/6`,
        type: "GET", 
        contentType: "application/json",  
        success: function(response) {
            if (response.data) {
                let sizes = response.data; 
                let htmlContent = '';

                sizes.forEach(function(size) {
                    htmlContent += `
                        <li>
                            <div class="form-check ps-0 m-0 category-list-box">
                                <input class="checkbox_animated attribute-checkbox" type="checkbox" 
                                       id="size_shoes_${size.id}" 
                                       data-attribute-id="6" 
                                       data-value-id="${size.id}" 
                                       data-name="${size.name}">
                                <label class="form-check-label" for="size_shoes_${size.id}">
                                    <span class="name">${size.name}</span>                                                        
                                </label>
                            </div>
                        </li>
                    `;
                });

                $('#sizeShoes').html(htmlContent);
            }
        },
        error: function(xhr, status, error) {
            let errorMessage = "Không thể tải kích thước giày";
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }
            showError(errorMessage);  
        }
    });
}

// Hàm khởi tạo cho màu sắc
function initColors() {
    $.ajax({
        url: `http://localhost:8080/attribute/find-value/2`,
        type: "GET", 
        contentType: "application/json",  
        success: function(response) {
            if (response.data) {
                let colors = response.data; 
                let htmlContent = '';

                colors.forEach(function(color) {
                    htmlContent += `
                        <li>
                            <div class="form-check ps-0 m-0 category-list-box">
                                <input class="checkbox_animated attribute-checkbox" type="checkbox" 
                                       id="color_${color.id}" 
                                       data-attribute-id="2" 
                                       data-value-id="${color.id}" 
                                       data-name="${color.name}">
                                <label class="form-check-label" for="color_${color.id}">
                                    <span class="name">${color.name}</span>                                                        
                                </label>
                            </div>
                        </li>
                    `;
                });

                $('#colorsearch').html(htmlContent);
            }
        },
        error: function(xhr, status, error) {
            let errorMessage = "Không thể tải màu sắc";
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }
            showError(errorMessage);  
        }
    });
}

// Xử lý sự kiện thay đổi radio button thương hiệu
$(document).on('change', '.brandlisst input[type="radio"]', function() {
    const brandId = $(this).data('brand-id');
    selectedBrandId = brandId === "null" ? null : brandId;
    
    // Gọi API tìm kiếm với thương hiệu đã chọn và các thuộc tính hiện tại
    fetchDataApi(selectedBrandId, selectedAttributes.length > 0 ? selectedAttributes : null);
});

// Xử lý sự kiện thay đổi checkbox thuộc tính (kích thước, màu sắc)
$(document).on('change', '.attribute-checkbox', function() {
    const valueId = $(this).data('value-id');
    const isChecked = $(this).is(':checked');
    
    if (isChecked) {
        // Thêm vào mảng nếu chưa có
        if (!selectedAttributes.includes(valueId.toString())) {
            selectedAttributes.push(valueId.toString());
        }
    } else {
        // Xóa khỏi mảng nếu có
        const index = selectedAttributes.indexOf(valueId.toString());
        if (index !== -1) {
            selectedAttributes.splice(index, 1);
        }
    }
    
    console.log("Selected attributes:", selectedAttributes);
    
    // Gọi API tìm kiếm với thương hiệu và thuộc tính đã chọn
    fetchDataApi(selectedBrandId, selectedAttributes.length > 0 ? selectedAttributes : null);
});

});

function renderProducts(products) {
    var productListHtml = '';

    products.forEach(function(product) {
        // Tìm hình ảnh có isPrimary = 1
        var primaryImage = product.productImages.find(image => image.isPrimary === 1);
        var imageUrl = primaryImage ? primaryImage.imageUrl : ''; // Nếu không tìm thấy, để trống

        var productHtml = `
            <div class="col">
                <div class="product-box-3 h-100 wow fadeInUp" style="padding: 0px !important;">

                    <div class="product-header" >
                        <div class="product-image" style="padding: 0px !important;">
                            <a href="product-left-thumbnail.html?id=${product.productId}">
                                <!-- Hiển thị hình ảnh chính -->
                                    <img src="${imageUrl}" class="img-fluid blur-up lazyload full-screen-image" alt="" style="object-fit: cover; height:180px;">
                            </a>


                            <ul class="product-option">
                                <li data-bs-toggle="tooltip" data-bs-placement="top" title="Xem nhanh">
                                    <a href="javascript:void(0)" data-bs-toggle="modal" data-bs-target="#view">
                                        <i data-feather="eye"></i>
                                    </a>
                                </li>
                                <li data-bs-toggle="tooltip" data-bs-placement="top" title="Yêu thích">
                                    <a href="compare.html">
                                             <i data-feather="heart"></i>
                                    </a>
                                </li>

                            </ul>
                        </div>
                    </div>
                    <div class="product-footer"  style="padding: 10px !important;">
                        <div class="product-detail">
                            <span class="span-name">${product.brandName || ''}</span>

                            <a href="product-left-thumbnail.html?id=${product.productId}">
                                <h5 class="name">${product.productName}</h5>
                            </a>
                            <h5 class="price">
                                <span class="theme-color">${product.productPrice.toLocaleString()}đ</span>

                            </h5>
                            <div class="product-rating mt-2" style="display: flex; justify-content: space-between; align-items: center;">
                                <ul class="rating">
                                    <li><i data-feather="star" class="fill"></i></li>
                                     <li><i data-feather="star" class="fill"></i></li>
                                      <li><i data-feather="star" class="fill"></i></li>
                                        <li><i data-feather="star" class="fill"></i></li>
                                        <li><i data-feather="star" class="fill"></i></li>
                                    <span>(${product.productTotalRating})</span>
                                </ul>
                                 <ul class="rating">
                         </ul>
                            </div>
                            <div class="add-to-cart-box bg-white">

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;

        productListHtml += productHtml;
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






