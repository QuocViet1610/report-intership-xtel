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
            let brandListHTML = '';
            
            brandList.forEach(function(brand) {
                brandListHTML += `
                    <li>
                        <div class="form-check ps-0 m-0 category-list-box">
                            <input class="checkbox_animated" type="checkbox" id="flexCheckDefault${brand.id}">
                            <label class="form-check-label" for="flexCheckDefault${brand.id}">
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

fetchDataApi();

function fetchDataApi(){

    var requestData = {
        searchText: "",
        fullParentId: id,
        brandId: 8,
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
        
            let errorMessage = "Lịch sử giao hàng đang trống";
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }
            showError(errorMessage);  
        }
    
    
    }); 
}




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



