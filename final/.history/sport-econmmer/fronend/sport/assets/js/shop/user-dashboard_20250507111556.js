$(document).ready(function() {
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('order-history') && urlParams.get('order-history') === 'true') {
        // Tự động click vào tab nếu tham số 'clickTab' là true
        $('#pills-order-tab').click();
    }


            // Hàm render đơn hàng
            function renderOrders(orders) {
                $('#orderHistory').empty();
                orders.forEach(order => {
                    // Xác định trạng thái đơn hàng
                    let orderStatus = '';
                    let orderStatusColor = '';
    
                    switch (order.statusId) {
                        case 1:
                            orderStatus = 'Chờ xử lý';
                            orderStatusColor = '#ffc107'; // Màu vàng
                            break;
                        case 2:
                            orderStatus = 'Đang xử lý';
                            orderStatusColor = '#17a2b8'; // Màu xanh lam
                            break;
                        case 3:
                            orderStatus = 'Đang vận chuyển';
                            orderStatusColor = '#007bff'; // Màu xanh dương
                            break;
                        case 4:
                            orderStatus = 'Giao hàng thành công';
                            orderStatusColor = '#28a745'; // Màu xanh lá cây
                            break;
                        case 5:
                            orderStatus = 'Huỷ';
                            orderStatusColor = '#dc3545'; // Màu đỏ
                            break;
                        default:
                            orderStatus = 'Chưa rõ';
                            orderStatusColor = '#6c757d'; // Màu xám
                    }
    
                    // Tạo HTML cho mỗi đơn hàng
                        let orderHTML = `
                            <div class="order-box dashboard-bg-box">
                                <div class="order-container">
                                    <div class="order-icon">
                                        <i data-feather="box"></i>
                                    </div>
                                    <div class="order-detail">
                                        <h4>Mã đơn hàng: ${order.orderCode} <span style="background: ${orderStatusColor};">${orderStatus}</span></h4>
                                    </div>
                                </div>
                                <div class="product-order-detail">
                            <ul class="product-size" style="display: flex; flex-direction: column;">
                                        <li>
                                            <div class="size-box">
                                                
                                  <h5><strong>Tổng sản phẩm</strong>: ${order.totalProduct || 0} sản phẩm</h5>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="size-box">
        
                                                  <h5><strong>Tổng đơn hàng:</strong> ${new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(order.totalPrice)}</h5>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="size-box">
                                                <h5><strong>Thời gian đặt đơn hàng:</strong> ${new Date(order.createdAt).toLocaleDateString()}</h5>
                                            </div>
                                        </li>
                                    </ul>
                                </div>
                                <div style="display: flex; justify-content: flex-end;">
                                    <button class="btn btn-danger" style="background-color: #dc3545; color:white; margin-right:10px" data-bs-toggle="modal" data-bs-target="#confirmCancelModal" id="cancelOrderBtn" data-order-id="${order.id}">Hủy Đơn Hàng</button>

                                    <a href="order-tracking.html?orderId=${order.id}">
                                            <button class="btn btn-success" style="background-color: #28a745; color:white;">Xem đơn hàng</button>
                                        </a>
                                </div>
                            </div>
                        `;
                    // Thêm HTML vào phần tử chứa đơn hàng
                    $('#orderHistory').append(orderHTML);
                });
            }
    
            function isTokenExpired(token) {
                try {
                    const payload = JSON.parse(atob(token.split('.')[1]));
                    const exp = payload.exp * 1000;
                    return Date.now() >= exp;
                } catch (e) {
                    return true;
                }
            }
            let token = localStorage.getItem('authToken');
            if(token){
             
                if (isTokenExpired(token)) {
                    showError("Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại!");
                    localStorage.removeItem("authToken");
                    return;
                }
           
                $.ajax({
                    url: `http://localhost:8080/order`, 
                    method: 'GET',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json',
                    },
                    success: function(response) {
                        console.log("response.data")

                        if(response.code === '200') {
                            console.log(response.data)
                            renderOrders(response.data); // Gọi hàm render với dữ liệu đơn hàng
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
            }else{
                showError("Xin vui lòng đăng nhập")
            }
            // Thực hiện yêu cầu AJAX để lấy dữ liệu từ API
            $(document).on('click', '.btn-danger', function() {
                console.log("aaaa");
                var orderId = $(this).data('order-id');

                $('#confirmCancelBtn').data('order-id', orderId);
            });

            $('#confirmCancelBtn').on('click', function() {
                
                var orderId = $(this).data('order-id');
    
                $.ajax({
                    url: 'http://localhost:8080/order/cancel/' + orderId,  
                    method: 'PUT',  // Method POST for cancellation
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json',
                    },
                    success: function(response) {

                        showSuccess('Đơn hàng đã được hủy thành công!');
                        location.reload();  
                    },
                    error: function(xhr, status, error) {
                 
                        let errorMessage = "Đơn hàng không thể huỷ";
                        if (xhr.responseJSON && xhr.responseJSON.message) {
                            errorMessage = xhr.responseJSON.message;
                        }
                        showError(errorMessage);  
                    }
                });
            });


var password= 1234568;
            $.ajax({
                url: 'http://localhost:8080/user/get-inf',  
                type: 'GET',
                contentType: 'application/json',
    
                headers: {
                    "Authorization": "Bearer " + token,
                    "Accept": "application/json"
                  },
                success: function(response) {
                    const data = response.data;
              
                    $('.fullName').text(data.fullName);  // Gán tên đầy đủ

                    $('.phone').html(`${data.phone}`);  // Gán số điện thoại
                    $('.email').html(data.email);  // Gán email
        
                    // Thông tin đăng nhập
                    $('.loginEmail').html(`${data.email}`);  // Gán email đăng nhập
                    $('#passwordField').text("●●●●●●");  // Không hiển thị mật khẩu trực tiếp (sử dụng dấu chấm)
              

                    document.getElementById('pname').value = data.fullName;
                    document.getElementById('mobile').value = data.phone;
                    document.getElementById('address1').value = data.email;
                },
                error: function(xhr, status, error) {
                    // Xử lý khi có lỗi
                    let errorMessage = "Đã xảy ra lỗi!";
                    if (xhr.responseJSON && xhr.responseJSON.message) {
                        errorMessage = xhr.responseJSON.message;
                    } else if (xhr.responseText) {
                        try {
                            let response = JSON.parse(xhr.responseText);
                            errorMessage = response.message || "Lỗi không xác định từ máy chủ!";
                        } catch (e) {
                            errorMessage = "Lỗi không thể đọc phản hồi từ server!";
                        }
                    }
                    showError(errorMessage);
                }
            });

            $('.btn.theme-bg-color').on('click', function() {
                // Lấy giá trị từ các trường input
                const fullName = $('#pname').val();  // Tên đầy đủ
                const phone = $('#mobile').val();    // Số điện thoại
                const email = $('#address1').val();  // Email (có thể sử dụng trường email thay cho address1 nếu cần)
            
                // Xây dựng dữ liệu để gửi đi
                const data = {
                    fullName: fullName,
                    phone: phone,
                    email: email,
                    password: 1234568
                };
            
                // Gọi API để cập nhật thông tin
                $.ajax({
                    url: `http://localhost:8080/user/1`,  // Địa chỉ API PUT
                    type: 'PUT',
                    contentType: 'application/json',
                    data: JSON.stringify(data),
                    headers: {
                        "Authorization": "Bearer " + token,  // Nếu cần token
                        "Accept": "application/json"
                    },
                    success: function(response) {
                        if (response.code === "200") {
                            showSuccess("Cập nhật thông tin thành công!");
            
                            // Hiển thị thông báo trong 1 giây rồi reload trang
                            setTimeout(function() {
                                location.reload();
                            }, 1000); // 1000 ms = 1 giây
                        } else {
                            showError("Có lỗi xảy ra khi cập nhật thông tin!");
                        }
                    },
                    error: function(xhr, status, error) {
                        // Xử lý khi có lỗi
                        let errorMessage = "Đã xảy ra lỗi!";
                        if (xhr.responseJSON && xhr.responseJSON.message) {
                            errorMessage = xhr.responseJSON.message;
                        } else if (xhr.responseText) {
                            try {
                                let response = JSON.parse(xhr.responseText);
                                errorMessage = response.message || "Lỗi không xác định từ máy chủ!";
                            } catch (e) {
                                errorMessage = "Lỗi không thể đọc phản hồi từ server!";
                            }
                        }
                        showError(errorMessage);
                    }
                });
            });

            $(".btn.theme-bg-color").click(function() {
                debugger
                // Lấy giá trị của các trường mật khẩu từ form
                var passwordOld = $("#password").val();
                var passwordNew = $("#newPassword").val();
                var confirmPassword = $("#confirmNewPassword").val(); // Xác nhận mật khẩu mới
        
                if (passwordNew.length < 8) {
                    showError("Mật khẩu mới phải có ít nhất 8 ký tự!");
                    return;
                }

                // Kiểm tra nếu mật khẩu mới và xác nhận mật khẩu mới giống nhau
                if (passwordNew !== confirmPassword) {
                    showError("Mật khẩu mới và xác nhận mật khẩu không khớp!");
                    return;
                }
        
               showSuccess("Cập nhật mật khẩu thành công")
            });
            
        });

    