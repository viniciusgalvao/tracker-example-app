package com.frevocomunicacao.tracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.frevocomunicacao.tracker.R;
import com.frevocomunicacao.tracker.database.models.Visit;

import java.util.List;

public class VisitsAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context ctx;
    private List<Visit> visits;

    public VisitsAdapter(Context ctx, List<Visit> visits) {
        this.ctx    = ctx;
        this.visits = visits;
        mInflater   = LayoutInflater.from(this.ctx);
    }

    @Override
    public int getCount() {
        return visits.size();
    }

    @Override
    public Object getItem(int position) {
        return visits.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder;

        // verifica se a view está nula
        if (convertView == null) {
            // inflando layout para poder pegar as views
            convertView = mInflater.inflate(R.layout.item_list_visits, null);

            // inflando as informações
            mHolder = new ViewHolder();
            mHolder.cep         = ((TextView) convertView.findViewById(R.id.txt_cep));
            mHolder.city_state  = ((TextView) convertView.findViewById(R.id.txt_city_state));
            mHolder.address     = ((TextView) convertView.findViewById(R.id.txt_address));
            mHolder.referencePoint = ((TextView) convertView.findViewById(R.id.txt_reference_point));

            // define os itens na view
            convertView.setTag(mHolder);
        } else {
            mHolder = ((ViewHolder) convertView.getTag());
        }

        // pega o registro referente a posição
        Visit visit = visits.get(position);

        if (visit != null) {
            // modifica os dados
            mHolder.cep.setText(visit.getCep());
            mHolder.city_state.setText(visit.getCity() + ", " + visit.getState());
            mHolder.address.setText(visit.getSimpleAddress());
            mHolder.referencePoint.setText('"' + visit.getReferencePoint() + '"');
        }

        // retorna a view com as informações
        return convertView;
    }

    private static class ViewHolder {
        TextView cep;
        TextView city_state;
        TextView address;
        TextView referencePoint;
    }
}
