package com.example.rytis.scheduleapp.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rytis.scheduleapp.R;
import com.example.rytis.scheduleapp.database.Task;

import java.util.ArrayList;

public class TaskListFragment extends ListFragment {
    private static final String STARTDATES = "startDates";
    private static final String ENDDATES = "endDates";
    private static final String NAMES = "names";
    private static final String DURATIONS = "durations";
    private static final String PRIORITIES = "priorities";

    public TaskListFragment() {
        // Required empty public constructor
    }

    public static TaskListFragment newInstance(ArrayList<Task> tasks) {
        TaskListFragment fragment = new TaskListFragment();

        ArrayList<String> startDates = new ArrayList<>();
        ArrayList<String> endDates = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> durations = new ArrayList<>();
        ArrayList<Integer> priorities = new ArrayList<>();

        for (Task task : tasks) {
            startDates.add(task.getStartDate());
            endDates.add(task.getEndDate());
            names.add(task.getName());
            durations.add(task.getDuration());
            priorities.add(task.getPriority());
        }

        Bundle arguments = new Bundle();
        arguments.putStringArray(STARTDATES, startDates.toArray(new String[0]));
        arguments.putStringArray(ENDDATES, endDates.toArray(new String[0]));
        arguments.putStringArray(NAMES, names.toArray(new String[0]));
        arguments.putIntegerArrayList(DURATIONS, durations);
        arguments.putIntegerArrayList(PRIORITIES, priorities);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String[] startDates = arguments.getStringArray(STARTDATES);
            String[] endDates = arguments.getStringArray(ENDDATES);
            String[] names = arguments.getStringArray(NAMES);
            ArrayList<Integer> durations = arguments.getIntegerArrayList(DURATIONS);
            ArrayList<Integer> priorities = arguments.getIntegerArrayList(PRIORITIES);

            if (startDates != null && endDates != null && names != null && durations != null && priorities != null) {
                int count = startDates.length;
                ArrayList<Task> tasks = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    Task task = new Task(startDates[i], endDates[i], names[i], durations.get(i), priorities.get(i));
                    tasks.add(task);
                }
                setListAdapter(new TaskListAdapter(getContext(), R.layout.fragment_task_list, tasks));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_list, container, false);
    }

}
