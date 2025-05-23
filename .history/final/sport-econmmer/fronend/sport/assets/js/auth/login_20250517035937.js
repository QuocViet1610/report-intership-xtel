

$(document).ready(function() {
    function isTokenExpired(token) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            const exp = payload.exp * 1000;
            return Date.now() >= exp;
        } catch (e) {
            return true;
        }
    }
    var authToken = localStorage.getItem('authToken'); 

        if (authToken) {
        
            window.location.href = 'index-5.html';  
            if (isTokenExpired(token)) {
                showError("Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại!");
                localStorage.removeItem("authToken");
                return;
            }
        } 

        if (localStorage.getItem('rememberMe') === 'true') {
            $('#email').val(localStorage.getItem('email'));
            $('#password').val(localStorage.getItem('password'));
            $('#flexCheckDefault').prop('checked', true);
    }


    if (localStorage.getItem('rememberMe') === 'true') {
        // Điền email và mật khẩu vào các trường tương ứng
        $('#email').val(localStorage.getItem('email'));
        $('#password').val(localStorage.getItem('password'));
  
        // Đánh dấu checkbox "Ghi nhớ tôi" nếu có dữ liệu trong localStorage
        $('#flexCheckDefault').prop('checked', true);
      }

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

// ghi nhớ tôi 
        var email = $('#email').val();
        var password = $('#password').val();
        var rememberMe = $('#flexCheckDefault').is(':checked');
        if (rememberMe) {
            localStorage.setItem('email', email);
            localStorage.setItem('password', password);
            localStorage.setItem('rememberMe', 'true');
        } else {
            localStorage.removeItem('email');
            localStorage.removeItem('password');
            localStorage.removeItem('rememberMe');
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
    
const payload = parseJwt(token);


    const scope = payload.scope;
    console.log('Scope:', scope); // 👉 Kết quả: "ROLE_USER"
    $('#yourScopeElement').text(scope); // Gán vào 1 phần tử nếu muốn hiển thị
if(scope === ROLE_USER){
                        if (token) {
        
                            // if (rememberMe) {
                            //     localStorage.setItem("authToken", token);
                            //     localStorage.setItem("username", email);
                            //     localStorage.setItem("password", password);
                            // }
                            localStorage.removeItem("authToken");
                            localStorage.setItem("authToken", token);

                            window.location.href = "index-5.html"; 
                        } else {
                            showError("Lỗi: Không nhận được token từ server!");
                        }
}else{
    showError("Tài khoản quản trị không đăng nhập được")
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
      var urlParams = new URLSearchParams(window.location.search);
      var code = urlParams.get('code');
      
    

      if(code){
        $.ajax({
            url: 'http://localhost:8080/auth/social/callback',
            type: 'GET',
            data: {
                code: code,
                login_type: "google"
            },
            success: function(response) {
                // Handle the response
                console.log('Response:', response);
        
                // Check the response structure and handle it
                if(response.code === '200') {
                    localStorage.removeItem("authToken");
                    localStorage.setItem("authToken", response.data.token);

                    window.location.href = "index-5.html"; 
                } else {
                    showError("Đăng nhập thất bại!");
                }
            },
            error: function(xhr, textStatus, errorThrown) {
                if (xhr.responseJSON && xhr.responseJSON.message) {
                    showError(xhr.responseJSON.message); 
                } else {
                    showError("Đăng nhập thất bại!");
                }
            }
        });
      }
     




});

function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(
            atob(base64)
                .split('')
                .map(function (c) {
                    return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
                })
                .join('')
        );

        return JSON.parse(jsonPayload);
    } catch (e) {
        console.error('Invalid token format', e);
        return null;
    }
}


