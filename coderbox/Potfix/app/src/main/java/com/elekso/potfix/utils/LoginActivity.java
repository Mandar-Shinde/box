package com.elekso.potfix.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elekso.potfix.MainActivity;
import com.elekso.potfix.R;
import com.elekso.potfix.ws.VOKpotfixservicePortBinding;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    public static String globaldata_test = "hit";
    public static VOKpotfixservicePortBinding service;
    static int loginstatus = 0;
    String email;
    String password;
    public static TaskNewAuth ta;
    public ProgressDialog progressDialog;


    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_login)
    Button _loginButton;
    @InjectView(R.id.link_signup)
    TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        service = new VOKpotfixservicePortBinding();

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

       progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();

        if (isNetworkConnected() == false) {
            onLoginFailed();
            progressDialog.dismiss();
            return;
        }

        String[] myTaskParams = {email, password};
        ta = new TaskNewAuth();
        ta.execute(myTaskParams);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    if (service.LoginWS(email, password)) {
//                        loginstatus = 1;
//                        Log.d(TAG, "Login OK");
//                    } else {
//                        loginstatus = 2;
//                        Log.d(TAG, "Login FAIL");
//                    }
//                } catch (Exception e) {
//                    Log.d(TAG, "Login FAILERR " + e.getMessage());
//
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        if (loginstatus == 1)
//                            onLoginSuccess();
//                        else
//                            onLoginFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);


//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        onLoginSuccess();
//                        // onLoginFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);

        Config.getInstance(getBaseContext(), getCacheDir()).setProfile("test", email);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

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


            try {
                if (service.LoginWS(data1, data2) ) {
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
                Toast.makeText(getApplicationContext(), "Welcome Back..", Toast.LENGTH_LONG).show();
                onLoginSuccess();

            } else {
                onLoginFailed();
                //onSignupFailed();
            }
            progressDialog.dismiss();
        }
    }
}
