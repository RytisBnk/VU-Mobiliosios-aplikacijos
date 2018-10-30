package com.example.rytis.scheduleapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.rytis.scheduleapp.database.DbController;
import com.example.rytis.scheduleapp.fragments.LoginFragment;
import com.example.rytis.scheduleapp.R;
import com.example.rytis.scheduleapp.fragments.RegistrationFragment;

public class AuthenticationActivity extends AppCompatActivity
        implements LoginFragment.OnLoginSuccessListener,
        RegistrationFragment.OnRegistrationSuccessListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.auth_fragment_container, LoginFragment.newInstance());
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onLoginSuccess(String email, String username, String password) {
        DbController db = new DbController(this);
        db.saveUserAccount(email, username, password);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onCreateAccountButtonClick() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.auth_fragment_container, RegistrationFragment.newInstance(), RegistrationFragment.FRAGMENT_TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void OnRegistrationSuccess(String email, String username, String password) {
        DbController db = new DbController(this);
        db.saveUserAccount(email, username, password);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment registrationFragment = fragmentManager.findFragmentByTag(RegistrationFragment.FRAGMENT_TAG);
        if (registrationFragment != null && registrationFragment.isVisible()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.replace(R.id.auth_fragment_container, LoginFragment.newInstance());
            transaction.commit();
        }
        else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
