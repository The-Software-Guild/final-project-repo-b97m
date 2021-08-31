package com.bm.sar.dao;

import com.bm.sar.dto.Review;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * An implementation of the ReviewDao that interacts
 * with a MySQL database
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 30, 2021
 */
@Repository
public class ReviewDaoMySqlDb implements ReviewDao {
    
    private final JdbcTemplate JDBC;

    private static final RowMapper<Review> REVIEW_MAPPER = (ResultSet rs, int index) -> {
	Review rev = new Review();
	rev.setId(rs.getInt(                "reviewId"));
	rev.setCreationTime(rs.getTimestamp("reviewCreationTime").toLocalDateTime());
	rev.setUpdateTime(rs.getTimestamp(  "reviewUpdateTime").toLocalDateTime());
	rev.setText(rs.getString(           "reviewText"));
	rev.setArticleId(rs.getInt(         "articleId"));
	return rev;
    };
    
    @Autowired
    public ReviewDaoMySqlDb(JdbcTemplate JDBC) {
	this.JDBC = JDBC;
    }
    
    @Override
    public List<Review> getReviews() {
	return JDBC.query("SELECT * FROM review", REVIEW_MAPPER);
    }

    @Override
    public List<Review> getReviewsByArticleId(int articleId) {
	return JDBC.query(
	    "SELECT * FROM review WHERE articleId = ?", 
	    REVIEW_MAPPER, 
	    articleId
	);
    }

    @Override
    public Optional<Review> getReviewById(int reviewId) {
	Optional<Review> receivedInstance;
	try {
	    receivedInstance = Optional.of(JDBC.queryForObject(
		"SELECT * FROM review WHERE reviewId = ?", 
		REVIEW_MAPPER, 
		reviewId
	    ));
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    receivedInstance = Optional.empty();
	}
	return receivedInstance;
    }

    @Override
    @Transactional
    public Optional<Review> createReview(String text, int articleId) {
	Optional<Review> receivedInstance;
	int rowsUpdated;
	var keyHolder = new GeneratedKeyHolder();
	try {
	    rowsUpdated = JDBC.update(
		(Connection conn) -> {
		    var stmt = conn.prepareStatement(
			"INSERT INTO review (reviewText, articleId) VALUES (?, ?)", 
			Statement.RETURN_GENERATED_KEYS
		    );
		    stmt.setString(1, text);
		    stmt.setInt(2, articleId);
		    return stmt;
		}, 
		keyHolder
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    rowsUpdated = 0;
	}
	if (rowsUpdated > 0) {
	    return getReviewById(keyHolder.getKey().intValue());
	} else {
	    return Optional.empty();
	}
    }

    @Override
    public boolean updateReview(int reviewId, String text) {
	int rowsUpdated;
	try {
	    rowsUpdated = JDBC.update(
		"UPDATE review SET "
		+ "reviewText = ?, "
		+ "reviewUpdateTime = NOW() "
		+ "WHERE reviewId = ?", text, reviewId
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    rowsUpdated = 0;
	}
	return rowsUpdated == 1;
    }

    @Override
    public boolean deleteReview(int reviewId) {
	boolean success;
	try {
	    JDBC.update(
		"DELETE FROM review WHERE reviewId = ?", 
		reviewId
	    );
	    success = true;
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    success = false;
	}
	return success;
    }

    @Override
    public boolean deleteReviews() {
    	boolean success;
	try {
	    JDBC.update("DELETE FROM review");
	    success = true;
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    success = false;
	}
	return success;
    }
}