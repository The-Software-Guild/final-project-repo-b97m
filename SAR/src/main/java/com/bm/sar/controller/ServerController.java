package com.bm.sar.controller;

import com.bm.sar.dto.Article;
import com.bm.sar.dto.Req;
import com.bm.sar.dto.Review;
import com.bm.sar.service.Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Gives information such as the # of requests
 * made within the last 24 hours, the articles saved
 * for review, and the reviews corresponding
 * to the articles.
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 30, 2021
 */
@RestController
@RequestMapping("sar-aux")
public class ServerController {

    private final Service SERVICE;

    @Autowired
    public ServerController(Service SERVICE) {
	this.SERVICE = SERVICE;
    }
    
    @GetMapping("request")
    public int requestsMade() {
	return SERVICE.getLatestRequests().size();
    }

    @PostMapping("request")
    public Req makeRequest(@RequestBody Req req) {
	var possReq = SERVICE.recordRequest(req.getText());
	if (possReq.isPresent()) {
	    return possReq.get();
	}
	return null;
    }

    @GetMapping("article")
    public List<Article> getArticles() {
    	return SERVICE.getSavedArticles();
    }

    @PostMapping("article")
    public Article saveArticle(@RequestBody Article art) {
	var possArticle = SERVICE.recordArticle(
	    art.getAuthor(), 
	    art.getTitle(), 
	    art.getSourceName(), 
	    art.getUrl(), 
	    art.getPublicationTime()
	);

	if (possArticle.isPresent()) {
	    return possArticle.get();
	} else {
	    return null;
	}
    }

    @DeleteMapping("article")
    public boolean deleteArticle(@RequestBody Article art) {
	return SERVICE.deleteArticle(art.getId());
    }

    @GetMapping("review/article/{articleId}")
    public List<Review> getReviewsByArticle(@PathVariable int articleId) {
	return SERVICE.getReviewsByArticleId(articleId);
    }

    @PostMapping("review")
    public Review makeReview(@RequestBody Review rev) {
    	var possReview = SERVICE.makeReview(rev.getText(), rev.getArticleId());
	if (possReview.isPresent()) {
	    return possReview.get();
	} else {
	    return null;
	}
    }

    @PutMapping("review")
    public boolean updateReview(@RequestBody Review rev) {
    	return SERVICE.updateReview(rev.getId(), rev.getText());
    }

    @DeleteMapping("review")
    public boolean deleteReview(@RequestBody Review rev) {
	return SERVICE.deleteReview(rev.getId());
    }
}