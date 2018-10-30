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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.rytis.scheduleapp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment {

    private OnLoginSuccessListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_login, container, false);

        Button createAccount = fragmentView.findViewById(R.id.login_create_account);
        createAccount.setOnClickListener(new OnCreateAccountButtonListener());

        final EditText emailV = fragmentView.findViewById(R.id.login_email);
        final EditText passwordV = fragmentView.findViewById(R.id.login_password);

        Button loginButton = fragmentView.findViewById(R.id.login_confirm);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailV.getText().toString();
                String password = passwordV.getText().toString();
                if (!email.isEmpty() && !password.isEmpty()) {
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    Request request = new JsonObjectRequest(
                            Request.Method.GET,
                            "http://schedullerapi.azurewebsites.net/api/users/login/" + email + "/" + password,
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String userName = response.getString("userName");
                                        String userEmail = response.getString("email");
                                        String userPw = response.getString("password");
                                        mListener.onLoginSuccess(userEmail, userName, userPw);
                                    }
                                    catch (JSONException exc) {
                                        Log.d("[login/jsonparse]", response.toString());
                                        Log.d("[login/jsonparse]", exc.getMessage());
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    passwordV.setText("");
                                    Toast.makeText(getContext(), "Wrong credentials entered", Toast.LENGTH_LONG).show();
                                }
                            }
                    );
                    queue.add(request);
                }
            }
        });

        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginSuccessListener) {
            mListener = (OnLoginSuccessListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginSuccessListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnLoginSuccessListener {
        void onLoginSuccess(String email, String username, String password);
        void onCreateAccountButtonClick();
    }

    private class OnCreateAccountButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            mListener.onCreateAccountButtonClick();
        }
    }
}
