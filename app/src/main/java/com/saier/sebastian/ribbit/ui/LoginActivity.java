package com.saier.sebastian.ribbit.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.saier.sebastian.ribbit.R;
import com.saier.sebastian.ribbit.ui.fragments.AlertDialogFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class LoginActivity extends ActionBarActivity {

    @InjectView(R.id.usernameText) EditText mUsername;
    @InjectView(R.id.passwordText) EditText mPassword;
    @InjectView(R.id.loginButton) Button mLoginButton;

    @InjectView(R.id.signUpText) TextView mSignupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        mSignupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() { // How can we use the listener with Butterknife?
            @Override
            public void onClick(View v) {
                // Check the values
                String username = mUsername.getText().toString(); // toString because the format is 'Editable'
                String password = mPassword.getText().toString(); // We don't need to initialize these variables with Butterknife
                                                                  // because they aren't views, but normal Strings in this method

                username = username.trim(); // We don't want any whitespace in the values
                password = password.trim();

                if (username.isEmpty() || password.isEmpty()) {
                    AlertDialogFragment dialog = AlertDialogFragment
                            .newInstance(getString(R.string.login_error_message)); // Why getString?
                    dialog.show(getFragmentManager(), "error_dialog");
                }
                else {
                    // Login

                    // ----- Show ProgressBar -----

                    ParseUser.logInInBackground(username, password, new LogInCallback() { // We don't need an instance of an object to call a class method
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {

                            // ----- Hide ProgressBar -----

                            if (parseUser != null) {
                                // Success
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else {
                                AlertDialogFragment dialog = AlertDialogFragment
                                        .newInstance((e.getMessage())); // Why getString?
                                dialog.show(getFragmentManager(), "error_dialog");
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
