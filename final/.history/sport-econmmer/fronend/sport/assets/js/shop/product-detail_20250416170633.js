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
                $('#stock').text(`${product.productStock}` || 'Không có thông tin');
          
                if (product.productAttributeValue && product.productAttributeValue.length > 0) {
                    renderProductAttributes(product);
                }                
                renderProductAttributes(product); 

                var rating = Math.round(product.productTotalRating); 
                $('.rating li').each(function(index) {
                    if (index < rating) {
                        $(this).find('i').addClass('fill'); 
                    } else {
                        $(this).find('i').removeClass('fill'); 
                    }

                    // renderProductAttributes(product.productAttributeValue);        
                });
                            } else {
                                console.log('Không tìm thấy sản phẩm');
                            }
                        },
                        error: function(xhr, status, error) {
                            console.error("Đã có lỗi xảy ra: " + error);
                        }
                    });
     renderProductImages(product);

                   
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

            // Sự kiện click vào ảnh con để thay đổi ảnh chính
            $('.sidebar-image img').off('click').on('click', function() { // Dùng .off() để tránh sự kiện bị thêm nhiều lần
                var selectedImageUrl = $(this).attr('data-src'); // Lấy URL ảnh con đã click
                $('#main-image').attr('src', selectedImageUrl);  // Thay đổi ảnh chính
                $('#main-image').attr('data-zoom-image', selectedImageUrl);  // Cập nhật ảnh zoom
            });
        }, 1000); // Trì hoãn 1 giây trước khi hiển thị ảnh phụ
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





