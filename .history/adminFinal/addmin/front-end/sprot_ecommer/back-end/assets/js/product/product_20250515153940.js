let allCategories = [];
$(document).ready(function () {
    function isTokenExpired(token) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1])); // Decode phần payload của JWT
            const exp = payload.exp * 1000; // Chuyển từ giây sang milliseconds
            return Date.now() >= exp; // Kiểm tra nếu token đã hết hạn
        } catch (e) {
            console.error("Token không hợp lệ:", e);
            return true; // Mặc định là hết hạn nếu có lỗi
        }
    }

    let token = localStorage.getItem("authToken");

    if (!token) {
        showError("Bạn chưa đăng nhập!");
        return;
    } else if (isTokenExpired(token)) {
        showError("Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại!");
        localStorage.removeItem("authToken"); // Xóa token cũ
    }

    let currentPage = 1; // ✅ Đảm bảo luôn bắt đầu từ trang 1
    let pageSize = 10;
    let totalItems = 25;

    let searchRequest = {
        searchText: "", // Chuỗi tìm kiếm trống
        brandId: null,
        categorySearch: null,
        genderId: null,
        productIsActive: 1,
        fullParentId: null
    };

    let typingTimer;
    const typingDelay = 500;

    // ✅ Xử lý sự kiện nhập tìm kiếm (Live Search)
    $("#searchInput").on("input", function () {
        clearTimeout(typingTimer);
        let searchText = $(this).val().trim();
        searchRequest.searchText = searchText;

        typingTimer = setTimeout(() => {
            currentPage = 1; // ✅ Reset về trang đầu khi tìm kiếm
            fetchProducts();
        }, typingDelay);
    });

    function fetchProducts() {
        let token = localStorage.getItem("authToken");

        $.ajax({
            url: `http://localhost:8080/product/search?page=${currentPage - 1}&size=${pageSize}`,
            method: "POST",
            dataType: "json",
            data: JSON.stringify(searchRequest),
            contentType: "application/json",
            headers: {
                "Authorization": "Bearer " + token,
                "Accept": "application/json"
            },
            success: function (response) {
                if (response.code === "200") {
                    let products = response.data.content.map(item => new Product(item));
                    renderProductTable(products);
                    totalItems = response.data.totalElements;
                    updatePagination();
                } else {
                    console.warn("Lỗi API:", response.message);
                }
            },
            error: function (xhr) {
                let errorMessage = xhr.responseJSON && xhr.responseJSON.message
                    ? xhr.responseJSON.message
                    : "Có lỗi xảy ra! Mã lỗi: " + xhr.status;
                showError(errorMessage);
            }
        });
    }

$("input[name='status']").on("change", function() {
    let val = $(this).val();
    if (val === "") {
        searchRequest.productIsActive = null; // hoặc undefined để không lọc
    } else {
        searchRequest.productIsActive = Number(val); // chuyển sang số 0 hoặc 1
    }
    currentPage = 1; // reset trang về 1
    fetchProducts();
});

    // ✅ Cập nhật thông tin phân trang
    function updatePagination() {
        let totalPages = Math.ceil(totalItems / pageSize);
        let startItem = (currentPage - 1) * pageSize + 1;
        let endItem = Math.min(startItem + pageSize - 1, totalItems);

        $("#paginationInfo").text(`${startItem} - ${endItem} trong ${totalItems} hàng hóa`);
        $("#currentPageInput").val(currentPage);

        // ✅ Cập nhật trạng thái nút
        $("#firstPage, #prevPage").prop("disabled", currentPage === 1);
        $("#nextPage, #lastPage").prop("disabled", currentPage >= totalPages);
    }

    // ✅ Xử lý sự kiện thay đổi trang
    $("#firstPage").click(() => { currentPage = 1; fetchProducts(); });
    $("#prevPage").click(() => { if (currentPage > 1) currentPage--; fetchProducts(); });
    $("#nextPage").click(() => { if (currentPage < Math.ceil(totalItems / pageSize)) currentPage++; fetchProducts(); });
    $("#lastPage").click(() => { currentPage = Math.ceil(totalItems / pageSize); fetchProducts(); });

    // ✅ Xử lý khi nhập trang trực tiếp
    $("#currentPageInput").change(function () {
        let newPage = parseInt($(this).val());
        if (newPage >= 1 && newPage <= Math.ceil(totalItems / pageSize)) {
            currentPage = newPage;
            fetchProducts();
        } else {
            $(this).val(currentPage);
        }
    });

    // ✅ Xử lý khi thay đổi số dòng hiển thị
    $("#pageSizeSelect").change(function () {
        pageSize = parseInt($(this).val());
        currentPage = 1; // ✅ Reset về trang đầu
        fetchProducts();
    });


    fetchProducts();


 let selectedProductId = null;
