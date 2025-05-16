function fetchUserData() {
    let token = localStorage.getItem("authToken");
    
    const queryString = window.location.search;

    // Tạo đối tượng URLSearchParams để dễ thao tác
    const urlParams = new URLSearchParams(queryString);

    // Lấy giá trị của tham số "id"
    const id = urlParams.get('id') || 0;

    if(id) {
        $.ajax({
            url: `http://localhost:8080/blog/get-detail/6`,
            type: 'GET',
            contentType: 'application/json',
            headers: {
                "Authorization": "Bearer " + token,
                "Accept": "application/json"
            },
            success: function(response) {
                console.log(response);
                function fillForm(data) {
    $('#codeProdutc').val(data.title);  // Gán tiêu đề
    // Nếu bạn dùng CKEditor, gán nội dung html vào editor
    if (CKEDITOR.instances['descriptionEditor']) {
        CKEDITOR.instances['descriptionEditor'].setData(data.content);
    } else {
        // Nếu bạn không dùng CKEditor thì gán vào thẻ editable
        document.querySelector(".ck-editor__editable").innerHTML = data.content;
    }

    // Gán selectedObjecValue và cập nhật nút dropdown
    selectedObjecValue = data.categoryId;
    // Giả sử bạn có một hàm để chuyển id thành tên category
    $('#dropdownButtonObject').text(getCategoryName(selectedObjecValue));
}

// Hàm map id -> tên danh mục (bạn đã có rồi)
function getCategoryName(categoryId) {
  const categories = {
    1: "Chọn đồ thể thao",
    2: "Bài tập thể thao",
    3: "Sự kiện thể thao"
  };
  return categories[categoryId] || "Không xác định";
}

// 2. Xử lý khi chọn danh mục
$('.dropdown-item-object').on('click', function(event) {
    event.preventDefault(); 
    var selectedObject = $(this).text();  // Hoặc dùng data('name') nếu có
    selectedObjecValue = $(this).data('id'); 
    console.log("Chọn categoryId:", selectedObjecValue);
    $('#dropdownButtonObject').text(selectedObject); 
});
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
}



$(document).ready(function() {
    // Gọi API lấy thông tin người dùng nếu có id
    fetchUserData();
    
});