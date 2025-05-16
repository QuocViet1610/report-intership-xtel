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
                 console.log("blog")
                const pageData = response.data;
                currentPage = pageData.number;
                pageSize = pageData.size;
                totalElements = pageData.totalElements;
                totalPages = pageData.totalPages;
                console.log(pageData)
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


// Cập nhật thông tin phân trang
function updatePaginationInfo() {
    const startItem = currentPage * pageSize + 1;
    const endItem = Math.min((currentPage + 1) * pageSize, totalElements);
    
    $("#paginationInfo").text(`${startItem} - ${endItem} trong ${totalElements} bài viết`);
    $("#currentPageInput").val(currentPage + 1);
}

// Cập nhật trạng thái các nút điều hướng phân trang
function updatePaginationControls() {
    // Disable/enable các nút điều hướng
    $("#firstPage, #prevPage").prop("disabled", currentPage === 0);
    $("#lastPage, #nextPage").prop("disabled", currentPage === totalPages - 1 || totalPages === 0);
    
    // Cập nhật giá trị và thuộc tính min/max cho input trang
    $("#currentPageInput").attr({
        "min": 1,
        "max": Math.max(totalPages, 1)
    });
}


// Khởi tạo các sự kiện phân trang
$(document).ready(function() {
    // Khởi tạo tìm kiếm ban đầu
    searchBlogs();
    
    // Sự kiện chuyển trang
    $("#firstPage").click(function() {
        if (currentPage > 0) {
            searchBlogs(0);
        }
    });
    
    $("#prevPage").click(function() {
        if (currentPage > 0) {
            searchBlogs(currentPage - 1);
        }
    });
    
    $("#nextPage").click(function() {
        if (currentPage < totalPages - 1) {
            searchBlogs(currentPage + 1);
        }
    });
    
    $("#lastPage").click(function() {
        if (currentPage < totalPages - 1) {
            searchBlogs(totalPages - 1);
        }
    });
    
    // Sự kiện nhập số trang
    $("#currentPageInput").change(function() {
        let targetPage = parseInt($(this).val()) - 1;
        
        // Đảm bảo số trang hợp lệ
        if (isNaN(targetPage) || targetPage < 0) {
            targetPage = 0;
        } else if (targetPage >= totalPages) {
            targetPage = totalPages - 1;
        }
        
        if (targetPage !== currentPage) {
            searchBlogs(targetPage);
        } else {
            // Nếu số trang không thay đổi, vẫn cập nhật lại giá trị hiển thị
            $(this).val(currentPage + 1);
        }
    });
    
    // Sự kiện thay đổi số lượng dòng hiển thị
    $("#pageSizeSelect").change(function() {
        pageSize = parseInt($(this).val());
        searchBlogs(0, pageSize); // Reset về trang đầu tiên
    });
    
    // Khởi tạo giá trị cho select box page size
    $("#pageSizeSelect").val(pageSize);
});


