package com.group.lbstore.Service;

import com.group.lbstore.Model.Review;
import com.group.lbstore.Service.ReviewService;
import com.group.lbstore.Repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    public Review createReview(Review review) {
        // TODO: Add logic to check if customer bought the product
        return reviewRepository.save(review);
    }

    public List<Review> findByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    public void deleteReview(Long id) {
        // Optional: Add logic to check if the user is the owner or an admin
        reviewRepository.deleteById(id);
    }
}