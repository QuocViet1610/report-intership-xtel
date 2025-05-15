function fetchUserData() {
    let token = localStorage.getItem("authToken");
    
    const queryString = window.location.search;

    // Tạo đối tượng URLSearchParams để dễ thao tác
    const urlParams = new URLSearchParams(queryString);

    // Lấy giá trị của tham số "id"
    const id = urlParams.get('id');

    if(id) {
        $.ajax({
            url: `http://localhost:8080/user/get-detail/${id}`,
            type: 'GET',
            contentType: 'application/json',
            headers: {
                "Authorization": "Bearer " + token,
                "Accept": "application/json"
            },
            success: function(response) {
                console.log(response);
                
                // Kiểm tra response có dữ liệu hợp lệ không
                if(response && response.code === "200" && response.data) {
                    // Điền dữ liệu vào form
                    $('#fullName').val(response.data.fullName);
                    $('#email').val(response.data.email);
                    // Không điền password vì lý do bảo mật
                    // $('#password').val(response.data.password);
                    $('#phone').val(response.data.phone);
                    
                    // Nếu đang chỉnh sửa, đổi nút thành "Cập nhật nhân viên"
                    $('#btn-create').text('Cập nhật nhân viên');
                    $('#btn-create').attr('id', 'btn-update');
                    
                    // Thay đổi tiêu đề form
                    $('.card-header-1 h5').text('Cập nhật thông tin nhân viên');
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
                if (typeof showError === 'function') {
                    showError(errorMessage);
                } else {
                    alert(errorMessage);
                }
            }
        });
    }
}

// Thêm hàm xử lý cập nhật thông tin
function setupFormEvents() {
    // Xử lý nút cập nhật
    $('body').on('click', '#btn-update', function(e) {
        e.preventDefault();
        
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        const id = urlParams.get('id');
        
        if (!id) return;
        
        let token = localStorage.getItem("authToken");
        
        // Thu thập dữ liệu từ form
        const userData = {
            fullName: $('#fullName').val(),
            email: $('#email').val(),
            phone: $('#phone').val()
        };
        
        // Thêm password nếu người dùng đã nhập
        if ($('#password').val().trim() !== '') {
            userData.password = $('#password').val();
        }
        
        $.ajax({
            url: `http://localhost:8080/user/update/${id}`,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(userData),
            headers: {
                "Authorization": "Bearer " + token,
                "Accept": "application/json"
            },
            success: function(response) {
                if (response.code === "200") {
                    if (typeof showSuccess === 'function') {
                        showSuccess("Cập nhật thông tin nhân viên thành công!");
                    } else {
                        alert("Cập nhật thông tin nhân viên thành công!");
                    }
                } else {
                    alert("Có lỗi xảy ra!");
                }
            },
            error: function(xhr, status, error) {
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
                if (typeof showError === 'function') {
                    showError(errorMessage);
                } else {
                    alert(errorMessage);
                }
            }
        });
    });
    
    // Xử lý nút tạo nhân viên mới
    $('body').on('click', '#btn-create', function(e) {
        e.preventDefault();
        
        let token = localStorage.getItem("authToken");
        
        // Thu thập dữ liệu từ form
        const userData = {
            fullName: $('#fullName').val(),
            email: $('#email').val(),
            password: $('#password').val(),
            phone: $('#phone').val()
        };
        
        $.ajax({
            url: 'http://localhost:8080/user/create-staff',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(userData),
            headers: {
                "Authorization": "Bearer " + token,
                "Accept": "application/json"
            },
            success: function(response) {
                if (response.code === "200") {
                    if (typeof showSuccess === 'function') {
                        showSuccess("Thêm nhân viên mới thành công!");
                        
                        // Reset form
                        $('form')[0].reset();
                    } else {
                        alert("Thêm nhân viên mới thành công!");
                    }
                } else {
                    alert("Có lỗi xảy ra!");
                }
            },
            error: function(xhr, status, error) {
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
                if (typeof showError === 'function') {
                    showError(errorMessage);
                } else {
                    alert(errorMessage);
                }
            }
        });
    });
    
    // Xử lý nút bỏ qua
    $('body').on('click', '.btn-outline-secondary', function(e) {
        e.preventDefault();
        window.location.href = "all-users.html"; // Chuyển về trang danh sách nhân viên
    });
}

$(document).ready(function() {
    // Gọi API lấy thông tin người dùng nếu có id
    fetchUserData();
    
    // Thiết lập sự kiện cho form
    setupFormEvents();
});