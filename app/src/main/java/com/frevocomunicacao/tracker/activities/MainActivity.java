package com.frevocomunicacao.tracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.frevocomunicacao.tracker.Constants;
import com.frevocomunicacao.tracker.R;
import com.frevocomunicacao.tracker.adapters.VisitsAdapter;
import com.frevocomunicacao.tracker.models.Visit;
import com.frevocomunicacao.tracker.utils.PrefUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private List<Visit> visits;
    private ListView visitList;
    private VisitsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // listview
        visitList   = (ListView) findViewById(R.id.visit_list);
        visits      = new ArrayList<Visit>();
        adapter     = new VisitsAdapter(this, visits);
        visitList.setAdapter(adapter);

        visitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), FormActivity.class);
                i.putExtra("mode", "edit");
                i.putExtra("record", (Serializable) visits.get(position));
                startActivity(i);
            }
        });

        populateList(); // REMOVE LATER

        // fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), FormActivity.class);
                i.putExtra("mode", "create");
                startActivity(i);
            }
        });

        buildDrawer();

        TextView mUsername = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUsername);
        mUsername.setText(prefs.get(Constants.PREFS_KEY_USER_NAME));
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
            // TODO: implement sync data
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    private void populateList() {
        visits.add(new Visit(1, 1, "", "", "Rua João Fernandes Vieira", "", "Sala 02", "Parque Amorim", "Recife", "PE", "Proxímo ao bompreço da agamenon"));
        visits.add(new Visit(2, 1, "", "53444-321", "Av.Conselheiro Aguiar", "1112", "Sala 15", "Boa Viagem", "Recife", "PE", "Em frente à auto nunes"));
        visits.add(new Visit(3, 1, "", "53212-123", "Av.Domingos Ferreira", "421", "5 andar", "Boa Viagem", "Recife", "PE", "Em frente à o supermercado extra"));
        visits.add(new Visit(4, 1, "", "51231-456", "Av.Agamenon Magalhães", "21", "Loja 10", "Espinheiro", "Recife", "PE", "Uma rua depois do cruzamento do derby"));
        visits.add(new Visit(5, 1, "", "59874-729", "Av Mascarenhas de Moraes", "4321", "", "Imbiribeira", "Recife", "PE", "Junto a marçal auto-peças"));

        visits.add(new Visit(1, 1, "", "50050-200", "Rua João Fernandes Vieira", "587", "Sala 02", "Parque Amorim", "Recife", "PE", "Proxímo ao bompreço da agamenon"));
        visits.add(new Visit(2, 1, "", "53444-321", "Av.Conselheiro Aguiar", "1112", "Sala 15", "Boa Viagem", "Recife", "PE", "Em frente à auto nunes"));
        visits.add(new Visit(3, 1, "", "53212-123", "Av.Domingos Ferreira", "421", "5 andar", "Boa Viagem", "Recife", "PE", "Em frente à o supermercado extra"));
        visits.add(new Visit(4, 1, "", "51231-456", "Av.Agamenon Magalhães", "21", "Loja 10", "Espinheiro", "Recife", "PE", "Uma rua depois do cruzamento do derby"));
        visits.add(new Visit(5, 1, "", "59874-729", "Av Mascarenhas de Moraes", "4321", "", "Imbiribeira", "Recife", "PE", "Junto a marçal auto-peças"));


        visitList.deferNotifyDataSetChanged();
    }
}
