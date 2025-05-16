let currentPage = 0;
let pageSize = 10;
let totalElements = 0;
let totalPages = 0;
let searchText = "";
let categoryId = null;

// Hàm chính để tìm kiếm và lấy dữ liệu
function searchBlogs(page = currentPage, size = pageSize) {
    let token = localStorage.getItem("authToken");
    let searchRequest = {
        searchText: searchText || "",
        categoryId: categoryId || null
    };

    $.ajax({
        url: `http://localhost:8080/blog/search?page=${page}&size=${size}`,
        method: "POST",
        dataType: "json",
        data: JSON.stringify(searchRequest),
        contentType: "application/json",
        headers: {
            "Authorization": "Bearer " + token,
            "Accept": "application/json"
        },
        success: function(response) {
            if (response.code === "200") {
                // Lưu thông tin phân trang
                const pageData = response.data;
                currentPage = pageData.number;
                pageSize = pageData.size;
                totalElements = pageData.totalElements;
                totalPages = pageData.totalPages;
                
                // Render dữ liệu và cập nhật phân trang
                renderBlogList(pageData.content);
                updatePaginationInfo();
                updatePaginationControls();
            } else {
                showError("Lỗi khi tải dữ liệu: " + response.message);
            }
        },
        error: function(xhr) {
            let errorMessage = xhr.responseJSON && xhr.responseJSON.message
                ? xhr.responseJSON.message
                : "Có lỗi xảy ra! Mã lỗi: " + xhr.status;
            showError(errorMessage);
        }
    });
}


