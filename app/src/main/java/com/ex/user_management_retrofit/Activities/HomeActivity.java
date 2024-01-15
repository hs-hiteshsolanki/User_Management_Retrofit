package com.ex.user_management_retrofit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.ex.user_management_retrofit.ModelResponse.UpdatePassResponse;
import com.ex.user_management_retrofit.NavFragments.DashboardFragment;
import com.ex.user_management_retrofit.NavFragments.ProfileFragment;
import com.ex.user_management_retrofit.NavFragments.UsersFragment;
import com.ex.user_management_retrofit.R;
import com.ex.user_management_retrofit.RetrofitClient;
import com.ex.user_management_retrofit.SharedPrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        bottomNavigationView = findViewById(R.id.bottomnav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        loadFragment(new DashboardFragment());

        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        int itemId = menuItem.getItemId();
        if (itemId == R.id.dashboard) {
            fragment = new DashboardFragment();
        } else if (itemId == R.id.users) {
            fragment = new UsersFragment();
        } else if (itemId == R.id.profile) {
            fragment = new ProfileFragment();
        }

        if (fragment != null) {
            loadFragment(fragment);
        }

        return true;
    }

    void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.relativelayout, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.logout) {
            logoutUser();
        } else if (itemId == R.id.deleteAccount) {
            deleteAccount();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAccount() {
        Call<UpdatePassResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .deleteUser(sharedPrefManager.getUser().getId());
        call.enqueue(new Callback<UpdatePassResponse>() {
            @Override
            public void onResponse(Call<UpdatePassResponse> call, Response<UpdatePassResponse> response) {
                UpdatePassResponse deleteResponse =response.body();
                if(response.isSuccessful()){
                    if (deleteResponse.getError().equals("200")){
                            logoutUser();
                        Toast.makeText(HomeActivity.this, deleteResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(HomeActivity.this, deleteResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(HomeActivity.this, "failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdatePassResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void logoutUser() {

        sharedPrefManager.logout();
        Intent i = new Intent(HomeActivity.this, LoginActivity.class);
        //repit previas task is clear
        i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | i.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        Toast.makeText(this, "You have been logged out", Toast.LENGTH_SHORT).show();
    }
}