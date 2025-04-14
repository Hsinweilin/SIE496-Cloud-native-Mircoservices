package com.ecommerce.review.repository;

import com.ecommerce.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByInventoryId(Long inventoryId); // get all reviews for a product
    List<Review> findByUserId(Long userId); // get all reviews by a user
    Optional<Review> findByUserIdAndInventoryId(Long userId, Long inventoryId); // get a specific review by user and product
}