let token = localStorage.getItem("authToken");
let currentPage = 1; // Bắt đầu từ trang 1
let totalPages = 1; // Mặc định là 1 trang
let totalItems = 0; // Tổng số mục dữ liệu
let pageSize = 10; // Mặc định là 10 dòng mỗi trang
let searchText = ""; // Mặc định tìm kiếm rỗng
let currentStatusId = null;

// Hàm tải đơn hàng và cập nhật bảng
function loadOrders(page, pageSize, searchText,statusId) {
    $.ajax({
        url: `http://localhost:8080/order/search?page=${page - 1}&size=${pageSize}`,
        type: 'POST',
        contentType: 'application/json',
        headers: {
            "Authorization": "Bearer " + token,
            "Accept": "application/json"
        },
        dataType: 'json',
        data: JSON.stringify({
            searchText: searchText,
            statusId: statusId
        }),
        success: function(response) {
            // Đảm bảo response có cấu trúc đúng
            if (response && response.data && Array.isArray(response.data.content)) {
                renderTable(response.data.content); // Render các đơn hàng vào bảng
                
                // Cập nhật thông tin phân trang
                totalPages = response.data.totalPages || 1;
                totalItems = response.data.totalElements || 0;
                
                // Điều chỉnh currentPage nếu vượt quá totalPages
                if (currentPage > totalPages && totalPages > 0) {
                    currentPage = totalPages;
                    // Tải lại với trang hiện tại đã điều chỉnh
                    loadOrders(currentPage, pageSize, searchText,statusId);
                    return;
                }
                
                updatePagination(); // Cập nhật các nút phân trang và thông tin
            } else {
                showError("Dữ liệu không hợp lệ");
            }
        },
        error: function(xhr) {
            let errorMessage = xhr.responseJSON && xhr.responseJSON.message
                ? xhr.responseJSON.message
                : "Có lỗi xảy ra! Mã lỗi: " + xhr.status;
            showError(errorMessage);
        }
    });
}

// Hàm render bảng
function renderTable(orders) {
    const tbody = $('#all-orders');
    tbody.empty(); // Xóa các dòng hiện tại
    
    if (orders.length === 0) {
        // Hiển thị thông báo khi không có dữ liệu
        tbody.append('<tr><td colspan="7" class="text-center">Không có dữ liệu</td></tr>');
        return;
    }

    orders.forEach(order => {
        const row = `
            <tr data-bs-toggle="offcanvas" href="#order-details" data-order-id="${order.id}">
                <td>
                    <div class="user-info">
                        <div class="info">
                            <span class="name">${order.userFullName || 'N/A'}</span>
                            <span class="email">${order.userEmail || 'N/A'}</span>
                            <span class="phone">${order.phone || 'N/A'}</span>
                        </div>
                    </div>
                </td>
                <td>${order.orderCode || 'N/A'}</td>
                <td>${formatDate(order.createdAt) || 'N/A'}</td>
                <td>${order.paymentMethod || 'N/A'}</td>
                <td class="${getStatusClass(order.statusId)}">
                       <span>${getStatusName(order.statusId) || 'N/A'}</span>
                </td>
                <td>${formatCurrency(order.finalPrice) || 'N/A'}</td>
                <td>
                    <ul class="action-buttons">

           <li>
<button class="btn" 
    onclick="showConfirmationModal(${order.statusId},${order.id}); event.stopPropagation();" 
    style="display: ${getStatusNameNext(order.statusId) ? 'inline-block' : 'none'};">
    ${getStatusNameNext(order.statusId)}
</button>

</li>
                        <li><a href="javascript:void(0);" onclick="window.print();">
<i class="ri-printer-line"></i> 
</a></li>
                        <li><a href="order-detail.html?id=${order.id}" onclick="event.stopPropagation();"><i class="ri-eye-line"></i></a></li>
                        <li><a href="javascript:void(0)" onclick="confirmDelete(${order.id}); event.stopPropagation();" data-bs-toggle="modal" data-bs-target="#deleteModal"><i class="ri-delete-bin-line"></i></a></li>
                    </ul>
                </td>
            </tr>
        `;
        tbody.append(row);
    });
    
    // Thêm sự kiện click cho các dòng để hiển thị chi tiết
    tbody.find('tr').click(function() {
        const orderId = $(this).data('order-id');
        if (orderId) {
            loadOrderDetails(orderId);
        }
    });
}



