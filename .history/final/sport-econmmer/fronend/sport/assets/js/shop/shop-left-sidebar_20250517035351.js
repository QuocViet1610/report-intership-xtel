$(document).ready(function () {
    // Khởi tạo biến phân trang
    let currentPage = 0;
    let totalPages = 0;
    let pageSize = 10;
    
    // Lấy giá trị id từ URL
    function getUrlParameter(name) {
        var url = new URL(window.location.href);
        var params = new URLSearchParams(url.search);
        return params.get(name);
    }

    // Lấy id từ URL
    var id = getUrlParameter('id');
    var search = getUrlParameter('search');
    
    if (search) {
        // Sử dụng querySelector để lấy ô input bên trong .search-box và gán giá trị tìm kiếm
        document.querySelector('.search-box input').value = search;
    }
    
    var requestDataBrand = {
        searchText: "",
    };

    // Thương hiệu
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

    // Gọi lần đầu để tải dữ liệu
    fetchDataApi(null, null, null, 'productId.desc', 0);

    function fetchDataApi(brandId, attributeArray, genderId, sort, page) {
        // Lấy toàn bộ query string từ URL hiện tại
const queryString = window.location.search;

// Tạo đối tượng URLSearchParams để xử lý query string
const urlParams = new URLSearchParams(queryString);

// Lấy giá trị của tham số 'gender'
const gender = urlParams.get('gender') || genderId;
console.log("gender")
console.log(gender)
        // Kiểm tra nếu attributeArray có giá trị và chuẩn bị nó trong requestData
        var requestData = {
            searchText: search,
            brandId: brandId,
            genderId: gender,
            fullParentId: id,
            categorySearch: parseInt(id),
            attributeSearch: attributeArray || [],
            productIsActive: 1
        };
        
        var idCate = id;
        var sortParam = sort || 'productId.desc';
        var currentPageParam = page !== undefined ? page : currentPage;

        // Gửi yêu cầu AJAX với các tham số đã được cập nhật
        $.ajax({
            url: `http://localhost:8080/product/search?page=${currentPageParam}&size=${pageSize}&sort=${sortParam}`,
            type: "POST", 
            contentType: "application/json",  
            data: JSON.stringify(requestData),  
            success: function(response) {
                if (response.data) {
                    // Lưu tổng số trang
                    totalPages = response.data.totalPages;
                    currentPage = response.data.number;
                    
                    // Hiển thị sản phẩm
                    if (response.data.content) {
                        renderProducts(response.data.content, idCate);
                    }
                    
                    // Hiển thị phân trang
                    renderPagination(currentPage, totalPages);
                }
            },
            error: function(xhr, status, error) {
                let errorMessage = "Không thể tải dữ liệu sản phẩm";
                if (xhr.responseJSON && xhr.responseJSON.message) {
                    errorMessage = xhr.responseJSON.message;
                }
                console.log(errorMessage);  
            }
        }); 
    }

    // Hàm hiển thị phân trang
    function renderPagination(currentPage, totalPages) {
        let paginationHtml = '';
        
        // Nút Previous
        paginationHtml += `
            <li class="page-item ${currentPage === 0 ? 'disabled' : ''}">
                <a class="page-link" href="javascript:void(0)" data-page="${currentPage - 1}" ${currentPage === 0 ? 'tabindex="-1"' : ''}>
                    <i class="fa-solid fa-angles-left"></i>
                </a>
            </li>
        `;
        
        // Xác định số trang hiển thị
        let startPage = Math.max(0, currentPage - 1);
        let endPage = Math.min(totalPages - 1, currentPage + 1);
        
        // Đảm bảo luôn hiển thị ít nhất 3 trang nếu có thể
        if (endPage - startPage < 2) {
            if (startPage === 0) {
                endPage = Math.min(2, totalPages - 1);
            } else if (endPage === totalPages - 1) {
                startPage = Math.max(0, totalPages - 3);
            }
        }
        
        // Trang đầu tiên
        if (startPage > 0) {
            paginationHtml += `
                <li class="page-item">
                    <a class="page-link" href="javascript:void(0)" data-page="0">1</a>
                </li>
            `;
            
            if (startPage > 1) {
                paginationHtml += `
                    <li class="page-item disabled">
                        <a class="page-link" href="javascript:void(0)">...</a>
                    </li>
                `;
            }
        }
        
        // Các trang giữa
        for (let i = startPage; i <= endPage; i++) {
            paginationHtml += `
                <li class="page-item ${i === currentPage ? 'active' : ''}">
                    <a class="page-link" href="javascript:void(0)" data-page="${i}">${i + 1}</a>
                </li>
            `;
        }
        
        // Trang cuối cùng
        if (endPage < totalPages - 1) {
            if (endPage < totalPages - 2) {
                paginationHtml += `
                    <li class="page-item disabled">
                        <a class="page-link" href="javascript:void(0)">...</a>
                    </li>
                `;
            }
            
            paginationHtml += `
                <li class="page-item">
                    <a class="page-link" href="javascript:void(0)" data-page="${totalPages - 1}">${totalPages}</a>
                </li>
            `;
        }
        
        // Nút Next
        paginationHtml += `
            <li class="page-item ${currentPage === totalPages - 1 ? 'disabled' : ''}">
                <a class="page-link" href="javascript:void(0)" data-page="${currentPage + 1}" ${currentPage === totalPages - 1 ? 'tabindex="-1"' : ''}>
                    <i class="fa-solid fa-angles-right"></i>
                </a>
            </li>
        `;
        
        // Thêm phân trang vào DOM
        $('.custom-pagination .pagination').html(paginationHtml);
        
        // Thêm sự kiện click cho các nút phân trang
        $('.custom-pagination .pagination .page-link').on('click', function(e) {
            e.preventDefault();
            
            // Kiểm tra nếu nút không bị disabled
            if (!$(this).parent().hasClass('disabled')) {
                const pageToGo = parseInt($(this).data('page'));
                
                // Gọi lại hàm fetchDataApi với trang mới
                fetchDataApi(brand, selectedItems, object, sort, pageToGo);
                
                // Cuộn lên đầu danh sách sản phẩm
                $('html, body').animate({
                    scrollTop: $('#product-list').offset().top - 100
                }, 500);
            }
        });
    }

    var brand = null;
    var object = null;
    let selectedItems = [];
    var sort = null;
    var searchProduct = null;
    
    $('.brandlisst').on('change', 'input[type="radio"]', function() {
        // Lấy brandId của radio button được chọn
        const selectedBrandId = $(this).data('brand-id');
        brand = selectedBrandId;
        // Gọi hàm fetchDataApi với brandId đã chọn và reset về trang 0
        fetchDataApi(brand, selectedItems, object, sort, 0);  
    });

    $('.objectlist').on('change', 'input[type="radio"]', function() {
        // Lấy brandId của radio button được chọn
        const selectedObjectd = $(this).data('object-id');
        object = selectedObjectd;
        // Gọi hàm fetchDataApi với brandId đã chọn và reset về trang 0
        fetchDataApi(brand, selectedItems, object, sort, 0);  
    });

    $.ajax({
        url: `http://localhost:8080/attribute/find-value/3`,
        type: "GET", 
        contentType: "application/json",  
        success: function(response) {
            // Giả sử dữ liệu trả về là một mảng các kích thước (ví dụ: ["S", "XS", "M", "L"])
            let sizes = response.data; 
            let htmlContent = '';

            // Duyệt qua mảng sizes và tạo HTML cho mỗi kích thước
            sizes.forEach(function(size) {
                htmlContent += `
                    <li>
                        <div class="form-check ps-0 m-0 category-list-box">
                            <input class="checkbox_animated" type="checkbox" id="${size.id}">
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

    $.ajax({
        url: `http://localhost:8080/attribute/find-value/6`,
        type: "GET", 
        contentType: "application/json",  
        success: function(response) {
            // Giả sử dữ liệu trả về là một mảng các kích thước (ví dụ: ["S", "XS", "M", "L"])
            let sizes = response.data; 
            let htmlContent = '';

            // Duyệt qua mảng sizes và tạo HTML cho mỗi kích thước
            sizes.forEach(function(size) {
                htmlContent += `
                    <li>
                        <div class="form-check ps-0 m-0 category-list-box">
                            <input class="checkbox_animated" type="checkbox" id="${size.id}">
                            <label class="form-check-label" for="flexCheckDefault${size.name}">
                                <span class="name">${size.name}</span>                                                        
                            </label>
                        </div>
                    </li>
                `;
            });

            // Render HTML vào phần tử có id 'sizeClothes'
            $('#sizeShoes').html(htmlContent);
        },
        error: function(xhr, status, error) {
            let errorMessage = "Lỗi";
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }
            showError(errorMessage);  
        }
    });

    $.ajax({
        url: `http://localhost:8080/attribute/find-value/2`,
        type: "GET", 
        contentType: "application/json",  
        success: function(response) {
            // Giả sử dữ liệu trả về là một mảng các kích thước (ví dụ: ["S", "XS", "M", "L"])
            let sizes = response.data; 
            let htmlContent = '';

            // Duyệt qua mảng sizes và tạo HTML cho mỗi kích thước
            sizes.forEach(function(size) {
                htmlContent += `
                    <li>
                        <div class="form-check ps-0 m-0 category-list-box">
                            <input class="checkbox_animated" type="checkbox" id="${size.id}">
                            <label class="form-check-label" for="flexCheckDefault${size.name}">
                                <span class="name">${size.name}</span>                                                        
                            </label>
                        </div>
                    </li>
                `;
            });

            // Render HTML vào phần tử có id 'sizeClothes'
            $('#colorsearch').html(htmlContent);
        },
        error: function(xhr, status, error) {
            let errorMessage = "Lỗi";
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }
            showError(errorMessage);  
        }
    });

    // Lắng nghe sự kiện 'change' khi người dùng chọn hoặc bỏ chọn checkbox
    $('#sizeClothes, #sizeShoes, #colorsearch').on('change', 'input[type="checkbox"]', function() {
        let checkboxId = String($(this).attr('id'));  
        
        if ($(this).is(':checked')) {
            // Nếu checkbox được tích, thêm vào mảng
            selectedItems.push(checkboxId);
        } else {
            // Nếu checkbox không được tích, loại bỏ khỏi mảng
            selectedItems = selectedItems.filter(item => item !== checkboxId);
        }

        console.log("Selected items: " + selectedItems);
        fetchDataApi(brand, selectedItems, object, sort, 0); // Reset về trang 0 khi thay đổi lọc
    });

    $('.js-range-slider').on('change', function() {
        let value = $(this).val();  // Lấy giá trị hiện tại của range slider
        console.log("Giá trị range slider: " + value);
    });

    document.getElementById('newest').addEventListener('click', function() {
        sort = 'productId.desc';
        fetchDataApi(brand, selectedItems, object, sort, 0); // Reset về trang 0 khi thay đổi sắp xếp
    });

    document.getElementById('low').addEventListener('click', function() {
        sort = 'productPrice.asc';
        fetchDataApi(brand, selectedItems, object, sort, 0); // Reset về trang 0 khi thay đổi sắp xếp
    });

    document.getElementById('high').addEventListener('click', function() {
        sort = 'productPrice.desc';
        fetchDataApi(brand, selectedItems, object, sort, 0); // Reset về trang 0 khi thay đổi sắp xếp
    });

    document.getElementById('aToz').addEventListener('click', function() {
        sort = 'productName.asc';
        fetchDataApi(brand, selectedItems, object, sort, 0); // Reset về trang 0 khi thay đổi sắp xếp
    });

    document.getElementById('zToa').addEventListener('click', function() {
        sort = 'productName.desc';
        fetchDataApi(brand, selectedItems, object, sort, 0); // Reset về trang 0 khi thay đổi sắp xếp
    });

    $.ajax({
        url: `http://localhost:8080/category/${id}`,
        type: "GET", 
        contentType: "application/json",  
        success: function(response) {
            var name = response.data.name; // Lấy name từ dữ liệu nhận được
            $('#heading-2').text(name); // Gán giá trị name vào phần tử h2
        },
        error: function(xhr, status, error) {
            let errorMessage = "Lỗi";
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }
            console.log(errorMessage);  
        }
    });
});

