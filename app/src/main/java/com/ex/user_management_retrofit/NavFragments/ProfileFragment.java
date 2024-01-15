package com.ex.user_management_retrofit.NavFragments;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.ex.user_management_retrofit.ModelResponse.LoginResponse;
import com.ex.user_management_retrofit.ModelResponse.UpdatePassResponse;
import com.ex.user_management_retrofit.R;
import com.ex.user_management_retrofit.RetrofitClient;
import com.ex.user_management_retrofit.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment implements View.OnClickListener {
    EditText etuserName,etuserEmail,currentPass,newPass;
    Button btnUpdateAccount,btnupdateuserPassword;
    SharedPrefManager sharedPrefManager;
    int userId;
    String userEmailId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);

        //for update Account
        etuserName=view.findViewById(R.id.userName);
        etuserEmail=view.findViewById(R.id.userEmail);
        sharedPrefManager=new SharedPrefManager(getActivity());
        btnUpdateAccount=view.findViewById(R.id.btnUpdateAccount);
        //get data shardprefrens
        userId=sharedPrefManager.getUser().getId();

        //Upadte Password
        currentPass=view.findViewById(R.id.currentPass);
        newPass=view.findViewById(R.id.newPassword);
        btnupdateuserPassword=view.findViewById(R.id.btnUpdatePassword);

        userEmailId=sharedPrefManager.getUser().getEmail();

        btnUpdateAccount.setOnClickListener(this);
        btnupdateuserPassword.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnUpdateAccount) {
            updateUserAccount();
        }else if (view.getId() == R.id.btnUpdatePassword){
            updatePassword();
        }
        
    }

    private void updatePassword() {
        String userCurrentPassword = currentPass.getText().toString().trim();
        String userNewPassword = newPass.getText().toString().trim();

        if (userCurrentPassword.isEmpty()) {
            currentPass.requestFocus();
            currentPass.setError("Enter current password");
            return;
        }
        if (userCurrentPassword.length()<8) {
            currentPass.requestFocus();
            currentPass.setError("Enter 8 digit password");
            return;
        }if (userNewPassword.isEmpty()) {
            newPass.requestFocus();
            newPass.setError("Enter new password");
            return;
        }
        if (newPass.length()<8) {
            newPass.requestFocus();
            newPass.setError("Enter 8 digit password");
            return;
        }
        Call<UpdatePassResponse> call=RetrofitClient.getInstance().getApi().updateUserPass(userEmailId,userCurrentPassword,userNewPassword);
        call.enqueue(new Callback<UpdatePassResponse>() {
            @Override
            public void onResponse(Call<UpdatePassResponse> call, Response<UpdatePassResponse> response) {
                UpdatePassResponse passwordResponse =response.body();
                if(response.isSuccessful()){
                    if(passwordResponse.getError().equals("200")){
                        Toast.makeText(getActivity(), passwordResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    
                }else {
                    Toast.makeText(getActivity(), "Faild", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdatePassResponse> call, Throwable t) {
                Toast.makeText(getActivity(),t.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateUserAccount() {
        String username = etuserName.getText().toString().trim();
        String email = etuserEmail.getText().toString().trim();

        if (username.isEmpty()) {
            etuserName.requestFocus();
            etuserName.setError("please enter a user name");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etuserEmail.requestFocus();
            etuserEmail.setError("please enter a email");
            return;
        }

        Call<LoginResponse> call= RetrofitClient.getInstance().getApi().updateUserAccount(userId,username,email);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse updateResponse=response.body();
                if(response.isSuccessful()){
                    if (updateResponse.getError().equals("200")){
                        sharedPrefManager.saveUser(updateResponse.getUser());

                        etuserName.setText("");
                        etuserEmail.setText("");

                        Toast.makeText(getActivity(), updateResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), updateResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}