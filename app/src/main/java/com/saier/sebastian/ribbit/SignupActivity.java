package com.saier.sebastian.ribbit;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class SignupActivity extends ActionBarActivity {

    @InjectView(R.id.usernameText) EditText mUsername;
    @InjectView(R.id.passwordText) EditText mPassword;
    @InjectView(R.id.emailText) EditText mEmail;
    @InjectView(R.id.signupButton) Button mSignupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        mSignupButton.setOnClickListener(new View.OnClickListener() { // How can we use the listener with Butterknife?
            @Override
            public void onClick(View v) {
                // Check the values
                String username = mUsername.getText().toString(); // toString because the format is 'Editable'
                String password = mPassword.getText().toString(); // We don't need to initialize these variables with Butterknife
                                                                  // because they aren't views, but normal Strings in this method
                String email = mEmail.getText().toString();

                username = username.trim(); // We don't want any whitespace in the values
                password = password.trim();
                email = email.trim();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    AlertDialogFragment dialog = AlertDialogFragment
                            .newInstance(getString(R.string.signup_error_message)); // Why getString?
                    dialog.show(getFragmentManager(), "error_dialog");
                }
                else {
                    // Create the new user

                    // ----- Show ProgressBar -----

                    ParseUser newUser = new ParseUser();
                    newUser.setUsername(username);
                    newUser.setPassword(password);
                    newUser.setEmail(email);
                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {

                            // ----- Hide ProgressBar -----

                            if (e == null) {
                                // Success
                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
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
        getMenuInflater().inflate(R.menu.menu_signup, menu);
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
