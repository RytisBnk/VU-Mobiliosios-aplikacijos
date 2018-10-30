package com.example.rytis.scheduleapp.activities;

import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.rytis.scheduleapp.database.DbController;
import com.example.rytis.scheduleapp.database.Event;
import com.example.rytis.scheduleapp.database.Task;
import com.example.rytis.scheduleapp.fragments.AddEventFragment;
import com.example.rytis.scheduleapp.fragments.AddEventInfoFragment;
import com.example.rytis.scheduleapp.fragments.AddTaskFragment;
import com.example.rytis.scheduleapp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AddEntryActivity extends SchedulerAppActivity
        implements AddEventFragment.onEventDateSelectedListener,
        AddTaskFragment.OnTaskCreationListener,
        AddEventInfoFragment.OnEventCreationListener {
    private DbController controller;
    private Event event;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        controller = new DbController(this);

        addFragment(AddTaskFragment.newInstance());

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.event_option :
                        swapFragment(AddEventFragment.newInstance(), R.anim.enter_from_right, R.anim.exit_to_left);
                        break;
                    case R.id.task_option :
                        swapFragment(AddTaskFragment.newInstance(), R.anim.enter_from_left, R.anim.exit_to_right);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            // set your height here
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, displayMetrics);
            // set your width here
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }
    }

    private void swapFragment(Fragment fragment, int entryAnimation, int exitAnimation) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(entryAnimation, exitAnimation);
        transaction.replace(R.id.add_entry_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.add_entry_fragment_container, fragment);
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
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onEventDateSelected(String date) {
        AddEventInfoFragment fragment = AddEventInfoFragment.newInstance(date);
        swapFragment(fragment, R.anim.enter_from_right, R.anim.exit_to_left);
    }

    @Override
    public void onTaskCreation(Task task) {
        this.task = task;
        controller = new DbController(this);
        saveLocally();
        if (controller.getUserData() != null) {
            saveToAPI();
        }
        Toast.makeText(this, "Task successfully created", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onEventCreation(Event event) {
        this.event = event;
        saveLocally();
        if (controller.getUserData() != null) {
            saveToAPI();
        }
        Toast.makeText(this, "Event successfully created", Toast.LENGTH_LONG).show();
        finish();
    }

    private void saveLocally() {
        if (event != null) {
            controller.saveEvent(event);
        }
        else if (task != null) {
            controller.saveTask(task);
        }
    }

    private void saveToAPI() {
        String url;
        JSONObject json = new JSONObject();
        if (task != null) {
            url = "http://schedullerapi.azurewebsites.net/api/tasks/"
                    + controller.getUserData().getEmail() + "/"
                    + controller.getUserData().getPassword();
            try {
                json.put("name", task.getName());
                json.put("startDate", task.getStartDate());
                json.put("endDate", task.getEndDate());
                json.put("duration", task.getDuration());
                json.put("priority", task.getPriority());
            }
            catch (JSONException exc) {
                Log.d("[JSON EXC]", exc.getMessage());
            }
        }
        else {
            url = "http://schedullerapi.azurewebsites.net/api/events/"
                    + controller.getUserData().getEmail() + "/"
                    + controller.getUserData().getPassword();
            try {
                json.put("name", event.getName());
                json.put("startDate", event.getStartDate());
                json.put("endDate", event.getEndDate());
                json.put("startTime", event.getStartTime());
                json.put("endTime", event.getEndTime());

            }
            catch (JSONException exc) {
                Log.d("[JSON EXC]", exc.getMessage());
            }
        }
        Log.d("jsonObject", json.toString());
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request =  new JsonObjectRequest(
                Request.Method.POST,
                url,
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volleyError", error.getMessage());
                    }
                }
        );
        queue.add(request);
    }
}
