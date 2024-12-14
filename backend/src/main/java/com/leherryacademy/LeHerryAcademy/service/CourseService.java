package com.leherryacademy.LeHerryAcademy.service;

import com.leherryacademy.LeHerryAcademy.reponsitory.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class CourseService {
    @Autowired
    CourseRepository courseRepository;
//    public List<Object> getAllCoursesFilter(
//            Double minRating,
//            List<String> languages,
//            List<String> categories,
//            List<String> levels,
//            List<String> prices,
//            int pageNumber) {
//        Double minPrice = null;
//        Double maxPrice = null;
//        String freePrice = null;
//        boolean includeFree = false;
//
//        if (prices != null && !prices.isEmpty()) {
//            includeFree = prices.stream().anyMatch(price -> price.equalsIgnoreCase("Free"));
//            if (includeFree == true){
//                freePrice = "Free";
//            }
//            // Lọc và chuyển đổi các giá trị số
//            List<Double> numericPrices = prices.stream()
//                    .filter(price -> !price.equalsIgnoreCase("Free"))
//                    .map(Double::parseDouble)
//                    .collect(Collectors.toList());
//
//            if (!numericPrices.isEmpty()) {
//                minPrice = Collections.min(numericPrices);
//                maxPrice = Collections.max(numericPrices);
//            }
//        }
//
//
//        Pageable pageable = PageRequest.of(pageNumber, 12, Sort.by("courseId").ascending());
//        Page<Object> page = courseRepository.findAllCoursesByFilter(minRating, languages, categories, levels,minPrice,maxPrice,freePrice, pageable);
//        return page.getContent();
//    }
//    public int getTotalPages(
//            Double minRating,
//            List<String> languages,
//            List<String> categories,
//            List<String> levels,
//            List<String> prices) {
//
//        Double minPrice = null;
//        Double maxPrice = null;
//        String freePrice = null;
//        boolean includeFree = false;
//
//        if (prices != null && !prices.isEmpty()) {
//            includeFree = prices.stream().anyMatch(price -> price.equalsIgnoreCase("Free"));
//            if (includeFree) {
//                freePrice = "Free";
//            }
//
//            // Lọc và chuyển đổi các giá trị số
//            List<Double> numericPrices = prices.stream()
//                    .filter(price -> !price.equalsIgnoreCase("Free"))
//                    .map(Double::parseDouble)
//                    .collect(Collectors.toList());
//
//            if (!numericPrices.isEmpty()) {
//                minPrice = Collections.min(numericPrices);
//                maxPrice = Collections.max(numericPrices);
//            }
//        }
//
//        Pageable pageable = PageRequest.of(0, 12, Sort.by("courseId").ascending());
//        Page<Object> page = courseRepository.findAllCoursesByFilter(
//                minRating, languages, categories, levels, minPrice, maxPrice, freePrice, pageable);
//
//        return page.getTotalPages();
//    }

    public List<Object> getAllCoursesFilter(
            Double minRating,
            List<String> languages,
            List<String> categories,
            List<String> levels,
            List<String> prices1,
            List<String> prices2,
            List<String> prices3,
            List<String> prices4,
            List<String> prices5,
            String freePrice,
            int pageNumber) {
        Double minPrice1 = null;
        Double maxPrice1 = null;
        Double minPrice2 = null;
        Double maxPrice2 = null;
        Double minPrice3 = null;
        Double maxPrice3 = null;
        Double minPrice4 = null;
        Double maxPrice4 = null;
        Double minPrice5 = null;
        Double maxPrice5 = null;
        if (prices1 != null) {
            minPrice1 = Double.parseDouble(prices1.get(0));
            maxPrice1 = Double.parseDouble(prices1.get(1));

        }
        if (prices2 != null) {
            minPrice2 = Double.parseDouble(prices2.get(0));
            maxPrice2 = Double.parseDouble(prices2.get(1));
        }
        if (prices3 != null) {
            minPrice3 = Double.parseDouble(prices3.get(0));
            maxPrice3 = Double.parseDouble(prices3.get(1));
        }
        if (prices4 != null) {
            minPrice4 = Double.parseDouble(prices4.get(0));
            maxPrice4 = Double.parseDouble(prices4.get(1));
        }
        if (prices5 != null) {
            minPrice5 = Double.parseDouble(prices5.get(0));
            maxPrice5 = Double.parseDouble(prices5.get(1));
        }

        if ("null".equals(freePrice)) {
            freePrice = null;
        }
        else {
            freePrice = "Free";
        }
        System.out.println("m1" + minPrice1);
        System.out.println("m1" + maxPrice1);
        System.out.println("m2" + minPrice2);
        System.out.println("m2" + maxPrice2);
        System.out.println("m3" + minPrice3);
        System.out.println("m3" + maxPrice3);
        System.out.println("m4" + minPrice4);
        System.out.println("m4" + maxPrice4);
        System.out.println("m5" + minPrice5);
        System.out.println("m5" + maxPrice5);
        System.out.println("free" + freePrice);
        System.out.println(pageNumber);


        Pageable pageable = PageRequest.of(pageNumber, 12, Sort.by("courseId").ascending());
        Page<Object> page = courseRepository.findAllCoursesByFilter(minRating, languages, categories, levels,
                minPrice1,maxPrice1,
                minPrice2,maxPrice2,
                minPrice3,maxPrice3,
                minPrice4,maxPrice4,
                minPrice5,maxPrice5,
                freePrice, pageable);
        return page.getContent();
    }



    public int getTotalPages(
            Double minRating,
            List<String> languages,
            List<String> categories,
            List<String> levels,
            List<String> prices1,
            List<String> prices2,
            List<String> prices3,
            List<String> prices4,
            List<String> prices5,
            String freePrice) {

        Double minPrice1 = null;
        Double maxPrice1 = null;
        Double minPrice2 = null;
        Double maxPrice2 = null;
        Double minPrice3 = null;
        Double maxPrice3 = null;
        Double minPrice4 = null;
        Double maxPrice4 = null;
        Double minPrice5 = null;
        Double maxPrice5 = null;
        if (prices1 != null) {
            minPrice1 = Double.parseDouble(prices1.get(0));
            maxPrice1 = Double.parseDouble(prices1.get(1));

        }
        if (prices2 != null) {
            minPrice2 = Double.parseDouble(prices2.get(0));
            maxPrice2 = Double.parseDouble(prices2.get(1));
        }
        if (prices3 != null) {
            minPrice3 = Double.parseDouble(prices3.get(0));
            maxPrice3 = Double.parseDouble(prices3.get(1));
        }
        if (prices4 != null) {
            minPrice4 = Double.parseDouble(prices4.get(0));
            maxPrice4 = Double.parseDouble(prices4.get(1));
        }
        if (prices5 != null) {
            minPrice5 = Double.parseDouble(prices5.get(0));
            maxPrice5 = Double.parseDouble(prices5.get(1));
        }

        if ("null".equals(freePrice)) {
            freePrice = null;
        }


        Pageable pageable = PageRequest.of(0, 12, Sort.by("courseId").ascending());
        Page<Object> page = courseRepository.findAllCoursesByFilter(minRating, languages, categories, levels,
                minPrice1,maxPrice1,
                minPrice2,maxPrice2,
                minPrice3,maxPrice3,
                minPrice4,maxPrice4,
                minPrice5,maxPrice5,
                freePrice, pageable);

        return page.getTotalPages();
    }



}
