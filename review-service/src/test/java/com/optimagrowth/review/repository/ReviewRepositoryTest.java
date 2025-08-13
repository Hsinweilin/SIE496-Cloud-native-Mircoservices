package com.optimagrowth.review.repository;

import com.ecommerce.review.model.Review;
import com.ecommerce.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
        Review review1 = new Review(null, 1L, 1L, "Great product!", 5);
        Review review2 = new Review(null, 2L, 1L, "Not bad", 4);
        reviewRepository.save(review1);
        reviewRepository.save(review2);
    }

    @Nested
    @DisplayName("findByProductId tests")
    class FindByProductId {
        @Test
        @DisplayName("Should return reviews for given productId")
        void shouldReturnReviewsForProductId() {
            // Arrange/Act
            List<Review> reviews = reviewRepository.findByInventoryId(1L);
            // Assert
            assertThat(reviews).hasSize(2);
        }

        @Test
        @DisplayName("Should return empty list for non-existent productId")
        void shouldReturnEmptyListForNonExistentProductId() {
            // Arrange/Act
            List<Review> reviews = reviewRepository.findByInventoryId(999L);
            // Assert
            assertThat(reviews).isEmpty();
        }
    }
}
