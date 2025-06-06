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
        
                            // if (rememberMe) {
                            //     localStorage.setItem("authToken", token);
                            //     localStorage.setItem("username", email);
                            //     localStorage.setItem("password", password);
                            // }
                            localStorage.setItem("authToken", token);

                            window.location.href = "index-5.html"; 
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
        } else {
            showError("Vui lòng điền đầy đủ thông tin.");
        }
    });

    $('#google-login-btn').click(function() {
        $.ajax({
          url: 'http://localhost:8080/auth/social-login?login_type=google',
          type: 'GET',
          dataType: 'json',
          success: function(response) {
            console.log(response)
            if (response.code === "200") { 
                window.location.href = response.data;
            } else {
              showError("Đăng nhập thất bại!");
            }
          },
          error: function(xhr, status, error) {
            if (xhr.responseJSON && xhr.responseJSON.message) {
                showError(xhr.responseJSON.message); 
            } else {
                showError("Đăng nhập thất bại! Vui lòng kiểm tra lại.");
            }
          }
        });
      });


      var url = window.location.href;

      // Use URLSearchParams to extract query parameters
      var urlParams = new URLSearchParams(window.location.search);
      var code = urlParams.get('code');
      
      // Now you have the 'code' parameter
      console.log(code);


});




