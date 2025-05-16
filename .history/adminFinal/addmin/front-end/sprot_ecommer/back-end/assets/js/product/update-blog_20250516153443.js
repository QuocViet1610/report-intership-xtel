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
                var data = response.data;
                   $('#codeProdutc').val(data.title || '');
                   if (window.editor) {
                    console.log(data.content)
    ClassicEditor
      .create(document.querySelector('#editor'))
      .then(editor => {
        window.editor = editor; // Lưu lại instance của CKEditor
        
      
          editor.setData(data.content || '');

      })
      .catch(error => {
        console.error("Lỗi khởi tạo CKEditor:", error);
      });
  } 

  // Lấy tất cả phần tử có class 'ck ck-reset ck-editor ck-rounded-corners'
const editors = document.querySelectorAll('.ck.ck-reset.ck-editor.ck-rounded-corners');

// Kiểm tra có ít nhất 2 phần tử không
if (editors.length >= 2) {
  // Loại bỏ phần tử thứ 2 (index 1)
  editors[1].remove();
}

// Chọn tất cả các phần tử có các lớp 'ck ck-reset ck-editor ck-rounded-corners'
let elements = document.querySelectorAll('.ck.ck-editor');
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