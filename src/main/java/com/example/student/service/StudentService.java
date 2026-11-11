package com.example.student.service;

import com.example.student.dto.PageResponse;
import com.example.student.dto.StudentRequest;
import com.example.student.dto.StudentResponse;
import com.example.student.entity.Course;
import com.example.student.entity.Student;
import com.example.student.exception.DuplicateResourceException;
import com.example.student.exception.ResourceNotFoundException;
import com.example.student.repository.CourseRepository;
import com.example.student.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    // constructor injection (recommended over field @Autowired)
    public StudentService(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public PageResponse<StudentResponse> list(String keyword, Pageable pageable) {
        Page<Student> page = (keyword == null || keyword.isBlank())
                ? studentRepository.findAll(pageable)
                : studentRepository.findByNameContainingIgnoreCaseOrStudentNoContainingIgnoreCase(
                        keyword, keyword, pageable);
        return PageResponse.from(page.map(StudentResponse::from));
    }

    public StudentResponse get(Long id) {
        return StudentResponse.from(findOrThrow(id));
    }

    @Transactional
    public StudentResponse create(StudentRequest req) {
        if (studentRepository.existsByStudentNo(req.studentNo())) {
            throw new DuplicateResourceException("Student number already exists: " + req.studentNo());
        }
        if (studentRepository.existsByEmail(req.email())) {
            throw new DuplicateResourceException("Email already exists: " + req.email());
        }
        Student s = new Student();
        apply(s, req);
        return StudentResponse.from(studentRepository.save(s));
    }

    @Transactional
    public StudentResponse update(Long id, StudentRequest req) {
        Student s = findOrThrow(id);
        if (studentRepository.existsByStudentNoAndIdNot(req.studentNo(), id)) {
            throw new DuplicateResourceException("Student number already exists: " + req.studentNo());
        }
        if (studentRepository.existsByEmailAndIdNot(req.email(), id)) {
            throw new DuplicateResourceException("Email already exists: " + req.email());
        }
        apply(s, req);
        // no save() needed: s is a managed entity, Hibernate flushes the changes at commit (dirty checking)
        return StudentResponse.from(s);
    }

    @Transactional
    public void delete(Long id) {
        studentRepository.delete(findOrThrow(id));   // join-table rows are removed automatically
    }

    @Transactional
    public StudentResponse enroll(Long studentId, Long courseId) {
        Student s = findOrThrow(studentId);
        s.addCourse(findCourseOrThrow(courseId));
        return StudentResponse.from(s);
    }

    @Transactional
    public StudentResponse drop(Long studentId, Long courseId) {
        Student s = findOrThrow(studentId);
        s.removeCourse(findCourseOrThrow(courseId));
        return StudentResponse.from(s);
    }

    private void apply(Student s, StudentRequest req) {
        s.setStudentNo(req.studentNo());
        s.setName(req.name());
        s.setEmail(req.email());
        s.setAge(req.age());
        s.setMajor(req.major());
    }

    private Student findOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
    }

    private Course findCourseOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
    }
}
