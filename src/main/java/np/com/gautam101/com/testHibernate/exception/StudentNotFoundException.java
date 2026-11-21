package np.com.gautam101.com.testHibernate.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class StudentNotFoundException extends ResponseStatusException {

    public StudentNotFoundException( @Nullable String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }
}
