package com.example.rytis.scheduleapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.rytis.scheduleapp.R;
import com.example.rytis.scheduleapp.database.DbController;
import com.example.rytis.scheduleapp.database.Event;
import com.example.rytis.scheduleapp.database.Task;
import com.example.rytis.scheduleapp.database.User;
import com.example.rytis.scheduleapp.fragments.EventListAdapter;
import com.example.rytis.scheduleapp.fragments.EventListFragment;
import com.example.rytis.scheduleapp.fragments.TaskListAdapter;
import com.example.rytis.scheduleapp.fragments.TaskListFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends SchedulerAppActivity implements EventListAdapter.OnEventDeletionListener,
        TaskListAdapter.OnTaskDeletionListener {

    public static final int RESPONSE_LIST_EVENT = 1;
    public static final int RESPONSE_LIST_TASK = 2;

    private List<Event> events = new ArrayList<>();
    private List<Task> tasks = new ArrayList<>();

    private boolean eventsLoaded = false;
    private boolean tasksLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView title = findViewById(R.id.main_title);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new ActionButtonListener());

        final DbController controller = new DbController(this);

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
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
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()) {
                    case R.id.event_option:
                        title.setText("Upcoming events");
                        if (controller.getUserData() == null) {
                            List<Event> events = controller.getEvents(true);
                            swapFragment(EventListFragment.newInstance(new ArrayList<>(events)), R.anim.enter_from_left, R.anim.exit_to_right);
                        }
                        else {

                                events = new ArrayList<>();
                             String url = "https://schedullerapi.azurewebsites.net/api/events/";
                             GetAPIData(MainActivity.RESPONSE_LIST_EVENT, controller, url);

                            swapFragment(EventListFragment.newInstance(new ArrayList<>(events)), R.anim.enter_from_left, R.anim.exit_to_right);
                        }
                        break;
                    case R.id.task_option:
                        title.setText("Ongoing tasks");
                        if (controller.getUserData() == null) {
                            ArrayList<Task> tasks = new ArrayList<>(controller.getTasks(false));
                            swapFragment(TaskListFragment.newInstance(tasks), R.anim.enter_from_right, R.anim.exit_to_left);
                        }
                        else{

                            tasks = new ArrayList<>();
                                String url = "https://schedullerapi.azurewebsites.net/api/tasks/";
                                GetAPIData(MainActivity.RESPONSE_LIST_TASK, controller, url);

                            swapFragment(TaskListFragment.newInstance(new ArrayList<>(tasks)), R.anim.enter_from_right, R.anim.exit_to_left);
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }

    @Override
    public void onEventDeletion(Event event, int position) {
        events.remove(position);
        DbController controller = new DbController(this);
        if (controller.getUserData() != null) {
            String url = "http://schedullerapi.azurewebsites.net/api/events/";
            JSONObject json = new JSONObject();
            try {
                json.put("startDate", event.getStartDate());
                json.put("endDate", event.getEndDate());
                json.put("endTime", event.getEndTime());
                json.put("startTime", event.getStartTime());
                json.put("name", event.getName());
                Log.d("jsonSent", json.toString());
            }
            catch (JSONException exc) {
                Log.d("jsonExc", exc.getMessage());
            }
            deleteRequest(json, url, controller);
        }
    }

    @Override
    public void onTaskDeletion(Task task, int position) {
        tasks.remove(position);
        DbController controller = new DbController(this);
        String url = "http://schedullerapi.azurewebsites.net/api/tasks/";
        JSONObject json = new JSONObject();
        try {
            json.put("startDate", task.getStartDate());
            json.put("endDate", task.getEndDate());
            json.put("name", task.getName());
            json.put("duration", task.getDuration());
            json.put("priority", task.getPriority());
        }
        catch (JSONException exc) {
            Log.d("jsonExc", exc.getMessage());
        }
        deleteRequest(json, url, controller);
    }

    private void deleteRequest(JSONObject object, String url, DbController controller) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url + controller.getUserData().getEmail() + "/" + controller.getUserData().getPassword() + "/delete",
                object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(MainActivity.this, "Successfully deleted", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // volley is a piece of shit, but no good alternatives
                    }
                }
        );
        queue.add(request);
    }

    private class ActionButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, AddEntryActivity.class);
            startActivity(intent);
        }
    }

    private void swapFragment(Fragment fragment, int entryAnimation, int exitAnimation) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(entryAnimation, exitAnimation);
        transaction.replace(R.id.main_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void updateFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, fragment);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        DbController controller = new DbController(this);
        User user = controller.getUserData();
        if (!eventsLoaded) {
            if (user != null) {
                GetAPIData(MainActivity.RESPONSE_LIST_EVENT, controller, "https://schedullerapi.azurewebsites.net/api/events/");
            }
            else {
                events = new ArrayList<>(controller.getEvents(true));

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.main_fragment_container, EventListFragment.newInstance((ArrayList<Event>) events));
                transaction.commit();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void GetAPIData(int expectedType, DbController controller, String url) {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url + controller.getUserData().getEmail() + "/" + controller.getUserData().getPassword(),
                null,
                new OnAPIResponseListener(expectedType),
                new OnApiErrorListener()
        );
        queue.add(request);
    }

    private class OnAPIResponseListener implements Response.Listener<JSONArray> {

        private int expectedResponse;

        public OnAPIResponseListener(int expectedResponse) {
            super();
            this.expectedResponse = expectedResponse;
        }

        @Override
        public void onResponse(JSONArray response) {
            for (int  i = 0; i < response.length(); i++) {
                try {
                    JSONObject object = response.getJSONObject(i);
                    if (expectedResponse == MainActivity.RESPONSE_LIST_EVENT) {
                        String eventName = object.getString("name");
                        String eventStartDate = object.getString("startDate");
                        String eventEndDate = object.getString("endDate");
                        String eventStartTime = object.getString("startTime");
                        String eventEndTime = object.getString("endTime");
                        Event event = new Event(eventStartDate, eventEndDate, eventStartTime, eventEndTime, eventName);
                        events.add(event);
                        eventsLoaded = true;
                        updateFragment(EventListFragment.newInstance(new ArrayList<>(events)));
                    }
                    else if (expectedResponse == MainActivity.RESPONSE_LIST_TASK) {
                        String taskStartDate = object.getString("startDate");
                        String taskEndDate = object.getString("endDate");
                        String taskName = object.getString("name");
                        int taskDuration = object.getInt("duration");
                        int taskPriority = object.getInt("priority");
                        Task task = new Task(taskStartDate, taskEndDate, taskName, taskDuration, taskPriority);
                        tasks.add(task);
                        tasksLoaded = true;
                        updateFragment(TaskListFragment.newInstance(new ArrayList<>(tasks)));
                    }
                }
                catch (JSONException exc) {
                    Log.d("apiResponseParseError", exc.getMessage());
                    Log.d("apiResponse", response.toString());
                }
            }
        }
    }

    private class OnApiErrorListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("apiResponseError", error.getMessage());
        }
    }
}
