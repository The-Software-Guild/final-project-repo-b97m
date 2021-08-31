/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bm.sar.dao;

import com.bm.sar.dto.Article;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
public class ArticleDaoMySqlDbTest {
    
    private ArticleDao articleDao;

    @Autowired
    public ArticleDaoMySqlDbTest(ArticleDao articleDao) {
	this.articleDao = articleDao;
    }
    
    @BeforeEach
    public void setUp() {
	assertTrue(articleDao.deleteArticles());
    }

    @Test
    public void testMakeAndGetArticles() {
	assertTrue(articleDao.getArticles().isEmpty());
	assertTrue(articleDao.getArticleById(0).isEmpty());
	assertTrue(articleDao.getArticleById(1).isEmpty());

	// The NewAPI's schema does not make clear that which
	// fields are nullable, so expect to support entries
	// that are null
	assertTrue(
	    articleDao.makeArticle(null, null, null, null, null).isPresent()
	);
	assertTrue(
	    articleDao.makeArticle(
		"Lewinsky", 
		"Strike at Khabat Studios Demanding Compensation for 90 hour workdays", 
		"The Megalith", 
		"http://megalith.org/article/12",
		LocalDateTime.of(2030, Month.MARCH, 10, 11, 12)
	    ).isPresent()
	);
	assertTrue(
	    articleDao.makeArticle(
		"Kublov", 
		"Military Operations in the Kashgathi Belt Authorized by the president", 
		"Red Flag Press", 
		"http://redflag.org/articles/66",
		LocalDateTime.of(2030, Month.MAY, 9, 1, 7)
	    ).isPresent()
	);
	// test unusually long fields
    	assertTrue(
	    articleDao.makeArticle(
		"*".repeat(1000), 
		"Military Operations in the Kashgathi Belt Authorized by the president", 
		"Red Flag Press", 
		"http://redflag.org/articles/66",
		LocalDateTime.of(2030, Month.MAY, 9, 1, 7)
	    ).isEmpty()
	);   	

	var articles = articleDao.getArticles();
	assertEquals(articles.size(), 3);
	articles.forEach(article -> {
	    assertEquals(article, articleDao.getArticleById(article.getId()).get());
	});
    }

    @Test
    public void testDeleteArticle() {
	Article articles[] = new Article[] {
	    articleDao.makeArticle(null, null, null, null, null).get(),
	    articleDao.makeArticle(
		"Lewinsky", 
		"Strike at Khabat Studios Demanding Compensation for 90 hour workdays", 
		"The Megalith", 
		"http://megalith.org/article/12",
		LocalDateTime.of(2030, Month.MARCH, 10, 11, 12)
	    ).get(),
	    articleDao.makeArticle(
		"Kublov", 
		"Military Operations in the Kashgathi Belt Authorized by the president", 
		"Red Flag Press", 
		"http://redflag.org/articles/66",
		LocalDateTime.of(2030, Month.MAY, 9, 1, 7)
	    ).get()
	};
	
	articleDao.getArticles().forEach(article -> {
	    articleDao.deleteArticle(article.getId());
	});
	assertTrue(articleDao.getArticles().isEmpty());

	Stream.of(articles[0], articles[1], articles[2]).forEach(article -> {
	    articleDao.makeArticle(
		article.getAuthor(), 
		article.getTitle(), 
		article.getSourceName(), 
		article.getUrl(), 
		article.getPublicationTime()
	    );
	});

	articleDao.deleteArticles();
	assertTrue(articleDao.getArticles().isEmpty());
    }
}
