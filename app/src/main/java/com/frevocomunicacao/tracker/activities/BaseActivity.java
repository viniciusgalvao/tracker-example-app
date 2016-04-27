package com.frevocomunicacao.tracker.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.frevocomunicacao.tracker.Constants;
import com.frevocomunicacao.tracker.R;
import com.frevocomunicacao.tracker.database.datasources.CheckinsDataSource;
import com.frevocomunicacao.tracker.database.datasources.ImagesDataSource;
import com.frevocomunicacao.tracker.database.datasources.OcurrencesDataSource;
import com.frevocomunicacao.tracker.database.datasources.VisitsDataSource;
import com.frevocomunicacao.tracker.database.models.Ocurrence;
import com.frevocomunicacao.tracker.database.models.Visit;
import com.frevocomunicacao.tracker.fragments.VisitFragment;
import com.frevocomunicacao.tracker.utils.PrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vinicius on 10/03/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected DrawerLayout drawer;
    protected ActionBarDrawerToggle toggle;
    protected NavigationView navigationView;

    protected PrefUtils prefs;

    private Fragment mCurrentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // layout resource
        setContentView(getLayoutResource());

        // toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            //setActionBarIcon(R.drawable.ic_action_logo);
        }

        // preferences
        prefs = new PrefUtils(getApplicationContext());
    }

    protected void buildDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, this.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    setTitle(R.string.app_name);
                    displayView(new VisitFragment(Constants.FRAGMENT_VIEW_STATUS_VISITS_LIST), null);
                } else if (id == R.id.nav_my_visits) {
                    setTitle("Visitas Realizadas");
                    displayView(new VisitFragment(Constants.FRAGMENT_VIEW_STATUS_MY_VISITS_LIST), null);
                } else if (id == R.id.nav_logout) {
                    logout();
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });
    }

    protected void setActionBarIcon(int iconRes) {
        toolbar.setNavigationIcon(iconRes);
    }

    protected void changeActivity(Class<?> cls, Bundle extras, boolean isFinish) {
        Intent i = new Intent(this, cls);
        if (extras != null) {
            i.putExtras(extras);
        }
        startActivity(i);

        if (isFinish) {
            finish();
        }
    }

    public void displayView(Fragment object, Bundle args) {
        mCurrentFragment = object;

        if (mCurrentFragment != null) {

            if (args != null) {
                mCurrentFragment.setArguments(args);
            }

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim);
            transaction.replace(R.id.frame_container, mCurrentFragment);
            transaction.commit();
        }
    }

    protected void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected void logout() {
        if (!prefs.isEmpty()) {
            prefs.empty();

            VisitsDataSource dsVisit = new VisitsDataSource(getApplicationContext());
            ImagesDataSource dsImage = new ImagesDataSource(getApplicationContext());
            CheckinsDataSource dsCheckin = new CheckinsDataSource(getApplicationContext());

            dsVisit.truncateTable();
            dsCheckin.truncateTable();
            dsImage.truncateTable();
        }

        changeActivity(LoginActivity.class, null, true);
    }

    protected void importOcurrences(JSONArray ocurrences) throws JSONException {
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
    }

    protected void importVisits(JSONArray visits) throws JSONException{
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
            visit.setVisitTypeId(object.getInt("visit_type_id"));

            if (!dsVisit.exist(visit)) {
                dsVisit.create(visit);
            } else {
                dsVisit.update(visit);
            }
        }
    }

    protected abstract int getLayoutResource();
}
