package com.ecommerce.review.controller;

import com.ecommerce.review.model.Review;
import com.ecommerce.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping(value = "v1/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // create a review
    @RolesAllowed("USER")
    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Review review) {
        Review createdReview = reviewService.createReview(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    // update a review
    @RolesAllowed("USER")
    @PutMapping("/{inventoryId}")
    public ResponseEntity<?> updateReview(@PathVariable Long inventoryId, @RequestBody Review updatedReview, @RequestParam Long userId) {
        Review review = reviewService.updateReview(userId, inventoryId, updatedReview);
        if (review == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(review);
    }

    // delete a review
    @RolesAllowed("USER")
    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long inventoryId, @RequestParam Long userId) {
        String message = reviewService.deleteReview(userId, inventoryId);
        if (message.equals("Review not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    // get all reviews for a product
    @GetMapping("/product/{inventoryId}")
    public ResponseEntity<List<Review>> getReviewsByInventoryId(@PathVariable Long inventoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewsByInventoryId(inventoryId));
    }

    // get all reviews by a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewsByUserId(userId));
    }

    // get a specific review by user and product
    @GetMapping("/{inventoryId}")
    public ResponseEntity<?> getReviewByUserAndInventory(@PathVariable Long inventoryId, @RequestParam Long userId) {
        Review review = reviewService.getReviewByUserAndInventory(userId, inventoryId);
        if (review == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(review);
    }
}