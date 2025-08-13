package com.ecommerce.review.service;

import com.ecommerce.review.model.Review;
import com.ecommerce.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RestTemplate restTemplate; // for calling other services

    // create a new review
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    // Alias for test compatibility
    public Review addReview(Review review) {
        return createReview(review);
    }

    // update an existing review
    public Review updateReview(Long userId, Long inventoryId, Review updatedReview) {
        Review existingReview = reviewRepository.findByUserIdAndInventoryId(userId, inventoryId).orElse(null);
        if (existingReview == null) {
            return null; // review doesn't exist
        }
        existingReview.setRating(updatedReview.getRating());
        existingReview.setTitle(updatedReview.getTitle());
        existingReview.setBody(updatedReview.getBody());
        return reviewRepository.save(existingReview);
    }

    // delete a review
    public String deleteReview(Long userId, Long inventoryId) {
        Review review = reviewRepository.findByUserIdAndInventoryId(userId, inventoryId).orElse(null);
        if (review == null) {
            return "Review not found";
        }
        reviewRepository.delete(review);
        return "Review successfully deleted";
    }

    // get all reviews for a product (alias for test compatibility)
    public List<Review> getReviewsByProductId(long productId) {
        return getReviewsByInventoryId(productId);
    }

    public List<Review> getReviewsByInventoryId(Long inventoryId) {
        List<Review> reviews = reviewRepository.findByInventoryId(inventoryId);
        String productDetails = getProductDetails(inventoryId); // call product service
        reviews.forEach(review -> review.setBody(review.getBody() + " | Product: " + productDetails));
        return reviews;
    }

    // get all reviews by a user
    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    // get a specific review by user and product
    public Review getReviewByUserAndProduct(Long userId, Long inventoryId) {
        return reviewRepository.findByUserIdAndInventoryId(userId, inventoryId).orElse(null);
    }

    // helper method to call product service and get product details
    private String getProductDetails(Long inventoryId) {
        // TODO: ****fix
        
        // assuming the product service has an endpoint like /v1/product/{inventoryId}
        String url = "http://product-service/v1/product/" + inventoryId;
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            return "Product details unavailable"; // fallback if service call fails
        }
    }
}