package com.vault687.gatherall;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class SignUpActivity
        extends FragmentActivity
        implements SignUpUsernameFragment.OnFragmentInteractionListener {

    // UI references.
/*    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText passwordAgainEditText;*/

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
        this.viewPager = (ViewPager)this.findViewById(R.id.pager);
        this.viewPager.setAdapter(new SignUpFragmentManager(getSupportFragmentManager()));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class SignUpFragmentManager extends FragmentPagerAdapter {

        public SignUpFragmentManager(FragmentManager mgr) {
            super(mgr);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return SignUpUsernameFragment.newInstance();

                case 1:
                    break;

                default:
                    return SignUpUsernameFragment.newInstance();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 1;
        }
    }
/*
    private void signup() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String passwordAgain = passwordAgainEditText.getText().toString().trim();

        // Validate the sign up data
        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder(getString(R.string.error_intro));

        if (username.length() == 0)
        {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_username));
        }

        if (password.length() == 0)
        {
            if (validationError)
            {
                validationErrorMessage.append(getString(R.string.error_join));
            }

            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_password));
        }

        if (!password.equals(passwordAgain))
        {
            if (validationError)
            {
                validationErrorMessage.append(getString(R.string.error_join));
            }

            validationError = true;
            validationErrorMessage.append(getString(R.string.error_mismatched_passwords));
        }
        validationErrorMessage.append(getString(R.string.error_end));

        // If there is a validation error, display the error
        if (validationError)
        {
            Toast.makeText(SignUpActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
            return;
        }

        // Set up a progress dialog
        final ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
        dialog.setMessage(getString(R.string.progress_signup));
        dialog.show();

        // Set up a new Parse user
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);

        // Call the Parse signup method
        user.signUpInBackground(new SignUpCallback()
        {
            @Override
            public void done(ParseException e)
            {
            dialog.dismiss();

            if (e != null)
            {
                // Show the error message
                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            else
            {
                // Check if there is current user info
                if (ParseUser.getCurrentUser() != null)
                {
                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();

                    if (installation.get("user") == null)
                    {
                        installation.put("user", ParseUser.getCurrentUser());
                        installation.saveInBackground();

                        ParsePush.subscribeInBackground("", new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                                } else {
                                    Log.e("com.parse.push", "failed to subscribe for push", e);
                                }
                            }
                        });
                    }
                }

                // Start an intent for the logged in activity
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
