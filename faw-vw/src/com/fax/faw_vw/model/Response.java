package com.fax.faw_vw.model;

import java.io.Serializable;

public class Response implements Serializable{
	int Success;
	String Message;
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

}
