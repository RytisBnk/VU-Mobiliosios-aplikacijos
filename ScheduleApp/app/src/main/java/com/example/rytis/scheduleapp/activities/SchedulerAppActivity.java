package com.example.rytis.scheduleapp.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rytis.scheduleapp.R;
import com.example.rytis.scheduleapp.database.DbController;

public class SchedulerAppActivity extends AppCompatActivity {
    private static final int AUTHENTICATION_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("SchedulerApp");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.appbar_account:
                DbController db = new DbController(this);
                if (db.getUserData() == null) {
                    Intent intent = new Intent(this, AuthenticationActivity.class);
                    startActivityForResult(intent, AUTHENTICATION_REQUEST);
                }
                else {
                    Intent intent =  new Intent(this, AccountManagementActivity.class);
                    startActivity(intent);
                }
                return super.onOptionsItemSelected(item);

            case R.id.appbar_settings:
                return super.onOptionsItemSelected(item);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTHENTICATION_REQUEST && resultCode == RESULT_OK) {
            Toast.makeText(this, "Sign in successful", Toast.LENGTH_LONG).show();
        }
    }
}
