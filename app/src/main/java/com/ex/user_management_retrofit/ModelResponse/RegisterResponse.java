package com.ex.user_management_retrofit.ModelResponse;

public class RegisterResponse{
	private String error;
	private String message;

	public RegisterResponse(String error, String message) {
		this.error = error;
		this.message = message;
	}

	public void setError(String error){
		this.error = error;
	}

	public String getError(){
		return error;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}
