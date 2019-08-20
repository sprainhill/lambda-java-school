package com.lambdaschool.school.controller;

import com.lambdaschool.school.model.ErrorDetail;
import com.lambdaschool.school.model.Student;
import com.lambdaschool.school.service.StudentService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController
{
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private StudentService studentService;

    // Swagger annotation, giving this endpoint documentation saying that it will
    // get all students with paging, and the response will be a container of type List
    @ApiOperation(value = "Get all students, with pagination", responseContainer = "List" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")})
    @GetMapping(value = "/students", produces = {"application/json"})
    public ResponseEntity<?> listAllStudentsWithPaging(@PageableDefault(page = 0, size = 3) Pageable pageable, HttpServletRequest request)
    {
        logger.info(request.getMethod() + " " + request.getRequestURI() + " accessed");

        List<Student> myStudents = studentService.findAll(pageable);
        return new ResponseEntity<>(myStudents, HttpStatus.OK);
    }

    // Swagger annotation, giving this endpoint documentation saying that it will
    // get all students without paging, and the response will be a container of type List
    @ApiOperation(value = "Get all students without pagination", responseContainer = "List" )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Students located", response = Student.class),
            @ApiResponse(code = 404, message = "Students could not be located", response = ErrorDetail.class),
            @ApiResponse(code = 500, message = "Error finding students", response = ErrorDetail.class)
    })
    @GetMapping(value = "/allstudents", produces = {"application/json"})
    public ResponseEntity<?> listAllStudents(HttpServletRequest request)
    {
        logger.info(request.getMethod() + " " + request.getRequestURI() + " accessed");

        List<Student> myStudents = studentService.findAll(Pageable.unpaged());
        return new ResponseEntity<>(myStudents, HttpStatus.OK);
    }



    // Swagger annotation, giving this endpoint documentation saying that it will
    // get a student by studid, and the response will be a Student type object
    @ApiOperation(value = "Get a student by id", response = Student.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Student located", response = Student.class),
            @ApiResponse(code = 404, message = "Student could not be located", response = ErrorDetail.class),
            @ApiResponse(code = 500, message = "Error finding student", response = ErrorDetail.class)
    })
    @GetMapping(value = "/Student/{StudentId}",
                produces = {"application/json"})
    public ResponseEntity<?> getStudentById(
            @PathVariable
                    Long StudentId, HttpServletRequest request)
    {
        logger.info(request.getMethod() + " " + request.getRequestURI() + " accessed");

        Student r = studentService.findStudentById(StudentId);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    // Swagger annotation, giving this endpoint documentation saying that it will
    // get a student with a name starting with the provided name and return a response
    // of a list of Student types
    @ApiOperation(value = "Get a list of students with names like provided name", responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Student located", response = Student.class),
            @ApiResponse(code = 404, message = "Student could not be located", response = ErrorDetail.class),
            @ApiResponse(code = 500, message = "Error finding student", response = ErrorDetail.class)
    })
    @GetMapping(value = "/student/namelike/{name}",
                produces = {"application/json"})
    public ResponseEntity<?> getStudentByNameContaining(
            @PathVariable String name, HttpServletRequest request)
    {
        logger.info(request.getMethod() + " " + request.getRequestURI() + " accessed");

        List<Student> myStudents = studentService.findStudentByNameLike(name);
        return new ResponseEntity<>(myStudents, HttpStatus.OK);
    }

    // Swagger annotation, giving this endpoint documentation saying that it will
    // add a new student
    @ApiOperation(value = "Creates a new student", notes = "newly created studentid will be provided in response header", response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Student created", response = Student.class),
            @ApiResponse(code = 404, message = "Error creating student", response = ErrorDetail.class)

    })
    @PostMapping(value = "/Student",
                 consumes = {"application/json"},
                 produces = {"application/json"})
    public ResponseEntity<?> addNewStudent(@Valid
                                           @RequestBody
                                                   Student newStudent, HttpServletRequest request) throws URISyntaxException
    {
        logger.info(request.getMethod() + " " + request.getRequestURI() + " accessed");

        newStudent = studentService.save(newStudent);

        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newStudentURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{Studentid}").buildAndExpand(newStudent.getStudid()).toUri();
        responseHeaders.setLocation(newStudentURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }
    // Swagger annotation, giving this endpoint documentation saying that it will
    // update student based on student id, or create a new student if none previously
    @ApiOperation(value = "Updates a student based on provided id", notes = "if no student existing will create student", response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Student updated", response = Student.class),
            @ApiResponse(code = 404, message = "Could not locate student", response = ErrorDetail.class),
            @ApiResponse(code = 500, message = "Error creating student", response = ErrorDetail.class)
    })
    @PutMapping(value = "/Student/{Studentid}")
    public ResponseEntity<?> updateStudent(
            @RequestBody
                    Student updateStudent,
            @PathVariable
                    long Studentid, HttpServletRequest request)
    {
        logger.info(request.getMethod() + " " + request.getRequestURI() + " accessed");

        studentService.update(updateStudent, Studentid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Swagger annotation, giving this endpoint documentation saying that it will
    // delete a student based on student id

    @ApiOperation(value = "Deletes student based off id", response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Student deleted", response = Student.class),
            @ApiResponse(code = 404, message = "Error deleting student", response = ErrorDetail.class)
    })
    @DeleteMapping("/Student/{Studentid}")
    public ResponseEntity<?> deleteStudentById(
            @PathVariable
                    long Studentid, HttpServletRequest request)
    {
        logger.info(request.getMethod() + " " + request.getRequestURI() + " accessed");

        studentService.delete(Studentid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
