// Global editor variable
let editorInstance = null;

var category = null;
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
                $('#codeProdutc').val(data.title || '');
                category = data.categoryId;
                console.log(category);
                  $('#dropdownButtonObject').text(getCategoryName(category));      
                // First destroy any existing editor
                if (editorInstance) {
                    editorInstance.destroy()
                        .then(() => {
                            console.log("Previous editor instance destroyed");
                            initEditor(data.content || '');
                        })
                        .catch(error => {
                            console.error("Error destroying editor:", error);
                            // Try to initialize anyway
                            initEditor(data.content || '');
                        });
                } else {
                    // No existing editor, initialize a new one
                    initEditor(data.content || '');
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

function initEditor(content) {
    // Check for any existing editor elements and remove them

    // Create new editor
    ClassicEditor
        .create(document.querySelector('#editor'))
        .then(editor => {
            editorInstance = editor; // Store the editor instance globally
            editor.setData(content);
            console.log('Editor initialized successfully');
        })
        .catch(error => {
            console.error("Lỗi khởi tạo CKEditor:", error);
        });


            const existingEditors = document.querySelectorAll('.ck-editor');
    console.log('Found existing editors:', existingEditors.length);
    

}

// Document ready handler
$(document).ready(function() {
    fetchUserData();
    

    
    var category = null;
                        $('.dropdown-item-object').on('click', function() {
                        
                        event.preventDefault(); 
                        var selectedObject = $(this).data('name');
                        category = $(this).data('id'); 
                        console.log(category)
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