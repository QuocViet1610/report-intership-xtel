// Global pagination variables
let currentPage = 0;
let totalPages = 0;
let pageSize = 10; // Default page size

function fetchData(page = 0, size = pageSize) {
    let token = localStorage.getItem("authToken");
    let searchText = $("#searchInput").val() || "";
    let searchRequest = { searchText: searchText };

    $.ajax({
        url: `http://localhost:8080/product-rating/search?page=${page}&size=${size}&sort=id.asc`,
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(searchRequest),
        headers: {
            "Authorization": "Bearer " + token,
            "Accept": "application/json"
        },
        success: function (response) {
            console.log(response);

            // Lấy dữ liệu đánh giá từ response
            const reviews = response.data.content;  // Mảng đánh giá
            
            // Cập nhật thông tin phân trang
            currentPage = response.data.number;
            totalPages = response.data.totalPages;
            const totalElements = response.data.totalElements;
            const start = currentPage * size + 1;
            const end = Math.min(start + size - 1, totalElements);
            
            // Cập nhật UI phân trang
            updatePaginationUI(start, end, totalElements);

            let tableRows = ''; // Chuỗi HTML chứa các hàng của bảng

            reviews.forEach(review => {
                let statusIcon = review.isActive === 1
                    ? '<i class="ri-checkbox-circle-line" style="color: #0da487 !important;"></i>'
                    : '<i class="ri-close-circle-line" style="color: red !important;"></i>';

                // Render đánh giá cho mỗi sản phẩm
                tableRows += `
                    <tr class="review-row">
                        <td style="padding: 10px;">
                            <div class="user-info">
                                <img src="${review.avatar}" class="img-fluid" alt="">
                                <div class="info">
                                    <span class="name">${review.fullName}</span>
                                </div>
                            </div>
                        </td>
                        <td style="max-width: 250px; word-wrap: break-word; white-space: normal;">
                            ${review.productName}
                        </td>
                        <td style="max-width: 50px; word-wrap: break-word; white-space: normal;">
                            ${review.productCode}
                        </td>
                        <td>
                            <ul class="rating">
                                ${review.ratingValue} <i class="fa-solid fa-star" style="color: #FFD700;"></i>
                            </ul>
                        </td>
                        <td>${review.ratingComment}</td>
                        <td class="td-check">
                            ${statusIcon}
                        </td>
                        <td class="small-cell">
                            <ul>
                                <li>
                                    <a href="javascript:void(0)" class="toggle-active-status" data-id="${review.id}">
                                        <i class="ri-pencil-line"></i>
                                    </a>
                                </li>
                            </ul>
                        </td>
                    </tr>
                `;
            });

            // Chèn các hàng vào bảng
            $("#reviewTableBody").html(tableRows);

            // Render lại Feather Icons
            if (typeof feather !== 'undefined') {
                feather.replace();
            }

            // Kích hoạt tìm kiếm trong bảng
            if (typeof activateSearch === 'function') {
                activateSearch();
            }
        },
        error: function (xhr, status, error) {
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
            } else {
                errorMessage = "Lỗi mạng hoặc máy chủ không phản hồi!";
            }
            console.error("Lỗi khi lấy dữ liệu:", errorMessage);
            if (typeof showError === 'function') {
                showError(errorMessage);
            } else {
                alert(errorMessage);
            }
        }
    });
}

function updatePaginationUI(start, end, total) {
    // Cập nhật thông tin phân trang
    $("#paginationInfo").text(`${start} - ${end} trong ${total} đánh giá`);
    
    // Cập nhật số trang hiện tại trong input
    $("#currentPageInput").val(currentPage + 1);
    
    // Vô hiệu hóa nút khi cần
    $("#firstPage, #prevPage").prop("disabled", currentPage === 0);
    $("#lastPage, #nextPage").prop("disabled", currentPage === totalPages - 1);
}

