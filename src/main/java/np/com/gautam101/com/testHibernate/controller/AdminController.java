package np.com.gautam101.com.testHibernate.controller;


import lombok.RequiredArgsConstructor;
import np.com.gautam101.com.testHibernate.advice.ApiResponse;
import np.com.gautam101.com.testHibernate.entity.Role;
import np.com.gautam101.com.testHibernate.entity.User;
import np.com.gautam101.com.testHibernate.repository.RoleRepository;
import np.com.gautam101.com.testHibernate.repository.UserRepository;
import np.com.gautam101.com.testHibernate.webSecurity.EncoderConfig;
import np.com.gautam101.com.testHibernate.webSecurity.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final   AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final   EncoderConfig encoderConfig;
    private  final JwtService jwtService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> createAdmin(@RequestBody Map<String,String>  adminDetails ) {

        User adminUser = new User();
        Role adminRole = roleRepository.findByName("ADMIN").orElseThrow(()->new RuntimeException("role_not_found"));



        adminUser.setEmail(adminDetails.get("email"));
        adminUser.setPassword(encoderConfig.getEncoder().encode(adminDetails.get("password")));
        adminUser.setRole(adminRole);

        userRepository.save(adminUser);

        return new ResponseEntity<>(new ApiResponse<Map<String,String>>(
                true,Map.of
                ("email",adminDetails.get("email"),
                "password",adminDetails.get("password")),null, HttpStatus.OK,"admin_credential_created"),HttpStatus.OK);

    }

//    Login

    @PostMapping("/login")
    public  ResponseEntity<ApiResponse<?>>loginAdmin(@RequestBody Map<String,String>adminDetails){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        adminDetails.get("email"),
                        adminDetails.get("password")
                )
        );


        UserDetails admin = (UserDetails) authentication.getPrincipal();

        if (admin == null) throw new  AssertionError() ;

        String token = jwtService.getJwtToken(admin);

        return  new ResponseEntity<>(new ApiResponse<Map<String,String>>(true,
                Map.of(
                        "token",token
                ),
                null,HttpStatus.OK,
                "successfully_login_admin"),HttpStatus.OK);


    }

}
