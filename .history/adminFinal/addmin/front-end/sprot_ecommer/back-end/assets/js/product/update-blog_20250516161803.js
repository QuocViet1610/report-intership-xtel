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
        
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const id = urlParams.get('id') || 0;


    
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

    if (category === null) {
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
            categoryId: category,
            content: description
        };

      
         let token = localStorage.getItem("authToken");
      
$.ajax({
    url: `http://localhost:8080/blog/${id}`, 
    type: "PUT",
    data: JSON.stringify(productData), // productData là object JS
    headers: {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"  // phải set content type
    },
    processData: true, // hoặc bỏ đi vì mặc định true
    success: function (response) {
        showSuccess("Sửa sản phẩm thành công!");
        setTimeout(() => {
            window.location.href = "products.html"; 
        }, 1000);
    },
    error: function (xhr, status, error) {
        if (xhr.responseJSON && xhr.responseJSON.message) {
            showError(xhr.responseJSON.message); 
        } else {
            showError(xhr.responseText); 
        }
    }
});

    });
});