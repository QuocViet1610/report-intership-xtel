// Biến toàn cục để theo dõi trạng thái phân trang
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

// Hàm hiển thị danh sách blog
// Hàm hiển thị danh sách blog
let blogIdToDelete = null;

// Function to render blog list
function renderBlogList(blogs) {
    const $tableBody = $("#all-orders");
    $tableBody.empty();
    
    if (blogs.length === 0) {
        $tableBody.append(`
            <tr>
                <td colspan="6" class="text-center">Không có bài viết nào</td>
            </tr>
        `);
        return;
    }
    
    blogs.forEach(blog => {
        // Cắt ngắn nội dung nếu quá dài
        const shortContent = blog.content.length > 50 
            ? blog.content.substring(0, 50) + "..." 
            : blog.content;
            
        // Xử lý ảnh (sử dụng ảnh mặc định nếu URL không hợp lệ)
        const imageUrl = blog.image;
        console.log("img");
        console.log(imageUrl);
        
        // Format thời gian
        const createdDate = new Date(blog.createdAt).toLocaleDateString('vi-VN');
        
        $tableBody.append(`
            <tr>
                <td>
                    <div class="table-image">
                        <img src="${imageUrl}" class="img-fluid" alt="Blog Image">
                    </div>
                </td>
                <td style="max-width:200px; white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">${blog.title}</td>
                <td>${shortContent}</td>
                <td>${getCategoryName(blog.categoryId) || 'Chưa phân loại'}</td>
                <td>
                    <div class="user-info">
                        <img src="${blog.avatar ? blog.avatar : 'assets/images/user-placeholder.png'}" 
                             class="user-avatar" alt="${blog.fullName}">
                        <div>
                            <div class="user-name">${blog.fullName}</div>
                            <div class="user-email">${blog.email}</div>
                        </div>
                    </div>
                </td>
                <td>
                    <ul>
                        <li>
                            <a href="update-blog.html?id=${blog.id}">
                                <i class="ri-pencil-line"></i>
                            </a>
                        </li>
                        <li>
                            <a href="javascript:void(0)" onclick="confirmDeleteBlog(${blog.id})">
                                <i class="ri-delete-bin-line"></i>
                            </a>
                        </li>
                    </ul>
                </td>
            </tr>
        `);
    });
} // End of renderBlogList function

// Function to handle delete confirmation
function confirmDeleteBlog(blogId) {
    blogIdToDelete = blogId;
    const deleteModal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
    deleteModal.show();
}

// Add the event listener outside of any functions (do this once)
$(document).ready(function() {
    // Set up the delete confirmation button event handler
    document.getElementById('confirmDeleteBtn').addEventListener('click', function () {
        if (!blogIdToDelete) return;
    let token = localStorage.getItem("authToken");
        $.ajax({
            url: `http://localhost:8080/blog/${blogIdToDelete}`,
            type: 'DELETE',
            contentType: 'application/json',
            headers: {
                "Authorization": "Bearer " + token,
                "Accept": "application/json"
            },
            success: function(response) {

                    showSuccess('Xoá thành công!');

        const deleteModal = bootstrap.Modal.getInstance(document.getElementById('deleteConfirmModal'));
        deleteModal.hide();
        blogIdToDelete = null;
              searchBlogs(page = currentPage, size = pageSize)
            },
            error: function(xhr, status, error) {
                // Xử lý lỗi khi gọi API
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
        
        // Temporary code until you uncomment the AJAX call

    });
});

// Hàm này giả định sẽ lấy tên danh mục từ một nguồn nào đó
// Bạn cần thay thế bằng hàm thực tế lấy tên danh mục từ ID
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



// Hàm để xem chi tiết blog (cần triển khai sau)
function viewBlog(id) {
    console.log("Xem blog ID:", id);
    // Mở modal hoặc chuyển trang để xem chi tiết
}

// Hàm để xóa blog (cần triển khai sau)
function deleteBlog(id) {
    if (confirm("Bạn có chắc chắn muốn xóa bài viết này?")) {
        console.log("Xóa blog ID:", id);
        // Gọi API xóa blog
    }
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