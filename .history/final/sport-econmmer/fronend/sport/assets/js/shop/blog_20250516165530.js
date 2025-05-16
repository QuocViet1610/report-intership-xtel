$(document).ready(function () {

    $.ajax({
        url: "http://localhost:8080/category",
        method: "GET",
        success: function (res) {
         
       
        },
        error: function (err) {
            console.error("Lỗi khi gọi API:", err);
            showError("Không thể tải danh mục. Vui lòng thử lại sau!");
        }
    });
    
  
});