let selectedProductVariant = null;
let isDeleteVariant = false;

// 🟢 Bắt sự kiện khi bấm xóa sản phẩm (Cha)
$(document).on("click", ".delete-btn", function () {
    selectedProductId = $(this).data("id");
    selectedProductVariant = null; // Reset giá trị biến thể
    isDeleteVariant = false; // Đánh dấu là xóa sản phẩm

});

// 🔵 Bắt sự kiện khi bấm xóa biến thể (Variant)
$(document).on("click", ".delete-btn-variant", function () {
    selectedProductVariant = $(this).data("id");
    selectedProductId = null; // Reset giá trị sản phẩm
    isDeleteVariant = true; // Đánh dấu là xóa biến thể
    console.log("🟠 Xóa biến thể ID:", selectedProductVariant);
});

// 🛑 Xác nhận xóa khi nhấn "Yes"
$("#confirmDelete").off("click").on("click", function () {
    if (!isDeleteVariant && selectedProductId !== null) {
        deleteProduct(selectedProductId);
    } else if (isDeleteVariant && selectedProductVariant !== null) {
        deleteVariant(selectedProductVariant);
    } else {
        console.error("Không tìm thấy sản phẩm hoặc biến thể để xóa!");
    }
});

// 🔥 Hàm xóa sản phẩm (Cha)
function deleteProduct(productId) {
    $.ajax({
        url: `http://localhost:8080/product/${productId}`,
        type: "DELETE",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("authToken"),
            "Accept": "application/json"
        },
        success: function (response) {
            console.log("✅ Xóa sản phẩm thành công:", response);
            $("#exampleModalToggle").modal("hide");
            fetchProducts(); // Load lại danh sách
        },
        error: function (xhr) {
            let errorMessage = xhr.responseJSON && xhr.responseJSON.message
                ? xhr.responseJSON.message
                : "Có lỗi xảy ra! Mã lỗi: " + xhr.status;
            showError(errorMessage);
        }
    });
}

