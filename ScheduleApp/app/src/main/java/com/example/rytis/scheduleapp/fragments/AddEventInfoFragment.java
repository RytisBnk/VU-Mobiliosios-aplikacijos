package com.example.rytis.scheduleapp.fragments;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.rytis.scheduleapp.R;
import com.example.rytis.scheduleapp.database.Event;

import java.util.Calendar;
import java.util.Locale;

public class AddEventInfoFragment extends Fragment {
    private OnEventCreationListener mListener;
    private String eventDate;
    private String eventStartTime;
    private String eventEndTime;
    private String eventTitle;

    public AddEventInfoFragment() {
        // Required empty public constructor
    }

    public static AddEventInfoFragment newInstance(String eventDate) {
        AddEventInfoFragment fragment = new AddEventInfoFragment();
        Bundle arguments = new Bundle();
        arguments.putString("eventDate", eventDate);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = this.getArguments();
        if (arguments != null) {
            eventDate = this.getArguments().getString("eventDate");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_add_event_info, container, false);
        final EditText eventName = fragmentView.findViewById(R.id.f_event_info_name);

        final EditText eventStart = fragmentView.findViewById(R.id.f_event_info_start);
        eventStart.setOnClickListener(new OnTimeFieldClickListener());

        final EditText eventEnd = fragmentView.findViewById(R.id.f_event_info_end);
        eventEnd.setOnClickListener(new OnTimeFieldClickListener());

        Button confirmButton = fragmentView.findViewById(R.id.f_event_info_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventTitle = eventName.getText().toString();
                eventStartTime = eventStart.getText().toString();
                eventEndTime = eventEnd.getText().toString();
                if (hasValue(eventTitle) && hasValue(eventStartTime) && hasValue(eventEndTime)) {
                    Event event = new Event(eventDate, eventDate, eventStartTime, eventEndTime, eventTitle);
                    mListener.onEventCreation(event);
                }
                else {
                    Toast.makeText((Activity)mListener, "All fields mut be filled", Toast.LENGTH_LONG).show();
                }
            }
        });

        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEventCreationListener) {
            mListener = (OnEventCreationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEventCreationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnEventCreationListener {
        void onEventCreation(Event event);
    }

    private boolean hasValue(String string){
        if (string != null) {
            return !string.isEmpty();
        }
        else {
            return false;
        }
    }

    private class OnTimeFieldClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view instanceof EditText) {
                final EditText editText = (EditText) view;
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                        String time = String.format(new Locale("en"), "%02d", hours) + ":"
                                + String.format(new Locale("en"), "%02d", minutes);
                        editText.setText(time);
                    }
                }, hour, minute, true);
                timePickerDialog.setTitle("Select time");
                timePickerDialog.show();
            }
        }
    }
}
