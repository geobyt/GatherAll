package com.vault687.gatherall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseUser;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this, "k5ifInLOxxJevkIyG1pK0AIcDBaRUtaKmFz7eS4P", "8UKAC86EuxUtxra9O7QZ2M1f7B0LCUH7fiiWzyfF");

        setContentView(R.layout.activity_main);

        // Log in button click handler
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                // Starts an intent of the log in activity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        // Sign up button click handler
        Button signupButton = (Button) findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                // Starts an intent for the sign up activity
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });

        TextView slogan = (TextView) findViewById(R.id.textview_3);

        if (ParseUser.getCurrentUser() != null)
        {
            loginButton.setVisibility(View.INVISIBLE);
            signupButton.setVisibility(View.INVISIBLE);
            slogan.setText(getResources().getString(R.string.slogan, ParseUser.getCurrentUser().getUsername()));
        }
        else
        {
            slogan.setText(getResources().getString(R.string.slogan, "y'all"));
            loginButton.setVisibility(View.VISIBLE);
            signupButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (ParseUser.getCurrentUser() != null)
        {
            getMenuInflater().inflate(R.menu.menu_main_logout, menu);
        }
        else
        {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }
        else
        if (id == R.id.action_logout)
        {
            ParseUser.logOut();
            finish();
            startActivity(getIntent());
        }

        return super.onOptionsItemSelected(item);
    }
}
