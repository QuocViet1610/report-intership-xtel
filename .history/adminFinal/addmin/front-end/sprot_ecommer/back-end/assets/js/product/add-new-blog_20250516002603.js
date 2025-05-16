 $(document).ready(function () {
        $('#btn-create').on('click', function (e) {
            e.preventDefault(); // Ngăn form reload trang

            // Lấy dữ liệu từ form
            const code = $('#codeProdutc').val();
            const category = $('#dropdownButtonObject').text().trim();
            const content = $('#editor').html(); // Nếu bạn dùng Quill hoặc tương tự

            // Kiểm tra dữ liệu cơ bản
            if (!content || category === "Chọn đối tượng") {
                alert("Vui lòng điền đầy đủ thông tin.");
                return;
            }


        });
    });