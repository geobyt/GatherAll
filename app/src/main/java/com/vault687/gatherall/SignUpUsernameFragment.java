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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSignUpFragmentListener} interface
 * to handle interaction events.
 * Use the {@link SignUpUsernameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpUsernameFragment extends Fragment {
    private OnSignUpFragmentListener mListener;

    public SignUpUsernameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SignUpUsernameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpUsernameFragment newInstance() {
        SignUpUsernameFragment fragment = new SignUpUsernameFragment();
        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_username, container, false);

        final EditText usernameText = (EditText)view.findViewById(R.id.username_edit_text);
        final EditText mobileText = (EditText)view.findViewById(R.id.phonenumber_edit_text);
        Button nextButton = (Button)view.findViewById(R.id.action_button);

        nextButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                final String username = usernameText.getText().toString();
                final String phone = mobileText.getText().toString();

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("phone", phone);
                params.put("username", username);

                ParseCloud.callFunctionInBackground("inviteWithSMS", params, new FunctionCallback<Object>() {
                    @Override
                    public void done(Object o, ParseException e) {
                        if (e == null) {
                            mListener.onUsernameAndPhoneEntered(username, phone);
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
