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



$(document).ready(function() {
    // Gọi API lấy thông tin người dùng nếu có id
    fetchUserData();

});