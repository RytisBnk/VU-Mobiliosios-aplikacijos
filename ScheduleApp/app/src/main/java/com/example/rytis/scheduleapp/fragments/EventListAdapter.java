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
import com.example.rytis.scheduleapp.database.Event;

import java.util.ArrayList;

public class EventListAdapter extends ArrayAdapter<Event> {
    private OnEventDeletionListener mListener;

    public EventListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Event> objects) {
        super(context, resource, objects);
        if (context instanceof OnEventDeletionListener) {
            mListener = (OnEventDeletionListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement EventListAdapter.OnEventDeletionListener");
        }
    }

    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_list_row, parent, false);
        }

        final Event event = getItem(position);

        TextView dateView = convertView.findViewById(R.id.list_row_date);
        dateView.setText(event.getStartDate());

        TextView nameView = convertView.findViewById(R.id.list_row_name);
        nameView.setText(event.getName());

        TextView startTimeView = convertView.findViewById(R.id.list_row_start_time);
        startTimeView.setText(event.getStartTime());

        TextView endTimeView = convertView.findViewById(R.id.list_row_end_time);
        endTimeView.setText(event.getEndTime());

        Button delete = convertView.findViewById(R.id.row_event_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventListAdapter.this.remove(EventListAdapter.this.getItem(position));
                EventListAdapter.this.notifyDataSetChanged();
                mListener.onEventDeletion(event, position);
            }
        });

        return convertView;
    }

    public interface OnEventDeletionListener {
        void onEventDeletion(Event event, int position);
    }
}
