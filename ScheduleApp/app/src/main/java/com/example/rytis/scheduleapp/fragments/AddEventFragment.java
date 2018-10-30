package com.example.rytis.scheduleapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.rytis.scheduleapp.R;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link onEventDateSelectedListener} interface
 * to handle interaction events.
 * Use the {@link AddEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEventFragment extends Fragment {

    private onEventDateSelectedListener mListener;

    public AddEventFragment() { }

    public static AddEventFragment newInstance() {
        return new AddEventFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_add_event, container, false);

        CalendarView calendarView = fragmentView.findViewById(R.id.add_event_calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String date = year + "-" + String.format(new Locale("en"),"%02d", month) + "-" +
                        String.format(new Locale("en"),"%02d", day);
                mListener.onEventDateSelected(date);
            }
        });

        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onEventDateSelectedListener) {
            mListener = (onEventDateSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onEventDateSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface onEventDateSelectedListener {
        void onEventDateSelected(String date);
    }
}
