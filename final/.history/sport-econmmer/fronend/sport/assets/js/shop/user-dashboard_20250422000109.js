$(document).ready(function() {
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('order-history') && urlParams.get('order-history') === 'true') {
        // Tự động click vào tab nếu tham số 'clickTab' là true
        $('#pills-order-tab').click();
    }


            // Hàm render đơn hàng
            function renderOrders(orders) {
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
                                    <h4>Mã đơn hàng: ${order.id} <span style="background: ${orderStatusColor};">${orderStatus}</span></h4>
                                </div>
                            </div>
                            <div class="product-order-detail">
                                <ul class="product-size">
                                    <li>
                                        <div class="size-box">
                                            <h6 class="text-content">Tổng sản phẩm  : </h6>
                                            <h5>${order.totalProduct || 0} sản phẩm</h5>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="size-box">
                                            <h6 class="text-content">Tổng giá: </h6>
                                            <h5>${order.totalPrice}</h5>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="size-box">
                                            <h6 class="text-content">Thời gian đặt </h6>
                                            <h5>${new Date(order.createdAt).toLocaleDateString()}</h5>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                            <div style="display: flex; justify-content: flex-end;">
                                <button class="btn btn-danger" style="background-color: #dc3545; color:white; margin-right:10px">Hủy Đơn Hàng</button>
                                <button class="btn btn-success" style="background-color: #28a745; color:white;">Xem đơn hàng</button>
                            </div>
                        </div>
                    `;
                    // Thêm HTML vào phần tử chứa đơn hàng
                    $('#orderHistory').append(orderHTML);
                });
            }
    

            let token = localStorage.getItem('authToken');
            
            // Thực hiện yêu cầu AJAX để lấy dữ liệu từ API
            $.ajax({
                url: '{{local}}/order', // Thay {{local}} bằng địa chỉ API thực tế
                type: 'GET',
                success: function(response) {
                    // Kiểm tra kết quả và gọi hàm render() để hiển thị đơn hàng
                    if(response.code === '200' && response.data.length > 0) {
                        renderOrders(response.data); // Gọi hàm render với dữ liệu đơn hàng
                    }
                },
                error: function(xhr, status, error) {
                    console.log("Đã xảy ra lỗi: " + error);
                }
            });
        });

    