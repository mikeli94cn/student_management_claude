package com.example.student.service;

import com.example.student.dto.CourseRequest;
import com.example.student.dto.CourseResponse;
import com.example.student.entity.Course;
import com.example.student.entity.Student;
import com.example.student.exception.DuplicateResourceException;
import com.example.student.exception.ResourceNotFoundException;
import com.example.student.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<CourseResponse> list() {
        return courseRepository.findAll().stream().map(CourseResponse::from).toList();
    }

    public CourseResponse get(Long id) {
        return CourseResponse.from(findOrThrow(id));
    }

    @Transactional
    public CourseResponse create(CourseRequest req) {
        if (courseRepository.existsByCode(req.code())) {
            throw new DuplicateResourceException("Course code already exists: " + req.code());
        }
        Course c = new Course();
        apply(c, req);
        return CourseResponse.from(courseRepository.save(c));
    }

    @Transactional
    public CourseResponse update(Long id, CourseRequest req) {
        Course c = findOrThrow(id);
        if (courseRepository.existsByCodeAndIdNot(req.code(), id)) {
            throw new DuplicateResourceException("Course code already exists: " + req.code());
        }
        apply(c, req);
        return CourseResponse.from(c);
    }

    @Transactional
    public void delete(Long id) {
        Course c = findOrThrow(id);
        // Student owns the relation, so detach this course from every student first
        for (Student s : List.copyOf(c.getStudents())) {
            s.removeCourse(c);
        }
        courseRepository.delete(c);
    }

    private void apply(Course c, CourseRequest req) {
        c.setCode(req.code());
        c.setName(req.name());
        c.setCredits(req.credits());
    }

    private Course findOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
    }
}
