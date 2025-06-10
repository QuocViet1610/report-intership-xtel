$(document).ready(function() {
    
    const urlParams = new URLSearchParams(window.location.search);
const orderId = urlParams.get('orderId');
console.log(orderId)
if(orderId){
    let address = null;
    let token = localStorage.getItem('authToken');
    if(token){

        $.ajax({
            url: `http://localhost:8080/order/` + orderId, 
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
                    address = response.data.shippingAddress;
                    addressDetail(address);
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


  
        function addressDetail(id) {
            $.ajax({
                url: `http://localhost:8080/addresses/` + address, 
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json',
                },
                success: function(response) {
                    const fullName = response.data.fullName;
                    const phoneNumber = response.data.phoneNumber; // Lấy số điện thoại
                    const addressText = response.data.addressText;
                    const wardName = response.data.wardName;
                    const districtName = response.data.districtName;
                    const provinceName = response.data.provinceName;
        
                    // Gộp các thông tin lại thành một chuỗi địa chỉ đầy đủ
                    const fullAddress = `${addressText}, ${wardName}, ${districtName}, ${provinceName}`;
        
                    // Gán tên người mua vào thẻ với id="nameAddress"
                    document.getElementById('nameAddress').textContent = fullName;
                    
                    // Gán số điện thoại vào thẻ với id="phoneNumber"
                    // document.getElementById('phoneNumber').textContent = phoneNumber;
        
                    // Gán địa chỉ đầy đủ vào thẻ với id="address"
                    document.getElementById('address').textContent = fullAddress;
                },
                error: function(xhr, status, error) {
                    let errorMessage = "Không thể lấy thông tin địa chỉ";
                    if (xhr.responseJSON && xhr.responseJSON.message) {
                        errorMessage = xhr.responseJSON.message;
                    }
                    showError(errorMessage);  
                }
            });
        } 
    }else{
        showError("Xin vui lòng đăng nhập")
    }
}




});