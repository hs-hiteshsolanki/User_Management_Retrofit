package com.ex.user_management_retrofit;

import com.ex.user_management_retrofit.ModelResponse.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchUserResponse {
    @SerializedName("users")
    List<User> usersList;
    String error;

    public FetchUserResponse(List<User> usersList, String error) {
        this.usersList = usersList;
        this.error = error;
    }

    public List<User> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<User> usersList) {
        this.usersList = usersList;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