// 🔥 Hàm xóa biến thể (Variant)
function deleteVariant(variantId) {
    $.ajax({
        url: `http://localhost:8080/product-variant/${variantId}`,
        type: "DELETE",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("authToken"),
            "Accept": "application/json"
        },
        success: function (response) {
            console.log("✅ Xóa sản phẩm thành công:", response);
            $("#exampleModalToggle").modal("hide");
            fetchProducts(); // Load lại danh sách
        },
        error: function (xhr) {
            let errorMessage = xhr.responseJSON && xhr.responseJSON.message
                ? xhr.responseJSON.message
                : "Có lỗi xảy ra! Mã lỗi: " + xhr.status;
            showError(errorMessage);
        }
    });
}
        
        // $(document).on("click", ".delete-btn", function () {
        //     selectedProductId = $(this).data("id"); 
        //     console.log("🔴 ID sản phẩm được chọn để xóa:", selectedProductId);
        //     showDeleteFunction(selectedProductId);
        //     $("#confirmDelete").click(function () {
        //         if (selectedProductId !== null) {
        //             console.log("🗑 Sản phẩm đã bị xóa có ID:", selectedProductId);
        //             $.ajax({
        //                 url: `http://localhost:8080/product/${selectedProductId}`,
        //                 type: "DELETE",
        //                 headers: {
        //                     "Authorization": "Bearer " + localStorage.getItem("authToken"),
        //                     "Accept": "application/json"
        //                 },
        //                 success: function (response) {
        //                     console.log("✅ Xóa thành công:", response);
        //                     $("#exampleModalToggle").modal("hide");
        //                     fetchProducts(); 
        //                 },
        //                 error: function (xhr) {
        //                     let errorMessage = xhr.responseJSON && xhr.responseJSON.message
        //                         ? xhr.responseJSON.message
        //                         : "Có lỗi xảy ra! Mã lỗi: " + xhr.status;
        //                     showError(errorMessage);
        //                 }
        //             });
        //         }});
        // });

            fetchAndRenderBrands();

    // Bắt sự kiện chọn thương hiệu để lọc
    $("body").on("change", "input[name='brand']", function() {
        let selectedBrandId = $(this).val();
        if (selectedBrandId === "") {
            console.log(selectedBrandId)
            searchRequest.brandId = null;  // Không lọc
        } else {
            searchRequest.brandId = Number(selectedBrandId);
        }
        currentPage = 1; // reset trang
        fetchProducts();
    });
    
        handleCategoriesResponse();

    // Khi chọn radio category
    $("body").on("change", "input[name='categoryFilter']", function() {
        let selectedCategoryId = $(this).val();
        if (selectedCategoryId === "") {
            searchRequest.categorySearch = null;  // Không lọc
            searchRequest.fullParentId = null;
        } else {
            searchRequest.categorySearch = Number(selectedCategoryId);
            searchRequest.fullParentId = Number(selectedCategoryId);
        }
        console.log(searchRequest);
        currentPage = 1;  // Reset trang
        fetchProducts();
    });
});



class Product {
    constructor(data) {
        this.id = data.productId || null;
        this.code = data.productCode || "";
        this.name = data.productName || "";
        this.price = data.productPrice || 0;
        this.costPrice = data.productCostPrice || 0;
        this.description = data.productDescription || "Không có mô tả";
        this.isActive = data.productIsActive || false;
        this.totalSold = data.productTotalSold || 0;
        this.totalRating = data.productTotalRating || 0.0;
        this.stock = data.productStock || 0;
        this.discountPrice = data.productDiscountPrice || 0;
        this.weight = data.productWeight || 0;
        this.createdAt = data.createdAt || "";
        this.updatedAt = data.updatedAt || "";
        this.createdBy = data.createdBy || "Không xác định";
        this.updatedBy = data.updatedBy || "Không xác định";

        this.gender = {
            id: data.genderId || null,
            name: data.genderName || "Không có giới tính"
        };

        this.brand = {
            id: data.brandId || null,
            name: data.brandName || "Không có thương hiệu"
        };

        this.category = {
            id: data.categoryId || null,
            name: data.categoryName || "Không có danh mục",
            image: data.categoryImage || ""
        };

        this.variants = Array.isArray(data.productVariants) ? 
            data.productVariants.map(variant => ({
                id: variant.id || null,
                code: variant.code || "",
                price: variant.price || 0,
                costPrice: variant.costPrice || 0,
                quantity: variant.quantity || 0
            })) : [];

        this.images = Array.isArray(data.productImages) ? 
            data.productImages.sort((a, b) => (b.isPrimary || 0) - (a.isPrimary || 0))
            .map(image => ({
                id: image.id || null,
                url: image.imageUrl || "",
                isPrimary: image.isPrimary || 0
            })) 
        : [];


        this.attributes = Array.isArray(data.productAttributeValue) ? 
            data.productAttributeValue.map(attr => ({
                id: attr.attributeId || null,
                name: attr.attributeName || "",
                values: Array.isArray(attr.attributeValues) ? 
                    attr.attributeValues.map(value => value.name || "") : []
            })) : [];
    }
}

