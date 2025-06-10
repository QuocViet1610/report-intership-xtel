$(document).ready(function() {
    // Lắng nghe sự kiện khi người dùng gửi form
    $('#login-form').on('submit', function(event) {
        event.preventDefault(); // Ngăn không cho form gửi lại trang

        // Lấy giá trị email và mật khẩu từ các trường nhập liệu
        var email = $('#email').val();
        var password = $('#password').val();

        // Kiểm tra nếu email và mật khẩu không trống
        if(email && password) {
            // Gửi yêu cầu AJAX để đăng nhập
            $.ajax({
                url: 'your-login-endpoint-url', // Thay 'your-login-endpoint-url' bằng URL thực tế để xử lý đăng nhập
                method: 'POST',
                data: {
                    email: email,
                    password: password
                },
                success: function(response) {
                    // Xử lý kết quả từ máy chủ
                    if (response.success) {
                        window.location.href = "dashboard.html"; // Chuyển hướng người dùng sau khi đăng nhập thành công
                    } else {
                        alert('Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.');
                    }
                },
                error: function(xhr, status, error) {
                    console.error('Có lỗi xảy ra: ', error);
                    alert('Đã có lỗi xảy ra. Vui lòng thử lại.');
                }
            });
        } else {
            alert("Vui lòng điền đầy đủ thông tin.");
        }
    });
});
