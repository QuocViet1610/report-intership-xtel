// Global pagination and search variables
let currentPage = 0;
let totalPages = 0;
let pageSize = 10; // Default page size
let allUserData = []; // Store all user data from server
let filteredUserData = []; // Store filtered user data after search
let searchTimer; // For debounce search

$(document).ready(function() {
    // Initialize page size from dropdown
    pageSize = parseInt($("#pageSizeSelect").val() || 10);
    
    // Set up pagination events
    setupPaginationEvents();
    
    // Set up search functionality
    setupSearchFunction();
    
    // Fetch initial data
    fetchUserData();
    
    // Set up event handlers for modal and action buttons
    setupActionEvents();
});

function fetchUserData() {
    let token = localStorage.getItem("authToken");
    
    $.ajax({
        url: 'http://localhost:8080/user/get-staff',
        type: 'GET',
        contentType: 'application/json',
        headers: {
            "Authorization": "Bearer " + token,
            "Accept": "application/json"
        },
        success: function(response) {
            if (response.code === "200") {
                // Store the full data
                allUserData = response.data;
                
                // Initialize filtered data with all data
                filteredUserData = [...allUserData];
                
                // Calculate total pages
                totalPages = Math.ceil(filteredUserData.length / pageSize);
                
                // Apply pagination to the data
                renderUserTable(currentPage, pageSize);
                
                // Update pagination UI
                updatePaginationUI();
            } else {
                console.error("Lỗi từ API:", response.message || "Không có thông báo lỗi");
                alert("Có lỗi xảy ra!");
            }
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

function renderUserTable(page, size) {
    // Clear current table
    $('#user-table-body').empty();
    
    // Calculate start and end indexes for the current page
    const startIndex = page * size;
    const endIndex = Math.min(startIndex + size, filteredUserData.length);
    
    // If no data to display
    if (filteredUserData.length === 0) {
        $('#user-table-body').append(`
            <tr>
                <td colspan="6" class="text-center">Không tìm thấy dữ liệu người dùng</td>
            </tr>
        `);
        return;
    }
    
    // Slice the data for the current page
    const currentPageData = filteredUserData.slice(startIndex, endIndex);
    
    // Render each row
    currentPageData.forEach(user => {
        let statusIcon = user.isActive === 1
            ? '<i class="ri-checkbox-circle-line" style="color: #0da487 !important;"></i>'
            : '<i class="ri-close-circle-line" style="color: red !important;"></i>';

        // Create a row for each user
        $('#user-table-body').append(`
            <tr>
                <td>
                    <div class="table-image">
                        <img src="${user.avatar}" class="img-fluid" alt="User Avatar">
                    </div>
                </td>
                <td>
                    <div class="user-name">
                        <span>${user.fullName}</span>
                    </div>
                </td>
                <td>${user.phone}</td>
                <td>${user.email}</td>
                <td class="td-check">${statusIcon}</td>
                <td>
                    <ul>
                        <li id="toggle-lock" data-id="${user.id}">
                            <a href="javascript:void(0)">
                                <i class="ri-lock-line"></i>
                            </a>
                        </li>
                        <li data-id="${user.id}">
                            <a href="javascript:void(0)" data-bs-toggle="modal"
                                data-bs-target="#exampleModalToggle">
                                <i class="ri-delete-bin-line"></i>
                            </a>
                        </li>
                    </ul>
                </td>
            </tr>
        `);
    });
}

function updatePaginationUI() {
    // Calculate pagination info
    const totalItems = filteredUserData.length;
    const start = totalItems === 0 ? 0 : Math.min(currentPage * pageSize + 1, totalItems);
    const end = Math.min((currentPage + 1) * pageSize, totalItems);
    
    // Update pagination info text
    $("#paginationInfo").text(`${start} - ${end} trong ${totalItems} người dùng`);
    
    // Update current page in input
    $("#currentPageInput").val(currentPage + 1);
    
    // Enable/disable pagination buttons
    $("#firstPage, #prevPage").prop("disabled", currentPage === 0);
    $("#lastPage, #nextPage").prop("disabled", currentPage >= totalPages - 1 || totalPages === 0);
}

// Setup search function
function setupSearchFunction() {
    // Handle search input with debounce effect
    $("#searchInput").on("input", function() {
        clearTimeout(searchTimer);
        
        // Use debounce to avoid too many filtering operations
        searchTimer = setTimeout(function() {
            const searchText = $("#searchInput").val().toLowerCase().trim();
            
            if (searchText === '') {
                // If search is empty, restore all data
                filteredUserData = [...allUserData];
            } else {
                // Filter users based on search text in name, email, or phone
                filteredUserData = allUserData.filter(user => 
                    (user.fullName && user.fullName.toLowerCase().includes(searchText)) ||
                    (user.email && user.email.toLowerCase().includes(searchText)) ||
                    (user.phone && user.phone.toLowerCase().includes(searchText))
                );
            }
            
            // Reset to first page
            currentPage = 0;
            
            // Calculate new total pages
            totalPages = Math.ceil(filteredUserData.length / pageSize);
            
            // Render the table with the filtered data
            renderUserTable(currentPage, pageSize);
            
            // Update pagination
            updatePaginationUI();
        }, 300); // 300ms delay for debounce
    });
    
    // Handle enter key in search box
    $("#searchInput").on("keydown", function(e) {
        if (e.keyCode === 13) {
            e.preventDefault();
            clearTimeout(searchTimer);
            
            const searchText = $(this).val().toLowerCase().trim();
            
            if (searchText === '') {
                filteredUserData = [...allUserData];
            } else {
                filteredUserData = allUserData.filter(user => 
                    (user.fullName && user.fullName.toLowerCase().includes(searchText)) ||
                    (user.email && user.email.toLowerCase().includes(searchText)) ||
                    (user.phone && user.phone.toLowerCase().includes(searchText))
                );
            }
            
            currentPage = 0;
            totalPages = Math.ceil(filteredUserData.length / pageSize);
            renderUserTable(currentPage, pageSize);
            updatePaginationUI();
        }
    });
}

// Set up pagination event handlers
function setupPaginationEvents() {
    // Page size selection
    $("#pageSizeSelect").change(function() {
        pageSize = parseInt($(this).val());
        totalPages = Math.ceil(filteredUserData.length / pageSize);
        
        // If the current page would now be out of bounds, adjust it
        if (currentPage >= totalPages && totalPages > 0) {
            currentPage = totalPages - 1;
        }
        
        renderUserTable(currentPage, pageSize);
        updatePaginationUI();
    });
    
    // First page button
    $("#firstPage").click(function() {
        if (currentPage > 0) {
            currentPage = 0;
            renderUserTable(currentPage, pageSize);
            updatePaginationUI();
        }
    });
    
    // Previous page button
    $("#prevPage").click(function() {
        if (currentPage > 0) {
            currentPage--;
            renderUserTable(currentPage, pageSize);
            updatePaginationUI();
        }
    });
    
    // Next page button
    $("#nextPage").click(function() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            renderUserTable(currentPage, pageSize);
            updatePaginationUI();
        }
    });
    
    // Last page button
    $("#lastPage").click(function() {
        if (currentPage < totalPages - 1) {
            currentPage = totalPages - 1;
            renderUserTable(currentPage, pageSize);
            updatePaginationUI();
        }
    });
    
    // Direct page input
    $("#currentPageInput").on("change", function() {
        let requestedPage = parseInt($(this).val()) - 1;
        if (!isNaN(requestedPage) && requestedPage >= 0 && requestedPage < totalPages) {
            currentPage = requestedPage;
            renderUserTable(currentPage, pageSize);
            updatePaginationUI();
        } else {
            $(this).val(currentPage + 1); // Reset to current page if input is invalid
        }
    });
    
    // Prevent form submission on Enter key in page input
    $("#currentPageInput").on("keydown", function(e) {
        if (e.keyCode === 13) {
            e.preventDefault();
            $(this).trigger("change");
        }
    });
}

// Set up event handlers for lock/unlock and delete actions
function setupActionEvents() {
    // Toggle lock/unlock user
    // $('#user-table-body').on('click', '#toggle-lock', function() {
    //     const userId = $(this).data('id');
    //     let token = localStorage.getItem("authToken");
        
    //     $.ajax({
    //         url: `http://localhost:8080/user/update-active/${userId}`,
    //         type: 'PUT',
    //         contentType: 'application/json',
    //         headers: {
    //             "Authorization": "Bearer " + token,
    //             "Accept": "application/json"
    //         },
    //         success: function(response) {
    //             if (response.code === "200") {
    //                 // Update user data in our local arrays
    //                 const userIndexAll = allUserData.findIndex(user => user.id === userId);
    //                 const userIndexFiltered = filteredUserData.findIndex(user => user.id === userId);
                    
    //                 if (userIndexAll >= 0) {
    //                     // Toggle isActive status in allUserData
    //                     allUserData[userIndexAll].isActive = allUserData[userIndexAll].isActive === 1 ? 0 : 1;
    //                 }
                    
    //                 if (userIndexFiltered >= 0) {
    //                     // Toggle isActive status in filteredUserData
    //                     filteredUserData[userIndexFiltered].isActive = filteredUserData[userIndexFiltered].isActive === 1 ? 0 : 1;
                        
    //                     // Re-render the current page
    //                     renderUserTable(currentPage, pageSize);
    //                 }
                    
    //                 if (typeof showSuccess === 'function') {
    //                     showSuccess("Cập nhật trạng thái thành công!");
    //                 } else {
    //                     alert("Cập nhật trạng thái thành công!");
    //                 }
    //             } else {
    //                 alert("Có lỗi xảy ra!");
    //             }
    //         },
    //         error: function(xhr, status, error) {
    //             // Xử lý khi có lỗi
    //             let errorMessage = "Đã xảy ra lỗi!";
    //             if (xhr.responseJSON && xhr.responseJSON.message) {
    //                 errorMessage = xhr.responseJSON.message;
    //             } else if (xhr.responseText) {
    //                 try {
    //                     let response = JSON.parse(xhr.responseText);
    //                     errorMessage = response.message || "Lỗi không xác định từ máy chủ!";
    //                 } catch (e) {
    //                     errorMessage = "Lỗi không thể đọc phản hồi từ server!";
    //                 }
    //             }
    //             if (typeof showError === 'function') {
    //                 showError(errorMessage);
    //             } else {
    //                 alert(errorMessage);
    //             }
    //         }
    //     });
    // });

    $('#user-table-body').on('click', '#toggle-lock', function() {
    selectedUserId = $(this).data('id');
    // Hiện modal xác nhận
    const modal = new bootstrap.Modal(document.getElementById('confirmLockModal'));
    modal.show();
});
    // Handle delete icon click - capture user ID for delete modal
    $('body').on('click', '.ri-delete-bin-line', function() {
        const userId = $(this).closest('li').data('id');
        $('#exampleModalToggle').data('userId', userId);
    });

    // Handle delete confirmation
    $('#delete-user').on('click', function() {
        const userId = $('#exampleModalToggle').data('userId');
        let token = localStorage.getItem("authToken");
        
        $.ajax({
            url: `http://localhost:8080/user/${userId}`,
            type: 'DELETE',
            contentType: 'application/json',
            headers: {
                "Authorization": "Bearer " + token,
                "Accept": "application/json"
            },
            success: function(response) {
                // Remove deleted user from our data arrays
                allUserData = allUserData.filter(user => user.id !== userId);
                filteredUserData = filteredUserData.filter(user => user.id !== userId);
                
                // Recalculate total pages
                totalPages = Math.ceil(filteredUserData.length / pageSize);
                
                // If the current page would now be out of bounds, adjust it
                if (currentPage >= totalPages && totalPages > 0) {
                    currentPage = totalPages - 1;
                }
                
                // Re-render the table
                renderUserTable(currentPage, pageSize);
                updatePaginationUI();
                
                if (typeof showSuccess === 'function') {
                    showSuccess("Xóa người dùng thành công!");
                } else {
                    alert("Xóa người dùng thành công!");
                }
                
                // Close the modal
                const modal = bootstrap.Modal.getInstance(document.getElementById('exampleModalToggle'));
                if (modal) {
                    modal.hide();
                }
            },
            error: function(xhr, status, error) {
                // Xử lý lỗi khi gọi API
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
    });
}