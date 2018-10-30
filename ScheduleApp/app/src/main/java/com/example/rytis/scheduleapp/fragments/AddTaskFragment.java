package com.example.rytis.scheduleapp.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.rytis.scheduleapp.R;
import com.example.rytis.scheduleapp.database.Task;

import java.util.Calendar;
import java.util.Locale;


public class AddTaskFragment extends Fragment {

    private OnTaskCreationListener mListener;
    private int taskPriority;
    private String taskName;
    private int taskDuration;
    private String taskStartDate;
    private String taskEndDate;

    public AddTaskFragment() {
        // Required empty public constructor
    }

    public static AddTaskFragment newInstance() {
        return new AddTaskFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_add_task, container, false);

        final Spinner priority = fragmentView.findViewById(R.id.f_add_task_priority);
        String[] choices = {"high", "medium", "low"};
        priority.setAdapter(new ArrayAdapter<>(getContext(), R.layout.priority_spinner_layout, choices));
        priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                taskPriority = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final EditText name = fragmentView.findViewById(R.id.f_add_task_name);
        final EditText duration = fragmentView.findViewById(R.id.f_add_task_duration);

        final EditText startDate = fragmentView.findViewById(R.id.f_add_task_start);
        startDate.setOnClickListener(new OnDateFieldClickListener());

        final EditText endDate = fragmentView.findViewById(R.id.f_add_task_end);
        endDate.setOnClickListener(new OnDateFieldClickListener());

        Button saveTask = fragmentView.findViewById(R.id.f_add_task_confirm);
        saveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskName = name.getText().toString();
                taskDuration = Integer.parseInt(duration.getText().toString());
                taskStartDate = startDate.getText().toString();
                taskEndDate = endDate.getText().toString();
                if (hasValue(taskName) && hasValue(taskStartDate)
                        && hasValue(taskEndDate)
                        && taskPriority > 0
                        && taskDuration > 0) {
                    Task task = new Task(taskStartDate, taskEndDate, taskName, taskDuration, taskPriority);
                    mListener.onTaskCreation(task);
                }
            }
        });

        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTaskCreationListener) {
            mListener = (OnTaskCreationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTaskCreationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnTaskCreationListener {
        void onTaskCreation(Task task);
    }

    private boolean hasValue(String string){
        if (string != null) {
            return !string.isEmpty();
        }
        else {
            return false;
        }
    }

    private class OnDateFieldClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view instanceof EditText) {
                final EditText editText = (EditText) view;
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String date = year + "-"
                                + String.format(new Locale("en"), "%02d", month) + "-"
                                + String.format(new Locale("en"), "%02d", day);
                        editText.setText(date);
                    }
                }, year, month, day);
                dialog.setTitle("Select date");
                dialog.show();
            }
        }
    }
}
