$(document).ready(function () {

    $.ajax({
        url: "http://localhost:8080/category",
        method: "GET",
        success: function (res) {
            const data = res.data;
            parentIds.forEach(parentId => {
                const level2 = data.filter(cat => cat.level === 2 && findParent(cat, parentId));
                const level3 = data.filter(cat => cat.level === 3 && findParent(cat, parentId));
                let container;
    
                // Chọn container phù hợp dựa trên parentId
                switch (parentId) {
                    case 1:
                        container = $("#category-container");
                        break;
                    case 2:
                        container = $("#category-container-nam");
                        break;
                    case 3:
                        container = $("#category-container-nu");
                        break;
                    case 4:
                        container = $("#category-container-kid");
                        break;
                    case 5:
                        container = $("#category-container-accessory");
                        break;
                }
    
                level2.forEach(level2Cat => {
                    const col = $(`
                        <div class="col-xl-3">
                            <div class="dropdown-column m-0">
                                <h5 class="dropdown-header">
                                    <a href="shop-left-sidebar.html?id=${level2Cat.id}">
                                        ${level2Cat.name}
                                    </a>
                                </h5>
                            </div>
                        </div>
                    `);
    
                    const column = col.find(".dropdown-column");
    
                    level3
                        .filter(l3 => l3.parentId == level2Cat.id)
                        .forEach(item => {
                            const link = $(`
                                <a class="dropdown-item" href="shop-left-sidebar.html?id=${item.id}">
                                    ${item.name}
                                </a>
                            `);
                            column.append(link);
                        });
    
                    container.append(col);
                });
            });
        },
        error: function (err) {
            console.error("Lỗi khi gọi API:", err);
            showError("Không thể tải danh mục. Vui lòng thử lại sau!");
        }
    });
    
  
});
