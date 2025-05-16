// Global editor variable
let editorInstance = null;

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
    
    // If there are multiple editors already, remove extra ones
// If there are multiple editors already, hide extra ones
if (existingEditors.length >= 2) {
    for (let i = 1; i < existingEditors.length; i++) {
        console.log('Hiding extra editor:', i);
        existingEditors[i].style.display = 'none';
    }
}
}

// Document ready handler
$(document).ready(function() {
    fetchUserData();
    
    // Add a cleanup routine when leaving the page (optional)
    $(window).on('beforeunload', function() {
        if (editorInstance) {
            editorInstance.destroy().catch(error => {
                console.error("Error destroying editor on page unload:", error);
            });
        }
    });
});