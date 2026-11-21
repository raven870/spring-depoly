package np.com.gautam101.com.testHibernate.service;

import lombok.RequiredArgsConstructor;
import np.com.gautam101.com.testHibernate.dto.StudentRequestDTO;
import np.com.gautam101.com.testHibernate.dto.StudentResponseDTO;
import np.com.gautam101.com.testHibernate.entity.Student;
import np.com.gautam101.com.testHibernate.exception.InvalidIDException;
import np.com.gautam101.com.testHibernate.exception.StudentNotFoundException;
import np.com.gautam101.com.testHibernate.repository.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;
    private final AtomicInteger counter = new AtomicInteger(100);

    private int generateRollNo() {
        int nextNumber = counter.incrementAndGet();
        return nextNumber;
    }

    // C R E A T E
    public StudentResponseDTO createStudent(StudentRequestDTO student) {
        Student toBecreatedStud = modelMapper.map(student, Student.class);
        toBecreatedStud.setRollNo(generateRollNo());

        StudentResponseDTO responseDTO = modelMapper.map(studentRepository.save(toBecreatedStud), StudentResponseDTO.class);


        return responseDTO;
    }

    // G E T
    public List<StudentResponseDTO> getAllStudent() {
        List<Student> students = studentRepository.findAll();
        List<StudentResponseDTO> studentResponseDTOS = students.stream().map(e -> modelMapper.map(e, StudentResponseDTO.class)).collect(Collectors.toList());
        return studentResponseDTOS;
    }

    // U P D A T E
    public StudentResponseDTO updateStudentById(Long id, StudentRequestDTO requestDTO) {
//        Check the Student Exist or Not ;
        Student isExisted = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("student not exist with id:" + id));
        if (requestDTO.getStudentName() == null && requestDTO.getAddress() == null) {
            return modelMapper.map(isExisted, StudentResponseDTO.class);
        }

//        Check the field changes
        if (!Objects.equals(requestDTO.getStudentName(), isExisted.getStudentName()) && requestDTO.getStudentName() != null) {
            isExisted.setStudentName(requestDTO.getStudentName());
        }
        if (!Objects.equals(requestDTO.getAddress(), isExisted.getAddress()) && requestDTO.getAddress() != null) {
            isExisted.setAddress(requestDTO.getAddress());
        }
        return modelMapper.map(studentRepository.save(isExisted), StudentResponseDTO.class);

    }

    //    D E L E T E
    public Boolean deleteStudentById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("student_is_not_existed_with_id:" + id));

        studentRepository.deleteById(id);
        return true;
    }

    //     G E T B Y ID
    public StudentResponseDTO getStudentById(Object id) {

        if (id == null) {
            throw new IllegalArgumentException("required_id");
        }

        if(!(id instanceof Long)){
            throw new InvalidIDException("invalid_id_type: Expected a Long, but got " + id.getClass().getSimpleName());

        }


        Long studentId = (Long) id;

        Student isExisted = studentRepository.findById(studentId).orElseThrow(() -> new StudentNotFoundException("student_notfound_with_id:" + id));

        return modelMapper.map(isExisted, StudentResponseDTO.class);

    }
}

