

$(document).ready(function() {

    fetchUserData();
});


function fetchUserData() {
    let token = localStorage.getItem("authToken");
    
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const id = urlParams.get('id') || 0;

    if(id) {
        $.ajax({
            url: `http://localhost:8080/blog/get-detail/${id}`,
            type: 'GET',
            contentType: 'application/json',
            headers: {
                "Authorization": "Bearer " + token,
                "Accept": "application/json"
            },
            success: function(response) {
                console.log(response);
                var data = response.data;
                console.log(data)
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
    function getCategoryName(categoryId) {
  switch (categoryId) {
    case 1:
      return "Chọn đồ thể thao";
    case 2:
      return "Bài tập thể thao";
    case 3:
      return "Sự kiện thể thao";
    default:
      return "Không xác định";
  }
}
}