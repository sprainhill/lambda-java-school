package com.lambdaschool.school.controller;

import com.lambdaschool.school.model.Course;
import com.lambdaschool.school.model.ErrorDetail;
import com.lambdaschool.school.service.CourseService;
import com.lambdaschool.school.view.CountStudentsInCourses;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    // get all courses available with pagination, and the response will be a container of type List
    @ApiOperation(value ="Get all courses available with pagination", responseContainer = "List")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")})
    @GetMapping(value = "/allcourses", produces = {"application/json"})
    public ResponseEntity<?> listAllCoursesPaging(@PageableDefault(page = 0, size = 3) Pageable pageable, HttpServletRequest request)
    {
        // logger will go here logging when accessed
        logger.info(request.getMethod() + " " + request.getRequestURI() + " accessed");

        ArrayList<Course> myCourses = courseService.findAll(pageable);
        return new ResponseEntity<>(myCourses, HttpStatus.OK);
    }


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
    public ResponseEntity<?> deleteCourseById(@ApiParam(value = "courseid", required = true, example = "1") @PathVariable long courseid, HttpServletRequest request)
    {
        // logger will go here logging when accessed
        logger.info(request.getMethod() + " " + request.getRequestURI() + " accessed");

        courseService.delete(courseid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
