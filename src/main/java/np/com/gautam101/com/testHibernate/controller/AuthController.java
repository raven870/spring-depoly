package np.com.gautam101.com.testHibernate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import np.com.gautam101.com.testHibernate.advice.ApiResponse;
import np.com.gautam101.com.testHibernate.dto.LoginRequestDTO;
import np.com.gautam101.com.testHibernate.dto.LoginResponseDTO;
import np.com.gautam101.com.testHibernate.dto.RegisterRequestDTO;
import np.com.gautam101.com.testHibernate.entity.Role;
import np.com.gautam101.com.testHibernate.entity.User;
import np.com.gautam101.com.testHibernate.repository.RoleRepository;
import np.com.gautam101.com.testHibernate.repository.UserRepository;
import np.com.gautam101.com.testHibernate.webSecurity.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO requestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDTO.getEmail(),
                        requestDTO.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (userDetails == null) throw new AssertionError();
        String token = jwtService.getJwtToken(userDetails);

        return new ResponseEntity<>(
                new ApiResponse<>(true, new LoginResponseDTO(token), null, HttpStatus.OK, "successfully_login"),
                HttpStatus.OK
        );
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        // 1. Check if email already exists
        if (userRepository.findByEmail(registerRequestDTO.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email_already_exists");
        }

        // 2. Fetch default USER role from DB
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "default_role_not_found"));

        // 3. Create & save user with BCrypt encoded password
        User newUser = new User();
        newUser.setEmail(registerRequestDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        newUser.setRole(userRole);

        userRepository.save(newUser);

        return new ResponseEntity<>(
                new ApiResponse<>(true, null, null, HttpStatus.CREATED, "user_registered_successfully"),
                HttpStatus.CREATED
        );
    }
}