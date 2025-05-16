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

            // Gọi AJAX
            $.ajax({
                url: '/api/add-post', // Thay bằng endpoint thực tế của bạn
                type: 'POST',
                data: {
                    code: code,
                    category: category,
                    content: content
                },
                success: function (response) {
                    alert('Thêm bài viết thành công!');
                    // Có thể reset form hoặc chuyển hướng
                },
                error: function (xhr) {
                    alert('Đã xảy ra lỗi khi thêm bài viết.');
                    console.log(xhr.responseText);
                }
            });
        });
    });