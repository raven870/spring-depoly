package np.com.gautam101.com.testHibernate.controller;

import np.com.gautam101.com.testHibernate.advice.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class healthController {

    @GetMapping("/")
    public ResponseEntity<ApiResponse<?>>getHealth(){
        return new ResponseEntity<>(new ApiResponse<>(true,null,null, HttpStatus.OK,"server is up and running"),HttpStatus.OK);
    }
}
