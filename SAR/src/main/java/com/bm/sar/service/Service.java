package com.bm.sar.service;

import com.bm.sar.dao.ArticleDao;
import com.bm.sar.dao.ReqDao;
import com.bm.sar.dao.ReviewDao;
import com.bm.sar.dto.Article;
import com.bm.sar.dto.Req;
import com.bm.sar.dto.Review;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The service layer 
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 30, 2021
 */
@Component
public class Service {
    private ArticleDao articleDao;
    private ReqDao reqDao;
    private ReviewDao reviewDao;

    @Autowired
    public Service(ArticleDao articleDao, ReqDao reqDao, ReviewDao reviewDao) {
	this.articleDao = articleDao;
	this.reqDao = reqDao;
	this.reviewDao = reviewDao;
    }

    public List<Req> getLatestRequests() {
	reqDao.removeRequestsBeforeTime(LocalDateTime.now().minusDays(1));
	return reqDao.getRequests();
    }

    public Optional<Req> recordRequest(String requestStr) {
	return reqDao.makeRequest(requestStr, LocalDateTime.now());
    }

    public Optional<Article> recordArticle(
	String author, 
	String title, 
	String sourceName,
	String url, 
	LocalDateTime publicationTime) {

	return articleDao.makeArticle(author, title, sourceName, url, publicationTime);
    }

    public List<Article> getSavedArticles() {
	return articleDao.getArticles();
    }

    public boolean deleteArticle(int articleId) {
	return articleDao.deleteArticle(articleId);
    }

    public List<Review> getReviewsByArticleId(int articleId) {
	return reviewDao.getReviewsByArticleId(articleId);
    }

    public Optional<Review> makeReview(String text, int articleId) {
	return reviewDao.createReview(text, articleId);
    }

    public boolean updateReview(int reviewId, String text) {
	return reviewDao.updateReview(reviewId, text);
    }

    public boolean deleteReview(int reviewId) {
	return reviewDao.deleteReview(reviewId);
    }
}