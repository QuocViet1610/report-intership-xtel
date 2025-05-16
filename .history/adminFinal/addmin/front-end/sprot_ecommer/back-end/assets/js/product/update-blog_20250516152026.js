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
    $('#codeProdutc').val(data.title || '');

    if (data.image) {
        $('#previewImage').attr('src', data.image).show();
    } else {
        $('#previewImage').hide();
    }

    // Gán giá trị dropdown
    selectedObjecValue = data.categoryId;
    $('#dropdownButtonObject').text(getCategoryName(selectedObjecValue));

    // Gán nội dung editor (giả sử bạn dùng CKEditor)
    if (window.CKEDITOR && CKEDITOR.instances.editor) {
        CKEDITOR.instances.editor.setData(data.content || '');
    } else {
        // Nếu không dùng CKEditor, gán thẳng vào div #editor
        $('#editor').html(data.content || '');
    }
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
}



$(document).ready(function() {
    // Gọi API lấy thông tin người dùng nếu có id
    fetchUserData();
    
});