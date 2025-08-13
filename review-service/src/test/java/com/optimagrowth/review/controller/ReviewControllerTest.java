package com.optimagrowth.review.controller;

import com.ecommerce.review.model.Review;
import com.ecommerce.review.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ActiveProfiles("test")
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Nested
    @DisplayName("GET /reviews/product/{productId}")
    class GetReviewsByProductId {
        @Test
        @WithMockUser(username = "user", roles = {"USER"})
        @DisplayName("Should return reviews for productId")
        void shouldReturnReviewsForProductId() throws Exception {
            // Arrange
            Review review = new Review(1L, 1L, 1L, "Great product!", 5);
            Mockito.when(reviewService.getReviewsByProductId(1L)).thenReturn(Arrays.asList(review));
            // Act & Assert
            mockMvc.perform(get("/reviews/product/1")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].comment").value("Great product!"));
        }

        @Test
        @WithMockUser(username = "user", roles = {"USER"})
        @DisplayName("Should return empty list for non-existent productId")
        void shouldReturnEmptyListForNonExistentProductId() throws Exception {
            // Arrange
            Mockito.when(reviewService.getReviewsByProductId(999L)).thenReturn(Arrays.asList());
            // Act & Assert
            mockMvc.perform(get("/reviews/product/999")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }
}
