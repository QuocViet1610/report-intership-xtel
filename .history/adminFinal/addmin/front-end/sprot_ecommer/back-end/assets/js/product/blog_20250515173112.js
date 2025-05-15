// Hàm hiển thị danh sách blog
function renderBlogList(blogs) {
    const $tableBody = $("#all-orders");
    $tableBody.empty();
    
    if (blogs.length === 0) {
        $tableBody.append(`
            <tr>
                <td colspan="6" class="text-center">Không có bài viết nào</td>
            </tr>
        `);
        return;
    }
    
    blogs.forEach(blog => {
        // Cắt ngắn nội dung nếu quá dài
        const shortContent = blog.content.length > 50 
            ? blog.content.substring(0, 50) + "..." 
            : blog.content;
            
        // Xử lý ảnh (sử dụng ảnh mặc định nếu URL không hợp lệ)
        const imageUrl = blog.image && blog.image.startsWith("http") 
            ? blog.image 
            : "assets/images/blog-placeholder.jpg";
            
        // Format thời gian
        const createdDate = new Date(blog.createdAt).toLocaleDateString('vi-VN');
        
        $tableBody.append(`
            <tr>
                <td>
                    <div class="table-image">
                        <img src="${imageUrl}" class="img-fluid" alt="Blog Image">
                    </div>
                </td>
                <td>${blog.title}</td>
                <td>${shortContent}</td>
                <td>${getCategoryName(blog.categoryId) || 'Chưa phân loại'}</td>
                <td>
                    <div class="user-info">
                        <img src="${blog.avatar ? blog.avatar : 'assets/images/user-placeholder.png'}" 
                             class="user-avatar" alt="${blog.fullName}">
                        <div>
                            <div class="user-name">${blog.fullName}</div>
                            <div class="user-email">${blog.email}</div>
                        </div>
                    </div>
                </td>
                <td>
                    <ul>
                        <li>
                            <a href="javascript:void(0)" onclick="viewBlog(${blog.id})">
                                <i class="ri-eye-line"></i>
                            </a>
                        </li>
                        <li>
                            <a href="blog-edit.html?id=${blog.id}">
                                <i class="ri-pencil-line"></i>
                            </a>
                        </li>
                        <li>
                            <a href="javascript:void(0)" onclick="deleteBlog(${blog.id})">
                                <i class="ri-delete-bin-line"></i>
                            </a>
                        </li>
                    </ul>
                </td>
            </tr>
        `);
    });
}