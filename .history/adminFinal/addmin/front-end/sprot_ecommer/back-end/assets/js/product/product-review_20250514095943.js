function fetchData() {
    let token = localStorage.getItem("authToken");
    let searchRequest = { searchText: "" };

    $.ajax({
        url: `http://localhost:8080/product-rating/search?page=0&size=20&sort=id.asc`,
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

            let tableRows = ''; // Chuỗi HTML chứa các hàng của bảng

            reviews.forEach(review => {
                                let statusIcon = review.isActive === 1
                    ? '<i class="ri-checkbox-circle-line"></i>'
                    : '<i class="ri-close-circle-line"></i>';
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
                        <td style="max-width: 500px; word-wrap: break-word; white-space: normal;">
                        ${review.productCode}</td>
                        <td>
                            <ul class="rating">
                                ${review.ratingValue} <i class="fa-solid fa-star" style="color: #FFD700;"></i> <!-- Render sao dựa trên rating -->
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
            feather.replace();

            // Kích hoạt tìm kiếm trong bảng
            activateSearch();
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
            showError(errorMessage);
        }
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
            
            // Cập nhật trạng thái trong bảng sau khi cập nhật thành công
            if (newStatus == 1) {
                $(`[data-id=${reviewId}]`).closest('tr').find('.td-check i').removeClass('ri-close-circle-line').addClass('ri-checkbox-circle-line');
            } else {
                $(`[data-id=${reviewId}]`).closest('tr').find('.td-check i').removeClass('ri-checkbox-circle-line').addClass('ri-close-circle-line');
            }

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
    fetchData();  // Lấy dữ liệu đánh giá khi trang tải
});
