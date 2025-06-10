function renderProductAttributes(product) {
    // Tạo HTML cho thông tin thuộc tính
    let attributesHTML = '';

    // Duyệt qua mảng productVariants để lấy các thuộc tính
    product.productVariants.forEach(variant => {
        variant.variantAttributes.forEach(attribute => {
            // Lấy tên thuộc tính và giá trị của thuộc tính
            let attributeName = attribute.attributeValue.attribute.name;
            let attributeValue = attribute.attributeValue.name;

            // Tạo HTML cho mỗi thuộc tính
            attributesHTML += `
                <li class="text-content">
                    <span class="text-title">${attributeName}: </span> ${attributeValue}
                </li>
            `;
        });
    });

    return attributesHTML; // Trả về các thuộc tính HTML để chèn vào giỏ hàng
}

// function calculateTotalCart() {
//     console.log("Aaa")
//     let cart = JSON.parse(localStorage.getItem('Cart')) || []; // Lấy giỏ hàng từ localStorage
//     let totalAmount = 0;

//     if (Array.isArray(cart)) {
//     // Duyệt qua từng sản phẩm trong giỏ hàng và cộng tổng tiền
//     cart.forEach(item => {
//         let productPrice = item.productPrice;
//         let quantity = item.quantityCart;
//         let totalPrice = productPrice * quantity; // Tính tổng tiền của sản phẩm

//         // Nếu có variant, sử dụng giá variant
//         if (item.productVariants && item.productVariants.length > 0) {
//             let variant = item.productVariants[0]; // Giả sử lấy variant đầu tiên
//             totalPrice = variant.price * quantity; // Tính lại tổng tiền cho variant
//         }

//         totalAmount += totalPrice; 
//     });

//     document.getElementById("provisionalPrice").textContent = totalAmount.toLocaleString() + 'đ';
//     }
// }
// $(document).ready(function () {
//     calculateTotalCart()
// });

// //
// Hàm để giải mã JWT
function parseJwt(token) {
    var base64Url = token.split('.')[1]; // Lấy phần payload
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/'); // Chuyển đổi URL-safe base64 thành chuẩn base64
    var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload); // Giải mã JSON
}

$(document).ready(function () {
    renderCart();
});

function renderCart() {
    debugger
    let token = localStorage.getItem('authToken');
    let cart = [];
    let userId;
    let decodedToken;



    if (token) {
        let decodedToken = parseJwt(token);
        let userId = decodedToken.userId; 

        userId = decodedToken.userId; 
        // Nếu token có tồn tại, gọi API để lấy giỏ hàng
      $.ajax({
        url: `http://localhost:8080/cart`, 
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json',
            },
            success: function(data) {
                cart = data.data.cartItems;
                console.log(cart);
                displayCart(cart);
            },
            error: function(error) {
                console.error("Error fetching cart from API:", error);
            }
        });
    } else {
        let cartHTML = ''; 
        cartHTML += `
        <tr class="product-box-contain"  >
           Giỏ hàng trống

        </tr>
    `;
    $('.cart-table tbody').html(cartHTML);
        // cart = JSON.parse(localStorage.getItem('Cart')) || [];
        // displayCart(cart);
    }    
  
}


