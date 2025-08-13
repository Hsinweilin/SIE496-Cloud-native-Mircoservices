package com.optimagrowth.review.service;

import com.ecommerce.review.model.Review;
import com.ecommerce.review.repository.ReviewRepository;
import com.ecommerce.review.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    private Review review;

    @BeforeEach
    void setUp() {
        review = new Review(1L, 1L, 1L, "Great product!", 5);
    }

    @Nested
    @DisplayName("getReviewsByProductId tests")
    class GetReviewsByProductId {
        @Test
        @DisplayName("Should return reviews for productId")
        void shouldReturnReviewsForProductId() {
            // Arrange
            when(reviewRepository.findByInventoryId(1L)).thenReturn(Arrays.asList(review));
            // Act
            List<Review> reviews = reviewService.getReviewsByProductId(1L);
            // Assert
            assertThat(reviews).hasSize(1);
            verify(reviewRepository).findByInventoryId(1L);
        }

        @Test
        @DisplayName("Should return empty list for non-existent productId")
        void shouldReturnEmptyListForNonExistentProductId() {
            // Arrange
            when(reviewRepository.findByProductId(999L)).thenReturn(Arrays.asList());
            // Act
            List<Review> reviews = reviewService.getReviewsByProductId(999L);
            // Assert
            assertThat(reviews).isEmpty();
            verify(reviewRepository).findByProductId(999L);
        }
    }

    @Nested
    @DisplayName("addReview tests")
    class AddReview {
        @Test
        @DisplayName("Should add and return review")
        void shouldAddAndReturnReview() {
            // Arrange
            when(reviewRepository.save(review)).thenReturn(review);
            // Act
            Review saved = reviewService.addReview(review);
            // Assert
            assertThat(saved).isEqualTo(review);
            verify(reviewRepository).save(review);
        }
    }
}
