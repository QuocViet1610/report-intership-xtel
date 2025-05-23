$(document).ready(function () {
  function isTokenExpired(token) {
    try {
        const payload = JSON.parse(atob(token.split('.')[1])); // Decode phần payload của JWT
        const exp = payload.exp * 1000; // Chuyển từ giây sang milliseconds

        return Date.now() >= exp; // Kiểm tra nếu token đã hết hạn
    } catch (e) {

        return true; // Mặc định là hết hạn nếu có lỗi
    }
}

let token = localStorage.getItem("authToken");

if (!token) {
    showError("Bạn chưa đăng nhập!");
    return; 
} else if (isTokenExpired(token)) {
    showError("Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại!");
    localStorage.removeItem("authToken"); // Xóa token cũ
} else {
    
}
  // let currentPage = 1;
  // let pageSize = 5;
  // let totalPages = 1;
  // let searchRequest = { searchText: "" };

  // let typingTimer;
  // const typingDelay = 500;

  let searchRequest = {};
  // // Xử lý sự kiện nhập tìm kiếm (Live Search)
//   $("#searchInput").on("input", function () {
//     searchText = $(this).val().trim();
//     searchRequest.searchText = searchText;
//     fetchData();
//   });
  fetchData();
  $("#searchInput").on("input", function() {
    let searchTerm = $(this).val().toLowerCase(); // Get the search term
    if (searchTerm === "") {
        // Nếu không có gì trong search input, gọi fetchData
        fetchData();
    } else {
        // Nếu có từ khóa tìm kiếm, gọi hàm filterTable
        filterTable(searchTerm);
    }
});

// Function to filter the table rows based on the search term
function filterTable(searchTerm) {

    let tableBody = $(".product-group");
    let rows = tableBody.find("tr");

    rows.each(function() {
        let rowText = $(this).text().toLowerCase();
        if (rowText.indexOf(searchTerm) === -1) {
            $(this).hide(); // Hide the row if it doesn't match the search term
        } else {
            $(this).show(); // Show the row if it matches the search term
        }
    });
}
  // Gửi yêu cầu AJAX để lấy dữ liệu
  function fetchData() {
    let token = localStorage.getItem("authToken");

    $.ajax({
      url: `http://localhost:8080/category/search?page=-1&size=10&sort=id.asc`,
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(searchRequest),
      headers: {
        "Authorization": "Bearer " + token,
        "Accept": "application/json"
      },
      success: function (response) {
        if (response.code === "200" && response.data) {


          renderTable(response.data);

        } else {
          $(".product-group").html("<tr><td colspan='4' class='text-center'>Không có dữ liệu</td></tr>");
        }
      },
      error: function (xhr, status, error) {
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
        } else {
          errorMessage = "Lỗi mạng hoặc máy chủ không phản hồi!";
        }

        showError(errorMessage);
      }
    });
  }

  // Hiển thị dữ liệu vào bảng
  function renderTable(data) {
    let tableBody = $(".product-group");
    tableBody.empty();
    if (!Array.isArray(data) || data.length === 0) {
        tableBody.append("<tr><td colspan='4' class='text-center'>Không có dữ liệu</td></tr>");
        return;
    }
    let parents = [];
    let children = {};
    // Separate parents and children
    data.forEach(item => {
        if (item.parentId == 0) {
            parents.push(item); // Parent item
        } else {
            if (!children[item.parentId]) {
                children[item.parentId] = [];
            }
            children[item.parentId].push(item); // Child item
        }
    });

    // Render parents first
    parents.forEach(parent => {
        let hasChildren = children[parent.id] && children[parent.id].length > 0;
        
        let expandIcon = hasChildren ? 
            '<i class="ri-add-line expand-icon" style="cursor: pointer; margin-right: 5px;"></i>' : 
            '<i style="margin-right: 5px; visibility: hidden;"></i>';
        
        let row = `
            <tr class="parent-row" data-id="${parent.id}">
                <td>${expandIcon}${parent.categoryName}</td>
                <td id="${parent.parentId}">${parent.parentName ? parent.parentName : "Không có"}</td>
                <td>
                    <ul>
        <li>
        <a href="#" class="edit-btn" data-id="${parent.id}" data-name="${parent.categoryName}" data-parent-name="${parent.parentName}" data-parent-id="${parent.parentId}">
            <i class="ri-pencil-line"></i>
        </a>
        </li>
                        <li><a href="#" class="delete-btn" data-id="${parent.id}" data-name="${parent.name}" data-bs-toggle="modal" data-bs-target="#deleteModal">
                            <i class="ri-delete-bin-line"></i></a>
                        </li>
                    </ul>
                </td>
            </tr>
        `;
        tableBody.append(row);
        
        // Pre-render children but keep them hidden initially
        renderChildren(parent.id, 1, true);
    });

    // Function to recursively render children for each parent
    function renderChildren(parentId, level, hidden = false) {
        if (children[parentId]) {
            children[parentId].forEach(child => {
                let hasGrandchildren = children[child.id] && children[child.id].length > 0;
                
                let expandIcon = hasGrandchildren ? 
                    '<i class="ri-add-line expand-icon" style="cursor: pointer; margin-right: 5px;"></i>' : 
                    '<i style="margin-right: 5px; visibility: hidden;"></i>';
                
                let childRow = `
                    <tr class="child-row child-of-${parentId}" ${hidden ? 'style="display: none;"' : ''} data-id="${child.id}">
                        <td>${'&nbsp;&nbsp;'.repeat(level * 4)}${expandIcon}${child.categoryName}</td>
                        <td id="${child.parentId}">${child.parentName ? child.parentName : "Không có"}</td>
                        <td>
                            <ul>
                                  <a href="#" class="edit-btn" data-id="${child.id}" data-name="${child.categoryName}" data-parent-name="${child.parentName}" data-parent-id="${child.parentId}">
    <i class="ri-pencil-line"></i>
  </a>
                                <li><a href="#" class="delete-btn" data-id="${child.id}" data-name="${child.name}" data-bs-toggle="modal" data-bs-target="#deleteModal">
                                    <i class="ri-delete-bin-line"></i></a>
                                </li>
                            </ul>
                        </td>
                    </tr>
                `;
                tableBody.append(childRow);
                
                // Recursive call for further children
                renderChildren(child.id, level + 1, hidden);
            });
        }
    }

    
    // Add click event handler for expand/collapse functionality
    $(document).on('click', '.parent-row, .child-row', function(e) {
        // Only trigger when clicking on the row itself or the expand icon, not on action buttons
        if (!$(e.target).closest('ul').length && !$(e.target).closest('a').length) {
            let rowId = $(this).data('id');
            let childRows = $('.child-of-' + rowId);
    
            if (childRows.length > 0) {
                // Kiểm tra trạng thái và trì hoãn thực thi
                if (childRows.first().is(':visible')) {
                    console.log("hide");
                    // Trì hoãn trước khi ẩn các dòng con
                    setTimeout(function() {
                        hideAllChildren(rowId);
                        // Change icon from minus to plus
                        $(this).find('.expand-icon').removeClass('ri-subtract-line').addClass('ri-add-line');
                    }.bind(this), 10);  // Thực hiện sau 1 giây
    
                } else {
                    console.log("show");
                    // Trì hoãn trước khi hiển thị các dòng con
                    setTimeout(function() {
                        childRows.show();
                        // Change icon from plus to minus
                        $(this).find('.expand-icon').removeClass('ri-add-line').addClass('ri-subtract-line');
                    }.bind(this), 10);  // Thực hiện sau 1 giây
                }
            }
        }
    });
    
    
    // Function to recursively hide all children
    function hideAllChildren(parentId) {
        let directChildren = $('.child-of-' + parentId);
        directChildren.hide();
        
        // Recursively hide children of each child
        directChildren.each(function() {
            let childId = $(this).data('id');
            hideAllChildren(childId);
            
            // Reset icon for this child
            $(this).find('.expand-icon').removeClass('ri-subtract-line').addClass('ri-add-line');
        });
    }
}

// Xóa sự kiện click cũ để tránh trùng lặp nếu hàm được gọi nhiều lần
$(document).off('click', '.parent-row, .child-row');


$("#openModalLink").on("click", function(e) {
    e.preventDefault(); // Prevent the default action (if any)
    
    // Show the modal
    $("#attributeModal").modal('show');
});

// Modal logic to close and handle button actions


$('#saveCategory').on('click', function() {
    // $('#attributeModal').modal('hide');
});


handleCategoriesResponse();
function handleCategoriesResponse() {
    $('#myDropdownModal').empty();  // Clear the existing dropdown

    // Now append the new search input item
    var searchItem = $('<li>').append(
        $('<input>', {
            type: 'text',
            class: 'form-control',
            id: 'myInputModal',
            placeholder: 'Tím kiếm...',
            onkeyup: 'filterFunctionModelAttribute()', // Adjust if necessary
            style: 'height: 45px; font-size: 16px;'
        })
    );
    var noSelectItem = $('<div>', {
        class: 'dropdown-item',
        text: 'Chọn thể loại',
        click: function() {
            selectItemmModelUpdate(null);
            $('#dropdownButtonModalUpdate').text('Chọn thể loại');
        }
    });
    $('#myDropdownModal').append(searchItem);
    $('#myDropdownModal').append(noSelectItem);
$.ajax({
    url: "http://localhost:8080/category",  
    type: "GET",
    contentType: "application/json",
    headers: {
        "Authorization": "Bearer " + token,
        "Accept": "application/json"
    },
    success: function(response) {

        if(response.code == 200) {
            var categories = response.data;
            var parents = [];
            var children = {};

            categories.forEach(function(category) {
                if (category.parentId == 0) {
                    // Mục chính (parent)
                    parents.push(category);
                } else {
                    // Mục con (child)
                    if (!children[category.parentId]) {
                        children[category.parentId] = [];
                    }
                    children[category.parentId].push(category);
                }
            });

            function addChildrenRecursively(parentId, level) {
                if (children[parentId]) {
                    children[parentId].forEach(function(child) {
                        var childItem = $('<div>', {
                            class: 'dropdown-item',
                            html: '&nbsp;&nbsp;&nbsp;&nbsp;'.repeat(level) + child.name,
                            click: function() {
                                selectItem(child.id); 
                                $('#dropdownButton').text(child.name);
                            }
                        });


                        $('#myDropdown').append(childItem);

                        var childItemModel = $('<div>', {
                            class: 'dropdown-item',
                            html: '&nbsp;&nbsp;&nbsp;&nbsp;'.repeat(level) + child.name,
                            click: function() {
                                selectItemmModel(child.id);
                                $('#dropdownButtonModal').text(child.name);
                            }
                        });
                        // Thêm mục con vào dropdown trong modal
                        $('#myDropdownModal').append(childItemModel);

                        addChildrenRecursively(child.id, level + 1);
                    });
                }
            }


            parents.forEach(function(parent) {
                var parentItem = $('<div>', {
                    class: 'dropdown-item',
                    text: parent.name,
                    click: function() {
                        selectItem(parent.id); // Khi chọn mục cha, lưu ID vào value
                        $('#dropdownButton').text(parent.name);

                    }   
                });

                $('#myDropdown').append(parentItem);

                var parentItem = $('<div>', {
                    class: 'dropdown-item',
                    text: parent.name,
                    click: function() {
                        selectItemmModel(parent.id); 
                        $('#dropdownButtonModal').text(parent.name);
                    }   
                });
 
                $('#myDropdownModal').append(parentItem);

                addChildrenRecursively(parent.id, 1);
            });
        } else {
            alert('Lỗi: ' + response.message);
        }
    },
    error: function(xhr, status, error) {
        var errorMessage = error;
        if (xhr.responseJSON && xhr.responseJSON.message) {
            errorMessage = xhr.responseJSON.message;
        } else if (xhr.responseText) {
            try {
            var response = JSON.parse(xhr.responseText);
            errorMessage = response.message || error;
            } catch(e) {
            errorMessage = error;
            }
        }
        if (!errorMessage || errorMessage.trim() === "") {
            errorMessage = "Đã xảy ra lỗi không xác định. Vui lòng thử lại sau!";
        }
        showError( errorMessage);
        let categoryDropdown = $("#myDropdown");
        categoryDropdown.empty();
        categoryDropdown.append('<div class="dropdown-item" disabled>Không thể tải thể loại</div>');
    }
});
    }

let valueModel = ""; 
function selectItemmModel(id) {
    valueModel = id;
}

//thêm
$('#saveCategory').on('click', function() {
    var categoryName = $('#nameCateogryInput').val();
    // Tạo đối tượng FormData
    var formData = new FormData();
    var data = {
      name: categoryName,
      parentId: valueModel
    };
    formData.append('data', JSON.stringify(data));
    $.ajax({
      url: 'http://localhost:8080/category', 
      type: 'POST',
      data: formData,
      headers: {
        "Authorization": "Bearer " + token,
        "Accept": "application/json"
    },
      contentType: false,
      processData: false,
      success: function(response) {
        $('#attributeModal').modal('hide');
        handleCategoriesResponse();
        $('#nameCateogryInput').val('');
        // selectItem(response.data.id);
        $('#dropdownButton').text(response.data.name);
        fetchData();
      },
    error: function(xhr, status, error) {
        var errorMessage = error;
        if (xhr.responseJSON && xhr.responseJSON.message) {
            errorMessage = xhr.responseJSON.message;
        } else if (xhr.responseText) {
            try {
            var response = JSON.parse(xhr.responseText);
            errorMessage = response.message || error;
            } catch(e) {
            errorMessage = error;
            }
        }

        showError( errorMessage);
        fetchData();  
        }
    });
  });

  //xoá
$(document).on('click', '.delete-btn', function() {
    var categoryId = $(this).data('id');  // Lấy ID của category từ thuộc tính data-id
    $('#exampleModalToggle').modal('show'); 
    // Hiển thị thông tin trong modal xác nhận xóa
    $('#exampleModalToggle .modal-body p').text("Bạn có chắc chắn muốn xóa nhóm hàng này?");
    $('#confirmDelete').off('click').on('click', function() {
        $.ajax({
            url: 'http://localhost:8080/category/' + categoryId,  // Đảm bảo địa chỉ API đúng
            type: 'DELETE',
            headers: {
                "Authorization": "Bearer " + token,  // Thêm Bearer token nếu cần
                "Accept": "application/json"
            },
            success: function(response) {
                $('#deleteModal').modal('hide');  
                showSuccess("Xoá thành công")
                handleCategoriesResponse();
                fetchData();  
            },
            error: function(xhr, status, error) {
                var errorMessage = error;
                if (xhr.responseJSON && xhr.responseJSON.message) {
                    errorMessage = xhr.responseJSON.message;
                } else if (xhr.responseText) {
                    try {
                        var response = JSON.parse(xhr.responseText);
                        errorMessage = response.message || error;
                    } catch(e) {
                        errorMessage = error;
                    }
                }
                showError(errorMessage);  
                $('#exampleModalToggle').modal('hide'); // Đóng modal xác nhận
                // $('#exampleModalToggle2 .modal-body .remove-box').html('<h4 class="text-content">' + errorMessage + '</h4>');
                // $('#exampleModalToggle2').modal('show');  // Hiển thị modal thông báo lỗi
            }
        });
    });

});


// update
function handleCategoriesResponseUpdate(id) {
    // Clear the existing dropdown content
    $('#myDropdownModalUpdate').empty();  // Clear dropdown in the modal

    // Now append the new search input item
    var searchItem = $('<li>').append(
        $('<input>', {
            type: 'text',
            class: 'form-control',
            id: 'myInputModalUpdate',
            placeholder: 'Tìm kiếm...',
            onkeyup: 'filterFunctionModelAttributeUpdate()', // Adjust if necessary
            style: 'height: 45px; font-size: 16px;'
        })
    );
    var noSelectItem = $('<div>', {
        class: 'dropdown-item',
        text: 'Chọn thể loại',
        click: function() {
            selectItemmModelUpdate(null);
            $('#dropdownButtonModalUpdate').text('Chọn thể loại');
        }
    });
    // Append the search input to the dropdown
    $('#myDropdownModalUpdate').append(searchItem);
    $('#myDropdownModalUpdate').append(noSelectItem);
    $.ajax({
        url: "http://localhost:8080/category",  
        type: "GET",
        contentType: "application/json",
        headers: {
            "Authorization": "Bearer " + token,
            "Accept": "application/json"
        },
        success: function(response) {
            if(response.code == 200) {
                var categories = response.data;
                var parents = [];
                var children = {};

                categories.forEach(function(category) {
                    if (category.parentId == 0) {
                        // Parent item
                        parents.push(category);
                    } else {
                        // Child item
                        if (!children[category.parentId]) {
                            children[category.parentId] = [];
                        }
                        children[category.parentId].push(category);
                    }
                });

                // Function to add children recursively to dropdown
                function addChildrenRecursively(parentId, level) {

                    if (Number(parentId) === Number(id)) {
                        return; 
                    }


                    if (children[parentId]) {
                        children[parentId].forEach(function(child) {
                            if (Number(child.id) === Number(id)) {
                                return; 
                            }
                            var childItemModel = $('<div>', {
                                class: 'dropdown-item',
                                html: '&nbsp;&nbsp;&nbsp;&nbsp;'.repeat(level) + child.name,
                                click: function() {
                                    selectItemmModelUpdate(child.id);
                                    $('#dropdownButtonModalUpdate').text(child.name);
                                }
                            });
                            // Append child item to modal dropdown
                            $('#myDropdownModalUpdate').append(childItemModel);

                            // Recursive call to add grandchildren
                            addChildrenRecursively(child.id, level + 1);
                        });
                    }
                }

                // Loop through the parents and append them to the dropdown
                parents.forEach(function(parent) {
                    if (Number(parent.id) !== Number(id)) {
                    var parentItem = $('<div>', {
                        class: 'dropdown-item',
                        text: parent.name,
                        click: function() {
                            selectItemmModelUpdate(parent.id);
                            $('#dropdownButtonModalUpdate').text(parent.name);
                        }
                    });
                    
                    // Append parent item to the dropdown in modal
                    $('#myDropdownModalUpdate').append(parentItem);

                    // Recursively add children for each parent
                    addChildrenRecursively(parent.id, 1);
                }
                });
            } else {
                alert('Lỗi: ' + response.message);
            }
        },
        error: function(xhr, status, error) {
            var errorMessage = error;
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            } else if (xhr.responseText) {
                try {
                    var response = JSON.parse(xhr.responseText);
                    errorMessage = response.message || error;
                } catch(e) {
                    errorMessage = error;
                }
            }
            if (!errorMessage || errorMessage.trim() === "") {
                errorMessage = "Đã xảy ra lỗi không xác định. Vui lòng thử lại sau!";
            }
            showError(errorMessage);
        }
    });
}

