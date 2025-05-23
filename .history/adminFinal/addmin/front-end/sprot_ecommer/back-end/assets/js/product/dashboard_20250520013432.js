
$(document).ready(function () {
let token = localStorage.getItem("authToken");
console.log("dashboard")
$.ajax({
  url: `http://localhost:8080/order/dashboard`,
  type: "GET",
  contentType: "application/json",
  headers: {
    "Authorization": "Bearer " + token,
    "Accept": "application/json"
  },
  success: function (response) {
    if (response.code === "200" && response.data) {

        const data = response.data;

        // Format tiền Việt Nam
        function formatCurrency(value) {
            return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
        }

        // Gắn dữ liệu vào HTML
        $("#totalRevenue").text(formatCurrency(data.totalRevenue));
        $("#totalOrders").text(data.totalOrders);
        $("#totalProduct").text(data.totalProduct);
        $("#totalUser").text(data.totalUser);


    } else {
      $(".product-group").html("<tr><td colspan='4' class='text-center'>Không có dữ liệu</td></tr>");
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

    showError(errorMessage);
  }

});

    let searchRequest = {
        searchText: "", // Chuỗi tìm kiếm trống
        brandId: null,
        categorySearch: null,
        genderId: null,
        productIsActive: 1,
        fullParentId: null
    };

$.ajax({
    url: `http://localhost:8080/product/search?page=0&size=5`,
            method: "POST",
            dataType: "json",
            data: JSON.stringify(searchRequest),
            contentType: "application/json",
            headers: {
                "Authorization": "Bearer " + token,
                "Accept": "application/json"
            },
success: function (response) {
    if (response.code === "200" && response.data && response.data.content) {
        $('#product-table-body').empty();
        const data = response.data.content; // Lấy mảng sản phẩm trong content

        console.log("data");
        console.log(data);

        data.forEach(product => {
            const imagesArray = Array.isArray(product.productImages)
                ? product.productImages
                : Array.from(product.productImages || []);

            const primaryImageObj = imagesArray.find(img => img.isPrimary === 1) || imagesArray[0];
            const primaryImageUrl = primaryImageObj ? primaryImageObj.imageUrl : 'default-image.png';

            $('#product-table-body').append(`
                <tr>
                    <td>
                        <div class="best-product-box">
                            <div class="product-image">
                                <img src="${primaryImageUrl}" class="img-fluid" alt="Sản phẩm">
                            </div>
                            <div class="product-name">
                                <h5 style="font-size: 13px;">${product.productName}</h5>
                                <h6>${new Date(product.createdAt).toLocaleDateString()}</h6>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="product-detail-box">
                            <h6>Giá</h6>
                            <h5>${product.productPrice?.toLocaleString() || 0}đ</h5>
                        </div>
                    </td>
                    <td>
                        <div class="product-detail-box">
                            <h6>Đã bán</h6>
                            <h5>${product.productTotalSold}</h5>
                        </div>
                    </td>
                    <td>
                        <div class="product-detail-box">
                            <h6>Tổng giá</h6>
                            <h5>${product.productTotalSold * product.productPrice?.toLocaleString() || 0}đ</h5>
                        </div>
                    </td>
                    <td>
                        <div class="product-detail-box">
                            <h6>Thể loại</h6>
                            <h5>${product.categoryName || ''}</h5>
                        </div>
                    </td>
                </tr>
            `);
        });
    } else {
        showError("Không có dữ liệu sản phẩm hoặc lỗi server.");
    }
}
,
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
        showError(errorMessage);
    }
});

  $.ajax({
    url: `http://localhost:8080/order/topOrder`,
    type: "GET",
    contentType: "application/json",
    headers: {
      "Authorization": "Bearer " + token,
      "Accept": "application/json"
    },
    success: function (response) {
        if (response.code === "200" && response.data) {
            $('#product-table-body-order').empty();
            const data = response.data;  // Lấy danh sách các đơn hàng
            
            // Duyệt qua từng đơn hàng và thêm vào bảng
            data.forEach(order => {
                let statusText = "";
                switch (order.statusId) {
                    case 1:
                        statusText = "Đang chờ xử lý";
                        break;
                    case 2:
                        statusText = "Đang xử lý";
                        break;
                    case 3:
                        statusText = "Vận chuyển";
                        break;
                    case 4:
                        statusText = "Thành công";
                        break;
                    default:
                        statusText = "Không xác định";
                        break;
                }

                $('#product-table-body-order').append(`
                    <tr>
                        <td>
                            <div class="best-product-box">
                                <div class="product-name">                                                               
                                    <h6>${order.orderCode}</h6>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="product-detail-box">
                                <h6>Ngày Đặt Hàng</h6>
                                <h5>${new Date(order.createdAt).toLocaleDateString()}</h5>
                            </div>
                        </td>
                        <td>
                            <div class="product-detail-box">
                                <h6>Tổng giá</h6>
                                <h5>${order.finalPrice.toLocaleString()}đ</h5>
                            </div>
                        </td>
                        <td>
                            <div class="product-detail-box">
                                <h6>Trạng Thái Đơn Hàng</h6>
                                <h5>${statusText}</h5>
                            </div>
                        </td>
                    </tr>
                `);
            });
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
  
      showError(errorMessage);
    }
  
  });
});