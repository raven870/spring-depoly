package np.com.gautam101.com.testHibernate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class LoginRequestDTO {

    @NotBlank(message = "required_field:email cannot be empty")
    private String email;

    @NotBlank(message = "require_field:password cannot be empty")
    private String password;

}
