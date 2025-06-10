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

    // hiển thị giỏ hàng
    renderCart()
});


function renderCart() {

    let token = localStorage.getItem('authToken');
    let cart = [];
    let userId;
    let decodedToken;

    if (token) {
        let decodedToken = parseJwt(token);
        let userId = decodedToken.userId; 

        userId = decodedToken.userId; 
   
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
        // Nếu không có token, lấy giỏ hàng từ localStorage

        cart = JSON.parse(localStorage.getItem('Cart')) || [];
        displayCart(cart);
    }    
  
}

function displayCart(cart) {
    let token = localStorage.getItem('authToken');
    const cartList = document.querySelector('.cart-list');
    const priceBox = document.querySelector('.price-box h4');
    let totalCartPrice = 0;
    cartList.innerHTML = '';
    cart.forEach(item => {
        let quantityProduct = item.quantity
        let id = item.id;
        item = item.productView;
        item.quantity = quantityProduct;
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


        if (item.productVariants && item.productVariants.length > 0) {
            let variant = item.productVariants[0]; // hoặc thay bằng logic chọn variant phù hợp
            variantName = variant.name;
            productPrice = variant.price;
            productVariantId = variant.id;
            totalPrice = productPrice * quantity;
            productName = variant.name;
        }

        totalCartPrice += totalPrice;

        const li = document.createElement('li');
        li.className = 'product-box-contain';

        li.innerHTML = `
            <div class="drop-cart">
                <a href="product-left-thumbnail.html?id=${id}" class="drop-image">
                    <img src="${imageUrl}" class="blur-up lazyload" alt="${productName}">
                </a>
                <div class="drop-contain">
                    <a href="product-left-thumbnail.html">
                        <h5>${productName}</h5>
                    </a>
                    ${variantName ? `<h6> ${variantName}</h6>` : ''}
                    <h6><span>${quantity} x</span> ${productPrice.toLocaleString('vi-VN')}đ</h6>
                    <button class="close-button close_button" data-id="${id}">
                        <i class="fa-solid fa-xmark"></i>
                    </button>
                </div>
            </div>
        `;

        cartList.appendChild(li);

    });

    priceBox.textContent = `${totalCartPrice.toLocaleString('vi-VN')}đ`;
}