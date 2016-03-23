package com.frevocomunicacao.tracker.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.frevocomunicacao.tracker.Constants;
import com.frevocomunicacao.tracker.R;
import com.frevocomunicacao.tracker.api.TrackerRestClientUsage;
import com.frevocomunicacao.tracker.database.datasources.OcurrencesDataSource;
import com.frevocomunicacao.tracker.database.datasources.VisitsDataSource;
import com.frevocomunicacao.tracker.database.models.Ocurrence;
import com.frevocomunicacao.tracker.database.models.Visit;
import com.frevocomunicacao.tracker.utils.ConnectionDetector;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        if (!ConnectionDetector.isNetworkConnected(this)) {
            showMessage("Verifique sua conexão com a internet!");
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            executeLogin();
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void executeLogin(){
        showProgress(true);

        RequestParams p = new RequestParams();
        p.put("email", mEmailView.getText().toString());
        p.put("password", mPasswordView.getText().toString());

        TrackerRestClientUsage.login(p, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                showProgress(false);

                try {
                    if (response.getBoolean(Constants.RESPONSE_KEY_ERROR) == false) {
                        // clear preferences
                        prefs.empty();

                        // put new preferences data
                        prefs.put(Constants.PREFS_KEY_USER_ID, response.getInt("user_id"));
                        prefs.put(Constants.PREFS_KEY_USER_EMPLOYEE_ID, response.getInt("employee_id"));
                        prefs.put(Constants.PREFS_KEY_USER_NAME, response.getString("name"));
                        prefs.put(Constants.PREFS_KEY_USER_EMAIL, response.getString("email"));

                        // import
                        importData();
                    } else {
                        showMessage(response.getString(Constants.RESPONSE_KEY_MESSAGE));
                        mPasswordView.requestFocus();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (statusCode == 404) {
                    showMessage("Ops! endpoit do serviço não encontrado.");
                } else if (statusCode == 500) {
                    showMessage("Ops! não foi possível encontrar o servidor.");
                }

                showProgress(false);
            }
        });
    }

    private void importData() {
        RequestParams p = new RequestParams();
        p.put("employee_id", prefs.getInt(Constants.PREFS_KEY_USER_EMPLOYEE_ID));

        TrackerRestClientUsage.importData(p, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    if (response.getBoolean(Constants.RESPONSE_KEY_ERROR) == false) {
                        JSONArray ocurrences = response.getJSONArray("ocurrences");
                        JSONArray visits     = response.getJSONArray("visits");

                        // import data
                        importOcurrences(ocurrences);
                        importVisits(visits);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                changeActivity(MainActivity.class, null, true);
            }
        });
    }

    private void importOcurrences(JSONArray ocurrences) throws JSONException {
        OcurrencesDataSource dsOcurrence = new OcurrencesDataSource(getApplicationContext());

        for (int x = 0; x < ocurrences.length(); x++) {
            JSONObject object = ocurrences.getJSONObject(x);
            Ocurrence ocurrence = new Ocurrence(object.getInt("id"), object.getString("name"));

            if (!dsOcurrence.exist(ocurrence)) {
                dsOcurrence.create(ocurrence);
            } else {
                dsOcurrence.update(ocurrence);
            }
        }

        dsOcurrence.close();
    }

    private void importVisits(JSONArray visits) throws JSONException{
        VisitsDataSource dsVisit = new VisitsDataSource(getApplicationContext());

        for (int x=0;x < visits.length();x++) {
            JSONObject object = visits.getJSONObject(x);

            Visit visit = new Visit();
            visit.setId(object.getInt("id"));
            visit.setEmployeeId(object.getInt("employee_id"));
            visit.setMotive(object.getString("motive"));
            visit.setCep(object.getString("cep"));
            visit.setState(object.getString("state"));
            visit.setCity(object.getString("city"));
            visit.setAddress(object.getString("address"));
            visit.setNeighborhood(object.getString("neighborhood"));
            visit.setComplement(object.getString("complement"));
            visit.setNumber(object.getString("number"));
            visit.setReferencePoint(object.getString("reference_point"));
            visit.setVisitStatusId(object.getInt("visit_status_id"));

            if (!dsVisit.exist(visit)) {
                dsVisit.create(visit);
                Log.d("LoginActivity", "Visita Criada: " + visit.getAddress() + ", " + visit.getNumber() + " - " + visit.getCity());
            } else {
                Log.d("LoginActivity", "Visita Atualizada: " + visit.getAddress() + ", " + visit.getNumber() + " - " + visit.getCity());
                dsVisit.update(visit);
            }
        }

        dsVisit.close();

    }

        @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }
}

