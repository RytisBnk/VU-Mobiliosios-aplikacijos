package com.example.rytis.scheduleapp.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.rytis.scheduleapp.R;
import com.example.rytis.scheduleapp.database.Task;

import java.util.ArrayList;

public class TaskListAdapter extends ArrayAdapter<Task> {

    private OnTaskDeletionListener mListener;

    public TaskListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Task> objects) {
        super(context, resource, objects);
        if (context instanceof OnTaskDeletionListener) {
            mListener = (OnTaskDeletionListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement TasKListAdapter.OnTaskDeletionListener");
        }
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_list_row, parent, false);
        }

        final Task task = getItem(position);

        TextView name = convertView.findViewById(R.id.task_row_name);
        name.setText(task.getName());

        TextView start = convertView.findViewById(R.id.task_row_start);
        start.setText(task.getStartDate());

        TextView end = convertView.findViewById(R.id.task_row_end);
        end.setText(task.getEndDate());

        TextView priority = convertView.findViewById(R.id.task_row_priority);
        switch (task.getPriority()) {
            case 0:
                priority.setText("High priority");
                break;
            case 1:
                priority.setText("Medium priority");
                break;

            case 2:
                priority.setText("Low priority");
                break;

            default:
                break;
        }

        TextView duration = convertView.findViewById(R.id.task_row_duration);
        duration.setText("Length: " + task.getDuration() + " hours");

        Button delete = convertView.findViewById(R.id.row_task_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskListAdapter.this.remove(TaskListAdapter.this.getItem(position));
                TaskListAdapter.this.notifyDataSetChanged();
                mListener.onTaskDeletion(task, position);
            }
        });

        return convertView;
    }

    public interface OnTaskDeletionListener {
        void onTaskDeletion(Task task, int position);
    }
}
