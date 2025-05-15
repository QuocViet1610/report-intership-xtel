       $(document).ready(function () {
       let token = localStorage.getItem("authToken");
    let searchRequest = {
        searchText: "", // Chuỗi tìm kiếm trống
    };

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
                console.log(response)
            },
            error: function (xhr) {
                let errorMessage = xhr.responseJSON && xhr.responseJSON.message
                    ? xhr.responseJSON.message
                    : "Có lỗi xảy ra! Mã lỗi: " + xhr.status;
                showError(errorMessage);
            }
        });
    
    });