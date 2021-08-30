package com.bm.sar.dao;

import com.bm.sar.dto.Article;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Handles storage and retrieval of Article information
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 30, 2021
 */
public interface ArticleDao {
    /**
     * Retrieves a list of all the articles saved
     * @return The above list
     */
    public List<Article> getArticles();

    /**
     * Attempts to retrieve an article corresponding to the
     * id. If successful, an instance containing the matching
     * article is returned. Otherwise, an empty instance is 
     * returned.
     * 
     * @param articleId
     * @return The above instance
     */
    public Optional<Article> getArticleById(int articleId);

    /**
     * Attempts to save an article. If successful, an 
     * instance containing the saved article is returned.
     * Otherwise, an empty instance is returned.
     * 
     * @param author
     * @param title
     * @param url
     * @param publicationTime
     * @param articleSavingTime
     * @return The above instance 
     */
    public Optional<Article> makeArticle(
	String author, 
	String title,
	String sourceName,
	String url,
	LocalDateTime publicationTime
    );

    /**
     * Attempts to delete an article with the matching id.
     * Returns true if successful, otherwise false.
     * 
     * @param articleId
     * @return The above value
     */
    public boolean deleteArticle(int articleId); 
}
