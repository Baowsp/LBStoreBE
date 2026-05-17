package com.group.lbstore.Service;
import com.group.lbstore.Model.Review;
import java.util.List;
public interface ReviewService {
    Review createReview(Review review);
    List<Review> findByProductId(Long productId);
    void deleteReview(Long id);
}