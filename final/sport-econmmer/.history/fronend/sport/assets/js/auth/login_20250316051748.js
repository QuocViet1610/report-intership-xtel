$(document).ready(function() {
    // Lắng nghe sự kiện khi người dùng gửi form
    $('#login-form').on('submit', function(event) {
        event.preventDefault(); // Ngăn không cho form gửi lại trang
        
        // Lấy giá trị email và mật khẩu từ các trường nhập liệu
        var email = $('#email').val();
        var password = $('#password').val();
        console.log(email)
         // Kiểm tra email hợp lệ
         var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
         if (!emailPattern.test(email)) {
            showError("Email không hợp lệ. Vui lòng nhập lại.");
             return; // Dừng lại nếu email không hợp lệ
         }
 
         // Kiểm tra mật khẩu dài ít nhất 8 ký tự
         if (password.length < 8) {
            showError("Mật khẩu phải có ít nhất 8 ký tự.");
             return; // Dừng lại nếu mật khẩu không đủ 8 ký tự
         }

        // Kiểm tra nếu email và mật khẩu không trống
        if(email && password) {
            // Gửi yêu cầu AJAX để đăng nhập
            // $.ajax({
            //     url: 'your-login-endpoint-url', // Thay 'your-login-endpoint-url' bằng URL thực tế để xử lý đăng nhập
            //     method: 'POST',
            //     data: {
            //         email: email,
            //         password: password
            //     },
            //     success: function(response) {
            //         // Xử lý kết quả từ máy chủ
            //         if (response.success) {
            //             window.location.href = "dashboard.html"; // Chuyển hướng người dùng sau khi đăng nhập thành công
            //         } else {
            //             alert('Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.');
            //         }
            //     },
            //     error: function(xhr, status, error) {
            //         console.error('Có lỗi xảy ra: ', error);
            //         alert('Đã có lỗi xảy ra. Vui lòng thử lại.');
            //     }
            // });
        } else {
            alert("Vui lòng điền đầy đủ thông tin.");
        }
    });
});



$(document).ready(function () {
    // Định nghĩa hàm showError sau khi DOM đã sẵn sàng
    window.showError = function (message) {
        $(".error-message").remove(); // Xóa thông báo cũ (nếu có)

        let errorHtml = `
            <div class="error-message">
                <div class="error-icon close-error">✖</div>
                <div class="error-text">${message}</div>
            </div>
        `;

        $("body").append(errorHtml); // Thêm vào cuối trang

        // Xử lý sự kiện khi click vào nút đóng (✖)
        $(".close-error").click(function () {
            $(this).parent().fadeOut(300, function () {
                $(this).remove();
            });
        });

        setTimeout(function () {
            $(".error-message").addClass("fade-out");
            setTimeout(function () {
                $(".error-message").remove();
            }, 500);
        }, 4000); // Hiển thị 4 giây rồi tự động ẩn
    };

    // Gọi showError nếu có lỗi (ví dụ)
    // showError('Lỗi đăng nhập!');

});
