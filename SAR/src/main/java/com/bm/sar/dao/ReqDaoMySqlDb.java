package com.bm.sar.dao;

import com.bm.sar.dto.Req;
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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

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
    public Optional<Req> makeRequest(String text, LocalDateTime time) {
	Optional<Req> receivedInstance;
	int rowsUpdated;
	var keyHolder = new GeneratedKeyHolder();
	try {
	    rowsUpdated = jdbc.update(
		(Connection conn) -> {
		    var stmt = conn.prepareStatement(
			"INSERT INTO request (requestText, requestTime) "
			+ "VALUES (?, ?)", 
			Statement.RETURN_GENERATED_KEYS
		    );
		    stmt.setString(1, text);
		    stmt.setTimestamp(2, Timestamp.valueOf(time));
		    return stmt;
		}, 
		keyHolder
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    rowsUpdated = 0;
	}
	if (rowsUpdated > 0) {
	    Req req = new Req();
	    req.setId(keyHolder.getKey().intValue());
	    req.setText(text);
	    req.setTime(time);
	    receivedInstance = Optional.of(req);
	} else {
	    receivedInstance = Optional.empty();
	}
	return receivedInstance;
    }

    @Override
    public boolean removeRequestsBeforeTime(LocalDateTime time) {
	int rowsUpdated;
	try {
	    rowsUpdated = jdbc.update(
		"DELETE FROM request WHERE requestTime < ?",
		Timestamp.valueOf(time)
	    );
	} catch (DataAccessException ex) {
	    System.out.println(ex.getMessage());
	    rowsUpdated = 0;   
	}
	return rowsUpdated > 0;
    }

    @Override
    public List<Req> getRequests() {
	return jdbc.query(
	    "SELECT * FROM request",
	    (ResultSet rs, int index) -> {
		Req req = new Req();
		req.setId(rs.getInt("requestId"));
		req.setText(rs.getString("requestText"));
		req.setTime(rs.getTimestamp("requestTime").toLocalDateTime());
		return req;
	    }
	);
    }
}
