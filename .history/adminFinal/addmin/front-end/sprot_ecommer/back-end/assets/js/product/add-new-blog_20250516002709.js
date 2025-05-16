 $(document).ready(function () {
        $('#btn-create').on('click', function (e) {
            e.preventDefault(); // Ngăn form reload trang

            // Lấy dữ liệu từ form
            const code = $('#codeProdutc').val();
            const category = $('#dropdownButtonObject').text().trim();
            const content = $('#editor').html(); // Nếu bạn dùng Quill hoặc tương tự
    let description = document.querySelector(".ck-editor__editable").innerHTML;
    if (description.trim() === '<p><br data-cke-filler="true"></p>') {
        description = null;
    }   

    console.log()



        });
    });