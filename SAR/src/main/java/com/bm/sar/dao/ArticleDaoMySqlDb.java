package com.bm.sar.dao;

import com.bm.sar.dto.Article;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

/**
 * An implementation of the ArticleDao
 * that interacts with a MySQL database
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 30, 2021
 */
@Repository
public class ArticleDaoMySqlDb implements ArticleDao {

    private final JdbcTemplate JDBC;

    private static final RowMapper<Article> ARTICLE_MAPPER = (ResultSet rs, int index) -> {
	var article = new Article();
	article.setId(rs.getInt(                   "articleId"));
	article.setAuthor(rs.getString(            "articleAuthor"));
	article.setTitle(rs.getString(             "articleTitle"));
	article.setSourceName(rs.getString(        "articleSource"));
	article.setUrl(rs.getString(               "articleUrl"));
	article.setPublicationTime(rs.getTimestamp("articlePublicationTime").toLocalDateTime());
	article.setSavingTime(rs.getTimestamp(     "articleSavingTime").toLocalDateTime());

	return article;
    };
    
    @Autowired
    public ArticleDaoMySqlDb(JdbcTemplate JDBC) {
	this.JDBC = JDBC;
    }
    
    @Override
    public List<Article> getArticles() {
	return JDBC.query("SELECT * FROM article", ARTICLE_MAPPER);
    }

    @Override
    public Optional<Article> getArticleById(int articleId) {
	Optional<Article> receivedInstance;
	try {
	    receivedInstance = Optional.of(JDBC.queryForObject(
		"SELECT * FROM article WHERE articleId = ?", 
		ARTICLE_MAPPER, 
		articleId
	    )); 
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    receivedInstance = Optional.empty();
	}
	return receivedInstance;
    }

    @Override
    public Optional<Article> makeArticle(
	String author, 
	String title,
	String sourceName,
	String url, 
	LocalDateTime publicationTime) {

	Optional<Article> receivedInstance;
	int rowsUpdated;
	var keyHolder = new GeneratedKeyHolder();
	try {
	    rowsUpdated = JDBC.update(
		(Connection conn) -> {
		    var stmt = conn.prepareStatement(
			"INSERT INTO article "
			+ "(articleAuthor, articleSource, articleTitle, articleUrl, articlePublicationTime) "
			+ "VALUES (?, ?, ?, ?, ?)", 
			Statement.RETURN_GENERATED_KEYS
		    );
		    stmt.setString(1, author);
		    stmt.setString(2, sourceName);
		    stmt.setString(3, title);
		    stmt.setString(4, url);
		    stmt.setTimestamp(5, Timestamp.valueOf(publicationTime));
		    return stmt;
		}, 
		keyHolder
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    rowsUpdated = 0;
	}

	if (rowsUpdated > 0) {
	    Article article = new Article();
	    article.setId(keyHolder.getKey().intValue());
	    article.setAuthor(author);
	    article.setTitle(title);
	    article.setUrl(url);
	    article.setPublicationTime(publicationTime);
	    receivedInstance = Optional.of(article);
	} else {
	    receivedInstance = Optional.empty();
	}
	return receivedInstance;
    }

    @Override
    public boolean deleteArticle(int articleId) {
	int rowsUpdated;
	try {
	    rowsUpdated = JDBC.update(
		"DELETE FROM review WHERE articleId = ?",
		articleId
	    );
	    rowsUpdated += JDBC.update(
		"DELETE FROM article WHERE articleId = ?", 
		articleId
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    rowsUpdated = 0;
	}
	return rowsUpdated > 0;
    }
}