function renderProducts(products, id) {
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
                            <a href="product-left-thumbnail.html?id=${product.productId}&cateId=${id}">
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
    <a href="javascript:void(0)" 
       class="notifi-wishlist"
       data-id="${product.productId}"
       data-name="${product.productName}"
       data-image="${imageUrl}"
       data-price="${product.productPrice.toLocaleString()}đ"
       data-article="product-left-thumbnail.html?id=${product.productId}&cateId=${id}">
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
                                      ${renderStars(product.productTotalRating)}
                                    <span>(${product.productTotalRating})</span>
                                </ul>
                                 <ul class="rating">
                         </ul>
                             <span>Đã bán: ${product.productTotalSold}</span>
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

function renderStars(rating) {
    let fullStars = Math.floor(rating);  // Lấy số sao đầy (làm tròn xuống)
    let halfStar = (rating % 1 >= 0.5) ? 1 : 0;  // Kiểm tra sao nửa
    let emptyStars = 5 - fullStars - halfStar;  // Số sao rỗng

    let starsHtml = '';

    // Render sao đầy
    for (let i = 0; i < fullStars; i++) {
        starsHtml += '<li><i data-feather="star" class="fill"></i></li>';
    }

    // Render sao nửa
    if (halfStar) {
        starsHtml += '<li><i data-feather="star" class="half-fill"></i></li>';
    }

    // Render sao rỗng
    for (let i = 0; i < emptyStars; i++) {
        starsHtml += '<li><i data-feather="star"></i></li>';
    }

    return starsHtml;
}

$(document).on('click', '.notifi-wishlist', function (e) {
    e.preventDefault();

    const $this = $(this);

    // Lấy dữ liệu từ data-* hoặc DOM liền kề
    let productId = $this.data('id');
    let name = $this.data('name');
    let image = $this.data('image');
    let price = $this.data('price');
    let article = $this.data('article');

    const productBox = $this.closest('.product-box-3');
    if (!name) name = productBox.find('.name').text().trim();
    if (!image) image = productBox.find('img').attr('src');
    if (!price) price = productBox.find('.price .theme-color').text().trim();
    if (!productId) productId = productBox.find('a').attr('href')?.split('id=')[1]?.split('&')[0];
    if (!article) article = `product-left-thumbnail.html?id=${productId}`;

    const favoriteProduct = {
        productId,
        name,
        image,
        price,
        article
    };

    // Lấy danh sách hiện tại từ localStorage
    let favorites = JSON.parse(localStorage.getItem('favorites')) || [];

    // Kiểm tra sản phẩm đã tồn tại
    const isExist = favorites.some(item => item.productId == productId);

    if (isExist) {
        showError(`Sản phẩm "${name}" đã có trong danh sách yêu thích.`);
        return; // Không thêm lại
    }

    // Thêm vào danh sách
    favorites.push(favoriteProduct);
    localStorage.setItem('favorites', JSON.stringify(favorites));

    showSuccess(`Đã thêm vào yêu thích: ${name}`);
});
