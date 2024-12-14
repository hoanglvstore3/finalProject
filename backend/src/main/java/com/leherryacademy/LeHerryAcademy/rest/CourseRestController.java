package com.leherryacademy.LeHerryAcademy.rest;

import com.leherryacademy.LeHerryAcademy.model.Courses;
import com.leherryacademy.LeHerryAcademy.model.Person;
import com.leherryacademy.LeHerryAcademy.reponsitory.CourseRepository;
import com.leherryacademy.LeHerryAcademy.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api/courses",produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
public class CourseRestController {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    CourseService courseService;

    //@ResponseBody
    @GetMapping("/auth/average-rating/{courseId}")
    public Double getAverageRating(@PathVariable int courseId) {
        Double averageRating = courseRepository.findAverageRatingByCourseId(courseId);
        return averageRating;
    }

    @GetMapping("/auth/suggest-course")
    public List<Object> getCoursesWithAverageRating(@RequestParam List<Integer> courseIds) {
        return courseRepository.findCoursesWithAverageRating(courseIds);
    }

    @GetMapping("/auth/getCourseWithAvgRating")
    public Object getCourseWithAvgRating(@RequestParam int courseId){
        return courseRepository.findCourseWithAverageRatingByID(courseId);
    }

    @GetMapping("/public/getCourseWithAvgRatingFreeRandom")
    public Object getCourseWithAvgRatingFreeRandom(){
        return courseRepository.findCourseWithAverageRatingFreeRandom();
    }
    @GetMapping("/public/getFinalCourseWithAverageRating")
    public Object getFinalCourseWithAverageRating(){
        return courseRepository.findFinalCourseWithAverageRating();
    }

    @GetMapping("/public/getCourseByName")
    public Object getCourseByName(@RequestParam String courseTitle){
        return courseRepository.findCourseWithAverageRatingByCourseTitle(courseTitle);
    }

//    @GetMapping("/public/coursefilter/{minRating}/{languages}/{categories}/{levels}/{prices}/{pageNumber}")
//    public List<Object> getCoursesByFilter(
//            @PathVariable("minRating") String minRating,
//            @PathVariable("languages") String languages,
//            @PathVariable("categories") String categories,
//            @PathVariable("levels") String levels,
//            @PathVariable("prices") String prices,
//            @PathVariable("pageNumber") int pageNumber) {
//
//        // Parse các giá trị hoặc gán null nếu là "null"
//        Double minRatingValue = "null".equals(minRating) ? null : Double.valueOf(minRating);
//        List<String> languagesList = "null".equals(languages) ? null : Arrays.asList(languages.split(","));
//        List<String> categoriesList = "null".equals(categories) ? null : Arrays.asList(categories.split(","));
//        List<String> levelsList = "null".equals(levels) ? null : Arrays.asList(levels.split(","));
//        List<String> pricesList = "null".equals(prices) ? null : Arrays.asList(prices.split(","));
//
//        // Gọi Service với các giá trị đã được parse
//        return courseService.getAllCoursesFilter(minRatingValue, languagesList, categoriesList, levelsList, pricesList, pageNumber);
//    }
//
//
//
//    @GetMapping("/public/totalPageCourses/{minRating}/{languages}/{categories}/{levels}/{prices}")
//    public int getTotalPageCourses(
//            @PathVariable("minRating") String minRating,
//            @PathVariable("languages") String languages,
//            @PathVariable("categories") String categories,
//            @PathVariable("levels") String levels,
//            @PathVariable("prices") String prices) {
//
//        // Parse values or set to null if "null"
//        Double minRatingValue = "null".equalsIgnoreCase(minRating) ? null : Double.valueOf(minRating);
//        List<String> languagesList = "null".equalsIgnoreCase(languages) ? null : Arrays.asList(languages.split(","));
//        List<String> categoriesList = "null".equalsIgnoreCase(categories) ? null : Arrays.asList(categories.split(","));
//        List<String> levelsList = "null".equalsIgnoreCase(levels) ? null : Arrays.asList(levels.split(","));
//        List<String> pricesList = "null".equalsIgnoreCase(prices) ? null : Arrays.asList(prices.split(","));
//
//        return courseService.getTotalPages(minRatingValue, languagesList, categoriesList, levelsList, pricesList);
//    }


