package com.example.rytis.scheduleapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rytis.scheduleapp.R;


public class EntryTypeSelectionFragment extends Fragment {
    private OnEntrySelectionListener mListener;

    public EntryTypeSelectionFragment() {
        // Required empty public constructor
    }

    public static EntryTypeSelectionFragment newInstance() {
        return new EntryTypeSelectionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entry_type_selection, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEntrySelectionListener) {
            mListener = (OnEntrySelectionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEntrySelectionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnEntrySelectionListener {
        void onFragmentInteraction();
    }
}
