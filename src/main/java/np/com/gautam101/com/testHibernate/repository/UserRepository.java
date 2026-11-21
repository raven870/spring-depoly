package np.com.gautam101.com.testHibernate.repository;

import np.com.gautam101.com.testHibernate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   Optional <User> findByEmail(String email);
}