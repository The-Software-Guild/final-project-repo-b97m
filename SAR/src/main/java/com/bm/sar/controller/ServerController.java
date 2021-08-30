package com.bm.sar.controller;

import com.bm.sar.dto.Article;
import com.bm.sar.dto.Req;
import com.bm.sar.service.Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
}