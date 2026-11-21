package np.com.gautam101.com.testHibernate.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentRequestDTO {

    @NotBlank(message = "require field studentName cannot be empty")
    @Size(min = 3,max = 25,message = "minimum 3 and max 15 only")
    private String studentName;

    @NotBlank(message = "requires field address cannot be empty")
    private String address;


}