function renderPagination(totalPages, currentPage) {
    let paginationContainer = $("#pagination");
    paginationContainer.empty(); // Xóa phân trang cũ

    for (let i = 0; i < totalPages; i++) {
        let pageItem = $(`<li class="page-item ${i === currentPage ? "active" : ""}">
                            <a class="page-link" href="#" data-page="${i}">${i + 1}</a>
                          </li>`);
        paginationContainer.append(pageItem);
    }

    // Thêm sự kiện click cho từng số trang
    $(".page-link").click(function (event) {
        event.preventDefault(); // Ngăn chặn tải lại trang
        let selectedPage = $(this).data("page");
        currentPage = selectedPage; // Cập nhật biến currentPage
        fetchProducts(currentPage); // Gọi lại API với trang mới
    });
}
const formatCurrency = (value) => {
    // Định dạng số theo kiểu VN
    let formattedValue = new Intl.NumberFormat('vi-VN', { minimumFractionDigits: 0 }).format(value);

    return formattedValue.replace(/\./g, ',') + ' đ';
};

function renderProductTable(products) {
    let tbody = $(".product-group");
    tbody.empty(); // Xóa dữ liệu cũ trước khi thêm mới

    products.forEach(product => {
        let mainImage = product.images.length > 0 ? product.images[0].url : "assets/images/product/default.png";

        // Bọc toàn bộ nhóm trong `wrapper-row`
        let wrapperRow = `<tr class="wrapper-row" data-id="${product.id}">`;

        // Dòng sản phẩm chính (cha)
        wrapperRow += `
        <tr class="product-row parent-row" data-id="${product.id}">
<td>
    <input type="checkbox" class="select-product" data-id="${product.id}" />
</td>
            <td>
                <div class="table-image">
                    <img src="${mainImage}" class="img-fluid">
                </div>
            </td>
            <td>${product.code}</td>
            <td>${product.name}</td>
            <td>${product.category.name}</td>
            <td>
    `;
    
    // ✅ Nếu có biến thể, tính tổng số lượng `quantity`, nếu không thì lấy `product.stock`
    if (product.variants.length > 0) {
        let totalQuantity = product.variants.reduce((sum, variant) => sum + variant.quantity, 0);
        wrapperRow += `${totalQuantity}`;
    } else {
        wrapperRow += `${product.stock}`;
    }
    
    wrapperRow += `</td>
        <td>`;
    
    // ✅ Nếu có biến thể, tính giá trung bình, nếu không thì lấy `product.price`
    if (product.variants.length > 0) {
        let totalPrice = product.variants.reduce((sum, variant) => sum + variant.price, 0);
        wrapperRow += `${formatCurrency(totalPrice)}`;
    } else {
        wrapperRow += `${formatCurrency(product.price)}`;
    }
    
    wrapperRow += `</td>
        <td>`;
    
    // ✅ Nếu có biến thể, tính giá vốn trung bình, nếu không thì lấy `product.costPrice`
    if (product.variants.length > 0) {
        let totalCostPrice = product.variants.reduce((sum, variant) => sum + variant.costPrice, 0);
        wrapperRow += `${formatCurrency(totalCostPrice)}`;
    } else {
        wrapperRow += `${formatCurrency(product.costPrice)}`;
    }
    
    wrapperRow += `</td>
        <td>
    `;
    
    // Nếu KHÔNG có biến thể, hiển thị các nút "Xem", "Sửa", "Xóa"

        wrapperRow += `
            <ul>

            <li><a href="add-new-product.html?product-id=${product.id}"><i class="ri-pencil-line" class="edit-btn"></i></a></li>

                <li>
                    <a href="#" data-bs-toggle="modal" data-bs-target="#exampleModalToggle" 
                       data-id="${product.id}" class="delete-btn">
                        <i class="ri-delete-bin-line"></i>
                    </a>
                </li>
            </ul>
        `;
    
    
    wrapperRow += `
            </td>
        </tr>
    `;

        console.log("Số lượng biến thể:", product.variants.length);

        // Nếu sản phẩm có biến thể, thêm các dòng con
        if (product.variants.length > 0) {
            // Duyệt qua tất cả các biến thể và thêm dòng con cho mỗi biến thể
            product.variants.forEach((variant, index) => {
                wrapperRow += `
                    <tr class="product-variants child-row" data-parent="${product.id}" style="display: none; border: 1px solid #000 !important;">
                        <td colspan="12" style="border-left: 1px solid green; border-right: 1px solid green; border-bottom: none;" >
                            <div class="variant-container">
                                <table class="variant-table">
                                    <tr>
                                <td>
                                    <input type="checkbox" class="select-variant" data-parent="${product.id}" data-id="${variant.id}"/>
                                </td>
                                        <td>
                                            <div class="table-image">
                                                <img src="${mainImage}" class="img-fluid">
                                            </div>
                                        </td>
                                        <td>${variant.code}</td>
                                        <td>${product.name} - ${variant.code}</td>
                                        <td>${product.category.name}</td>
                                        <td>${variant.quantity}</td>
                                        <td>${formatCurrency(variant.price)}</td>
                                        <td>${formatCurrency(variant.costPrice)}</td>
                                        <td>
                                            <ul>
                                                <li><a href="add-new-product.html?variant-id=${variant.id}"><i class="ri-pencil-line" data-id="${variant.id}" class="edit-btn"></i></a></li>



                                            </ul>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </td>
                    </tr>
                `;
        
            //     <li>
            //     <a href="#" data-bs-toggle="modal" data-bs-target="#exampleModalToggle" 
            //        data-id="${variant.id}" class="delete-btn-variant">
            //         <i class="ri-delete-bin-line"></i>
            //     </a>
            // </li>
                // Nếu là biến thể cuối cùng, thêm border-bottom màu xanh lá cây và một tr mới
                if (index === product.variants.length - 1) {
                    wrapperRow += `
                    <tr class="product-variants child-row product-last" data-parent="${product.id}" style="display: none; border-right: 1px solid green;">
                        <td colspan="6" style="text-align: center; font-weight: bold; border-left: 1px solid green; border-bottom: none;">
                        </td>
                        <td colspan="12" style="text-align: center; font-weight: bold; border-right: 1px solid green; border-bottom: none;">
                            <div style="display: flex; justify-content: right;">
                                ${product.isActive == 0 ? '<button class="btn btn-update btn-solid">Đã ngừng kinh doanh</button>' : ''}
                            </div>
                        </td>
                    </tr>
                
                    <tr class="product-variants child-row product-last" data-parent="${product.id}" style="display: none; border-bottom: 1px solid green; border-left: 1px solid green; border-right: 1px solid green; border-top: none;">
                        <td colspan="12" style="text-align: center; font-weight: bold; border-bottom: 1px solid green; border-left: 1px solid green; border-right: 1px solid green; margin-bottom:7px !important">
                        </td>
                    </tr>
                `;
                }
            });
        }
        // Đóng nhóm bao bọc
        wrapperRow += `</tr>`;

        tbody.append(wrapperRow);
    });
}
// ckecked box
$(document).ready(function () {
    // Tích chọn tất cả checkbox khi click vào checkbox ở header
    $("#selectAll").on('change', function () {
        
        let isChecked = $(this).prop('checked');
        
        // Tích chọn hoặc bỏ chọn tất cả các checkbox sản phẩm và biến thể
        $(".select-product, .select-variant").prop('checked', isChecked);
    });
});

