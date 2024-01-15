package com.ex.user_management_retrofit.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ex.user_management_retrofit.ModelResponse.LoginResponse;
import com.ex.user_management_retrofit.R;
import com.ex.user_management_retrofit.RetrofitClient;
import com.ex.user_management_retrofit.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email, password;
    Button login;
    TextView registerlink;
    SharedPrefManager sharedPrefManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        login = findViewById(R.id.btnlogin);
        registerlink = findViewById(R.id.registerlink);

        registerlink.setOnClickListener(this);
        login.setOnClickListener(this);

        sharedPrefManager = new SharedPrefManager(getApplicationContext());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnlogin) {
            userLogin();
        } else if (id == R.id.registerlink) {
            switchOnRegister();
        }

    }

    private void userLogin() {
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        if (userEmail.isEmpty()) {
            email.requestFocus();
            email.setError("please enter a email");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            email.requestFocus();
            email.setError("please enter a email");
            return;
        }
        if (userPassword.isEmpty()) {
            password.requestFocus();
            password.setError("please enter a password");
            return;
        }
        if (userPassword.length() < 8) {
            password.requestFocus();
            password.setError("please enter a password");
            return;
        }
        Call<LoginResponse> call = RetrofitClient.getInstance().getApi().login(userEmail, userPassword);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                LoginResponse loginResponse = response.body();
                if (response.isSuccessful()) {
                    if (loginResponse.getError().equals("200")) {

                        sharedPrefManager.saveUser(loginResponse.getUser());

                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        //repit previas task is clear
                        i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | i.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void switchOnRegister() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sharedPrefManager.isLoggedIn()){
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            //repit previas task is clear
            i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | i.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

    }
}