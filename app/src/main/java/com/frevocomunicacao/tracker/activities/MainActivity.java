package com.frevocomunicacao.tracker.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.frevocomunicacao.tracker.Constants;
import com.frevocomunicacao.tracker.R;
import com.frevocomunicacao.tracker.api.TrackerRestClientUsage;
import com.frevocomunicacao.tracker.fragments.VisitFragment;
import com.frevocomunicacao.tracker.utils.ConnectionDetector;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends BaseActivity {

    private FloatingActionButton fab;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // fab
        fab = (FloatingActionButton) findViewById(R.id.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putString("mode", "create");

                changeActivity(FormActivity.class, b, false);
            }
        });

        buildDrawer();

        TextView mUsername = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUsername);
        mUsername.setText("Olá, " + prefs.get(Constants.PREFS_KEY_USER_NAME));

        // show fragment
        displayView(new VisitFragment(), null);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sync) {
            if (!ConnectionDetector.isNetworkConnected(this)) {
                showMessage("Verifique sua conexão com a internet!");
            } else {
                importData();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void importData() {
        RequestParams p = new RequestParams();
        p.put("employee_id", prefs.getInt(Constants.PREFS_KEY_USER_EMPLOYEE_ID));

        TrackerRestClientUsage.importData(p, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                showSyncProgressDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    if (response.getBoolean(Constants.RESPONSE_KEY_ERROR) == false) {
                        JSONArray ocurrences = response.getJSONArray("ocurrences");
                        JSONArray visits = response.getJSONArray("visits");

                        // import data
                        importOcurrences(ocurrences);
                        importVisits(visits);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    dialog.dismiss();
                }

                displayView(new VisitFragment(), null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (statusCode == 404) {
                    showMessage("Opa! endpoit do serviço não encontrado.");
                } else if (statusCode == 500) {
                    showMessage("Opa! não foi possível encontrar o servidor.");
                }

                dialog.dismiss();
            }
        });
    }

    private void showSyncProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("sincronizando informações...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }
}
