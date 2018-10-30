package com.example.rytis.scheduleapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rytis.scheduleapp.R;
import com.example.rytis.scheduleapp.database.Event;

import java.util.ArrayList;

public class EventListFragment extends ListFragment {
    private static final String DATES = "dates";
    private static final String STARTTIMES = "startTimes";
    private static final String ENDTIMES = "endTimes";
    private static final String NAMES = "names";

    public EventListFragment() {
        // Required empty public constructor
    }

    public static EventListFragment newInstance(ArrayList<Event> events) {
        EventListFragment fragment = new EventListFragment();
        Bundle arguments = new Bundle();
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> startTimes = new ArrayList<>();
        ArrayList<String> endTimes = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();

        for (Event event : events) {
            dates.add(event.getStartDate());
            startTimes.add(event.getStartTime());
            endTimes.add(event.getEndTime());
            names.add(event.getName());
        }

        arguments.putStringArray(DATES,  dates.toArray(new String[0]));
        arguments.putStringArray(STARTTIMES,  startTimes.toArray(new String[0]));
        arguments.putStringArray(ENDTIMES,  endTimes.toArray(new String[0]));
        arguments.putStringArray(NAMES, names.toArray(new String[0]));
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null ) {
            String[] dates = arguments.getStringArray(DATES);
            String[] names = arguments.getStringArray(NAMES);
            String[] startTimes = arguments.getStringArray(STARTTIMES);
            String[] endTimes = arguments.getStringArray(ENDTIMES);

            if (dates != null && names != null && startTimes != null && endTimes != null) {
                int count = dates.length;
                ArrayList<Event> events = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    Event event =  new Event(dates[i], dates[i], startTimes[i], endTimes[i], names[i]);
                    events.add(event);
                }
                setListAdapter(new EventListAdapter(getContext(), R.layout.fragment_event_list,
                        events));
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }
}
