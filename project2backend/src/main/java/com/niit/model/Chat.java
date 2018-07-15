package com.niit.model;

public class Chat {

	private int id;
	private String message;				/* @NotEmpty(message="enter the valid message to be sent(*)") */
	private String to;
	private String from;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	@Override
	public String toString() {
		return "Chat [message=" + message + ", to=" + to + "]";
	}
	
}
