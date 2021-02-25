package com.jenson.errorhandler.controller;

import java.util.List;

import com.jenson.errorhandler.domain.Post;
import com.jenson.errorhandler.exceptions.ContentNotAllowedException;
import com.jenson.errorhandler.util.ContentUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {
	@PostMapping(value = "/users/{username}/posts")
	public ResponseEntity<Post> create(@PathVariable String username, @RequestBody Post post)
			throws ContentNotAllowedException {
		List<ObjectError> contentNotAllowedErrors = ContentUtils.getContentErrorsFrom(post);

		if (!contentNotAllowedErrors.isEmpty()) {
			throw ContentNotAllowedException.createWith(contentNotAllowedErrors);
		}

		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
