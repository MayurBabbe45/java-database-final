package com.project.code.Controller;

import com.project.code.Model.Customer;
import com.project.code.Model.Review;
import com.project.code.Repo.CustomerRepository;
import com.project.code.Repo.ReviewRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public Map<String, Object> getAllReviews() {
        Map<String, Object> response = new HashMap<>();
        response.put("reviews", reviewRepository.findAll());
        return response;
    }

    @GetMapping("/{storeId}/{productId}")
    public Map<String, Object> getReviews(@PathVariable("storeId") Long storeId,
            @PathVariable("productId") Long productId) {
        Map<String, Object> response = new HashMap<>();
        List<Review> reviews = reviewRepository.findByStoreIdAndProductId(storeId, productId);
        List<Map<String, Object>> reviewList = new ArrayList<>();
        for (Review review : reviews) {
            Customer customer = customerRepository.findCustomerById(review.getCustomerId());
            String customerName = customer != null ? customer.getName() : "Unknown";
            Map<String, Object> reviewObject = new HashMap<>();
            reviewObject.put("comment", review.getComment());
            reviewObject.put("rating", review.getRating());
            reviewObject.put("customerName", customerName);
            reviewList.add(reviewObject);
        }
        response.put("reviews", reviewList);
        return response;
    }
}
