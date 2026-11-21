package np.com.gautam101.com.testHibernate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank(message = "required_field_email_cannot_be_blank")
    @Email(message = "invalid_email_format")
    private String email;

    @NotBlank(message = "required_field_password_cannot_be_blank")
    @Size(min = 8, max = 14, message = "min_8_char_max_14")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,14}$",
            message = "password_must_contain_upper_lower_digit_and_special_char"
    )
    private String password;
}