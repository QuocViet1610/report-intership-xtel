       $(document).ready(function () {
       let token = localStorage.getItem("authToken");

        $.ajax({
            url: `http://localhost:8080/blog/search?page=0&size=10`,
            method: "POST",
            dataType: "json",
            data: JSON.stringify(searchRequest),
            contentType: "application/json",
            headers: {
                "Authorization": "Bearer " + token,
                "Accept": "application/json"
            },
            success: function (response) {
                if (response.code === "200") {
                    let products = response.data.content.map(item => new Product(item));
                    renderProductTable(products);
                    totalItems = response.data.totalElements;
                    updatePagination();
                } else {
                    console.warn("Lỗi API:", response.message);
                }
            },
            error: function (xhr) {
                let errorMessage = xhr.responseJSON && xhr.responseJSON.message
                    ? xhr.responseJSON.message
                    : "Có lỗi xảy ra! Mã lỗi: " + xhr.status;
                showError(errorMessage);
            }
        });
    
    });