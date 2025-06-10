$(document).ready(function() {
    
    const urlParams = new URLSearchParams(window.location.search);
const orderId = urlParams.get('orderId');
console.log(orderId)
if(orderId){
    let token = localStorage.getItem('authToken');
    if(token){

        $.ajax({
            url: `http://localhost:8080/order/21`, 
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json',
            },
            success: function(response) {
                console.log("response.data")

                if(response.code === '200') {
                    console.log(response.data)
                    document.getElementById('idOrder').textContent = response.data.orderCode;
                    const createdAtDate = new Date(response.data.createdAt);
                    const updateAtDate = new Date(response.data.createdAt);
                    
                    // Cộng thêm 3 ngày vào updateAtDate
                    updateAtDate.setDate(createdAtDate.getDate() + 3);
                    
                    // Tùy chọn cho định dạng có giờ
                    const options = { year: 'numeric', month: 'long', day: 'numeric' };
                    const formattedCreatedAt = createdAtDate.toLocaleString('vi-VN', options);
                    
                    // Tùy chọn cho định dạng không có giờ (chỉ có năm, tháng, ngày)
                    const optionUpdate = { year: 'numeric', month: 'long', day: 'numeric' };
                    const formattedUpdatedAt = updateAtDate.toLocaleString('vi-VN', optionUpdate);
                    
                    // Gán thời gian đặt vào thẻ có id là timePlaced
                    document.getElementById('timePlaced').textContent = formattedCreatedAt;
                    
                    // Gán thời gian dự kiến đến vào thẻ có id là estimatedTime
                    document.getElementById('estimatedTime').textContent = formattedUpdatedAt;
                    
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


        $.ajax({
            url: `http://localhost:8080/order/21`, 
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json',
            },
            success: function(response) {
                console.log("response.data")

                if(response.code === '200') {
                    console.log(response.data)
                    document.getElementById('idOrder').textContent = response.data.orderCode;
                    const createdAtDate = new Date(response.data.createdAt);
                    const updateAtDate = new Date(response.data.createdAt);
                    
                    // Cộng thêm 3 ngày vào updateAtDate
                    updateAtDate.setDate(createdAtDate.getDate() + 3);
                    
                    // Tùy chọn cho định dạng có giờ
                    const options = { year: 'numeric', month: 'long', day: 'numeric' };
                    const formattedCreatedAt = createdAtDate.toLocaleString('vi-VN', options);
                    
                    // Tùy chọn cho định dạng không có giờ (chỉ có năm, tháng, ngày)
                    const optionUpdate = { year: 'numeric', month: 'long', day: 'numeric' };
                    const formattedUpdatedAt = updateAtDate.toLocaleString('vi-VN', optionUpdate);
                    
                    // Gán thời gian đặt vào thẻ có id là timePlaced
                    document.getElementById('timePlaced').textContent = formattedCreatedAt;
                    
                    // Gán thời gian dự kiến đến vào thẻ có id là estimatedTime
                    document.getElementById('estimatedTime').textContent = formattedUpdatedAt;
                    
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
}




});