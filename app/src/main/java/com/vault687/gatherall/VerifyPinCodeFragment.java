package com.vault687.gatherall;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.HashMap;

/**
 * Created by kiwil_000 on 12/22/2014.
 */
public class VerifyPinCodeFragment extends Fragment {
    private HashMap<String, String> params = new HashMap<String, String>();
    private String username;
    private OnSignUpFragmentListener mListener;

    public VerifyPinCodeFragment() {
        // Required empty public constructor
    }

    public static VerifyPinCodeFragment newInstance() {
        VerifyPinCodeFragment fragment = new VerifyPinCodeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify_pin_code, container, false);

        final EditText pinCodeText = (EditText)view.findViewById(R.id.pin_code_edit_text);

        Button nextButton = (Button)view.findViewById(R.id.signup_button);
        nextButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                params.put("pinCode", pinCodeText.getText().toString());
                params.put("username", username);

                ParseCloud.callFunctionInBackground("verifyWithCode", params, new FunctionCallback<String>() {
                    @Override
                    public void done(String password, ParseException e) {
                        if (e == null && password != null) {
                            mListener.onPasswordObtained(username, password);
                        }
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSignUpFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
