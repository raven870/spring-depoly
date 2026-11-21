package np.com.gautam101.com.testHibernate.webSecurity;

import lombok.RequiredArgsConstructor;
import np.com.gautam101.com.testHibernate.entity.User;
import np.com.gautam101.com.testHibernate.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService  implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            User user = userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("email_donot_exist"));



        return org.springframework.security.core.userdetails.User.builder()
                    .roles(user.getRole().getName())
                    .password(user.getPassword())
                    .username(user.getEmail())
                    .build();
    }
}
