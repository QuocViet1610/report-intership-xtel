package com.example.project1.module.User.service.serviceImpl;
import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.mapper.User.UserVerificationMapper;
import com.example.project1.model.dto.request.RegisterRequest;
import com.example.project1.model.enity.User.UserVerification;
import com.example.project1.module.User.repository.UserVerificationRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserVerificationService {
    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRATION_MINUTES = 5;
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final int LOCK_DURATION_MINUTES = 1;
    UserVerificationRepository verificationRepository;
    UserVerificationMapper userVerificationMapper;
    PasswordEncoder passwordEncoder;

    public String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public LocalDateTime getExpiryTime() {
        return LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES);
    }

    public void saveOtp(RegisterRequest request, String otp) {
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);
        UserVerification verification = verificationRepository.findByEmail(request.getEmail())
                .orElse(new UserVerification());
        userVerificationMapper.partialUpdate(verification,request);
        verification.setPassword(passwordEncoder.encode(request.getPassword()));
        verification.setExpiryTime(expiryTime);
        verification.setOtp(otp);
        verification.setLockTime(null);
        verificationRepository.save(verification);
    }

    public void saveOtpForgetPassword(String Email, String otp) {
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);
        UserVerification verification = verificationRepository.findByEmail(Email)
                .orElse(new UserVerification());
        verification.setEmail(Email);
        verification.setOtp(otp);
        verification.setExpiryTime(expiryTime);
        verification.setLockTime(null);
        verificationRepository.save(verification);
    }

    public UserVerification verifyOtp(String email, String otp) {
        UserVerification verification = verificationRepository.findByEmail(email).orElseThrow(()-> new ValidateException(Translator.toMessage("Tài khoản gmail chưa đăng ký")));;
//        if (verification.isLocked()) {
//            throw new ValidateException(Translator.toMessage("error.otp.too_many_attempts"));
//        }

        if (!verification.getOtp().equals(otp)) {
            throw new ValidateException(Translator.toMessage("error.otp.invalid"));
        }

        if (verification.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new ValidateException(Translator.toMessage("error.otp.expired"));
        }

//        if (!verification.getOtp().equals(otp)) {
//            verification.setFailedAttempts(verification.getFailedAttempts() + 1);
//
//            if (verification.getFailedAttempts() >= MAX_FAILED_ATTEMPTS) {
//                verification.setLockTime(LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES));
//                verificationRepository.save(verification);
//                throw new ValidateException(Translator.toMessage("error.otp.locked"));
//            }
//
//            verificationRepository.save(verification);
//            throw new ValidateException(Translator.toMessage("error.otp.invalid"));
//        }

        verificationRepository.delete(verification);

        return verification;
    }

}