$(document).ready(function () {

    $("#selectAll").on('change', function () {
        let isChecked = $(this).prop('checked');
        
        // Tích chọn hoặc bỏ chọn tất cả các checkbox sản phẩm và biến thể
        $(".select-product, .select-variant").prop('checked', isChecked);

        // Kiểm tra lại để hiển thị/ẩn nút Cập nhật
        if ($(".select-product:checked").length > 0 || $(".select-variant:checked").length > 0) {
            $("#updateButton").show();
        } else {
            $("#updateButton").hide();
        }
    });

    $("#updateButtonClick").on('click', function () {
        let selectedProductIds = []; // Mảng chứa các id của sản phẩm cha được chọn
        let selectedVariantIds = []; // Mảng chứa các id của biến thể được chọn

        // Lấy id của tất cả checkbox .select-product được chọn
        $(".select-product:checked").each(function () {
            selectedProductIds.push($(this).data('id')); // Lấy giá trị của data-id
        });

        // Lấy id của tất cả checkbox .select-variant được chọn
        $(".select-variant:checked").each(function () {
            selectedVariantIds.push($(this).data('id')); // Lấy giá trị của data-id
        });

        // In các id vào console
        console.log("Selected Product IDs: ", selectedProductIds);
        console.log("Selected Variant IDs: ", selectedVariantIds);
    });


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
            console.log(brandList)

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

});
//

