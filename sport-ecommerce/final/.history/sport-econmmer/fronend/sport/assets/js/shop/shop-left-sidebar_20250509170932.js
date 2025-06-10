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


    var requestDataBrand = {
        searchText: "",
    };


//thuong hieu
$.ajax({
    url: "http://localhost:8080/brand/search?page=0&size=20&sort=id.asc", 
    type: "POST", 
    contentType: "application/json",  
    data: JSON.stringify(requestDataBrand),  
    success: function(response) {
        if (response.data && response.data.content) {
            let brandList = response.data.content;
            
            // Duyệt qua danh sách các thương hiệu và tạo các radio buttons
let brandListHTML = `
    <li>
        <div class="form-check ps-0 m-0 category-list-box">
            <input class="form-check-input" type="radio" name="brand" id="flexRadioDefaultAll" data-brand-id="null">
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

// Thêm danh sách thương hiệu vào phần tử ul
$('.brandlisst').html(brandListHTML);

        }
    },
    error: function(xhr, status, error) {
        let errorMessage = "Lịch sử giao hàng đang trống";
        if (xhr.responseJSON && xhr.responseJSON.message) {
            errorMessage = xhr.responseJSON.message;
        }
        showError(errorMessage);  
    }
});

fetchDataApi(null,null);

function fetchDataApi(brandId, attributeArray) {
    // Kiểm tra nếu attributeArray có giá trị và chuẩn bị nó trong requestData
    var requestData = {
        searchText: "",
        brandId: brandId,
        fullParentId: id, // id là biến toàn cục hoặc bạn cần gán giá trị cho nó
        categorySearch: parseInt(id), // id là biến toàn cục hoặc bạn cần gán giá trị cho nó
        attributeSearch: attributeArray || [] // Nếu attributeArray có giá trị, sử dụng nó; nếu không, sử dụng mảng rỗng
    };

    // Gửi yêu cầu AJAX với các tham số đã được cập nhật
    $.ajax({
        url: "http://localhost:8080/product/search?page=0&size=10", 
        type: "POST", 
        contentType: "application/json",  
        data: JSON.stringify(requestData),  
        success: function(response) {
            if (response.data && response.data.content) {
                console.log(response.data.content); 
                renderProducts(response.data.content); // Hiển thị kết quả tìm kiếm
            }
        },
        error: function(xhr, status, error) {
            let errorMessage = "Lịch sử giao hàng đang trống";
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }
            showError(errorMessage);  
        }
    }); 
}



$('.brandlisst').on('change', 'input[type="radio"]', function() {
    // Lấy brandId của radio button được chọn
    const selectedBrandId = $(this).data('brand-id');

    // Gọi hàm fetchDataApi với brandId đã chọn
    fetchDataApi(selectedBrandId ? selectedBrandId : null);  
});


$.ajax({
    url: `http://localhost:8080/attribute/find-value/3`,
    type: "GET", 
    contentType: "application/json",  
    success: function(response) {
        console.log(response.data); // Kiểm tra dữ liệu nhận được từ API

        // Giả sử dữ liệu trả về là một mảng các kích thước (ví dụ: ["S", "XS", "M", "L"])
        let sizes = response.data; 
        let htmlContent = '';

        // Duyệt qua mảng sizes và tạo HTML cho mỗi kích thước
        sizes.forEach(function(size) {
            htmlContent += `
                <li>
                    <div class="form-check ps-0 m-0 category-list-box">
                        <input class="checkbox_animated" type="checkbox" id="flexCheckDefault${size.id}">
                        <label class="form-check-label" for="flexCheckDefault${size.name}">
                            <span class="name">${size.name}</span>                                                        
                        </label>
                    </div>
                </li>
            `;
        });

        // Render HTML vào phần tử có id 'sizeClothes'
        $('#sizeClothes').html(htmlContent);
    },
    error: function(xhr, status, error) {
        let errorMessage = "Lỗi";
        if (xhr.responseJSON && xhr.responseJSON.message) {
            errorMessage = xhr.responseJSON.message;
        }
        showError(errorMessage);  
    }
});

    
// Hàm gọi API và tạo danh sách kích thước giày
function loadSizeShoes() {
    $.ajax({
        url: `http://localhost:8080/attribute/find-value/6`,
        type: "GET", 
        contentType: "application/json",  
        success: function(response) {
            console.log(response.data);
            let sizes = response.data; 
            let htmlContent = '';

            sizes.forEach(function(size) {
                htmlContent += `
                    <li>
                        <div class="form-check ps-0 m-0 category-list-box">
                            <input class="checkbox_animated attribute-checkbox" type="checkbox" 
                                  id="size_${size.id}" 
                                  data-attribute-id="6" 
                                  data-value-id="${size.id}" 
                                  data-name="${size.name}">
                            <label class="form-check-label" for="size_${size.id}">
                                <span class="name">${size.name}</span>                                                        
                            </label>
                        </div>
                    </li>
                `;
            });

            $('#sizeShoes').html(htmlContent);
            // Sau khi render HTML, gắn lại sự kiện
            attachCheckboxEvents();
        },
        error: function(xhr, status, error) {
            let errorMessage = "Lỗi";
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }
            showError(errorMessage);  
        }
    });
}

// Hàm gọi API và tạo danh sách màu sắc
function loadColors() {
    $.ajax({
        url: `http://localhost:8080/attribute/find-value/2`,
        type: "GET", 
        contentType: "application/json",  
        success: function(response) {
            console.log(response.data);
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
            // Sau khi render HTML, gắn lại sự kiện
            attachCheckboxEvents();
        },
        error: function(xhr, status, error) {
            let errorMessage = "Lỗi";
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }
            showError(errorMessage);  
        }
    });
}

