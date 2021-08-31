/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bm.sar.dao;

import com.bm.sar.dto.Req;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
public class ReqDaoMySqlDbTest {

    private ReqDao reqDao;
    
    @Autowired
    public ReqDaoMySqlDbTest(ReqDao reqDao) {
	this.reqDao = reqDao;
    }
    
    @BeforeEach
    public void setUp() {
	assertTrue(reqDao.removeAllRequests());
    }

    @Test
    public void testMakeAndGetRequest() {
	assertTrue(reqDao.getRequests().isEmpty());
	assertTrue(reqDao.getRequestById(1).isEmpty());
	assertTrue(reqDao.getRequestById(2).isEmpty());

	assertTrue(reqDao.makeRequest(null).isEmpty());
    	assertTrue(reqDao.makeRequest("Request A").isPresent());
	assertTrue(reqDao.makeRequest("Request B").isPresent());

    	List<Req> reqs = reqDao.getRequests();
	assertEquals(reqs.size(), 2);
	assertNotEquals(reqs.get(0).getId(), reqs.get(1).getId());

	assertEquals(
	    reqs.get(0), 
	    reqDao.getRequestById(reqs.get(0).getId()).get()
	);
	assertEquals(
	    reqs.get(1), 
	    reqDao.getRequestById(reqs.get(1).getId()).get()
	);
    }

    @Test
    public void testRemoveRequestsBeforeTime() throws InterruptedException {
	reqDao.makeRequest("Some request");
	Thread.sleep(1000);
	reqDao.makeRequest("Some other request");

	List<Req> reqs = reqDao.getRequests();
    	LocalDateTime timeA = reqs.get(0).getTime();
	LocalDateTime timeB = reqs.get(1).getTime();

	LocalDateTime minTime, maxTime;
	if (timeA.compareTo(timeB) < 0) {
	    minTime = timeA;
	    maxTime = timeB;
	} else {
	    minTime = timeB;
	    maxTime = timeA;
	}

	var lowTime = minTime.minusDays(1);
	reqDao.removeRequestsBeforeTime(lowTime);
    	List<Req> lowReqs = reqDao.getRequests();
	assertEquals(reqs, lowReqs);

	reqDao.removeRequestsBeforeTime(maxTime);
	List<Req> highReqs = reqDao.getRequests();
	assertNotEquals(reqs, highReqs);
	assertEquals(highReqs.get(0).getTime(), maxTime);


	reqDao.removeRequestsBeforeTime(maxTime.plusDays(1));
	assertTrue(reqDao.getRequests().isEmpty());
    }
}
