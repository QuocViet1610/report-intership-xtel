function isTokenExpired(token) {
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        const exp = payload.exp * 1000;
        return Date.now() >= exp;
    } catch (e) {
        return true;
    }
}

function parseJwt(token) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
  
    return JSON.parse(jsonPayload);
  }

$(document).ready(function () {

    const token = localStorage.getItem("authToken");
    const parentId = 1; 
    const parentIdNam = 2;
    const parentIdNu = 3;
    const parentIdKid = 4;
    const parentIdAssory = 5;

    if (token) {
        const decodedToken = parseJwt(token);
        const username = decodedToken.userName;
        localStorage.setItem('username', username);
        if (username) {
            document.getElementById('username').textContent = username;  
        } else {
            document.getElementById('username').textContent = "My Account"; 
        }
        if (isTokenExpired(token)) {
            showError("Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại!");
            localStorage.removeItem("authToken");
            return;
        }
      
        $('.loginBtn').hide();
        $('.registerBtn').hide();

    
    }else{
        $('.orderHistory').hide();
        $('.myAccountBtn').hide();
    }

    const parentIds = [1, 2, 3, 4, 5]; 

    $.ajax({
        url: "http://localhost:8080/category",
        method: "GET",
        success: function (res) {
            const data = res.data;
            parentIds.forEach(parentId => {
                const level2 = data.filter(cat => cat.level === 2 && findParent(cat, parentId));
                const level3 = data.filter(cat => cat.level === 3 && findParent(cat, parentId));
                let container;
    
                // Chọn container phù hợp dựa trên parentId
                switch (parentId) {
                    case 1:
                        container = $("#category-container");
                        break;
                    case 2:
                        container = $("#category-container-nam");
                        break;
                    case 3:
                        container = $("#category-container-nu");
                        break;
                    case 4:
                        container = $("#category-container-kid");
                        break;
                    case 5:
                        container = $("#category-container-accessory");
                        break;
                }
    
                level2.forEach(level2Cat => {
                    const col = $(`
                        <div class="col-xl-3">
                            <div class="dropdown-column m-0">
                                <h5 class="dropdown-header">
                                    <a href="shop-left-sidebar.html?id=${level2Cat.id}">
                                        ${level2Cat.name}
                                    </a>
                                </h5>
                            </div>
                        </div>
                    `);
    
                    const column = col.find(".dropdown-column");
    
                    level3
                        .filter(l3 => l3.parentId == level2Cat.id)
                        .forEach(item => {
                            const link = $(`
                                <a class="dropdown-item" href="shop-left-sidebar.html?id=${item.id}">
                                    ${item.name}
                                </a>
                            `);
                            column.append(link);
                        });
    
                    container.append(col);
                });
            });
        },
        error: function (err) {
            console.error("Lỗi khi gọi API:", err);
            showError("Không thể tải danh mục. Vui lòng thử lại sau!");
        }
    });
    
    // Hàm tìm parent
    function findParent(cat, parentId) {
        if (!cat.fullParentId) return false;
        return cat.fullParentId.split(",").includes(String(parentId));
    }


    renderCartHeader();
});


function renderCartHeader() {

    let token = localStorage.getItem('authToken');
    let cart = [];

    if (!token) {
        $('#cart-button').attr('href', 'login.html'); 
    }

    if (token) {
        let decodedToken = parseJwt(token);
    
        if (isTokenExpired(token)) {
            showError("Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại!");
            localStorage.removeItem("authToken");
            return;
        }
        $('#cart-button').attr('href', 'cart.html'); 
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
                displayCartHeader(cart);
            },
            error: function(error) {
         
                console.error("Error fetching cart from API:", error);
            }
        });
    } else {
        displayCartHeader(cart);
        
    }    
  
}




function displayCartHeader(cart) {
    let token = localStorage.getItem('authToken');
    const cartList = document.querySelector('.cart-list');
    const priceBox = document.querySelector('.price-box h4');
    let totalCartPrice = 0;
    let totalProduct = 0;
    cartList.innerHTML = '';
    if (cart.length === 0) {
        const li = document.createElement('li');
        li.className = 'product-box-contain';
        li.innerHTML = `
            <div class="drop-cart text-center fw-bold py-2">
               <h4 style="margin: auto;"> Giỏ hàng trống</h4> 
            </div>
        `;
        cartList.appendChild(li);
    
        // Ẩn giá và nút thanh toán nếu cần
        if (priceBox) priceBox.innerHTML = '0đ';
    
        return;
    }
    console.log(cart)
    cart.forEach(item => {
        totalProduct += 1;
        let quantityProduct = item.quantity
        let idCart = item.id;
        item = item.productView;
        item.quantity = quantityProduct;
        let productId = item.productId;
        let mainImage = item.productImages.find(image => image.isPrimary === 1);
        let imageUrl = mainImage ? mainImage.imageUrl : 'default-image.jpg';
        let productName = item.productName;
        let productPrice = item.productPrice;
        let quantity = item.quantity;  
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

        totalCartPrice += totalPrice;

        const li = document.createElement('li');
        li.className = 'product-box-contain';
        let attributesHTML = renderProductAttributesHeader(item);
        li.innerHTML = `
            <div class="drop-cart">
                <a href="product-left-thumbnail.html?id=${productId}" class="drop-image">
                    <img src="${imageUrl}" class="blur-up lazyload" alt="${productName}">
                </a>
                <div class="drop-contain">
                    <a href="product-left-thumbnail.html?id=${productId}">
                        <h5>${productName}</h5>
                    </a>
             ${attributesHTML}  
                    <h6><span>${quantity} x</span> ${productPrice.toLocaleString('vi-VN')}đ</h6>
                       <button class="close-button close_button" data-id="${idCart}"> 
                        <i class="fa-solid fa-xmark"></i>
                    </button>
                </div>
            </div>
        `;
            // data id xoa o buttun
        cartList.appendChild(li);

        // thêm nút xoá
        li.querySelector('.close_button').addEventListener('click', function () {
            const itemId = this.getAttribute('data-id');
            
            // Xử lý xoá item ở đây
            removeFromCartHeader(itemId);
        
            // Xoá khỏi giao diện
            li.remove();
        });
    });
    document.querySelector('.badge').innerText = totalProduct;
    priceBox.textContent = `${totalCartPrice.toLocaleString('vi-VN')}đ`;
}


function renderProductAttributesHeader(product) {
    // Tạo HTML cho thông tin thuộc tính
    let attributesHTML = '';

    // Duyệt qua mảng productVariants để lấy các thuộc tính
    product.productVariants.forEach(variant => {
        let values = variant.variantAttributes.map(attribute => {
            return attribute.attributeValue.name;
        });
    
    // Nối các giá trị lại với nhau bằng dấu -
    let joinedValues = values.join(' - ');

    // Tạo HTML hiển thị
    attributesHTML += `
        <span class="text-title">${joinedValues}</span>
    `;
      
    });

    return attributesHTML; // Trả về các thuộc tính HTML để chèn vào giỏ hàng
}

function removeFromCartHeader(cartItemId) {
    let token = localStorage.getItem('authToken');
    $.ajax({
        url: `http://localhost:8080/cart/remove/${cartItemId}`,  
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
}