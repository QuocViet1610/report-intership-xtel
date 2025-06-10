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
                    

                    const createdAtDate = new Date( response.data.createdAt);
                    const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit' };
                    const formattedCreatedAt = createdAtDate.toLocaleString( options);
                    
                    document.getElementById('timePlaced').textContent = formattedCreatedAt;
                    // Gán thời gian dự kiến đến vào thẻ có id là estimatedTime
                    document.getElementById('estimatedTime').textContent = response.data.updatedAt;
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