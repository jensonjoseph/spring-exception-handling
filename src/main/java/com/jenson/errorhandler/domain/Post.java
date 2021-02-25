package com.jenson.errorhandler.domain;

public class Post implements Containable {
	private Student owner;
	private Long id;
	private String content;

	public Student getOwner() {
		return owner;
	}

	public Post setOwner(Student owner) {
		this.owner = owner;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
