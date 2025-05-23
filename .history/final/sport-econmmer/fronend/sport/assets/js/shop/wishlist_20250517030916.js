// Hàm chính để hiển thị danh sách yêu thích
function renderWishlist() {
    // Lấy danh sách yêu thích từ localStorage
    let favorites = [];
    try {
        favorites = JSON.parse(localStorage.getItem('favorites')) || [];
    } catch (error) {
        console.error("Lỗi khi lấy danh sách yêu thích:", error);
        favorites = [];
    }
    
    // Kiểm tra nếu không có sản phẩm yêu thích
    if (!favorites || favorites.length === 0) {
        // Hiển thị thông báo nếu không có sản phẩm yêu thích
        $('.wishlist-section .row').html(`
            <div class="col-12 text-center my-5">
                <div class="empty-wishlist">
                    <img src="assets/images/empty-wishlist.svg" class="img-fluid mb-4" style="max-width: 150px; opacity: 0.5;">
                    <h3>Danh sách yêu thích trống</h3>
                    <p class="mb-4">Bạn chưa thêm sản phẩm nào vào danh sách yêu thích</p>
                    <a href="shop.html" class="btn btn-animation btn-md fw-bold">Tiếp tục mua sắm</a>
                </div>
            </div>
        `);
        return;
    }
    
    // Xây dựng HTML cho danh sách yêu thích
    let wishlistHtml = '';
    
    // Lặp qua từng sản phẩm yêu thích
    favorites.forEach((product, index) => {
        // Trích xuất productId từ URL nếu chưa có
        let productId = product.productId;
        if (!productId && product.article) {
            try {
                const urlParams = new URLSearchParams(product.article.split('?')[1]);
                productId = urlParams.get('id');
            } catch (error) {
                console.error("Lỗi khi trích xuất ID từ URL:", error);
                productId = `unknown-${index}`;
            }
        }
        
        // Định dạng URL sản phẩm
        const productUrl = product.article || `product-left-thumbnail.html?id=${productId}`;
        
        // Thêm HTML cho sản phẩm
        wishlistHtml += `
            <div class="col-xxl-2 col-lg-3 col-md-4 col-6 product-box-contain" data-product-id="${productId}">
                <div class="product-box-3 h-100">
                    <div class="product-header">
                        <div class="product-image">
                            <a href="${productUrl}">
                                <img src="${product.image}" class="img-fluid blur-up lazyload" alt="${product.name}">
                            </a>

                            <div class="product-header-top">
                                <button class="btn wishlist-button close_button" data-product-id="${productId}">
                                    <i data-feather="x"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="product-footer">
                        <div class="product-detail">
                            <a href="${productUrl}">
                                <h5 class="name">${product.name}</h5>
                            </a>
                            
                            <!-- Nếu có thông tin giá, hiển thị -->
                            ${product.price ? `
                                <h5 class="price">
                                    <span class="theme-color">${product.price}</span>
                                    ${product.originalPrice ? `<del>${product.originalPrice}</del>` : ''}
                                </h5>
                            ` : ''}

                            <div class="add-to-cart-box bg-white mt-2">
                                <button class="btn btn-add-cart addcart-button" data-product-id="${productId}">
                                    Thêm vào giỏ hàng
                                    <span class="add-icon bg-light-gray">
                                        <i class="fa-solid fa-plus"></i>
                                    </span>
                                </button>
                                <div class="cart_qty qty-box">
                                    <div class="input-group bg-white">
                                        <button type="button" class="qty-left-minus bg-gray" data-type="minus" data-field="">
                                            <i class="fa fa-minus"></i>
                                        </button>
                                        <input class="form-control input-number qty-input" type="text" name="quantity" value="1">
                                        <button type="button" class="qty-right-plus bg-gray" data-type="plus" data-field="">
                                            <i class="fa fa-plus"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;
    });
    
    // Thêm HTML vào trang
    $('.wishlist-section .row').html(wishlistHtml);
    
    // Khởi tạo lại icon feather (nếu sử dụng thư viện feather icons)
    if (typeof feather !== 'undefined') {
        feather.replace();
    }
    
    // Cập nhật số lượng sản phẩm yêu thích trên biểu tượng giỏ hàng
    updateWishlistCount();
    
    // Khởi tạo các sự kiện
    initWishlistEvents();
}

// Cập nhật số lượng sản phẩm yêu thích
function updateWishlistCount() {
    const favorites = JSON.parse(localStorage.getItem('favorites')) || [];
    const count = favorites.length;
    
    // Cập nhật số hiển thị trên biểu tượng giỏ hàng (nếu có)
    const wishlistCountElement = $('.wishlist-count');
    if (wishlistCountElement.length) {
        wishlistCountElement.text(count);
    }
}

// Khởi tạo các sự kiện liên quan đến danh sách yêu thích
function initWishlistEvents() {
    // Xử lý sự kiện khi nhấp vào nút xóa sản phẩm
    $('.wishlist-button.close_button').on('click', function() {
        const $productBox = $(this).closest('.product-box-contain');
        const productId = $(this).data('product-id') || $productBox.data('product-id');
        
        // Xác nhận xóa sản phẩm
        if (confirm('Bạn có chắc chắn muốn xóa sản phẩm này khỏi danh sách yêu thích?')) {
            removeFromWishlist(productId);
            $productBox.fadeOut(300, function() {
                $(this).remove();
                
                // Kiểm tra nếu danh sách trống sau khi xóa
                if ($('.wishlist-section .product-box-contain').length === 0) {
                    renderWishlist(); // Render lại để hiển thị thông báo trống
                }
            });
        }
    });
    
    // Xử lý sự kiện khi nhấp vào nút thêm vào giỏ hàng
    $('.btn-add-cart.addcart-button').on('click', function() {
        const productId = $(this).data('product-id');
        const $productBox = $(this).closest('.product-box-contain');
        const productName = $productBox.find('.name').text();
        const productImage = $productBox.find('.product-image img').attr('src');
        const quantity = parseInt($productBox.find('.qty-input').val()) || 1;
        
        // Thêm vào giỏ hàng
        addToCart(productId, productName, productImage, quantity);
    });
    
    // Xử lý sự kiện khi nhấp vào nút tăng/giảm số lượng
    $('.qty-left-minus, .qty-right-plus').on('click', function() {
        const type = $(this).data('type');
        const $input = $(this).closest('.input-group').find('.qty-input');
        const currentVal = parseInt($input.val());
        
        if (type === 'minus') {
            if (currentVal > 1) {
                $input.val(currentVal - 1);
            }
        } else if (type === 'plus') {
            $input.val(currentVal + 1);
        }
    });
}

// Xóa sản phẩm khỏi danh sách yêu thích
function removeFromWishlist(productId) {
    if (!productId) return;
    
    let favorites = JSON.parse(localStorage.getItem('favorites')) || [];
    
    // Tìm và xóa sản phẩm khỏi danh sách
    favorites = favorites.filter(item => {
        // Kiểm tra theo ID sản phẩm
        if (item.productId && item.productId === productId) {
            return false;
        }
        
        // Kiểm tra ID từ URL nếu không có productId trực tiếp
        if (!item.productId && item.article) {
            try {
                const urlParams = new URLSearchParams(item.article.split('?')[1]);
                const itemId = urlParams.get('id');
                return itemId !== productId;
            } catch (error) {
                return true;
            }
        }
        
        return true;
    });
    
    // Cập nhật localStorage
    localStorage.setItem('favorites', JSON.stringify(favorites));
    
    // Cập nhật số lượng trên biểu tượng
    updateWishlistCount();
}

// Thêm sản phẩm vào giỏ hàng
function addToCart(productId, productName, productImage, quantity) {
    // // Lấy giỏ hàng hiện tại từ localStorage
    // let cart = JSON.parse(localStorage.getItem('cart')) || [];
    
    // // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
    // const existingItemIndex = cart.findIndex(item => item.productId === productId);
    
    // if (existingItemIndex >= 0) {
    //     // Nếu sản phẩm đã có trong giỏ hàng, cập nhật số lượng
    //     cart[existingItemIndex].quantity += quantity;
    // } else {
    //     // Thêm sản phẩm mới vào giỏ hàng
    //     cart.push({
    //         productId: productId,
    //         name: productName,
    //         image: productImage,
    //         quantity: quantity,
    //         dateAdded: new Date().toISOString()
    //     });
    // }
    
    // // Cập nhật localStorage
    // localStorage.setItem('cart', JSON.stringify(cart));
    
    // // Hiển thị thông báo thành công
    // alert(`Đã thêm ${quantity} sản phẩm "${productName}" vào giỏ hàng!`);
    
    // // Cập nhật số lượng trên biểu tượng giỏ hàng (nếu có)
    // updateCartCount();
}

// Cập nhật số lượng sản phẩm trong giỏ hàng
function updateCartCount() {
    // const cart = JSON.parse(localStorage.getItem('cart')) || [];
    // const count = cart.reduce((total, item) => total + (item.quantity || 1), 0);
    
    // // Cập nhật số hiển thị trên biểu tượng giỏ hàng (nếu có)
    // const cartCountElement = $('.cart-count');
    // if (cartCountElement.length) {
    //     cartCountElement.text(count);
    // }
}

// Gọi hàm renderWishlist khi trang đã tải xong
$(document).ready(function() {
    renderWishlist();
});