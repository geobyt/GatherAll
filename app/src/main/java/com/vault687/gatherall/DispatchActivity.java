package com.vault687.gatherall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class DispatchActivity extends Activity {

    public DispatchActivity()
    {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Check if there is current user info
        if (ParseUser.getCurrentUser() != null)
        {
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();

            if (installation.get("user") == null) {
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

            // Start an intent for the logged in activity
            startActivity(new Intent(this, MainActivity.class));
        }
        else
        {
            // Start and intent for the logged out activity
            startActivity(new Intent(this, MainActivity.class));
        }
    }

}
