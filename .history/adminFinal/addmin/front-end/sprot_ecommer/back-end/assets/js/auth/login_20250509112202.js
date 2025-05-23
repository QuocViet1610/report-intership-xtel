$(document).ready(function () {

    checkAuth();
// login
    $("#login-form").submit(function (event) {
        event.preventDefault();

        let email = $("#username").val();
        let password = $("#password").val();
        let rememberMe = $("#rememberMe").prop("checked"); 

        let emailPattern = /^[a-zA-Z0-9._%+-]+@gmail\.com$/;
        if (!emailPattern.test(email)) {
            showError("Email không hợp lệ");
            return;
        }


        if (password.length < 8) {
            showError("Mật khẩu phải có ít nhất 8 ký tự!");
            return;
        }

        $.ajax({
            url: "http://localhost:8080/auth/login", 
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({
                email: email,
                password: password
            }),
            success: function (response) {
                if (response.code === "200") { 
                    let token = response.data.token;

                    if (token) {
                        if (rememberMe) {
                            localStorage.setItem("authToken", token);
                            localStorage.setItem("email", email);
                            localStorage.setItem("password", password);
                        }
                        localStorage.setItem("authToken", token);
                        window.location.href = "index.html"; 
                    } else {
                        showError("Lỗi: Không nhận được token từ server!");
                    }
                } else {
                    showError(response.message || "Đăng nhập thất bại!");
                }
            },
            error: function (xhr) {
                if (xhr.responseJSON && xhr.responseJSON.message) {
                    showError(xhr.responseJSON.message); 
                } else {
                    showError("Đăng nhập thất bại! Vui lòng kiểm tra lại.");
                }
            }
        });
    });
// kiem tra da dang nhap chua
    function checkAuth() {
        let token = localStorage.getItem("authToken");
        if (token) {
            if (isTokenExpired(token)) {
                localStorage.removeItem("authToken");
                window.location.href = "login.html"; 
            } else {
                window.location.href = "index.html"; 
            }
        } else {
            let username = localStorage.getItem("email");
            let password = localStorage.getItem("password");
    
            if (username && password) {
                $("#username").val(username);
                $("#password").val(password);
                $("#rememberMe").prop("checked", true); 
            }
        }
    }

    function isTokenExpired(token) {
        try {
            let payloadBase64 = token.split('.')[1]; 
            let payloadJson = atob(payloadBase64);  
            let payload = JSON.parse(payloadJson); 
            
            let exp = payload.exp; 
            let currentTime = Math.floor(Date.now() / 1000); 
    
            return exp < currentTime; 
        } catch (error) {
            showError("Token không hợp lệ:", error);
            return true; 
        }                         
    }

    // Đăng xuất
    $("#logoutBtn").click(function () {
        localStorage.removeItem("authToken"); 
        localStorage.removeItem("username"); 
        localStorage.removeItem("password"); 
        window.location.href = "login.html"; 
    });

    window.checkAuth = checkAuth;
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
    
        // Hiển thị thông báo trong 4 giây và sau đó ẩn nó
        var errorMessage = $(".error-message").last(); // Lấy thông báo mới nhất
        setTimeout(function () {
            errorMessage.addClass("fade-out"); // Thêm lớp fade-out cho hiệu ứng
            setTimeout(function () {
                errorMessage.remove(); // Xóa thông báo sau khi fade-out
            }, 500); // Ẩn sau 500ms
        }, 4000); // Hiển thị 4 giây rồi tự động ẩn
    };

    // Gọi showError nếu có lỗi (ví dụ)
    // showError('Lỗi đăng nhập!');
 // Định nghĩa hàm showSuccess
 window.showSuccess = function (message) {
    // Xóa thông báo cũ nếu có
    $(".success-message").remove(); 

    // Tạo HTML cho thông báo thành công
    let successHtml = `
        <div class="success-message success">
            <div class="success-icon">✔</div>
            <div class="success-text">${message}</div>
        </div>
    `;

    // Thêm thông báo vào cuối trang
    $("body").append(successHtml); 

    // Lấy phần tử thông báo mới nhất
    var successMessage = $(".success-message").last();

    // Xóa lớp "fade-out" nếu có
    successMessage.removeClass("fade-out"); 

    // Đảm bảo thông báo sẽ tự ẩn sau 2 giây
    setTimeout(function () {
        successMessage.addClass("fade-out"); // Thêm lớp fade-out
        setTimeout(function () {
            successMessage.remove(); // Xóa thông báo sau khi hiệu ứng fade-out
        }, 500); // Ẩn sau 500ms
    }, 2000); // Hiển thị trong 2 giây rồi tự động ẩn
};

});