// hiển thị sản phẩm con 
    document.addEventListener("DOMContentLoaded", function () {

        // $(".select-product").on('change', function () {
        //     isCheckboxChanging = true; 
        //     console.log("hello");
        //     let productId = $(this).data('id');
        //     let isChecked = $(this).prop('checked');  
    
      
        //     $(`.select-variant[data-parent="${productId}"]`).prop('checked', isChecked);
    
    
        //     setTimeout(() => {
        //         isCheckboxChanging = false;
        //     }, 100);
        // });
        document.querySelector("tbody.product-group").addEventListener("click", function (event) {
            let clickedRow = event.target.closest(".parent-row");
  
            if (clickedRow) {
                console.log("Bạn đã bấm vào sản phẩm:", clickedRow);

                let productId = clickedRow.getAttribute("data-id");
                let wrapperRow = clickedRow.closest(".parent-row");                     

                let variantRows = document.querySelectorAll(`.product-variants[data-parent="${productId}"]`);
                console.log(wrapperRow)
                console.log(variantRows)
                if (variantRows.length === 0) {
                    return; 
                }

                let isVisible = [...variantRows].some(row => row.style.display === "table-row");

                if (isVisible) {
                    let isCheckboxChanging = true;
                    $(".select-product").on('change', function () {
                        isCheckboxChanging = false;
                        console.log(isCheckboxChanging)
                        let productId = $(this).data('id');  // Lấy ID của sản phẩm cha
                        let isChecked = $(this).prop('checked');  // Kiểm tra trạng thái của checkbox cha
                
                        $(`.select-variant[data-parent="${productId}"]`).prop('checked', isChecked);
                        
                    });
                    $(".select-product, .select-variant").on('change', function () {
                        // Kiểm tra nếu có ít nhất 1 checkbox .select-product hoặc .select-variant được chọn
                        if ($(".select-product:checked").length > 0 || $(".select-variant:checked").length > 0) {
                            // Hiển thị nút Cập nhật nếu có checkbox được chọn
                            $("#updateButton").show();
                        } else {
                            // Ẩn nút Cập nhật nếu không có checkbox nào được chọn
                            $("#updateButton").hide();
                        }
                    });
                
                    setTimeout(() => { 
                    if(isCheckboxChanging){
                        console.log("Ẩn biến thể...");
                        variantRows.forEach(row => row.style.display = "none");
                        wrapperRow.classList.remove("active-border");
                    }
                }, 0);
                } else {
                    let isCheckboxChanging = true;
                    $(".select-product").on('change', function () {
                        isCheckboxChanging = false;
                        console.log(isCheckboxChanging)
                        let productId = $(this).data('id');  // Lấy ID của sản phẩm cha
                        let isChecked = $(this).prop('checked');  // Kiểm tra trạng thái của checkbox cha
                
                        $(`.select-variant[data-parent="${productId}"]`).prop('checked', isChecked);
                    });
                    $(".select-product, .select-variant").on('change', function () {
                        // Kiểm tra nếu có ít nhất 1 checkbox .select-product hoặc .select-variant được chọn
                        if ($(".select-product:checked").length > 0 || $(".select-variant:checked").length > 0) {
                            // Hiển thị nút Cập nhật nếu có checkbox được chọn
                            $("#updateButton").show();
                        } else {
                            // Ẩn nút Cập nhật nếu không có checkbox nào được chọn
                            $("#updateButton").hide();
                        }
                    });
                
                    setTimeout(() => {
                        if (isCheckboxChanging) {
                            console.log("Hiển thị biến thể...");
                            variantRows.forEach(row => row.style.display = "table-row");
                            wrapperRow.classList.add("active-border");
                        }
                    }, 0);
                }
            }
        });

    });

