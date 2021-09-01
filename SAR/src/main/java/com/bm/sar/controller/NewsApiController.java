package com.bm.sar.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Makes calls to the News API. 
 * 
 * In order for this controller to work properly, this class
 * must be able to access the 
 * news_api_key.txt file which should contain only the NewsAPI
 *
 * @author Benjamin Munoz
 * email: driver396@gmail.com
 * date: Aug 27, 2021
 */
@RestController
@RequestMapping("sar-secrets")
public class NewsApiController {
    // Please ensure the API key does not get seen!
    private final String BASE = "https://newsapi.org/v2/everything";

    public final String API_KEY;

    public NewsApiController() throws FileNotFoundException {
	Scanner apiScanner = new Scanner(
	    new BufferedReader(
		new FileReader("news_api_key.txt")
	    )
	);
	API_KEY = apiScanner.nextLine();
	apiScanner.close();
    }

    @GetMapping("articles/{queryStr}")
    public ResponseEntity getArticles(@PathVariable String queryStr) {
	System.out.println("/--\\");
	System.out.println(queryStr);
	queryStr = URLEncoder.encode(queryStr, StandardCharsets.UTF_8);
	System.out.println(queryStr);
	System.out.println("\\--/");

	var requestSpec = WebClient.create(BASE)
	    .method(HttpMethod.GET)
	    .uri(String.format("?apiKey=%s&q=%s", API_KEY, queryStr));

	/* -- Seems to result in a status code of 401 if no API key is provided
	var requestSpec = WebClient.create(BASE)
	    .method(HttpMethod.GET)
	    .uri("?q=" + queryStr);
	*/

	// bypass requirement that variables outsitde lambdas must be 
	// "effectively final" within lambdas
    	HttpStatus stats[] = new HttpStatus[1];
    	Mono<String> res = requestSpec.exchangeToMono(response -> {
	    var statCode = response.statusCode();
	    stats[0] = response.statusCode();
    	    if (statCode.equals(HttpStatus.OK)) {
		return response.bodyToMono(String.class);
	    } else if (statCode.is4xxClientError()) {
		return Mono.just("Error response " + statCode);
	    } else {
		return response.createException().flatMap(Mono::error);
	    }
	});
	return new ResponseEntity(res.block(), stats[0]);
    }
}