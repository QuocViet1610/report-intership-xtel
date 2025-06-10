$(document).ready(function() {
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('order-history') && urlParams.get('order-history') === 'true') {
        // Tự động click vào tab nếu tham số 'clickTab' là true
        $('#pills-order-tab').click();
    }

});