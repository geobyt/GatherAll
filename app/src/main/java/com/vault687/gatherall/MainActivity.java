package com.vault687.gatherall;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this, "k5ifInLOxxJevkIyG1pK0AIcDBaRUtaKmFz7eS4P", "8UKAC86EuxUtxra9O7QZ2M1f7B0LCUH7fiiWzyfF");

        setContentView(R.layout.activity_main);

        TextView slogan = (TextView) findViewById(R.id.textview_3);

        FragmentManager fm = getFragmentManager();
        Fragment loginFragment = fm.findFragmentById(R.id.loginFragment);
        Fragment newGatherFragment = fm.findFragmentById(R.id.createGatherFragment);

        if (ParseUser.getCurrentUser() != null)
        {
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

            ft.hide(loginFragment);
            ft.show(newGatherFragment);
            ft.commit();

            slogan.setText(getResources().getString(R.string.slogan, ParseUser.getCurrentUser().getUsername()));
        }
        else
        {
            slogan.setText(getResources().getString(R.string.slogan, "y'all"));

            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

            ft.show(loginFragment);
            ft.hide(newGatherFragment);
            ft.commit();

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

            FragmentManager fm = getFragmentManager();
            Fragment loginFragment = fm.findFragmentById(R.id.loginFragment);
            Fragment newGatherFragment = fm.findFragmentById(R.id.createGatherFragment);

            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

            ft.show(loginFragment);
            ft.hide(newGatherFragment);
            ft.commit();

            this.invalidateOptionsMenu();
        }

        return super.onOptionsItemSelected(item);
    }

    public void CreateGathering(String gatheringName)
    {
        ParseObject newGathering = new ParseObject("Gathering");
        newGathering.put("name", gatheringName);
        newGathering.put("owner", ParseUser.getCurrentUser());
        newGathering.saveInBackground();
    }

    public static class MainActivityLoginFragment extends Fragment
    {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.main_activity_login_fragment, container, false);

            // Sign up button click handler
            Button signupButton = (Button) v.findViewById(R.id.signup_button);
            signupButton.setOnClickListener(new OnClickListener()
            {
                public void onClick(View v)
                {
                    // Starts an intent for the sign up activity
                    startActivity(new Intent(getActivity(), SignUpActivity.class));
                }
            });

            return v;
        }
    }

    public static class MainActivityCreateGatherFragment extends Fragment
    {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.main_activity_newgather_fragment, container, false);

            // gather button click handler
            Button gatherButton = (Button) v.findViewById(R.id.create_gather_button);
            final EditText gatheringName = (EditText) v.findViewById(R.id.gathering_name_text);

            gatherButton.setOnClickListener(new OnClickListener()
            {
                public void onClick(View v)
                {
                    //create a gathering and hide the fragment
                    ((MainActivity) getActivity()).CreateGathering(gatheringName.getText().toString());
                }
            });

            return v;
        }
    }
}
