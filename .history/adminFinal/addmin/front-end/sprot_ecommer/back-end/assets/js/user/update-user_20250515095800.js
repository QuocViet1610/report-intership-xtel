function fetchUserData() {
    let token = localStorage.getItem("authToken");
    
    $.ajax({
        url: 'http://localhost:8080/user/get-staff',
        type: 'GET',
        contentType: 'application/json',
        headers: {
            "Authorization": "Bearer " + token,
            "Accept": "application/json"
        },
        success: function(response) {
            if (response.code === "200") {
                // Store the full data
                allUserData = response.data;
                
                // Initialize filtered data with all data
                filteredUserData = [...allUserData];
                
                // Calculate total pages
                totalPages = Math.ceil(filteredUserData.length / pageSize);
                
                // Apply pagination to the data
                renderUserTable(currentPage, pageSize);
                
                // Update pagination UI
                updatePaginationUI();
            } else {
                console.error("Lỗi từ API:", response.message || "Không có thông báo lỗi");
                alert("Có lỗi xảy ra!");
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


$(document).ready(function() {
fetchUserData();
    });