package com.bm.sar.dao;

import com.bm.sar.dto.Review;
import java.util.List;
import java.util.Optional;

/**
 * Handles storage and retrieval of Review information
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 30, 2021
 */
public interface ReviewDao {
    /**
     * Retrieves a list of all reviews
     * 
     * @return The above list
     */
    public List<Review> getReviews();

    /**
     * Retrieves a list of all reviews written for a given article
     * 
     * @param articleId
     * @return The above list
     */
    public List<Review> getReviewsByArticleId(int articleId);

    /**
     * Attempts to retrieve the review corresponding to the
     * given id. If successful, an instance containing the matching
     * review is returned. An empty instance is returned otherwise.
     * 
     * @param reviewId
     * @return The above instance
     */
    public Optional<Review> getReviewById(int reviewId);

    /**
     * Attempts to create a review with the given text for the 
     * given article. If successful, an instance containing the created
     * review is returned. An empty instance is returned otherwise.
     * 
     * @param text
     * @param articleId
     * @return The above instance
     */
    public Optional<Review> createReview(String text, int articleId); 

    /**
     * Attempts to update the review by changing the text.
     * If successful, true is returned. Otherwise, false is returned.
     * 
     * @param reviewId
     * @param text
     * @return The above value
     */
    public boolean updateReview(int reviewId, String text); 

    /**
     * Attempts to delete the review. True if successful, false otherwise
     * 
     * @param reviewId
     * @return The above value
     */
    public boolean deleteReview(int reviewId);
}