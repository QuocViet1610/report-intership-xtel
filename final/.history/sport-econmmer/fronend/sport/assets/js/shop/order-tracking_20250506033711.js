$(document).ready(function() {
            let token = localStorage.getItem('authToken');
            if(token){

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
                            renderOrders(response.data); 
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
});