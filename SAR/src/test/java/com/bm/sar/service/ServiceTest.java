/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bm.sar.service;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * From the design of the Service Layer, a lot of the required
 * tests are already provided in the corresponding DAO tests.
 * 
 * Only the method for getting the latest requests will be tested
 *
 * @author drive
 */
@SpringBootTest
public class ServiceTest {

    private Service service;

    @Autowired
    public ServiceTest(Service service) {
	this.service = service;
    }
    
    @BeforeEach
    public void setUp() {
	assertTrue(service.clearData());
    }

    @Test
    public void testGetLatestRequests() {
    	assertTrue(service.getLatestRequests().isEmpty());

	assertTrue(service.recordRequest("Some request").isPresent());
	assertTrue(service.recordRequest("Some other request").isPresent());
	var latestReqs = service.getLatestRequests();
	var dayBefore = LocalDateTime.now().minusDays(1);
	assertEquals(latestReqs.size(), 2);
	latestReqs.forEach(req -> {
	    assertTrue(req.getTime().compareTo(dayBefore) >= 0);
	});
    }
}
