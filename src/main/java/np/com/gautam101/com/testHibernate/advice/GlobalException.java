package np.com.gautam101.com.testHibernate.advice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>>handleValidationError(MethodArgumentNotValidException e){


        Map<String,String> errors =new HashMap<>();
        e.getBindingResult()
                .getFieldErrors()
                .forEach(err->
                        errors.put(err.getField(),err.getDefaultMessage())
                        );


        return new ResponseEntity<>(new ApiResponse<Object>(
                false,
                null,
                errors,
                HttpStatus.BAD_REQUEST,
                "validation error"

        ),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthError(AuthenticationException e) {
        return new ResponseEntity<>(new ApiResponse<>(
                false,
                null,
                e.getMessage(),
                HttpStatus.UNAUTHORIZED,
                "Invalid email or password"
        ), HttpStatus.UNAUTHORIZED);
    }

    // 1. Galat URL / Endpoint ke liye (404 NOT_FOUND)
    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    public ResponseEntity<ApiResponse<?>> handleNotFound(Exception e) {
        return new ResponseEntity<>(
                new ApiResponse<>(
                        false,
                        null,
                        null,
                        HttpStatus.NOT_FOUND,
                        "api_endpoint_not_found"
                ),
                HttpStatus.NOT_FOUND
        );
    }

    // 2. Galat HTTP Method ke liye (e.g., POST ki jagah GET -> 405 METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
        return new ResponseEntity<>(
                new ApiResponse<>(
                        false,
                        null,
                        null,
                        HttpStatus.METHOD_NOT_ALLOWED,
                        "http_method_not_supported: " + e.getMethod()
                ),
                HttpStatus.METHOD_NOT_ALLOWED
        );
    }


    // 1. Token Expire hone par (401 UNAUTHORIZED)
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<?>> handleExpiredJwt(ExpiredJwtException e) {
        return new ResponseEntity<>(
                new ApiResponse<>(
                        false,
                        null,
                        null,
                        HttpStatus.UNAUTHORIZED,
                        "jwt_token_expired"
                ),
                HttpStatus.UNAUTHORIZED
        );
    }

    // 2. Invalid / Malformed / Tampered Token hone par (401 UNAUTHORIZED)
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidJwt(JwtException e) {
        return new ResponseEntity<>(
                new ApiResponse<>(
                        false,
                        null,
                        null,
                        HttpStatus.UNAUTHORIZED,
                        "invalid_jwt_token"
                ),
                HttpStatus.UNAUTHORIZED
        );
    }




    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>>handleInternalError(Exception e){
        e.printStackTrace(); // <-- Console mein exact error line dikhegi
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "an_unexpected_internal_error_occurred";

        if (e instanceof ResponseStatusException){
            ResponseStatusException rse = (ResponseStatusException) e;
            status = HttpStatus.valueOf(rse.getStatusCode().value());

            message = rse.getReason();

        }

        return new ResponseEntity<>(new ApiResponse<Object>(
                false,
                null,
                null,
                status,
                message
        ),status);
    }
}
