/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bm.sar.dao;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author drive
 */
@SpringBootTest
public class ReviewDaoMySqlDbTest {
    
    private ReviewDao reviewDao;
    private ArticleDao articleDao;

    @Autowired
    public ReviewDaoMySqlDbTest(ReviewDao reviewDao, ArticleDao articleDao) {
	this.reviewDao = reviewDao;
	this.articleDao = articleDao;
    }
    
    @BeforeEach
    public void setUp() {
	assertTrue(articleDao.deleteArticles());
	assertTrue(reviewDao.deleteReviews());
    }

    @Test
    public void testCreateAndGetReviews() {
	assertTrue(reviewDao.getReviews().isEmpty());
	assertTrue(reviewDao.getReviewById(0).isEmpty());
	assertTrue(reviewDao.getReviewsByArticleId(0).isEmpty());

	var sampleArticle = articleDao.makeArticle(
	    "Sabhila", 
	    "Works on the Tigris Frontier", 
	    "Titan Chronicles",
	    "http://titan.com",
	    LocalDateTime.of(2034, 10, 2, 0, 4)
	).get();

	assertTrue(
	    reviewDao.createReview(
		"Unpublished review", 
		sampleArticle.getId() + 100
	    ).isEmpty()
	);

	reviewDao.createReview("Review 1", sampleArticle.getId());
	reviewDao.createReview("Review 2", sampleArticle.getId());
	reviewDao.createReview("Review 3", sampleArticle.getId());

	var reviews = reviewDao.getReviews();
	assertEquals(reviews.size(), 3);	
	assertTrue(
	    reviewDao.getReviewsByArticleId(
		sampleArticle.getId() + 100
	    ).isEmpty()
	);
	var reviewsByArticle = reviewDao.getReviewsByArticleId(
	    sampleArticle.getId()
	);
	assertEquals(reviews, reviewsByArticle);
	reviews.forEach(review -> {
	    assertEquals(review.getArticleId(), sampleArticle.getId());
	    assertEquals(review, reviewDao.getReviewById(review.getId()).get());
	});
    }

    @Test
    public void testUpdateReview() {
	assertFalse(reviewDao.updateReview(0, null));
	assertFalse(reviewDao.updateReview(0, "Some text"));

	var sampleArticle = articleDao.makeArticle(
	    "Sabhila", 
	    "Works on the Tigris Frontier", 
	    "Titan Chronicles",
	    "http://titan.com",
	    LocalDateTime.of(2034, 10, 2, 0, 4)
	).get();

	reviewDao.createReview("Review 1", sampleArticle.getId());
	var subjectReview = reviewDao.getReviews().get(0);

	assertFalse(reviewDao.updateReview(
	    subjectReview.getId() + 100, 
	    "oaijweoif"
	));

	assertTrue(reviewDao.updateReview(
	    subjectReview.getId(), 
	    "New Review 1"
	));

	var newReview = reviewDao.getReviews().get(0);
	assertEquals(subjectReview.getId(), newReview.getId());
	assertEquals(subjectReview.getArticleId(), newReview.getArticleId());
	assertEquals(subjectReview.getCreationTime(), newReview.getCreationTime());
	assertTrue(subjectReview.getUpdateTime().compareTo(newReview.getUpdateTime()) <= 0);
	assertEquals(newReview.getText(), "New Review 1");
    }

    @Test
    public void testDeleteReview() {
    }
    
}