let valueModelUpdate = ""; 
function selectItemmModelUpdate(id) {
    valueModelUpdate = id;
    console.log("Parent Category ID: " + id);
}

$(document).on('click', '.edit-btn', function() {
    var id = $(this).data('id');
    handleCategoriesResponseUpdate(id);
    var name = $(this).data('name');
    var parentName = $(this).data('parent-name') || 'Chọn thể loại';

    var parentId = $(this).data('parent-id') === 0 ? null : $(this).data('parent-id');
    valueModelUpdate = parentId;

    console.log("ID: " + id);
    console.log("Category Name: " + name);
    console.log("Parent Category Name: " + parentName);
    console.log("Parent Category ID: " + parentId);
    $('#updateCategoryModal').modal('show'); 
    $('#categoryIdInput').val(id); // Set hidden category ID input
    $('#nameCateogryUpdateInput').val(name); 
    $('#dropdownButtonModalUpdate').text(parentName);

    $('#updateCategory').off('click').on('click', function() {
    var updatedName = $('#nameCateogryUpdateInput').val();  
    console.log(parentId)
    var data = {
        name: updatedName,
        parentId: valueModelUpdate
      };

      var formData = new FormData();
      formData.append('data', JSON.stringify(data));

    console.log("id chỉnh sửa là :"+ id)

// ##############################################################################################################

    $.ajax({
        url: 'http://localhost:8080/category/' + id,  // Đường dẫn API cập nhật danh mục
        type: 'PUT',  // Sử dụng phương thức PUT để cập nhật
        data: formData,  // Dữ liệu FormData đã tạo
        processData: false,  // Không xử lý dữ liệu bởi jQuery
        contentType: false,  // Không cần đặt contentType vì FormData tự động xử lý
        headers: {
            "Authorization": "Bearer " + token,  // Nếu cần Authorization
            "Accept": "application/json"
        },
        success: function(response) {
            $('#updateCategoryModal').modal('hide'); 
 
            console.log("thành công:"+ id)
            showSuccess("Cập nhật thành công");
            $('#dropdownButtonModalUpdate').text(updatedName); 
     
                handleCategoriesResponse();  // Cập nhật danh sách thể loại
                fetchData();  // Lấy lại dữ liệu mới

        },
        error: function(xhr, status, error) {
            var errorMessage = error;
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            } else if (xhr.responseText) {
                try {
                    var response = JSON.parse(xhr.responseText);
                    errorMessage = response.message || error;
                } catch(e) {
                    errorMessage = error;
                }
            }
            console.log("lỗi:" + id)
            showError(errorMessage);  // Show error if the request fails
        }
    });
    });
});
  


});
function filterFunctionModelAttribute() {
    const input = document.getElementById("myInputModal");
    const filter = input.value.toUpperCase();
    const div = document.getElementById("myDropdownModal");
    const a = div.getElementsByClassName("dropdown-item");

    for (let i = 0; i < a.length; i++) {
      const txtValue = a[i].textContent || a[i].innerText;
      if (txtValue.toUpperCase().indexOf(filter) > -1) {
        a[i].style.display = "";
      } else {
        a[i].style.display = "none";
      }
    }
  }

  function filterFunctionModelAttributeUpdate() {
    const input = document.getElementById("myInputModalUpdate");
    const filter = input.value.toUpperCase();
    const div = document.getElementById("myDropdownModalUpdate");
    const a = div.getElementsByClassName("dropdown-item");

    for (let i = 0; i < a.length; i++) {
      const txtValue = a[i].textContent || a[i].innerText;
      if (txtValue.toUpperCase().indexOf(filter) > -1) {
        a[i].style.display = "";
      } else {
        a[i].style.display = "none";
      }
    }
  }