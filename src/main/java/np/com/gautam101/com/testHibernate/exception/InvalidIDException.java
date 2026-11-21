package np.com.gautam101.com.testHibernate.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class InvalidIDException extends HttpStatusCodeException {


    public  InvalidIDException(@Nullable String reason){
        super(HttpStatus.BAD_REQUEST,reason);

    }
}
