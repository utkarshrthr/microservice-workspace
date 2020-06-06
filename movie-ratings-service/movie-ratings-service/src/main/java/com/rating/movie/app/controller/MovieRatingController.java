package com.rating.movie.app.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rating.movie.app.model.Rating;
import com.rating.movie.app.model.UserRating;

@RestController
@RequestMapping("/api/ratings")
public class MovieRatingController {

	@GetMapping("/{movieId}")
	public Rating getRating(@PathVariable("movieId")String movieId) {
		return new Rating(movieId, 7);
	}
	
	@GetMapping("/user/{userId}")
	public UserRating getUserRatings(@PathVariable("userId")String userId) {
		List<Rating> ratings = Arrays.asList(new Rating("12", 7), new Rating("23", 8));
		UserRating userRating = new UserRating();
		userRating.setRatings(ratings);
		return userRating;
	}
}
