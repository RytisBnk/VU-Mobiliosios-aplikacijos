package com.example.rytis.scheduleapp.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class DbController {
    private static SchedulerDb database;

    public DbController(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, SchedulerDb.class, "schedulerDb").allowMainThreadQueries().build();
        }
    }

    public void saveUserAccount(String email, String username, String password) {
        User user = new User(username, email, password);
        database.userDao().insertUser(user);
    }

    public User getUserData() {
        List<User> users = database.userDao().getUsers();
        if (users != null && !users.isEmpty()) {
            return users.get(0);
        }
        else {
            return null;
        }
    }

    public void deleteUserData(User user) {
        database.userDao().deleteUser(user);
    }

    public void saveEvent(Event event) {
        database.eventDao().insertEvent(event);
    }

    public List<Event> getEvents(boolean showCompleted) {
        List<Event> events = database.eventDao().getEvents();
        Log.d("eventCount", Integer.toString(events.size()));
        if (showCompleted) {
            return events;
        }
        else if (!events.isEmpty()) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Calendar calendar  = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            Date current = calendar.getTime();
            List<Event> toRemove = new LinkedList<>();
            for (Event event : events) {
                try {
                    Date eventDate = format.parse(event.getStartDate());
                    if (eventDate.before(current)) {
                        toRemove.add(event);
                    }
                    Log.d("eventDate", eventDate.toString());
                }
                catch (ParseException exc) {
                    Log.d("[dateParsing]", format.toString() + "\n" + event.getStartDate());
                }
            }
            events.removeAll(toRemove);
        }
        Log.d("eventCount", Integer.toString(events.size()));
        return events;
    }

    public void saveTask(Task task) {
        database.taskDao().insertTask(task);
    }

    public List<Task> getTasks(boolean showCompleted) {
        List<Task> tasks = database.taskDao().getTasks();
        if (showCompleted) {
            return tasks;
        }
        else if (!tasks.isEmpty()) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            Date current = calendar.getTime();
            List<Task> toRemove = new LinkedList<>();
            for (Task task : tasks) {
                try {
                    Date finishDate = format.parse(task.getEndDate());
                    if (finishDate.before(current)) {
                        toRemove.add(task);
                    }
                }
                catch (ParseException exc) {
                    Log.d("[dateParsing]", format.toString() + "\n" + task.getStartDate());
                }
            }
            tasks.removeAll(toRemove);
        }
        return tasks;
    }
}
