package com.frevocomunicacao.tracker.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.frevocomunicacao.tracker.Constants;
import com.frevocomunicacao.tracker.R;
import com.frevocomunicacao.tracker.activities.FormActivity;
import com.frevocomunicacao.tracker.adapters.VisitsAdapter;
import com.frevocomunicacao.tracker.database.DbHelper;
import com.frevocomunicacao.tracker.database.contracts.VisitContract;
import com.frevocomunicacao.tracker.database.datasources.VisitsDataSource;
import com.frevocomunicacao.tracker.database.models.Visit;
import com.frevocomunicacao.tracker.utils.PrefUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VisitFragment extends Fragment {

    private List<Visit> visits;
    private ListView visitList;
    private VisitsAdapter adapter;

    private TextView txtNotFound;
    private PrefUtils prefs;
    private int statusId = Constants.FRAGMENT_VIEW_STATUS_VISITS_LIST;

    public VisitFragment() {}

    public VisitFragment(int statusId) {
        this.statusId = statusId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_visit, container, false);

        prefs       = new PrefUtils(getActivity().getApplicationContext());
        txtNotFound = (TextView) rootView.findViewById(R.id.txt_not_found);

        // listview
        visitList   = (ListView) rootView.findViewById(R.id.visit_list);
        visits      = new ArrayList<Visit>();
        adapter     = new VisitsAdapter(getActivity(), visits);
        visitList.setAdapter(adapter);

        visitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                // params
                Bundle b = new Bundle();
                b.putString("mode", "edit");
                b.putSerializable("record", (Serializable) visits.get(position));

                // intent
                Intent i = new Intent(getActivity(), FormActivity.class);
                i.putExtras(b);

                // change activity
                startActivity(i);
            }
        });

        populateList();

        return rootView;
    }

    private void populateList() {
        VisitsDataSource dsVisit = new VisitsDataSource(getActivity());
        String query             = VisitContract.VisitEntry.COLUMN_FIELD_EMPLOYEE_ID + " = ? AND " + VisitContract.VisitEntry.COLUMN_FIELD_VISIT_STATUS_ID + " = ?";
        String[] args            = new String[]{String.valueOf(prefs.getInt(Constants.PREFS_KEY_USER_EMPLOYEE_ID)), String.valueOf(statusId)};
        List<Visit> visitRecords = dsVisit.findAll(query, args, VisitContract.VisitEntry.COLUMN_FIELD_ID + " ASC");

        if (visitRecords.size() > 0) {
            txtNotFound.setVisibility(View.GONE);
            visits.addAll(visitRecords);
            visitList.deferNotifyDataSetChanged();
        } else {
            txtNotFound.setVisibility(View.VISIBLE);
        }
    }
}
