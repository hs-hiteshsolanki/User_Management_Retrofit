package com.ex.user_management_retrofit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.user_management_retrofit.ModelResponse.RegisterResponse;
import com.ex.user_management_retrofit.R;
import com.ex.user_management_retrofit.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView loginlink;
    EditText name,email,password;
    Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name=findViewById(R.id.etname);
        email=findViewById(R.id.etemail);
        password=findViewById(R.id.etpassword);
        loginlink=findViewById(R.id.loginlink);
        register=findViewById(R.id.btnregister);

        loginlink.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnregister) {
            registerUser();
        } else if (id == R.id.loginlink) {
            switchOnLogin();
        }

    }

    private void switchOnLogin() {
        Intent i=new Intent(MainActivity.this,LoginActivity.class);
        startActivity(i);
    }

    private void registerUser() {
        String userName=name.getText().toString();
        String userEmail=email.getText().toString();
        String userPassword=password.getText().toString();
        if(userName.isEmpty()){
            name.requestFocus();
            name.setError("please enter a name");
            return;
        }if(userEmail.isEmpty()){
            email.requestFocus();
            email.setError("please enter a email");
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            email.requestFocus();
            email.setError("please enter a email");
            return;
        }if(userPassword.isEmpty()){
            password.requestFocus();
            password.setError("please enter a password");
            return;
        }if(userPassword.length()<8) {
            password.requestFocus();
            password.setError("please enter a password");
            return;
        }
        Call<RegisterResponse> call= RetrofitClient
                .getInstance()
                .getApi()
                .register(userName,userEmail,userPassword);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                RegisterResponse registerResponse=response.body();
                if (response.isSuccessful()){
                    Intent i =new Intent(MainActivity.this,LoginActivity.class);
                    //repit previas task is clear
                    i.setFlags(i.FLAG_ACTIVITY_NEW_TASK |i.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                    Toast.makeText(MainActivity.this,registerResponse.getMessage(),Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this,registerResponse.getError(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }
}