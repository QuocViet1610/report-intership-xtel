package com.example.project1.expection;
import com.example.project1.local.Translator;
import com.example.project1.model.dto.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.NoSuchMessageException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.concurrent.TimeoutException;

@Slf4j
@RestControllerAdvice
public class GoalDefaultExpectionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseResult<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String message = "Invalid request";
        try {
            message = Translator.toMessage(exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        } catch (Exception ignore) {
        }
        return ResponseEntity.badRequest().body(ResponseResult.ofFail(message));
    }

    @ExceptionHandler({ValidateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<String> handleValidateException(ValidateException exception) {
        return ResponseResult.ofFail(Translator.toMessage(exception.getMessage()));
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class}) // inputType
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<String> handleHttpMessageNotReadableException(Exception e) {
        return ResponseResult.ofFail(Translator.toMessage("common.error.input.dataType"), e.getMessage());
    }


    @ExceptionHandler(value = HttpServerErrorException.InternalServerError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //HTTP 500
    public ResponseResult<String> handleInternalException() {
        return ResponseResult.errorServer(Translator.toMessage("common.server.internal"));
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // HTTP 500 message
    public ResponseResult<String> defaultExceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseResult.errorServer(Translator.toMessage("common.server.internal"));
    }


    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseResult<String> handleAccessDeniedException() {
        return ResponseResult.ofFail(Translator.toMessage("error.user.granted_role.not_exist"));
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseResult<String> notFound(Exception e) {
        return ResponseResult.ofFail(Translator.toMessage("common.end_point.not_found"));
    }

    @ExceptionHandler(value = AuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseResult<String> handleAuthorizedException(Exception e) {
        return ResponseResult.ofFail(Translator.toMessage("common.token.expired"));
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseResult<String> notAllowed(Exception e) {
        return ResponseResult.ofFail(Translator.toMessage("common.end_point.not_allowed"));
    }

    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ResponseResult<String> notUnSupported(Exception e) {
        return ResponseResult.ofFail(Translator.toMessage("common.end_point.unsupported_media_type"));
    }

    @ExceptionHandler(value = ClassCastException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<String> notCast(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseResult.ofFail(Translator.toMessage("common.end_point.not_cat"));
    }

    @ExceptionHandler(value = InvalidDataAccessResourceUsageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<String> invalidIdentifier(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseResult.ofFail(Translator.toMessage("common.end_point.invalid_identifier"));
    }

    @ExceptionHandler(value = CannotCreateTransactionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<String> cannotCreateTransaction(Exception e) {
        return ResponseResult.ofFail(Translator.toMessage("common.end_point.cannot_create_transaction"));
    }

    @ExceptionHandler(value = NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<String> nullPointer(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseResult.ofFail(Translator.toMessage("common.end_point.null_pointer"));
    }

    @ExceptionHandler(value = IncorrectResultSizeDataAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<String> incorrectResultSizeData(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseResult.ofFail(Translator.toMessage("common.end_point.incorrect_result"));
    }

    @ExceptionHandler(value = TimeoutException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<String> timeout(Exception e) {
        return ResponseResult.ofFail(Translator.toMessage("common.end_point.time_out"));
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<String> illegalArgument(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseResult.ofFail(Translator.toMessage("common.end_point.illegal_argument"));
    }

    @ExceptionHandler(NoSuchMessageException.class)
    public ResponseEntity<String> handleNoSuchMessageException(NoSuchMessageException ex) {
        return ResponseEntity.badRequest().body("Thông báo không tồn tại.");
    }

}
