// Global pagination variables
let currentPage = 0;
let totalPages = 0;
let pageSize = 10; // Default page size
let staffData = []; // Store all staff data

function fetchStaffData(page = 0, size = pageSize) {
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
                staffData = response.data;
                
                // Calculate total pages
                totalPages = Math.ceil(staffData.length / size);
                
                // Apply pagination to the data
                renderStaffTable(page, size);
                
                // Update pagination UI
                updatePaginationUI(page, size);
            } else {
                console.error("Lỗi từ API:", response.message || "Không có thông báo lỗi");
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
            console.error("Lỗi khi lấy dữ liệu:", errorMessage);
            if (typeof showError === 'function') {
                showError(errorMessage);
            } else {
                alert(errorMessage);
            }
        }
    });
}

function renderStaffTable(page, size) {
    // Clear current table
    $('#user-table-body').empty();
    
    // Calculate start and end indexes for the current page
    const startIndex = page * size;
    const endIndex = Math.min(startIndex + size, staffData.length);
    
    // Slice the data for the current page
    const currentPageData = staffData.slice(startIndex, endIndex);
    
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
                        <li>
                            <a href="javascript:void(0)" class="toggle-lock" data-id="${user.id}">
                                <i class="ri-lock-line"></i> 
                            </a>
                        </li>
                        <li>
                            <a href="javascript:void(0)" data-bs-toggle="modal" data-bs-target="#exampleModalToggle" data-id="${user.id}">
                                <i class="ri-delete-bin-line"></i>
                            </a>
                        </li>
                    </ul>
                </td>
            </tr>
        `);
    });
    
    // Set up event handlers for the newly added rows
    setupRowEventHandlers();
}

function setupRowEventHandlers() {
    // Setup lock button event handlers
    $('.toggle-lock').on('click', function() {
        const userId = $(this).data('id');
        console.log("Toggle lock for user ID:", userId);
        // Add your lock/unlock functionality here
    });
}

function updatePaginationUI(page, size) {
    // Calculate pagination info
    const totalItems = staffData.length;
    const start = Math.min(page * size + 1, totalItems);
    const end = Math.min((page + 1) * size, totalItems);
    
    // Update pagination info text
    $("#paginationInfo").text(`${start} - ${end} trong ${totalItems} nhân viên`);
    
    // Update current page in input
    $("#currentPageInput").val(page + 1);
    
    // Enable/disable pagination buttons
    $("#firstPage, #prevPage").prop("disabled", page === 0);
    $("#lastPage, #nextPage").prop("disabled", page >= totalPages - 1);
}

// Set up pagination event handlers
function setupPaginationEvents() {
    // Page size selection
    $("#pageSizeSelect").change(function() {
        pageSize = parseInt($(this).val());
        currentPage = 0; // Reset to first page when changing page size
        totalPages = Math.ceil(staffData.length / pageSize);
        
        renderStaffTable(currentPage, pageSize);
        updatePaginationUI(currentPage, pageSize);
    });
    
    // First page button
    $("#firstPage").click(function() {
        if (currentPage > 0) {
            currentPage = 0;
            renderStaffTable(currentPage, pageSize);
            updatePaginationUI(currentPage, pageSize);
        }
    });
    
    // Previous page button
    $("#prevPage").click(function() {
        if (currentPage > 0) {
            currentPage--;
            renderStaffTable(currentPage, pageSize);
            updatePaginationUI(currentPage, pageSize);
        }
    });
    
    // Next page button
    $("#nextPage").click(function() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            renderStaffTable(currentPage, pageSize);
            updatePaginationUI(currentPage, pageSize);
        }
    });
    
    // Last page button
    $("#lastPage").click(function() {
        if (currentPage < totalPages - 1) {
            currentPage = totalPages - 1;
            renderStaffTable(currentPage, pageSize);
            updatePaginationUI(currentPage, pageSize);
        }
    });
    
    // Direct page input
    $("#currentPageInput").on("change", function() {
        let requestedPage = parseInt($(this).val()) - 1;
        if (!isNaN(requestedPage) && requestedPage >= 0 && requestedPage < totalPages) {
            currentPage = requestedPage;
            renderStaffTable(currentPage, pageSize);
            updatePaginationUI(currentPage, pageSize);
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

// Initialize on document ready
$(document).ready(function() {
    // Initialize page size from dropdown
    pageSize = parseInt($("#pageSizeSelect").val() || 10);
    
    // Set up pagination events
    setupPaginationEvents();
    
    // Fetch initial data
    fetchStaffData(0, pageSize);
});