package com.alexpersaud.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    EditText username;
    EditText password;
    Boolean signInModeActive = false;
    TextView changeSignInMode;
    Button signInButton;
    RelativeLayout relativeLayout;
    ImageView igBackground;


    public void showUserList(){
        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
            signIn(view);
        }

        return false;
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.changeSignInMode){
            signInButton = (Button) findViewById(R.id.signInButton);
            if(signInModeActive == false){
                signInModeActive = true;
                signInButton.setText("Sign Up");
                changeSignInMode.setText("Already have an account? Log in.");
            }else{
                signInModeActive = false;
                signInButton.setText("Log In");
                changeSignInMode.setText("Don't have an account? Sign up.");
            }
        }else if (view.getId() == R.id.relativeLayout || view.getId() == R.id.igBackground){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void signIn(View view){
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        if(username.getText().toString().matches("") || password.getText().toString().matches("")){
            Toast.makeText(this, "A username and password are required.", Toast.LENGTH_SHORT).show();
        }else {
            if (signInModeActive) {
                ParseUser user = new ParseUser();
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user != null){
                            showUserList();
                        }else{
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeSignInMode = (TextView) findViewById(R.id.changeSignInMode);
        changeSignInMode.setOnClickListener(this);

        password = (EditText) findViewById(R.id.password);
        password.setOnKeyListener(this);

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        igBackground = (ImageView) findViewById(R.id.igBackground);

        relativeLayout.setOnClickListener(this);
        igBackground.setOnClickListener(this);

        if(ParseUser.getCurrentUser() != null){
            showUserList();
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}
