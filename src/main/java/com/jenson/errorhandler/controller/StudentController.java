package com.jenson.errorhandler.controller;

import com.jenson.errorhandler.domain.Student;
import com.jenson.errorhandler.exceptions.UserNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {

	@GetMapping(value = "/hello/{name}")
	public ResponseEntity<Student> greetings(@PathVariable String name) throws UserNotFoundException {
		System.out.println("Hello :" + name);

		throw UserNotFoundException.createWith(name);
	}
}
