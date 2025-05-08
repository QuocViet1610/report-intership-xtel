$(document).ready(function() {
    
    const urlParams = new URLSearchParams(window.location.search);
const orderId = urlParams.get('orderId');
console.log(orderId)
if(orderId){
    let address = null;
    let token = localStorage.getItem('authToken');
    if(token){

        $.ajax({
            url: `http://localhost:8080/order/` + orderId, 
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json',
            },
            success: function(response) {
                console.log("response.data")

                if(response.code === '200') {
                    console.log(response.data)
                    document.getElementById('idOrder').textContent = response.data.orderCode;
                    const createdAtDate = new Date(response.data.createdAt);
                    const updateAtDate = new Date(response.data.createdAt);
                    address = response.data.shippingAddress;
                    addressDetail(address);
                    // Cộng thêm 3 ngày vào updateAtDate
                    updateAtDate.setDate(createdAtDate.getDate() + 3);
                    
                    // Tùy chọn cho định dạng có giờ
                    const options = { year: 'numeric', month: 'long', day: 'numeric' };
                    const formattedCreatedAt = createdAtDate.toLocaleString('vi-VN', options);
                    
                    // Tùy chọn cho định dạng không có giờ (chỉ có năm, tháng, ngày)
                    const optionUpdate = { year: 'numeric', month: 'long', day: 'numeric' };
                    const formattedUpdatedAt = updateAtDate.toLocaleString('vi-VN', optionUpdate);
                    
                    // Gán thời gian đặt vào thẻ có id là timePlaced
                    document.getElementById('timePlaced').textContent = formattedCreatedAt;
                    
                    // Gán thời gian dự kiến đến vào thẻ có id là estimatedTime
                    document.getElementById('estimatedTime').textContent = formattedUpdatedAt;
                    

                    const statusId = response.data.statusId; // Giá trị có thể là 1 hoặc 2

                    // Lấy tất cả các phần tử li
                    const statusElements = document.querySelectorAll('.progtrckr li');

                    // Kiểm tra giá trị statusId và thêm lớp tương ứng
                    if (statusId === 1) {
                        // Nếu statusId là 1, chỉ thêm 'progtrckr-done' vào phần tử đầu tiên
                        statusElements[0].classList.add('progtrckr-done');
                    } else if (statusId === 2) {
                        // Nếu statusId là 2, thêm 'progtrckr-done' vào 2 phần tử đầu tiên
                        statusElements[0].classList.add('progtrckr-done');
                        statusElements[1].classList.add('progtrckr-done');
                    } else {
                        // Nếu không phải 1 hoặc 2, không làm gì (hoặc có thể thêm logic khác nếu cần)
                    }
                }
                const totalPrice = response.data.totalPrice;
                const totalDiscount = response.data.totalDiscount;
                const finalPrice = response.data.finalPrice;
                const totalProduct = response.data.totalProduct;
                const paymentMethod = response.data.paymentMethod;
    
                // Gán số sản phẩm vào phần tử với id="totalProducts"
                document.getElementById('totalProducts').textContent = `(${totalProduct} sản phẩm)`;
    
                // Gán giá đơn hàng vào phần tử với id="orderPrice"
                document.getElementById('orderPrice').textContent = formatCurrency(totalPrice);
    
                // Gán giá vận chuyển vào phần tử với id="shippingPrice"
                document.getElementById('shippingPrice').textContent = formatCurrency(response.data.finalPrice- (response.data.finalPrice - response.data.totalDiscount)); // Thay đổi giá vận chuyển nếu cần
    
                // Gán giảm giá vào phần tử với id="discountPrice"
                document.getElementById('discountPrice').textContent = formatCurrency(totalDiscount);
    
                // Gán tổng đơn hàng vào phần tử với id="finalPrice"
                document.getElementById('finalPrice').textContent = formatCurrency(finalPrice);
    
                // Gán phương thức thanh toán vào phần tử với id="paymentMethod"
                document.getElementById('paymentMethod').textContent = paymentMethod;

                var orderDetail = [];
                orderDetail = response.data.orderDetails;
                
                let totalAmount = 0; // Biến để tính tổng số tiền của tất cả các sản phẩm
                
                // Hàm để render các thuộc tính của sản phẩm
                function renderProductAttributes(item) {
                    let attributesHTML = '';
                    if (item.productAttributeValue && item.productAttributeValue.length > 0) {
                        item.productAttributeValue.forEach(attribute => {
                            attributesHTML += `
                                <li>${attribute.attributeName}: ${attribute.attributeValues.map(val => val.name).join(', ')}</li>
                            `;
                        });
                    }
                    return attributesHTML;
                }
                
                orderDetail.forEach(item => {
                    // Lấy số lượng sản phẩm
                    let quantityProduct = item.quantity;
                console.log(quantityProduct)
                    // Lấy thông tin sản phẩm
                    item = item.productView; // Lấy thông tin của productView
                
                    // Lấy hình ảnh chính của sản phẩm
                    let mainImage = item.productImages.find(image => image.isPrimary === 1);
                    let imageUrl = mainImage ? mainImage.imageUrl : 'default-image.jpg'; // Nếu không có ảnh chính, dùng ảnh mặc định
                
                    // Lấy tên sản phẩm và giá
                    let productName = item.productName;
                    let productPrice = item.productPrice;
                    let quantity = item.quantity;
                    let totalPrice = productPrice * quantity; // Tính tổng giá của sản phẩm
                
                    // Kiểm tra và lấy thông tin variant nếu có
                    let productVariantId = null;
                    if (item.productVariants && item.productVariants.length > 0) {
                        let variant = item.productVariants[0]; // Lấy variant đầu tiên
                        productName = variant.name;
                        productPrice = variant.price;
                        productVariantId = variant.id;
                        totalPrice = productPrice * quantity; // Cập nhật tổng giá khi có variant
                    }
                
                    // Hiển thị thông tin sản phẩm trong console
                    console.log(`Sản phẩm: ${productName}`);
                    console.log(`Giá: ${productPrice.toLocaleString('vi-VN')}đ`);
                    console.log(`Số lượng: ${quantity}`);
                    console.log(`Tổng giá: ${totalPrice.toLocaleString('vi-VN')}đ`);
                
                    // Lấy và hiển thị các thuộc tính của sản phẩm
                    let attributesHTML = renderProductAttributes(item);
                    if (attributesHTML) {
                        console.log(`Thuộc tính sản phẩm:`);
                        console.log(attributesHTML);
                    } else {
                        console.log('Không có thuộc tính sản phẩm.');
                    }
                
                    console.log('--------------------------------------');
                
                    // Cộng tổng số tiền cho tất cả các sản phẩm
                    totalAmount += totalPrice;
                });
                


            },
            error: function(xhr, status, error) {
         
                let errorMessage = "Lịch sử giao hàng đang trống";
                if (xhr.responseJSON && xhr.responseJSON.message) {
                    errorMessage = xhr.responseJSON.message;
                }
                showError(errorMessage);  
            }
        });



        function createProductRow(product) {
            return `
                <tr>
                    <td class="product-detail">
                        <div class="product border-0">
                            <a href="product.left-sidebar.html" class="product-image">
                                <img src="${product.productImage}" class="img-fluid blur-up lazyloaded" alt="${product.productName}">
                            </a>
                            <div class="product-detail">
                                <ul>
                                    <li class="name">
                                        <a href="product-left-thumbnail.html">${product.productName}</a>
                                    </li>
                                    <li class="text-content">Màu sắc: ${product.color}</li>
                                    <li class="text-content">Kích cỡ: ${product.size}</li>
                                </ul>
                            </div>
                        </div>
                    </td>
        
                    <td class="price">
                        <h4 class="table-title text-content">Giá</h4>
                        <h6 class="theme-color">${formatCurrency(product.price)}</h6>
                    </td>
        
                    <td class="quantity">
                        <h4 class="table-title text-content">Số lượng</h4>
                        <h4 class="text-title">${product.quantity}</h4>
                    </td>
        
                    <td class="subtotal">
                        <h4 class="table-title text-content">Tổng cộng</h4>
                        <h5>${formatCurrency(product.subtotal)}</h5>
                    </td>
                </tr>
            `;
        }
        

        function formatCurrency(amount) {
            return amount.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
        }

        function addressDetail(id) {
            $.ajax({
                url: `http://localhost:8080/addresses/` + address, 
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json',
                },
                success: function(response) {
                    const fullName = response.data.fullName;
                    const phoneNumber = response.data.phoneNumber; // Lấy số điện thoại
                    const addressText = response.data.addressText;
                    const wardName = response.data.wardName;
                    const districtName = response.data.districtName;
                    const provinceName = response.data.provinceName;
              
                    // Gộp các thông tin lại thành một chuỗi địa chỉ đầy đủ
                    const fullAddress = `${addressText}, ${wardName}, ${districtName}, ${provinceName}`;
        
                    // Gán tên người mua vào thẻ với id="nameAddress"
                    document.getElementById('nameAddress').textContent = fullName;
                    
                    console.log(phoneNumber)
                    // Gán số điện thoại vào thẻ với id="phoneNumber"
                    document.getElementById('phoneNumber').textContent = phoneNumber;
        
                    // Gán địa chỉ đầy đủ vào thẻ với id="address"
                    document.getElementById('address').textContent = fullAddress;
                },
                error: function(xhr, status, error) {
                    let errorMessage = "Không thể lấy thông tin địa chỉ";
                    if (xhr.responseJSON && xhr.responseJSON.message) {
                        errorMessage = xhr.responseJSON.message;
                    }
                    showError(errorMessage);  
                }
            });
        } 
    }else{
        showError("Xin vui lòng đăng nhập")
    }
}




});