function renderPagination(totalPages, currentPage) {
    const pagination = $("#pagination");
    pagination.empty();
    
    for (let i = 0; i < totalPages; i++) {
        pagination.append(`
            <li class="page-item ${i === currentPage ? 'active' : ''}">
                <a class="page-link" href="#" onclick="fetchProducts(${i})">${i + 1}</a>
            </li>
        `);
    }
}


function fetchAndRenderBrands() {
    let requestDataBrand = {}; // nếu có dữ liệu cần gửi thì thêm vào, nếu không thì để {}
    $.ajax({
        url: "http://localhost:8080/brand/search?page=0&size=20&sort=id.asc",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(requestDataBrand),
        success: function(response) {
            if (response.data && response.data.content) {
                let brandList = response.data.content;
                renderBrandFilter(brandList);
            }
        },
        error: function(xhr, status, error) {
            let errorMessage = "Lấy danh sách thương hiệu thất bại";
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }
            showError(errorMessage);
        }
    });
}

function renderBrandFilter(brandList) {
    let $brandFilterList = $("#brandFilterList");
    // Xóa các thương hiệu cũ (ngoại trừ Tất cả)
    $brandFilterList.find("li:not(:first)").remove();

    // Thêm radio brand vào
    brandList.forEach(brand => {
        let li = `<li>
                    <input type="radio" name="brand" value="${brand.id}"> ${brand.name}
                  </li>`;
        $brandFilterList.append(li);
    });
}

// Gọi hàm fetch danh sách thương hiệu khi load trang
function renderCategoryFilter(categories) {
    const $categoryFilterList = $("#categoryFilterList");
    // Xóa các radio cũ (ngoài cái Tất cả đầu tiên)
    $categoryFilterList.find("li:not(:first)").remove();

    let parents = [];
    let children = {};

    categories.forEach(category => {
        if (category.parentId === 0) {
            parents.push(category);
        } else {
            if (!children[category.parentId]) {
                children[category.parentId] = [];
            }
            children[category.parentId].push(category);
        }
    });

    function addChildrenRecursively(parentId, level) {
        if (children[parentId]) {
            children[parentId].forEach(child => {
                let padding = '&nbsp;&nbsp;&nbsp;&nbsp;'.repeat(level);
                let li = $(`
                    <li>
                        <input type="radio" name="categoryFilter" value="${child.id}">
                        <span style="padding-left: ${level * 16}px;">${child.name}</span>
                    </li>
                `);
                $categoryFilterList.append(li);
                addChildrenRecursively(child.id, level + 1);
            });
        }
    }

    // Render parent trước
    parents.forEach(parent => {
        let li = $(`
            <li>
                <input type="radio" name="categoryFilter" value="${parent.id}">
                <span>${parent.name}</span>
            </li>
        `);
        $categoryFilterList.append(li);

        addChildrenRecursively(parent.id, 1);
    });
}

function handleCategoriesResponse() {
    $.ajax({
        url: "http://localhost:8080/category",
        type: "GET",
        contentType: "application/json",
        headers: {
            "Authorization": "Bearer " + token,
            "Accept": "application/json"
        },
        success: function(response) {
            if(response.code == 200) {
                allCategories = response.data;  // Lưu toàn bộ dữ liệu
                renderCategoryFilter(allCategories);
            } else {
                alert('Lỗi: ' + response.message);
            }
        },
        error: function(xhr) {
            let errorMessage = "Không thể tải thể loại";
            if(xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }
            showError(errorMessage);
            $("#categoryFilterList").empty();
            $("#categoryFilterList").append('<li>Không thể tải thể loại</li>');
        }
    });
}

