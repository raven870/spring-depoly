package np.com.gautam101.com.testHibernate.advice;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Locale;

@RestControllerAdvice
@Data
public class ApiResponse<T> {
    @JsonFormat(pattern = "hh:ss:mm yyyy-MM-dd")
    private LocalDateTime timestamzp;
    private HttpStatus httpStatus;
    private String message;
    private Boolean success;
    private T data;
    private T error;


    public ApiResponse() {
        this.timestamzp = LocalDateTime.now();
    }

    public ApiResponse(Boolean success, T data, T error, HttpStatus httpStatus, String message) {
        this();
        this.success = success;
        this.message = message.toUpperCase(Locale.ROOT);
        this.httpStatus = httpStatus;
        this.data = data;
        this.error =error;
    }





}
