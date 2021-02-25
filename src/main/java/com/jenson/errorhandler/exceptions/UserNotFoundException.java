package com.jenson.errorhandler.exceptions;

public class UserNotFoundException extends Exception {
	private String username;

	private UserNotFoundException(String username) {
		this.username = username;
	}

	public static UserNotFoundException createWith(String username) {
		return new UserNotFoundException(username);
	}

	@Override
	public String getMessage() {
		return "User '" + username + "' not found";
	}
}
