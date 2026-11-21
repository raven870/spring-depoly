package np.com.gautam101.com.testHibernate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import np.com.gautam101.com.testHibernate.advice.ApiResponse;
import np.com.gautam101.com.testHibernate.dto.StudentRequestDTO;
import np.com.gautam101.com.testHibernate.dto.StudentResponseDTO;
import np.com.gautam101.com.testHibernate.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
@Slf4j
public class Student {

    private final StudentService studentService;
    private ApiResponse<?> response;

//    C R E A T E S T U D E N T

    @PostMapping("/")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> createStudent(@RequestBody @Valid StudentRequestDTO student) {

        StudentResponseDTO responseDTO = studentService.createStudent(student);
        response = new ApiResponse<>(true, responseDTO,null, HttpStatus.CREATED, "student_created");
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

//    Get ALl the Students

    @GetMapping("/")
    public ResponseEntity<ApiResponse<?>> getAllStudent() {
        log.info("GET /api/v1/student called - fetching all students");
        List<StudentResponseDTO> responseDTOList = studentService.getAllStudent();
        log.info("GET /api/v1/student called - fetching all students");
        if (responseDTOList.isEmpty()) {
            response = new ApiResponse<>(false, null,null, HttpStatus.NO_CONTENT, "no_students");
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }

        response = new ApiResponse<>(true, responseDTOList,null, HttpStatus.OK, "fetch_all_the_student");

        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    /*Get by Id*/
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>>getStudentById(@PathVariable Long id){

        StudentResponseDTO responseDTO =studentService.getStudentById(id);

        response = new ApiResponse<>(true,responseDTO,null,HttpStatus.OK,"successfully_fetch_data");

        return new ResponseEntity<>(response,HttpStatus.OK);
    }



    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> updateStudentById(@PathVariable Long id, @RequestBody @Valid StudentRequestDTO requestDTO) {
        response = new ApiResponse<>(true, studentService.updateStudentById(id, requestDTO),null, HttpStatus.OK, "student_details_updated_successfully");
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteStudentById(@PathVariable Long id) {
        Boolean isDeleted = studentService.deleteStudentById(id);
        if (!isDeleted) {
            response = new ApiResponse<>(false, null,null, HttpStatus.NOT_MODIFIED, "cannot_delete");
            return new ResponseEntity<>(response, HttpStatus.NOT_MODIFIED);
        }
        response = new ApiResponse<>(true, null,null, HttpStatus.OK, "successfully_deleted");

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