// Hàm để gắn sự kiện cho các checkbox sau khi chúng được tạo động
function attachCheckboxEvents() {
    // Sử dụng event delegation để bắt sự kiện từ các phần tử được tạo động
    $(document).off('change', '.attribute-checkbox').on('change', '.attribute-checkbox', function() {
        // Lấy thông tin từ data attributes
        let checkboxId = $(this).attr('id');
        let attributeId = $(this).data('attribute-id');
        let valueId = $(this).data('value-id');
        let valueName = $(this).data('name');
        let isChecked = $(this).is(':checked');
        
        console.log(`Checkbox: ${checkboxId}, Attribute: ${attributeId}, Value: ${valueId}, Name: ${valueName}, Checked: ${isChecked}`);
        
        // Thêm logic tìm kiếm sản phẩm tại đây
        if (isChecked) {
            // Thêm filter
            addFilter(attributeId, valueId);
        } else {
            // Xóa filter
            removeFilter(attributeId, valueId);
        }
    });
}

// Mảng lưu các thuộc tính đã chọn để lọc sản phẩm
let selectedFilters = [];

// Hàm thêm filter
function addFilter(attributeId, valueId) {
    selectedFilters.push(valueId);
    applyFilters();
}

// Hàm xóa filter
function removeFilter(attributeId, valueId) {
    // Tìm và xóa valueId khỏi mảng selectedFilters
    let index = selectedFilters.indexOf(valueId);
    if (index !== -1) {
        selectedFilters.splice(index, 1);
    }
    applyFilters();
}

// Hàm áp dụng filters để tìm kiếm sản phẩm
function applyFilters() {
    console.log("Applying filters with values:", selectedFilters);
    
    // Gọi API tìm kiếm với các filter đã chọn
    // Ví dụ:
    $.ajax({
        url: 'http://localhost:8080/product/search',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            searchText: '',
            brandId: null,
            fullParentId: null,
            categorySearch: null,
            attributeSearch: selectedFilters.length > 0 ? selectedFilters : null
        }),
        success: function(response) {
            // Hiển thị kết quả tìm kiếm
            console.log("Search results:", response);
            // Cập nhật UI hiển thị sản phẩm ở đây
        },
        error: function(xhr, status, error) {
            console.error("Search error:", error);
        }
    });
}

// Khởi tạo khi trang tải xong
$(document).ready(function() {
    // Tải dữ liệu kích thước giày và màu sắc
    loadSizeShoes();
    loadColors();
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