function displayCart(cart) {
    console.log(cart)
    let token = localStorage.getItem('authToken');
   
    if (cart.length > 0) {
        let cartHTML = ''; 
        cart.forEach(item => {
            let id = item.id;
            if(token){
                let quantityProduct = item.quantity
                let id = item.id;
                item = item.productView;
                item.quantity = quantityProduct;
            }
            console.log(item)
            let mainImage = item.productImages.find(image => image.isPrimary === 1);
            let imageUrl = mainImage ? mainImage.imageUrl : 'default-image.jpg';
            let productName = item.productName;
            let productPrice = item.productPrice;
            let quantity = item.quantity;  // Lấy số lượng từ `quantityCart`, không phải `productStock`
            let totalPrice = productPrice * quantity;  // Tính tổng cộng cho sản phẩm chính
            let productVariantId = null;

            if (item.productVariants && item.productVariants.length > 0) {
                let variant = item.productVariants[0]; // Lấy variant đầu tiên (hoặc thay đổi logic nếu cần)
                productName = variant.name;  // Lấy tên variant
                productPrice = variant.price; // Lấy giá variant
                productVariantId = variant.id; // Lấy id của variant
                totalPrice = productPrice * quantity;  // Tính lại tổng cộng cho variant
                console.log(quantity);
            }

            let attributesHTML = renderProductAttributes(item);

            // Tạo HTML cho mỗi sản phẩm
            cartHTML += `
                <tr class="product-box-contain" data-Item-id =${id} >
                    <td class="product-detail">
                        <div class="product border-0">
                            <a href="product-left-thumbnail.html?id=${item.productId}" class="product-image">
                                <img src="${imageUrl}" class="img-fluid blur-up lazyload" alt="">
                            </a>
                            <div class="product-detail">
                                <ul>
                                    <li class="name" style="max-width: 300px; word-wrap: break-word !important; white-space: normal;">
                                        <a href="product-left-thumbnail.html" class="nameProduct">${productName}</a>
                                    </li>
                                   ${attributesHTML}  
                                </ul>
                            </div>
                        </div>
                    </td>

                    <td class="price">
                        <h4 class="table-title text-content">Giá</h4>
                        <h5>${productPrice.toLocaleString()}đ</h5>
                    </td>

                    <td class="quantity">
                        <h4 class="table-title text-content">Số Lượng</h4>
                        <div class="quantity-price">
                            <div class="cart_qty">
                                <div class="input-group">
                                    <button type="button" class="btn qty-left-minus" data-type="minus" data-field="">
                                        <i class="fa fa-minus ms-0"></i>
                                    </button>
                                    <input class="form-control input-number qty-input" type="text" name="quantity" value="${item.quantity}" data-product-id="${item.productId}" data-variant-id="${productVariantId}" readonly>
                                    <button type="button" class="btn qty-right-plus" data-type="plus" data-field="">
                                        <i class="fa fa-plus ms-0"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </td>

                    <td class="subtotal">
                        <h4 class="table-title text-content">Tổng cộng</h4>
                        <h5 class="totalProduct">${totalPrice.toLocaleString()}đ</h5>
                    </td>

                    <td class="save-remove">
                        <h4 class="table-title text-content">Hoạt động</h4>
                        <i class="fa fa-trash delete-product" aria-hidden="true" style="color:var(--theme-color); margin: auto;"></i>
                    </td>

                </tr>
            `;
        });
        // Chèn các sản phẩm vào bảng giỏ hàng
        $('.cart-table tbody').html(cartHTML);

        updateProvisionalPrice();
        $(document).on('click', '.delete-product', function () {
            if(token){
                let $row = $(this).closest('tr'); 
                let cartItemId = $row.attr('data-Item-id'); 
                $.ajax({
                    url: `http://localhost:8080/cart/remove/${cartItemId}`,  // Đường dẫn đến API của bạn
                    method: 'DELETE',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json',
                    },
                    success: function(response) {
                        showSuccess('Sản phẩm đã được xóa khỏi giỏ hàng');
               
                    },
                    error: function(xhr, status, error) {
                        console.error("Error removing product from cart:", error);
                        let errorMessage = "Có lỗi xảy ra khi xóa sản phẩm khỏi giỏ hàng.";
                        if (xhr.responseJSON && xhr.responseJSON.message) {
                            errorMessage = xhr.responseJSON.message;
                        }
                        showError(errorMessage);  
                    }
                });
                
                $row.remove();
            }else{
                 let $row = $(this).closest('tr'); 
                let productVariantId = $row.find('.qty-input').data('variant-id');  
                let productId = $row.find('.qty-input').data('product-id');

                removeFromCart(productId, productVariantId);

                $row.remove();
                calculateTotalCart() 
            }
               
                
           
                updateProvisionalPrice();

            });
    } else {
        // Nếu giỏ hàng trống
        console.log("Giỏ hàng trống.");
    }
}
    function removeFromCart(productId, productVariantId) {
        let cart = JSON.parse(localStorage.getItem('Cart')) || [];
    
        if (productVariantId) {
            cart = cart.filter(item => {
                // Nếu có variant, xóa sản phẩm với cả `productId` và `productVariantId`
                if (item.productId === productId && item.productVariants && item.productVariants.length > 0) {
                    // Xóa variant khỏi mảng productVariants nếu có
                    item.productVariants = item.productVariants.filter(variant => variant.id !== productVariantId);

                    // Nếu không còn variant nào cho sản phẩm, xóa sản phẩm khỏi giỏ hàng
                    return item.productVariants.length > 0;
                }
                item.productId !== productId;  // Nếu không có variant, chỉ xóa sản phẩm chính
            });
        } else {
            // Nếu không có `productVariantId`, chỉ xóa sản phẩm chính
            cart = cart.filter(item => item.productId !== productId);
        }
    
        // Lưu lại giỏ hàng vào localStorage
        localStorage.setItem('Cart', JSON.stringify(cart));
        console.log('Updated cart after removal:', cart);  // Kiểm tra giỏ hàng đã được cập nhật
    
    }
