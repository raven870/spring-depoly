package np.com.gautam101.com.testHibernate;

import np.com.gautam101.com.testHibernate.entity.Role;
import np.com.gautam101.com.testHibernate.entity.User;
import np.com.gautam101.com.testHibernate.repository.RoleRepository;
import np.com.gautam101.com.testHibernate.repository.UserRepository;
import np.com.gautam101.com.testHibernate.webSecurity.EncoderConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestHibernateApplicationTests {
@Autowired
private UserRepository userRepository;
	@Autowired
	private EncoderConfig encoderConfig;

	@Autowired
	private RoleRepository roleRepository;

	@Test
	void testUserCreationWithRole() {

		Role userRole = roleRepository.findByName("ADMIN").orElseThrow();

		User user = new User();

		user.setEmail("gautam@gmail.com");
		user.setPassword(encoderConfig.getEncoder().encode("gautam"));
		user.setRole(userRole);

		IO.println(userRepository.save(user).toString());



	}



}
