$(document).ready(function () {

    // Xử lý preview ảnh khi chọn
    document.getElementById('thumbnailImage').addEventListener('change', function (event) {
        const reader = new FileReader();
        reader.onload = function () {
            const preview = document.getElementById('previewImage');
            preview.src = reader.result;
            preview.style.display = 'block';
        }
        reader.readAsDataURL(event.target.files[0]);
    });

    var selectedObjecValue = null;
                        $('.dropdown-item-object').on('click', function() {
                        
                        event.preventDefault(); 
                        var selectedObject = $(this).data('name');
                        selectedObjecValue = $(this).data('id'); 
                        console.log(selectedObjecValue)
                        $('#dropdownButtonObject').text(selectedObject); 
                    });


    $('#btn-create').on('click', function (e) {
        e.preventDefault(); // Ngăn reload trang

        // Lấy dữ liệu form
        const code = $('#codeProdutc').val();
        let description = document.querySelector(".ck-editor__editable").innerHTML;

        if (description.trim() === '<p><br data-cke-filler="true"></p>') {
            description = null;
        }

            if (!code || code.length < 3) {
        showError("Mã sản phẩm phải từ 3 ký tự trở lên và không được để trống!");
        return; // Dừng xử lý nếu lỗi
    }

    if (selectedObjecValue === null) {
        showError("Vui lòng chọn thể loại sản phẩm!");
        return;
    }

    if (!description || description.length < 3) {
        showError("Mô tả sản phẩm không được để trống!");
        return;
    }

        // Tạo object chứa dữ liệu text
        const productData = {
            title: code,
            categoryId: selectedObjecValue,
            content: description
        };

        // Tạo FormData mới
        const formData = new FormData();

        // Đính kèm dữ liệu JSON stringify vào key "data"
        formData.append("data", JSON.stringify(productData));

        // Lấy tất cả file input class image-input (nếu bạn có nhiều input)
        let imageInputs = document.querySelectorAll(".image-input");

        // Mảng lưu ảnh đã chọn (nếu bạn có đa ảnh) - giả sử selectedImages
        // Nếu bạn chỉ có 1 input file: dùng $('#thumbnailImage')[0].files
        // Ở đây giả sử bạn muốn lấy file từ input #thumbnailImage

        const files = $('#thumbnailImage')[0].files;

        for (let i = 0; i < files.length; i++) {
            formData.append("image[]", files[i], files[i].name);
            console.log(`📸 Ảnh ${i + 1}:`, files[i].name, "| MIME:", files[i].type);
        }

        // Debug FormData contents
        for (let pair of formData.entries()) {
            // Nếu giá trị là File thì có property type, nếu không thì in giá trị thường
            if (pair[1] instanceof File) {
                console.log(`📝 ${pair[0]}:`, pair[1].name, "| MIME:", pair[1].type);
            } else {
                console.log(`📝 ${pair[0]}:`, pair[1]);
            }
        }
if (files.length === 0) {
    showError("Vui lòng chọn ít nhất một ảnh!");
    return; // Dừng xử lý nếu chưa chọn ảnh
}
         let token = localStorage.getItem("authToken");
      
        $.ajax({
                url: `http://localhost:8080/blog`, 
                type: "POST",
                data: formData,
                headers: {
                    "Authorization": "Bearer " + token,
                },
                data: formData,
                processData: false,
                contentType: false,
                success: function (response) {
                    showSuccess("Sửa sản phẩm thành công!");
                    setTimeout(() => {
                        window.location.href = "blog-list.html"; // Chuyển đến trang danh sách sản phẩm
                    }, 1000);
            
                },
                error: function (xhr, status, error) {
                    if (xhr.responseJSON && xhr.responseJSON.message) {
                        // Nếu có thông báo lỗi trong response JSON
                        showError(xhr.responseJSON.message); 
                    } else {
                        // Nếu không có thông báo lỗi trong response JSON, hiển thị toàn bộ responseText
                        showError(xhr.responseText); 
                    }
                }
                
            });
    });
});
