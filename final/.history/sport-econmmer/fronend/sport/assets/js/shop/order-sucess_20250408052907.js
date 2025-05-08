window.onload = function() {
    const urlParams = new URLSearchParams(window.location.search);

    const vnpAmount = urlParams.get('vnp_Amount');
    const vnpBankCode = urlParams.get('vnp_BankCode');
    const vnpBankTranNo = urlParams.get('vnp_BankTranNo');
    const vnpCardType = urlParams.get('vnp_CardType');
    const vnpOrderInfo = urlParams.get('vnp_OrderInfo');
    const vnpPayDate = urlParams.get('vnp_PayDate');
    const vnpResponseCode = urlParams.get('vnp_ResponseCode');
    const vnpTmnCode = urlParams.get('vnp_TmnCode');
    const vnpTransactionNo = urlParams.get('vnp_TransactionNo');
    const vnpTransactionStatus = urlParams.get('vnp_TransactionStatus');
    const vnpTxnRef = urlParams.get('vnp_TxnRef');
    const vnpSecureHash = urlParams.get('vnp_SecureHash');
    const success = urlParams.get('success');

    console.log('success:', success);
    console.log('vnp_Amount:', vnpAmount);
    console.log('vnp_BankCode:', vnpBankCode);
    console.log('vnp_BankTranNo:', vnpBankTranNo);
    console.log('vnp_CardType:', vnpCardType);
    console.log('vnp_OrderInfo:', vnpOrderInfo);
    console.log('vnp_PayDate:', vnpPayDate);
    console.log('vnp_ResponseCode:', vnpResponseCode);
    console.log('vnp_TmnCode:', vnpTmnCode);
    console.log('vnp_TransactionNo:', vnpTransactionNo);
    console.log('vnp_TransactionStatus:', vnpTransactionStatus);
    console.log('vnp_TxnRef:', vnpTxnRef);
    console.log('vnp_SecureHash:', vnpSecureHash);

    if (vnpResponseCode === '00' || success === true) {
        // Thanh toán thành công
        alert('Thanh toán thành công!');
    } else {
        // window.location.href = '/cart.html';
    }
};
