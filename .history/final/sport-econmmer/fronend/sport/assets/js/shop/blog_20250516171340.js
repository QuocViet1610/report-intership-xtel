let currentPage = 0;
let pageSize = 5;
let totalElements = 0;
let totalPages = 0;
let searchText = "";
let categoryId = null;


function getCategoryName(categoryId) {
  switch (parseInt(categoryId)) {
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
// Function to render blog cards
function renderBlogCards(blogs) {
    // Select the container where blog cards will be displayed
    const blogContainer = document.querySelector('.row.g-4.ratio_65');
    
    // Clear existing content
    blogContainer.innerHTML = '';
    
    // Check if there are no blogs
    if (!blogs || blogs.length === 0) {
        blogContainer.innerHTML = '<div class="col-12 text-center"><p>Không có bài viết nào</p></div>';
        return;
    }

    // Loop through each blog and create a card
    blogs.forEach((blog, index) => {
        // Format the date
        const createdDate = new Date(blog.createdAt).toLocaleDateString('vi-VN');
        
        // Create a delay value for animation based on index
        const delay = index * 0.05;
        
        // Create blog card HTML
        const blogCardHTML = `
            <div class="col-xxl-6 col-sm-6">
                <div class="blog-box wow fadeInUp" data-wow-delay="${delay}s">
                    <div class="blog-image">
                        <a href="blog-detail.html?id=${blog.id}">
                            <img src="${blog.image}" class="bg-img blur-up lazyload" alt="${blog.title}">
                        </a>
                    </div>

                    <div class="blog-contain">
                        <div class="blog-label">
                            <span class="time"><i data-feather="clock"></i> <span>${createdDate}</span></span>
                            <span class="super"> <span>${getCategoryName(blog.categoryId)}</span></span>
                        </div>
                        <a href="blog-detail.html?id=${blog.id}">
                            <h3>${blog.title}</h3>
                        </a>
                    </div>
                </div>
            </div>
        `;
        
        // Append the card to the container
        blogContainer.innerHTML += blogCardHTML;
    });
    
    // Re-initialize Feather icons (if they're being used)
    if (typeof feather !== 'undefined') {
        feather.replace();
    }
}

// Function to update pagination controls based on API response
function updatePaginationControls(pageData) {
    const paginationContainer = document.querySelector('.custom-pagination ul');
    
    if (!paginationContainer) return;
    
    const currentPage = pageData.number;
    const totalPages = pageData.totalPages;
    
    let paginationHTML = '';
    
    // First page button
    paginationHTML += `
        <li class="page-item ${currentPage === 0 ? 'disabled' : ''}">
            <a class="page-link" href="javascript:void(0)" onclick="goToPage(0)" tabindex="-1">
                <i class="fa-solid fa-angles-left"></i>
            </a>
        </li>
    `;
    
    // Page numbers
    for (let i = 0; i < totalPages; i++) {
        paginationHTML += `
            <li class="page-item ${i === currentPage ? 'active' : ''}">
                <a class="page-link" href="javascript:void(0)" onclick="goToPage(${i})">${i + 1}</a>
            </li>
        `;
    }
    
    // Last page button
    paginationHTML += `
        <li class="page-item ${currentPage === totalPages - 1 ? 'disabled' : ''}">
            <a class="page-link" href="javascript:void(0)" onclick="goToPage(${totalPages - 1})">
                <i class="fa-solid fa-angles-right"></i>
            </a>
        </li>
    `;
    
    paginationContainer.innerHTML = paginationHTML;
}

// Function to navigate to a specific page
function goToPage(page) {
    searchBlogs(page);
}

// Modify your existing searchBlogs function to call these render functions
function searchBlogs(page = currentPage, size = pageSize) {
    let token = localStorage.getItem("authToken");
    let searchRequest = {
        searchText: searchText || "",
        categoryId: categoryId || null
    };

    $.ajax({
        url: `http://localhost:8080/blog/search?page=${page}&size=${size}`,
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

                
                // Get page data and blog content
                const pageData = response.data;
                const blogs = pageData.content;
                
                // Update pagination variables
                currentPage = pageData.number;
                pageSize = pageData.size;
                totalElements = pageData.totalElements;
                totalPages = pageData.totalPages;
                
                // Render blog cards
                renderBlogCards(blogs);
                
                // Update pagination controls
                updatePaginationControls(pageData);
                
                // Update pagination info if that element exists
                if ($("#paginationInfo").length) {
                    updatePaginationInfo();
                }
            } else {
                showError("Lỗi khi tải dữ liệu: " + response.message);
            }
        },
        error: function(xhr) {
            let errorMessage = xhr.responseJSON && xhr.responseJSON.message
                ? xhr.responseJSON.message
                : "Có lỗi xảy ra! Mã lỗi: " + xhr.status;
            showError(errorMessage);
        }
    });
}

// Function to show errors (if not already defined)
function showError(message) {
    console.error(message);
    // You can implement a more user-friendly error display here
    // For example, using a toast notification or alert
    alert(message);
}

// Initialize everything when the document is ready
$(document).ready(function() {
    // Initial search to load blogs
    searchBlogs();
       
    // Load recent posts for the sidebar
    fetchLatestBlogs();
});

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
        url: `http://localhost:8080/blog/search?page=0&size=5&sort=createdAt,desc`, // Sort by creation date descending
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
