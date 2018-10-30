package com.example.rytis.scheduleapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.rytis.scheduleapp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationFragment extends Fragment {

    private OnRegistrationSuccessListener mListener;
    public static String FRAGMENT_TAG = "registrationFragment";

    public RegistrationFragment() {
        // Required empty public constructor
    }

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_registration, container, false);

        final EditText usernameV = fragmentView.findViewById(R.id.register_username);

        final EditText emailV = fragmentView.findViewById(R.id.register_email);

        final EditText passwordv = fragmentView.findViewById(R.id.register_password);

        final EditText repeatV = fragmentView.findViewById(R.id.register_repeat_password);

        Button confirm = fragmentView.findViewById(R.id.register_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameV.getText().toString();
                String email = emailV.getText().toString();
                String password = passwordv.getText().toString();
                String repeat = repeatV.getText().toString();
                if (hasValue(username)
                        && hasValue(email)
                        && hasValue(password)
                        && hasValue(repeat)
                        && password.equals(repeat)) {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("userName", username);
                        json.put("email", email);
                        json.put("password", password);
                    }
                    catch (JSONException exc){
                        exc.printStackTrace();
                    }
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    queue.add(new JsonObjectRequest(
                            Request.Method.POST,
                            "http://schedullerapi.azurewebsites.net/api/users",
                            json,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String name = response.getString("userName");
                                        String pass = response.getString("password");
                                        String mail = response.getString("email");
                                        mListener.OnRegistrationSuccess(mail, name, pass);
                                    }
                                    catch (JSONException exc) {
                                        Log.d("Response", response.toString());
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("Response", error.toString());
                                }
                            }
                    ));
                }
            }
        });

        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegistrationSuccessListener) {
            mListener = (OnRegistrationSuccessListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRegistrationSuccessListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnRegistrationSuccessListener {
        void OnRegistrationSuccess(String email, String username, String password);
    }

    private boolean hasValue(String string){
        if (string != null) {
            return !string.isEmpty();
        }
        else {
            return false;
        }
    }
}
