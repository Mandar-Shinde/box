package com.elekso.potfix.utils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elekso.potfix.MainActivity;
import com.elekso.potfix.R;
import com.elekso.potfix.ws.VOKpotfixservicePortBinding;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    public static VOKpotfixservicePortBinding service;
    ProgressDialog progressDialog;
    public static int loginstatus = 0;

    enum ENUM_STATUS_SIGNUP {REGISTER_OK, REGISTER_DUPLICATE, REGISTER_ERROR}

    ;

    @InjectView(R.id.input_name)
    EditText _nameText;
    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_signup)
    Button _signupButton;
    @InjectView(R.id.link_login)
    TextView _loginLink;

    public static TaskNewAuth ta;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }


    @Subscribe
    public void getMessage(ENUM_STATUS_SIGNUP s) {
        Toast.makeText(this, "data", Toast.LENGTH_LONG).show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        if (loginstatus == 1) {
                            onSignupSuccess();
                        } else
                            onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);

    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final String name = _nameText.getText().toString();
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        service = new VOKpotfixservicePortBinding();

        progressDialog = new ProgressDialog(SignupActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String[] myTaskParams = {name, password, email};
        ta = new TaskNewAuth();
        ta.execute(myTaskParams);
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }


    class TaskNewAuth extends AsyncTask<String, Void, Void> {
        String data1;
        String data2;
        String data3;

        @Override
        protected Void doInBackground(String... pdata) {
            data1 = pdata[0];
            data2 = pdata[1];
            data3 = pdata[2];

            try {
                if (service.NewUserWS(data1, data2, data3) == 0) {
                    loginstatus = 1;
                } else {
                    loginstatus = 2;
                }
            } catch (Exception e) {

            }
            return (null);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onProgressUpdate(Void... item) {

        }

        @Override
        protected void onPostExecute(Void unused) {

            if (loginstatus == 1) {
                onSignupSuccess();
                Intent myIntent = new Intent(SignupActivity.this, MainActivity.class);
                SignupActivity.this.startActivity(myIntent);
            } else {
                onSignupFailed();
            }
            progressDialog.dismiss();
        }
    }
}