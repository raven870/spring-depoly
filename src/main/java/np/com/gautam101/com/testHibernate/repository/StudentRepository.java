package np.com.gautam101.com.testHibernate.repository;

import np.com.gautam101.com.testHibernate.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByRollNo(int rollNo);


}