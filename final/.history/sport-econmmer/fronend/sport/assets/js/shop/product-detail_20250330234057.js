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
                $('#stock').text(totalStock || 'Không có thông tin');
                renderProductAttributes(product.productAttributeValue); 
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

});

function renderProductImages(product) {
    // Render ảnh chính (first image)
    var mainImage = product.productImages.find(image => image.isPrimary === 1); 
    if (mainImage) {
        $('#main-image').attr('src', mainImage.imageUrl);
        $('#main-image').attr('data-zoom-image', mainImage.imageUrl);
    }

    // Render ảnh phụ (sidebar images)
    var sidebarImagesHtml = '';
    product.productImages.forEach(function(image) {
        if (image.isPrimary === 0) {
            sidebarImagesHtml += `
                <div>
                    <div class="sidebar-image">
                        <img src="${image.imageUrl}" class="img-fluid blur-up lazyload" alt="">
                    </div>
                </div>
            `;
        }
    });
    $('#sidebar-images').html(sidebarImagesHtml); // Thêm ảnh phụ vào phần tử
}
// Mảng để lưu trữ các id thuộc tính đã chọn
var selectedAttributeIds = [];

function renderProductAttributes(products) {
    var attributesHtml = '';
    
    products.forEach(function(productAttributeValue) {
      
        // Tạo phần tử cho mỗi thuộc tính (Kích thước, Màu sắc, ...)
        var attributeHtml = `
            <div class="product-package" data-id="${productAttributeValue.attributeId}">
                <div class="product-title">
                    <h4>${productAttributeValue.attributeName}</h4>
                </div>
                <ul class="select-package">
                    ${productAttributeValue.attributeValues.map(function(value, index) {
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

        // Đặt lại màu sắc của tất cả các thuộc tính trong cùng một package về mặc định
        $parentPackage.find('.attribute-value').css('background-color', '').css('color', '');

        // Chuyển màu xanh cho giá trị đã chọn
        $this.css('background-color', 'green').css('color', 'white');

        var valueId = $this.data('id');  // ID của giá trị thuộc tính đã chọn
        var attributeName = $this.closest('.product-package').find('.product-title h4').text(); // Tên thuộc tính (Kích thước, màu sắc,...)

        // Kiểm tra nếu thuộc tính đó đã có trong mảng, nếu có thì thay thế, nếu chưa có thì thêm mới
        var existingIndex = selectedAttributeIds.findIndex(function(item) {
            return item.attributeName === attributeName;  // Tìm thuộc tính đã chọn trong mảng
        });

        if (existingIndex !== -1) {
            // Nếu thuộc tính đã có trong mảng, thay thế giá trị cũ
            selectedAttributeIds[existingIndex] = {
                attributeName: attributeName,
                valueId: valueId
            };
        } else {
            // Nếu chưa có trong mảng, thêm mới
            selectedAttributeIds.push({
                attributeName: attributeName,
                valueId: valueId
            });
        }

        // Hiển thị mảng id đã chọn trong console (hoặc bạn có thể gửi nó đi)
        console.log(selectedAttributeIds);
    });

    // Chọn giá trị mặc định của mỗi thuộc tính và đánh dấu là đã chọn
    products.forEach(function(productAttributeValue) {
        var firstValue = productAttributeValue.attributeValues[0];
        var $firstAttributeValue = $(`.product-package[data-id='${productAttributeValue.attributeId}'] .attribute-value[data-id='${firstValue.id}']`);

        // Đánh dấu giá trị đầu tiên là đã chọn
        $firstAttributeValue.css('background-color', 'green').css('color', 'white');

        // Thêm vào mảng selectedAttributeIds
        selectedAttributeIds.push({
            attributeName: productAttributeValue.attributeName,
            valueId: firstValue.id
        });

        // Hiển thị mảng id đã chọn trong console
        console.log(selectedAttributeIds);
    });
}


