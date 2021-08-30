package com.bm.sar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Serves the template pages 
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 26, 2021
 */
@Controller
@RequestMapping("sar")
public class PagesController {
    @GetMapping("home")
    public String home() {
    	return "index";
    }

    @GetMapping("search")
    public String search() {
	return "search";
    }

    @GetMapping("review")
    public String review() {
	return "review";
    }
}