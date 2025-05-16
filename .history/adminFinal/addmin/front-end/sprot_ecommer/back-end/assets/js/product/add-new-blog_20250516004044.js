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

    $('#btn-create').on('click', function (e) {
        e.preventDefault(); // Ngăn reload trang

        // Lấy dữ liệu form
        const code = $('#codeProdutc').val();
        const category = $('#dropdownButtonObject').text().trim();
        let description = document.querySelector(".ck-editor__editable").innerHTML;

        if (description.trim() === '<p><br data-cke-filler="true"></p>') {
            description = null;
        }

        // Tạo object chứa dữ liệu text
        const productData = {
            code: code,
            category: category,
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

         let token = localStorage.getItem("authToken");
      
        $.ajax({
                url: `http://localhost:8080/product/${productId}`, 
                type: "PUT",
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
                        window.location.href = "products.html"; // Chuyển đến trang danh sách sản phẩm
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
