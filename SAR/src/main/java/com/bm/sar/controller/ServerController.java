package com.bm.sar.controller;

import com.bm.sar.dto.Article;
import com.bm.sar.dto.Req;
import com.bm.sar.dto.Review;
import com.bm.sar.service.Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity makeRequest(@RequestBody Req req) {
	var possReq = SERVICE.recordRequest(req.getText());
	if (possReq.isPresent()) {
	    return new ResponseEntity(possReq.get(), HttpStatus.CREATED);
	}
	return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("article")
    public List<Article> getArticles() {
    	return SERVICE.getSavedArticles();
    }

    @PostMapping("article")
    public ResponseEntity saveArticle(@RequestBody Article art) {
	var possArticle = SERVICE.recordArticle(
	    art.getAuthor(), 
	    art.getTitle(), 
	    art.getSourceName(), 
	    art.getUrl(), 
	    art.getPublicationTime()
	);

	if (possArticle.isPresent()) {
	    return new ResponseEntity(possArticle.get(), HttpStatus.CREATED);
	} else {
	    return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
	}
    }

    @DeleteMapping("article")
    public ResponseEntity deleteArticle(@RequestBody Article art) {
	boolean success = SERVICE.deleteArticle(art.getId());
	if (success) {
	    return new ResponseEntity(success, HttpStatus.OK);
	}
	return new ResponseEntity(success, HttpStatus.INTERNAL_SERVER_ERROR);
	
    }

    @GetMapping("review/article/{articleId}")
    public List<Review> getReviewsByArticle(@PathVariable int articleId) {
	return SERVICE.getReviewsByArticleId(articleId);
    }

    @PostMapping("review")
    public ResponseEntity makeReview(@RequestBody Review rev) {
    	var possReview = SERVICE.makeReview(rev.getText(), rev.getArticleId());
	if (possReview.isPresent()) {
	    return new ResponseEntity(possReview.get(), HttpStatus.CREATED);
	} else {
	    return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
	}
    }

    @PutMapping("review")
    public ResponseEntity updateReview(@RequestBody Review rev) {
	boolean success = SERVICE.updateReview(rev.getId(), rev.getText());
	if (success) {
	    return new ResponseEntity(success, HttpStatus.OK);
	}
	return new ResponseEntity(success, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("review")
    public ResponseEntity deleteReview(@RequestBody Review rev) {
	boolean success = SERVICE.deleteReview(rev.getId());
	if (success) {
	    return new ResponseEntity(success, HttpStatus.OK);
	}
	return new ResponseEntity(success, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}