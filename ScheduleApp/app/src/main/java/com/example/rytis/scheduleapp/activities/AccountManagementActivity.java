package com.example.rytis.scheduleapp.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rytis.scheduleapp.R;
import com.example.rytis.scheduleapp.database.DbController;
import com.example.rytis.scheduleapp.database.User;

public class AccountManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final DbController controller = new DbController(this);
        final User user = controller.getUserData();

        TextView username = findViewById(R.id.account_username);
        username.setText(user.getUserName());

        TextView email = findViewById(R.id.account_email);
        email.setText(user.getEmail());

        Button logout = findViewById(R.id.account_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.deleteUserData(user);
                Toast.makeText(AccountManagementActivity.this, "Logout successful", Toast.LENGTH_LONG).show();
                finish();
            }
        });
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
}
