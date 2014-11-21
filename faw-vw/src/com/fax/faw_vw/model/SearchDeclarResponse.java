package com.fax.faw_vw.model;

import java.io.Serializable;

public class SearchDeclarResponse implements Serializable{
	private int Success;
	private String Message;
	private String Body;
	public int getSuccess() {
		return Success;
	}
	public void setSuccess(int success) {
		Success = success;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public String getBody() {
		return Body;
	}
	public void setBody(String body) {
		Body = body;
	}

}
