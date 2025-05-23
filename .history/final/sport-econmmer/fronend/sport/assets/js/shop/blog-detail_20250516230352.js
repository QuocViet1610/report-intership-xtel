

$(document).ready(function() {
    fetchUserData();

     fetchLatestBlogs();
});


function fetchUserData() {
    let token = localStorage.getItem("authToken");
    
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const id = urlParams.get('id') || 0;

    if(id) {
        $.ajax({
            url: `http://localhost:8080/blog/get-detail/${id}`,
            type: 'GET',
            contentType: 'application/json',
            headers: {
                "Authorization": "Bearer " + token,
                "Accept": "application/json"
            },
            success: function(response) {
                console.log(response);
           
                 var data = response.data;


            $('.blog-detail-image').css('background-image', `url("${data.image}")`);
            
            // Nếu bạn muốn đồng thời cập nhật src của thẻ img bên trong
            $('#imageBanner').attr('src', data.image);
                // Gắn tiêu đề bài viết
                $('#titleBlog').text(data.title);

                // Gắn ngày tạo, định dạng dd/mm/yyyy
                let createdDate = new Date(data.createdAt);
                let formattedDate = createdDate.getDate().toString().padStart(2, '0') + '/' +
                                    (createdDate.getMonth() + 1).toString().padStart(2, '0') + '/' +
                                    createdDate.getFullYear();
                $('#createAT').text(formattedDate);

                // Gắn tên chuyên mục
                $('.contain-list li').text(getCategoryName(data.categoryId));

                // Gắn nội dung bài viết (HTML) vào chỗ nội dung
                $('.blog-detail-contain').prepend(`<div class="blog-content">${data.content}</div>`);
            },
            error: function(xhr, status, error) {
                // Xử lý khi có lỗi
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
                }
                if (typeof showError === 'function') {
                    showError(errorMessage);
                } else {
                    alert(errorMessage);
                }
            }
        });
    }
    function getCategoryName(categoryId) {
  switch (categoryId) {
    case 1:
      return "Chọn đồ thể thao";
    case 2:
      return "Bài tập thể thao";
    case 3:
      return "Sự kiện thể thao";
    default:
      return "Không xác định";
  }
}
}

function renderRecentPosts(blogs) {
    // Select the container for recent posts
    const recentPostsContainer = document.querySelector('.recent-post-box');
    
    // Clear existing content
    if (recentPostsContainer) {
        recentPostsContainer.innerHTML = '';
        
        // Check if there are no blogs
        if (!blogs || blogs.length === 0) {
            recentPostsContainer.innerHTML = '<p class="text-center">Không có bài viết mới</p>';
            return;
        }
        
        // Loop through each blog and create a recent post box
        blogs.forEach(blog => {
            // Format the date
            const createdDate = new Date(blog.createdAt).toLocaleDateString('vi-VN');
            
            // Create the HTML for each recent post
            const recentPostHTML = `
                <div class="recent-box">
                    <a href="blog-detail.html?id=${blog.id}" class="recent-image">
                        <img src="${blog.image}" class="img-fluid blur-up lazyload" alt="${blog.title}">
                    </a>
                    <div class="recent-detail">
                        <a href="blog-detail.html?id=${blog.id}">
                            <h5 class="recent-name">${blog.title}</h5>
                        </a>
                        <h6>${createdDate}</h6>
                    </div>
                </div>
            `;
            
            // Append to the container
            recentPostsContainer.innerHTML += recentPostHTML;
        });
    }
}

// Function to fetch the latest blogs for the sidebar
function fetchLatestBlogs() {
    let token = localStorage.getItem("authToken");
    let searchRequest = {
        searchText: "",
        categoryId: null
    };
    
    $.ajax({
        url: `http://localhost:8080/blog/search?page=0&size=5&sort=createdAt.desc`, // Sort by creation date descending
        method: "POST",
        dataType: "json",
        data: JSON.stringify(searchRequest),
        contentType: "application/json",
        headers: {
            "Authorization": "Bearer " + token,
            "Accept": "application/json"
        },
        success: function(response) {
            if (response.code === "200") {
                console.log("Latest blogs received:", response);
                
                // Get blog content
                const blogs = response.data.content;
                
                // Render recent posts
                renderRecentPosts(blogs);
            } else {
                console.error("Error loading latest blogs:", response.message);
            }
        },
        error: function(xhr) {
            let errorMessage = xhr.responseJSON && xhr.responseJSON.message
                ? xhr.responseJSON.message
                : "Có lỗi xảy ra khi tải bài viết mới! Mã lỗi: " + xhr.status;
            console.error(errorMessage);
        }
    });
}