// Xử lý sự kiện phân trang
function setupPaginationEvents() {
    // Sự kiện chọn kích thước trang
    $("#pageSizeSelect").change(function() {
        pageSize = parseInt($(this).val());
        fetchData(0, pageSize); // Reset về trang đầu tiên khi thay đổi kích thước trang
    });
    
    // Sự kiện di chuyển trang
    $("#firstPage").click(function() {
        if (currentPage > 0) {
            fetchData(0, pageSize);
        }
    });
    
    $("#prevPage").click(function() {
        if (currentPage > 0) {
            fetchData(currentPage - 1, pageSize);
        }
    });
    
    $("#nextPage").click(function() {
        if (currentPage < totalPages - 1) {
            fetchData(currentPage + 1, pageSize);
        }
    });
    
    $("#lastPage").click(function() {
        if (currentPage < totalPages - 1) {
            fetchData(totalPages - 1, pageSize);
        }
    });
    
    // Sự kiện nhập số trang trực tiếp
    $("#currentPageInput").on("change", function() {
        let requestedPage = parseInt($(this).val()) - 1;
        if (!isNaN(requestedPage) && requestedPage >= 0 && requestedPage < totalPages) {
            fetchData(requestedPage, pageSize);
        } else {
            $(this).val(currentPage + 1); // Reset về trang hiện tại nếu nhập không hợp lệ
        }
    });
    
    // Ngăn chặn submit form khi nhấn Enter trong input số trang
    $("#currentPageInput").on("keydown", function(e) {
        if (e.keyCode === 13) {
            e.preventDefault();
            $(this).trigger("change");
        }
    });
    
    // Tìm kiếm
    $("#searchInput").on("keyup", function(e) {
        if (e.keyCode === 13) {
            fetchData(0, pageSize); // Reset về trang đầu tiên khi tìm kiếm
        }
    });
    
    $("#searchButton").click(function() {
        fetchData(0, pageSize); // Reset về trang đầu tiên khi tìm kiếm
    });
}
$(document).on('click', '.toggle-active-status', function() {
    // Lấy reviewId từ phần tử <a> (chứ không phải <i>)
    var reviewId = $(this).data('id');  // Lấy giá trị data-id từ <a>

    // Kiểm tra trạng thái isActive từ icon trong dòng
    var isActive = $(this).closest('tr').find('.td-check i').hasClass('ri-checkbox-circle-line') ? 1 : 0;

    // Mở modal và gán giá trị trạng thái hiện tại
    $('#statusSelect').val(isActive);  // Gán giá trị trạng thái vào modal
    $('#statusModal').modal('show');  // Hiển thị modal
    $('#updateStatusBtn').data('review-id', reviewId);  // Lưu reviewId vào button để gửi yêu cầu cập nhật
});

$('#updateStatusBtn').on('click', function() {
    var reviewId = $(this).data('review-id');  // Lấy reviewId từ button
    var newStatus = $('#statusSelect').val();  // Lấy trạng thái mới từ dropdown

    console.log('Review ID:', reviewId);
    console.log('New Status:', newStatus);
let token = localStorage.getItem("authToken");

    $.ajax({
        url: `http://localhost:8080/product-rating/${reviewId}/${newStatus}`,  // Đường dẫn API để cập nhật trạng thái
        type: 'PUT',
        contentType: 'application/json',
        headers: {
            "Authorization": "Bearer " + token,
            "Accept": "application/json"
        },
        success: function(response) {
            console.log('Trạng thái đã được cập nhật:', response);

     location.reload(); 

            // Đóng modal sau khi cập nhật
            $('#statusModal').modal('hide');
        },
        error: function(xhr, status, error) {
            console.error("Lỗi khi cập nhật trạng thái:", error);
            alert("Đã xảy ra lỗi khi cập nhật trạng thái!");
        }
    });
});



// Hàm để render sao
function renderStars(rating) {
    let fullStars = Math.floor(rating);  // Tính số sao đầy
    let halfStar = (rating % 1 >= 0.5) ? 1 : 0;  // Kiểm tra sao nửa
    let emptyStars = 5 - fullStars - halfStar;  // Tính số sao rỗng

    let starsHtml = '';

    // Render sao đầy
    for (let i = 0; i < fullStars; i++) {
        starsHtml += '<li><i data-feather="star" class="fill"></i></li>';
    }

    // Render sao nửa
    if (halfStar) {
        starsHtml += '<li><i data-feather="star" class="half-fill"></i></li>';
    }

    // Render sao rỗng
    for (let i = 0; i < emptyStars; i++) {
        starsHtml += '<li><i data-feather="star"></i></li>';
    }

    return starsHtml;
}

// Hàm để kích hoạt tìm kiếm trong bảng
function activateSearch() {
    $("#searchInput").on("keyup", function () {
        var searchValue = $(this).val().toLowerCase();

        $(".review-row").each(function () {
            var rowText = $(this).text().toLowerCase();
            if (rowText.indexOf(searchValue) > -1) {
                $(this).show();  // Hiển thị hàng nếu có từ khóa tìm kiếm
            } else {
                $(this).hide();  // Ẩn hàng nếu không có từ khóa tìm kiếm
            }
        });
    });
}

$(document).ready(function () {
    // Khởi tạo giá trị mặc định cho kích thước trang từ dropdown
    pageSize = parseInt($("#pageSizeSelect").val() || 10);
    
    // Thiết lập sự kiện phân trang
    setupPaginationEvents();
    
    // Lấy dữ liệu ban đầu
    fetchData(0, pageSize);
});