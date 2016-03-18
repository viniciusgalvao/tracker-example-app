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

import com.frevocomunicacao.tracker.R;
import com.frevocomunicacao.tracker.activities.FormActivity;
import com.frevocomunicacao.tracker.adapters.VisitsAdapter;
import com.frevocomunicacao.tracker.models.Visit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VisitFragment extends Fragment {

    private List<Visit> visits;
    private ListView visitList;
    private VisitsAdapter adapter;

    public VisitFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_visit, container, false);

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

        populateList(); // REMOVE LATER

        return rootView;
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
