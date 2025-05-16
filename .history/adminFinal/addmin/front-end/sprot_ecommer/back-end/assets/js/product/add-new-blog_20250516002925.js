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
const formData = new FormData();
formData.append('code', $('#codeProdutc').val());
formData.append('category', $('#dropdownButtonObject').text().trim());
formData.append('content', $('#editor').html());
formData.append('image', $('#thumbnailImage')[0].files[0]);

console.log(formData)
    document.getElementById('thumbnailImage').addEventListener('change', function (event) {
        const reader = new FileReader();
        reader.onload = function () {
            const preview = document.getElementById('previewImage');
            preview.src = reader.result;
            preview.style.display = 'block';
        }
        reader.readAsDataURL(event.target.files[0]);
    });
        });


    });