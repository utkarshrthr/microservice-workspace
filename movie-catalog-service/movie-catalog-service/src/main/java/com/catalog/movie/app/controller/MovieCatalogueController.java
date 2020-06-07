package com.catalog.movie.app.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
//import org.springframework.web.reactive.function.client.WebClient;

import com.catalog.movie.app.model.CatalogItem;
import com.catalog.movie.app.model.Movie;
import com.catalog.movie.app.model.Rating;
import com.catalog.movie.app.model.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
//import com.netflix.discovery.DiscoveryClient;

@RestController
@RequestMapping("/api/catalog")
public class MovieCatalogueController {

	@Autowired
	private RestTemplate restTemplate;
	
	/*@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Autowired
	private DiscoveryClient discoveryClient;*/
	
	@GetMapping(path = "/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
		
		//discoveryClient.getInstancesById("");
		
		//WebClient.Builder builder = WebClient.builder();
		
		//RestTemplate restTemplate = new RestTemplate();
		
		UserRating userRating = getUserRating(userId);

		// using rest-template
		return userRating
				.getRatings()
				.stream()
				.map(rating -> getCatalogItem(rating))
				.collect(Collectors.toList()); 
		
		// using web-client builder
		/*return userRating
				.getRatings()
				.stream()
				.map(rating -> {
					Movie movie = webClientBuilder.build()	// using builder-pattern to get a client
									.get()					// for GET request
									.uri("http://movie-info-service/api/movie/"+rating.getMovieId())
									.retrieve()
									.bodyToMono(Movie.class)
									.block();				// the block converts asynchronous to synchronous
					return new CatalogItem(movie.getName(), "Leonardo DeCaprio, Kate Winslet", rating.getRating());
				})
				.collect(Collectors.toList());*/
		
		//return Collections.singletonList(new CatalogItem("Titanic", "Leonardo DeCaprio, Kate Winslet", 7));
	}

	@HystrixCommand(fallbackMethod = "getFallbackUserRating")
	private UserRating getUserRating(String userId) {
		return restTemplate.getForObject("http://movie-ratings-service/api/ratings/user/"+userId, UserRating.class);
	}
	
	private UserRating getFallbackUserRating(String userId) {
		UserRating userRating = new UserRating();
		userRating.setRatings(Arrays.asList(new Rating("0", 0)));
		userRating.setUserId(userId);
		return userRating;
	}
	
	@HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
	private CatalogItem getCatalogItem(Rating rating) {
		Movie movie = restTemplate.getForObject("http://movie-info-service/api/movie/"+rating.getMovieId(), Movie.class);
		return new CatalogItem(movie.getName(), movie.getOverview(), rating.getRating());
	}
	
	private CatalogItem getFallbackCatalogItem(Rating rating) {
		return new CatalogItem("", "", 0);
	}
}
 