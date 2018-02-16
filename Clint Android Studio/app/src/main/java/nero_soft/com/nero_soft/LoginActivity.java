package nero_soft.com.nero_soft;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nero_soft.com.nero_soft.DialogClass.DailogMessage;
import nero_soft.com.nero_soft.interFace.Setting;
import nero_soft.com.nero_soft.networkSocket.UserLoginTask;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private CheckBox RemPass;
    private ScrollView ScrollView;
    private ProgressDialog progressDialog;
    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.input_email);
        ScrollView =(ScrollView) findViewById(R.id.idlogin);
        RemPass = (CheckBox) findViewById(R.id.link_signup);
        // populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.input_password);
        sharedPreferences = getSharedPreferences("SettingSmartHome", getApplicationContext().MODE_PRIVATE);
        setUpUserPass();
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        RemPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String User=sharedPreferences.getString("User","");
                String Pass=sharedPreferences.getString("Pass","");
                final DailogMessage message= new DailogMessage();
                final CheckBox checkBox=(CheckBox) view;
                if(!checkBox.isChecked())
                    if(!(User=="" && Pass=="")) {
                        checkBox.setChecked(true);
                        message.setDialogMessage("Delete User And Password",
                                "Do you Want Clear User And Password "
                                , new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        sharedPreferences.edit().putString("User", "").commit();
                                        sharedPreferences.edit().putString("Pass", "").commit();
                                        mEmailView.setText("");
                                        mPasswordView.setText("");
                                        checkBox.setChecked(false);
                                        message.getDialog().cancel();

                                    }
                                }).show(getFragmentManager(), null);
                    }
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.btn_login);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");

    }

    private void setUpUserPass(){
        String User=sharedPreferences.getString("User","");
        String Pass=sharedPreferences.getString("Pass","");
        if(!(User=="" && Pass=="")){
            mEmailView.setText(User);
            mPasswordView.setText(Pass);
            RemPass.setChecked(true);
        }
    }

    private void saveUserPass(){
        if(RemPass.isChecked()){
            sharedPreferences.edit().putString("User",  mEmailView.getText().toString()).commit();
            sharedPreferences.edit().putString("Pass",  mPasswordView.getText().toString()).commit();
        }
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email) || !isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (!ServiceConnection.checkNetworkState(getBaseContext())) {
                ServiceConnection.MeassagDialog("Please Check Your Connection And Try Again", ScrollView, null);
                return;
            }
            JSONObject Send_Data = new JSONObject();
            try {
                Send_Data.put("user", email);
                Send_Data.put("pass", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.show();
            mAuthTask = new UserLoginTask("moplogin", Send_Data, new Setting.SetPost() {
                @Override
                public void PostExecute(JSONObject result) {
                    mAuthTask = null;
                    try {
                        if (result.getBoolean("state")) {
                            ServiceConnection.User = email;
                            ServiceConnection.Pass = password;
                            saveUserPass();
                            ServiceConnection.TypeOfUser=result.getString("GroupU");
                            ServiceConnection.getSettingLedInfo(new Setting.GetPost.getSettingLedInfo() {
                                @Override
                                public void PostExecuteSettingLedInfo(JSONObject result) {
                                    try {
                                        if (result.getBoolean("state")) {
                                            //true
                                            ServiceConnection.Led = result.getJSONArray("Led");
                                            ServiceConnection.Sensor_State = result.getJSONArray("Setting").getJSONObject(0);

                                            if (  ServiceConnection.TypeOfUser.contentEquals("admin")) {
                                                ServiceConnection.getSettingDashInfo(new Setting.GetPost.getSettingLedInfo() {
                                                    @Override
                                                    public void PostExecuteSettingLedInfo(JSONObject result) {
                                                        try {
                                                            if (result.getString("state").contentEquals("true")) {
                                                                ServiceConnection.Dash_Info = result;
                                                                ServiceConnection.TypeOfUser="admin";
                                                                startActivity(new Intent(getBaseContext(), MainActivity.class));
                                                                progressDialog.dismiss();
                                                                //finish();
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                });


                                            } else {
                                                ServiceConnection.TypeOfUser="user";
                                                startActivity(new Intent(getBaseContext(), MainActivity.class));
                                                progressDialog.dismiss();
                                                //finish();
                                            }
                                        } else {
                                            //false
                                        }
                                    } catch (JSONException e) {
                                        //false
                                        e.printStackTrace();
                                    }

                                }
                            });
//                            startActivity(new Intent(getBaseContext(), MainActivity.class));
//                            progressDialog.dismiss();
//                            finish();


                        } else {
                            mPasswordView.setError(getString(R.string.error_incorrect_password));
                            mPasswordView.requestFocus();
                            progressDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                }

                @Override
                public void Cansle() {
                    mAuthTask = null;
                    progressDialog.dismiss();
                }
            });
            mAuthTask.execute((Void) null);
//
            // Toast.makeText(getBaseContext(),jsonObject.getString("state"),Toast.LENGTH_LONG).show();
        }
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
         return password.length() > 6;
    }

    private boolean isEmailValid(String password) {
        //TODO: Replace this with your own logic
         return password.length() > 6;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        //mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */

}

