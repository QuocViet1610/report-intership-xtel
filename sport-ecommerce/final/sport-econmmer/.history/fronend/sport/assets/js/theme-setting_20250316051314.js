/*=====================
      Color Picker
==========================*/
var color_picker1 = document.getElementById("colorPick").value;
document.getElementById("colorPick").onchange = function () {
    color_picker1 = this.value;
    document.body.style.setProperty("--theme-color", color_picker1);
    document.body.style.setProperty("--theme-color-rgb", color_picker1);
};

/*========================
 Dark setting js
 ==========================*/
$("#darkButton").on("click", function () {
    var href = $("#color-link").attr("href");
    $("body").removeClass("light");
    $("body").addClass("dark");
    document
        .getElementById("color-link")
        .setAttribute("href", "../assets/css/dark.css");
});

$("#lightButton").on("click", function () {
    var href = $("#color-link").attr("href");
    $("body").removeClass("dark");
    $("body").addClass("light");
    document
        .getElementById("color-link")
        .setAttribute("href", "../assets/css/style.css");
    console
});

/*========================
   RTL setting js
   ==========================*/
$(".rtl").on("click", function () {
    if ($("body").hasClass("ltr")) {
        $("html").attr("dir", "rtl");
        $("body").removeClass("ltr");
        $("body").addClass("rtl");
        $("#rtl-link").attr("href", "../assets/css/vendors/bootstrap.rtl.css");
    } else {
        $("html").attr("dir", "");
        $("body").removeClass("rtl");
        $("body").addClass("ltr");
        $("#rtl-link").attr("href", "../assets/css/vendors/bootstrap.css");
    }
});


$(document).ready(function () {
    window.showError = function (message) {
        $(".error-message").remove(); // Xóa thông báo cũ (nếu có)

        let errorHtml = `
            <div class="error-message">
                <div class="error-icon close-error">✖</div>
                <div class="error-text">${message}</div>
            </div>
        `;

        $("body").append(errorHtml); // Thêm vào cuối trang

        // Xử lý sự kiện khi click vào nút đóng (✖)
        $(".close-error").click(function () {
            $(this).parent().fadeOut(300, function () {
                $(this).remove();
            });
        });

        setTimeout(function () {
            $(".error-message").addClass("fade-out");
            setTimeout(function () {
                $(".error-message").remove();
            }, 500);
        }, 4000); // Hiển thị 4 giây rồi tự động ẩn
    };
});