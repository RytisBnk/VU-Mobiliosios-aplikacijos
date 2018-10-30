package com.example.rytis.scheduleapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Event.class, User.class, Task.class}, version = 1)
public abstract class SchedulerDb extends RoomDatabase {
    public abstract EventDao eventDao();
    public abstract UserDao userDao();
    public abstract TaskDao taskDao();
}
