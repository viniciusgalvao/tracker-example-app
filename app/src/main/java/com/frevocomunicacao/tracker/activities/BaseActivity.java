package com.frevocomunicacao.tracker.activities;

import android.app.Fragment;
import android.app.FragmentManager;
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

import com.frevocomunicacao.tracker.R;
import com.frevocomunicacao.tracker.fragments.MyVisitFragment;
import com.frevocomunicacao.tracker.fragments.VisitFragment;
import com.frevocomunicacao.tracker.utils.PrefUtils;

/**
 * Created by Vinicius on 10/03/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected DrawerLayout drawer;
    protected ActionBarDrawerToggle toggle;
    protected NavigationView navigationView;

    protected PrefUtils prefs;

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
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    setTitle(R.string.app_name);
                    displayView(new VisitFragment(), null);
                } else if (id == R.id.nav_my_visits) {
                    setTitle("Visitas Realizadas");
                    displayView(new MyVisitFragment(), null);
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
        Fragment fragment = object;

        if (fragment != null) {

            if (args != null) {
                fragment.setArguments(args);
            }

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        }
    }

    protected void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected void logout() {
        if (!prefs.isEmpty()) {
            prefs.empty();
        }
        changeActivity(LoginActivity.class, null, true);
    }

    protected abstract int getLayoutResource();
}
