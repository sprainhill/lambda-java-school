package com.lambdaschool.school.controller;

import com.lambdaschool.school.model.Course;
import com.lambdaschool.school.model.ErrorDetail;
import com.lambdaschool.school.service.CourseService;
import com.lambdaschool.school.view.CountStudentsInCourses;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "/courses")
public class CourseController
{
    // create logger
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);


    @Autowired
    private CourseService courseService;

    // Swagger annotation, giving this endpoint documentation saying that it will
    // get all courses available, and the response will be a container of type List
    @ApiOperation(value ="Get all courses available", responseContainer = "List")
    @GetMapping(value = "/courses", produces = {"application/json"})
    public ResponseEntity<?> listAllCourses(HttpServletRequest request)
    {
        // logger will go here logging when accessed
        logger.info(request.getMethod() + " " + request.getRequestURI() + " accessed");

        ArrayList<Course> myCourses = courseService.findAll();
        return new ResponseEntity<>(myCourses, HttpStatus.OK);
    }

    @ApiOperation(value = "Get count of all students enrolled in a single course", responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Count determined", responseContainer = "List", response = CountStudentsInCourses.class),
            @ApiResponse(code = 404, message = "Could not find count", response = ErrorDetail.class )
    })
    @GetMapping(value = "/studcount", produces = {"application/json"})
    public ResponseEntity<?> getCountStudentsInCourses(HttpServletRequest request)
    {
        // logger will go here logging when accessed
        logger.info(request.getMethod() + " " + request.getRequestURI() + " accessed");

        return new ResponseEntity<>(courseService.getCountStudentsInCourse(), HttpStatus.OK);
    }

    // no response so response is void
    @ApiOperation(value = "Deletes a course by courseid", response = void.class)
    // add custom error handling
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Course deleted successfully", response = void.class),
            @ApiResponse(code = 404, message = "Course Not Found", response = ErrorDetail.class),
            @ApiResponse(code = 500, message = "Error deleting course", response = ErrorDetail.class)
    })

    @DeleteMapping("/courses/{courseid}")
    public ResponseEntity<?> deleteCourseById(@PathVariable long courseid, HttpServletRequest request)
    {
        // logger will go here logging when accessed
        logger.info(request.getMethod() + " " + request.getRequestURI() + " accessed");

        courseService.delete(courseid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
