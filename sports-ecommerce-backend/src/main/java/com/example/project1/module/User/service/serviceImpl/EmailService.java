package com.example.project1.module.User.service.serviceImpl;

import com.example.project1.expection.ValidateException;
import com.example.project1.model.dto.request.RegisterRequest;
import com.example.project1.model.enity.User.User;
import com.example.project1.model.enity.User.UserVerification;
import com.example.project1.module.User.repository.UserVerificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    JavaMailSender mailSender;
    UserVerificationRepository verificationRepository;
    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRATION_MINUTES = 5;


    public void sendVerificationEmail(RegisterRequest request, String verificationCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(request.getEmail());
            helper.setSubject("Xác nhận tài khoản");
            // Nội dung email với mã OTP
            String emailContent = "<p>Kính gửi"+ " " + request.getFullName() +",</p>"
                    + "<p>Cảm ơn bạn đã đăng ký tài khoản tại GOFIT. Để hoàn tất quá trình đăng ký, vui lòng nhập mã xác nhận OTP (One-Time Password) dưới đây:</p>"
                    + "<p><b>Mã OTP của bạn là: " + verificationCode + "</b></p>"
                    + "<p>Lưu ý:</p>"
                    + "<ul>"
                    + "<li>Mã OTP này có giá trị trong <b>5 phút</b> kể từ khi bạn nhận được email này.</li>"
                    + "<li>Nếu bạn không yêu cầu mã OTP này, vui lòng bỏ qua email này.</li>"
                    + "</ul>"
                    + "<p>Chúng tôi rất vui mừng khi có bạn là một phần của cộng đồng GOFIT.</p>"
                    + "<p>Trân trọng,<br>GOFIT</p>";

            helper.setText(emailContent, true);
            helper.setFrom("vietddls06@gmail.com", "GOFIT ");
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    // Tính thời gian hết hạn của OTP (5 phút)

    public LocalDateTime getExpiryTime() {
        return LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES);
    }


    public void sendEmailForgetPassword(User request, String verificationCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(request.getEmail());
            helper.setSubject("Xác nhận tài khoản");
            // Nội dung email với mã OTP
            String emailContent = "<p>Kính gửi"+ " " + request.getFullName() +",</p>"
                    + "<p>Chúng tôi nhận được yêu cầu thay đổi mật khẩu cho tài khoản của bạn tại GOFIT. Để hoàn tất quá trình thay đổi mật khẩu,, vui lòng nhập mã xác nhận OTP (One-Time Password) dưới đây:</p>"
                    + "<p><b>Mã OTP của bạn là: " + verificationCode + "</b></p>"
                    + "<p>Lưu ý:</p>"
                    + "<ul>"
                    + "<li>Mã OTP này có giá trị trong <b>5 phút</b> kể từ khi bạn nhận được email này.</li>"
                    + "<li>Nếu bạn không yêu cầu mã OTP này, vui lòng bỏ qua email này.</li>"
                    + "</ul>"
                    + "<p>Chúng tôi luôn sẵn sàng hỗ trợ bạn. Nếu có bất kỳ vấn đề gì, bạn có thể liên hệ với chúng tôi qua email này..</p>"
                    + "<p>Trân trọng,<br>GOFIT</p>";

            helper.setText(emailContent, true);
            helper.setFrom("vietddls06@gmail.com", "GOFIT ");
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