// Hàm format ngày tháng
function formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    if (isNaN(date.getTime())) return dateString; // Trả về nguyên mẫu nếu không phải date hợp lệ
    
    return date.toLocaleDateString('vi-VN', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

// Hàm format tiền tệ
function formatCurrency(amount) {
    if (amount === undefined || amount === null) return '';
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(amount);
}

// Hàm lấy class tương ứng với trạng thái
function getStatusClass(statusId) {
    switch (statusId) {
        case 1: return 'order-pending'; // "Đang chờ xử lý"
        case 2: return 'order-processing'; // "Đang xử lý"
        case 3: return 'order-shipping'; // "Đang vận chuyển"
        case 4: return 'order-success'; // "Giao hàng thành công"
        case 5: return 'order-cancle'; // "Huỷ đơn hàng"
        default: return '';
    }
}

function getStatusName(statusId) {
    switch (statusId) {
        case 1: return 'Đang chờ xử lý'; // "Pending"
        case 2: return 'Đang xử lý'; // "Processing"
        case 3: return 'Đang vận chuyển'; // "Shipping"
        case 4: return 'Giao hàng thành công'; // "Delivered"
        case 5: return 'Huỷ đơn hàng'; // "Cancelled"
        default: return 'N/A'; // Default to "N/A" if statusId doesn't match
    }
}

function getStatusNameNext(statusId) {
    switch (statusId) {
        case 1: return 'Xử lý'; // "Pending"
        case 2: return 'Vận chuyển'; // "Processing"
        case 3: return 'Hoàn thành'; // "Shipping"
        default: return ''; // Default to "N/A" if statusId doesn't match
    }
}

// Hàm cập nhật điều khiển phân trang và thông tin
function updatePagination() {
    // Tắt/bật các nút
    $('#prevPage').prop('disabled', currentPage <= 1);
    $('#nextPage').prop('disabled', currentPage >= totalPages);
    $('#firstPage').prop('disabled', currentPage <= 1);
    $('#lastPage').prop('disabled', currentPage >= totalPages);

    // Tính toán phạm vi hiển thị hiện tại
    const startItem = totalItems === 0 ? 0 : (currentPage - 1) * pageSize + 1;
    const endItem = Math.min(currentPage * pageSize, totalItems);

    // Cập nhật thông tin phân trang và input
    $('#paginationInfo').text(`${startItem} - ${endItem} trong ${totalItems} đơn hàng`);
    $('#currentPageInput').val(currentPage);
    
    // Hiển thị tổng số trang
    $('#totalPagesInfo').text(`/ ${totalPages}`);
}

// Khởi tạo các sự kiện cho phân trang
function initPaginationEvents() {
    // Xử lý trang trước, tiếp theo, đầu tiên, cuối cùng
    $('#prevPage').click(function() {
        if (currentPage > 1) {
            currentPage--;
            loadOrders(currentPage, pageSize, searchText,currentStatusId);
        }
    });

    $('#nextPage').click(function() {
        if (currentPage < totalPages) {
            currentPage++;
            loadOrders(currentPage, pageSize, searchText,currentStatusId);
        }
    });

    $('#firstPage').click(function() {
        if (currentPage !== 1) {
            currentPage = 1;
            loadOrders(currentPage, pageSize, searchText,currentStatusId);
        }
    });

    $('#lastPage').click(function() {
        if (currentPage !== totalPages) {
            currentPage = totalPages;
            loadOrders(currentPage, pageSize, searchText,currentStatusId);
        }
    });

    // Xử lý khi người dùng nhập số trang
    $('#currentPageInput').on('keypress', function(e) {
        if (e.which === 13) { // Phím Enter
            const page = parseInt($(this).val());
            if (!isNaN(page) && page >= 1 && page <= totalPages) {
                currentPage = page;
                loadOrders(currentPage, pageSize, searchText,currentStatusId);
            } else {
                // Nếu giá trị không hợp lệ, trả về giá trị hiện tại
                $(this).val(currentPage);
            }
        }
    });

    // Xử lý khi input mất focus
    $('#currentPageInput').on('blur', function() {
        const page = parseInt($(this).val());
        if (!isNaN(page) && page >= 1 && page <= totalPages) {
            if (page !== currentPage) {
                currentPage = page;
                loadOrders(currentPage, pageSize, searchText,currentStatusId);
            }
        } else {
            // Nếu giá trị không hợp lệ, trả về giá trị hiện tại
            $(this).val(currentPage);
        }
    });

    // Xử lý thay đổi kích thước trang
    $('#pageSizeSelect').change(function() {
        pageSize = parseInt($(this).val());
        currentPage = 1; // Reset về trang đầu khi thay đổi số lượng hiển thị
        loadOrders(currentPage, pageSize, searchText,currentStatusId);
    });

    // Xử lý tìm kiếm
    let searchTimeout;
    $('#searchInput').on('input', function() {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(() => {
            searchText = $(this).val().trim();
            currentPage = 1; // Reset về trang đầu khi tìm kiếm
            loadOrders(currentPage, pageSize, searchText,currentStatusId);
        }, 500); // Delay 500ms để tránh gọi API quá nhiều
    });
}

$('#orderTabs a').on('click', function(e) {
    e.preventDefault();
    const statusId = $(this).data('status'); // Get the statusId from the data-status attribute
    currentStatusId = statusId; // Update the currentStatusId
    loadOrders(currentPage, pageSize, searchText, currentStatusId); // Reload orders with the selected statusId
});

// Khởi tạo khi trang được tải
$(document).ready(function() {
    // Khởi tạo các sự kiện phân trang
    initPaginationEvents();
    
    // Tải dữ liệu ban đầu
    loadOrders(currentPage, pageSize, searchText, currentStatusId);
});

$(document).ready(function() {
    // When a tab is clicked
    $('.nav-link').click(function() {
        // Remove 'active' class from all tabs
        $('.nav-link').removeClass('active');
        
        // Add 'active' class to the clicked tab
        $(this).addClass('active');
        
        // Get the statusId (optional, if you need to use it in AJAX)
        const statusId = $(this).data('status');
        console.log('Selected status:', statusId);
        
        // Load orders based on the selected status (AJAX request)
        loadOrders(currentPage, pageSize, searchText, statusId);
    });
});

function showConfirmationModal(orderId, idOrder ) {
    // Get the order status
    const statusId = orderId;  // Assuming a function to get statusId for an order
    const statusName = getStatusNameNext(statusId); // Get the status name using the statusId

    // Set default modal title and content
    let modalTitle = `Xác nhận về ${statusName}`;
    let modalContent = `Đơn hàng sẽ chuyển sang trạng thái ${statusName}`;

    // Check the statusId and set appropriate title and content
    if (statusId === 1) {
        modalTitle = "Xác nhận xử lý đơn hàng";  // "Chờ xử lý"
        modalContent = "Đơn hàng sẽ chuyển sang trạng thái xử lý";  // "Pending"
    } else if (statusId === 2) {
        modalTitle = "Xác nhận vận chuyển đơn hàng";  // "Đang vận chuyển"
        modalContent = "Đơn hàng sẽ chuyển sang trạng thái vận chuyển";  // "Shipping"
    } else if (statusId === 3) {
        modalTitle = "Xác nhận giao hàng thành công";  // "Giao hàng thành công"
        modalContent = "Đơn hàng sẽ chuyển sang trạng thái giao hàng thành công";  // "Delivered"
    }

    // Update the modal title
    const modalTitleElement = document.getElementById("confirmationModalLabel");
    modalTitleElement.textContent = modalTitle;

    // Update the modal body content
    const modalBody = document.getElementById("modalBody");
    modalBody.textContent = modalContent;

    // Show the modal
    const modal = new bootstrap.Modal(document.getElementById('confirmationModal'));
    modal.show();


    document.getElementById('confirmButtonProcess').onclick = function() {
        // Make the AJAX request here
        $.ajax({
            url: `http://localhost:8080/order/process/${idOrder}`,  // Replace with your correct endpoint
            type: 'PUT',  // Method PUT
            contentType: 'application/json',
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("authToken"),  // Assuming token authentication
                "Accept": "application/json"
            },
            success: function(response) {
                // Handle the success
                console.log('Order processed successfully:', response);
                // Hide the modal after processing
                modal.hide();
                showSuccess('Đơn hàng đã được xử lý thành công!');
                loadOrders(currentPage, pageSize, searchText, currentStatusId);
                // Optionally, you can update the UI or refresh the list here
            },
            error: function(xhr) {
                let errorMessage = xhr.responseJSON && xhr.responseJSON.message
                    ? xhr.responseJSON.message
                    : "Có lỗi xảy ra! Mã lỗi: " + xhr.status;
                showError(errorMessage);
            }
        });
    };
}
