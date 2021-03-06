package com.bm.sar.dao;

import com.bm.sar.dto.Req;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Handles storage and retrieval of NewsAPI request information
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 30, 2021
 */
public interface ReqDao {
    /**
     * Attempts to record a new NewsAPI request in the database
     * If successful, an instance containing the created request 
     * is returned. Otherwise, an empty instance is returned.
     * 
     * @param text
     * @return The above instance
     */
    public Optional<Req> makeRequest(String text);

    /**
     * Removes all requests that are older than 24 hours. If
     * the removal is successful, true is returned. False is 
     * returned otherwise.
     * 
     * @return The above value
     */
    public boolean removeRequestsBeforeTime(LocalDateTime time);

    /**
     * Removes all requests. If the removal is successful, true is
     * returned. False is returned otherwise.
     * @return The above value
     */
    public boolean removeAllRequests();

    /**
     * Returns a list of all requests
     * 
     * @return The above list
     */
    public List<Req> getRequests();

    /**
     * Attempts to retrieve the request corresponding to this id
     * If successful, an instance containing the matching request is
     * returned. Otherwise, an empty instance is returned.
     * 
     * @param requestId
     * @return The above instance
     */
    public Optional<Req> getRequestById(int requestId);
}