// Khi giảm số lượng
$(document).on('click', '.qty-left-minus', function () {
    let $input = $(this).closest('.input-group').find('.qty-input');
    let quantity = parseInt($input.val());

    if (quantity > 1) {
        quantity--;
        $input.val(quantity);  // Cập nhật giá trị trong ô input
        updateProductSubtotal($input, quantity);
    }
    updateProvisionalPrice();
});

// Khi tăng số lượng
$(document).on('click', '.qty-right-plus', function () {
    let $input = $(this).closest('.input-group').find('.qty-input');
    let quantity = parseInt($input.val());
    quantity++;
    $input.val(quantity); 
    updateProductSubtotal($input, quantity);
    updateProvisionalPrice();
});


function updateProductSubtotal($input, quantity) {
    let token = localStorage.getItem('authToken');
    let cart = [];

    // let decodedToken = parseJwt(token);
    // let userId = decodedToken.userId; 

    let $row = $input.closest('tr'); // phần tử gần nhất
    let productPrice = parseInt($row.find('.price h5').text().replace('đ', '').replace(/\./g, '').trim());

    let productVariantId = $input.data('variant-id');
    let productId = $input.data('product-id'); 

    let totalPrice = productPrice * quantity;
    // Cập nhật giá trị trong giỏ hàng
    $row.find('.totalProduct').text(totalPrice.toLocaleString() + 'đ');
    if(token){
        updateCartQuantitybackend($row, productId, productVariantId, quantity); // truyền giá trị cần thiết vào đây
    }else{
        updateCartQuantity(productId, productVariantId, quantity, totalPrice);
    }
}


function updateCartQuantitybackend($row, productId, productVariantId, quantity) {
    let token = localStorage.getItem('authToken');
    
    let cartItemId = $row.attr('data-Item-id'); 
    // Gửi PUT request để cập nhật số lượng giỏ hàng
    $.ajax({
        url: `http://localhost:8080/cart/update/${cartItemId}?quantity=${quantity}`,  // Thêm tham số quantity vào URL
        method: 'PUT',
        headers: {
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json',
        },
        success: function(response) {
            console.log('Giỏ hàng đã được cập nhật:', response);
            // Cập nhật lại tổng giỏ hàng sau khi thành công
            updateProvisionalPrice();  // Cập nhật lại tổng giỏ hàng
        },
        error: function(xhr, status, error) {
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }
            showError(errorMessage);  // Hiển thị thông báo lỗi cho người dùng
        }
    });
    
}



function updateCartQuantity(productId, productVariantId, quantity, totalPrice) {
    let cart = JSON.parse(localStorage.getItem('Cart')) || [];

    // Tìm sản phẩm có `productId` và `productVariantId` (nếu có)
    let product = cart.find(item => {
        // Kiểm tra nếu sản phẩm có variant
        if (item.productVariants && Array.isArray(item.productVariants) && item.productVariants.length > 0) {
            // Nếu có `productVariantId`, tìm variant phù hợp
            if (productVariantId) {
                return item.productId === productId && 
                       item.productVariants.some(variant => variant.id === productVariantId);
            }
            // Nếu không có `productVariantId`, chỉ tìm sản phẩm chính
            return item.productId === productId && !productVariantId;
        } else {
            // Nếu không có variant, tìm sản phẩm chính
            return item.productId === productId && !productVariantId;
        }
    });
};

function updateProvisionalPrice() {
    let totalPrice = 0;

    let totalProductElements = document.querySelectorAll('.totalProduct');

    console.log(totalProductElements);  

    if (totalProductElements.length > 0) {
  
        for (let i = 0; i < totalProductElements.length; i++) {
            let priceText = totalProductElements[i].innerText.replace('đ', '').replace(',', '');
            let price = parseFloat(priceText);
            console.log(price);
            if (!isNaN(price)) {  
                totalPrice += price; 
            }
        }

        // Cập nhật tổng vào phần tử với id "provisionalPrice"
        document.getElementById('provisionalPrice').innerText = totalPrice.toLocaleString() + "đ"; 
        document.getElementById('total').innerText = totalPrice.toLocaleString() + "đ"; 
    } else {
        console.log('Không có phần tử totalProduct trong DOM');
    }
}
