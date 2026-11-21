package np.com.gautam101.com.testHibernate.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class StudentResponseDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentName;

    private String address;

    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rollNo;

    @CreationTimestamp
    private LocalDateTime created_at;

    @UpdateTimestamp
    private  LocalDateTime updated_at;
}
