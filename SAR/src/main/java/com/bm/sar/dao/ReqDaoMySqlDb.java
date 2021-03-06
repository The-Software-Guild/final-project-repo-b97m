package com.bm.sar.dao;

import com.bm.sar.dto.Req;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * An implementation of the ReqDao that interacts with a
 * MySQL database
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 30, 2021
 */
@Repository
public class ReqDaoMySqlDb implements ReqDao {
    private JdbcTemplate jdbc;

    @Autowired
    public ReqDaoMySqlDb(JdbcTemplate jdbc) {
	this.jdbc = jdbc;
    }
    
    @Override
    @Transactional
    public Optional<Req> makeRequest(String text) {
	Optional<Req> receivedInstance;
	int rowsUpdated;
	var keyHolder = new GeneratedKeyHolder();
	try {
	    rowsUpdated = jdbc.update(
		(Connection conn) -> {
		    var stmt = conn.prepareStatement(
			"INSERT INTO request (requestText) "
			+ "VALUES (?)", 
			Statement.RETURN_GENERATED_KEYS
		    );
		    stmt.setString(1, text);
		    return stmt;
		}, 
		keyHolder
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    rowsUpdated = 0;
	}
	if (rowsUpdated > 0) {
	    receivedInstance = getRequestById(keyHolder.getKey().intValue());
	} else {
	    receivedInstance = Optional.empty();
	}
	return receivedInstance;
    }

    @Override
    public boolean removeRequestsBeforeTime(LocalDateTime time) {
	boolean success;
	try {
	    jdbc.update(
		"DELETE FROM request WHERE requestTime < ?",
		Timestamp.valueOf(time)
	    );
	    success = true;
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    success = false;
	}
	return success;
    }

    @Override
    public boolean removeAllRequests() {
	boolean success;
	try {
	    jdbc.update("DELETE FROM request");
	    success = true;
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    success = false;
	}
	return success;
    }

    @Override
    public List<Req> getRequests() {
	var retrVal = jdbc.query(
	    "SELECT * FROM request",
	    (ResultSet rs, int index) -> {
		Req req = new Req();
		req.setId(rs.getInt("requestId"));
		req.setText(rs.getString("requestText"));
		req.setTime(rs.getTimestamp("requestTime").toLocalDateTime());
		return req;
	    }
	);
	if (retrVal == null) {
	    return new LinkedList<>();
	}
	return retrVal;
    }

    @Override
    public Optional<Req> getRequestById(int requestId) {
	Optional<Req> receivedInstance;
	try {
	    receivedInstance = Optional.of(jdbc.queryForObject(
		"SELECT * FROM request where requestId = ?",
		(ResultSet rs, int index) -> {
		    Req req = new Req();
		    req.setId(rs.getInt("requestId"));
		    req.setText(rs.getString("requestText"));
		    req.setTime(rs.getTimestamp("requestTime").toLocalDateTime());
		    return req;
		},
		requestId
	    ));
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    receivedInstance = Optional.empty();
	}
	return receivedInstance;
    }
}