    @GetMapping("/public/coursefilter/{minRating}/{languages}/{categories}/{levels}/{prices1}/{prices2}/{prices3}/{prices4}/{prices5}/{freePrice}/{pageNumber}")
    public List<Object> getCoursesByFilter(
            @PathVariable("minRating") String minRating,
            @PathVariable("languages") String languages,
            @PathVariable("categories") String categories,
            @PathVariable("levels") String levels,
            @PathVariable("prices1") String prices1,
            @PathVariable("prices2") String prices2,
            @PathVariable("prices3") String prices3,
            @PathVariable("prices4") String prices4,
            @PathVariable("prices5") String prices5,
            @PathVariable("freePrice") String freePrice,
            @PathVariable("pageNumber") int pageNumber) {

        // Parse các giá trị hoặc gán null nếu là "null"
        Double minRatingValue = "null".equals(minRating) ? null : Double.valueOf(minRating);
        List<String> languagesList = "null".equals(languages) ? null : Arrays.asList(languages.split(","));
        List<String> categoriesList = "null".equals(categories) ? null : Arrays.asList(categories.split(","));
        List<String> levelsList = "null".equals(levels) ? null : Arrays.asList(levels.split(","));
        List<String> pricesList1 = "null".equals(prices1) ? null : Arrays.asList(prices1.split(","));
        List<String> pricesList2 = "null".equals(prices2) ? null : Arrays.asList(prices2.split(","));
        List<String> pricesList3 = "null".equals(prices3) ? null : Arrays.asList(prices3.split(","));
        List<String> pricesList4 = "null".equals(prices4) ? null : Arrays.asList(prices4.split(","));
        List<String> pricesList5 = "null".equals(prices5) ? null : Arrays.asList(prices5.split(","));

        // Gọi Service với các giá trị đã được parse
        return courseService.getAllCoursesFilter(minRatingValue, languagesList, categoriesList, levelsList, pricesList1, pricesList2,pricesList3,pricesList4,pricesList5, freePrice, pageNumber);
    }



    @GetMapping("/public/totalPageCourses/{minRating}/{languages}/{categories}/{levels}/{prices1}/{prices2}/{prices3}/{prices4}/{prices5}/{freePrice}")
    public int getTotalPageCourses(
            @PathVariable("minRating") String minRating,
            @PathVariable("languages") String languages,
            @PathVariable("categories") String categories,
            @PathVariable("levels") String levels,
            @PathVariable("prices1") String prices1,
            @PathVariable("prices2") String prices2,
            @PathVariable("prices3") String prices3,
            @PathVariable("prices4") String prices4,
            @PathVariable("prices5") String prices5,
            @PathVariable("freePrice") String freePrice) {

        // Parse values or set to null if "null"
        Double minRatingValue = "null".equals(minRating) ? null : Double.valueOf(minRating);
        List<String> languagesList = "null".equals(languages) ? null : Arrays.asList(languages.split(","));
        List<String> categoriesList = "null".equals(categories) ? null : Arrays.asList(categories.split(","));
        List<String> levelsList = "null".equals(levels) ? null : Arrays.asList(levels.split(","));
        List<String> pricesList1 = "null".equals(prices1) ? null : Arrays.asList(prices1.split(","));
        List<String> pricesList2 = "null".equals(prices2) ? null : Arrays.asList(prices2.split(","));
        List<String> pricesList3 = "null".equals(prices3) ? null : Arrays.asList(prices3.split(","));
        List<String> pricesList4 = "null".equals(prices4) ? null : Arrays.asList(prices4.split(","));
        List<String> pricesList5 = "null".equals(prices5) ? null : Arrays.asList(prices5.split(","));

        return courseService.getTotalPages(minRatingValue, languagesList, categoriesList, levelsList, pricesList1, pricesList2,pricesList3,pricesList4,pricesList5, freePrice);
    }



}
