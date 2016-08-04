package com.veempees.lol;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MementoAdapter extends BaseAdapter implements Filterable {

    private class ChildViewHolder
    {
        TextView txtMementoTitle;
        TextView txtMementoPropList;
    }

    private class MementoFilter extends Filter
    {
        public MementoFilter()
        {
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults oReturn = new FilterResults();
            List<Memento> results = new ArrayList<Memento>();

            oReturn.values = results;
            if (constraint != null)
            {
                for (Memento m : ItemFramework.getInstance().getMementos())
                {
                    if (m.getValue().toLowerCase().contains(constraint.toString().toLowerCase()))
                    {
                        results.add(m);
                    }
                }
            }
            return oReturn;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            matchingMementos = (List<Memento>)results.values;
            notifyDataSetChanged();
        }

    }

    private Context context;
    private MementoFilter filter;
    List<Memento> matchingMementos;

    private boolean hideAdditionalProperties = false;

    public MementoAdapter(Context context) {
        this.context = context;
        matchingMementos = ItemFramework.getInstance().getMementos();
        filter = new MementoFilter();
    }

    @Override
    public int getCount() {
        return matchingMementos.size();
    }

    @Override
    public Object getItem(int arg0) {
        return matchingMementos.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;	// id is the position
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null)
        {
            LayoutInflater inflater = ((Activity)this.context).getLayoutInflater();
            row = inflater.inflate(R.layout.memento_list, parent, false);

            ChildViewHolder holder = new ChildViewHolder();
            holder.txtMementoTitle = (TextView) row.findViewById(R.id.textMementoTitle);
            holder.txtMementoPropList = (TextView) row.findViewById(R.id.textMementoPropList);

            row.setTag(holder);
        }

        ChildViewHolder holder = (ChildViewHolder) row.getTag();

        Memento memento = matchingMementos.get(position);

        holder.txtMementoTitle.setText(memento.getValue());

        if (!this.hideAdditionalProperties)
        {
            String propList = "";

            for (int propId : memento.getPropertyIds()) {
                propList += ItemFramework.getInstance().getProp(propId).getValue();
                propList += " ";
            }

            holder.txtMementoPropList.setVisibility(View.VISIBLE);
            holder.txtMementoPropList.setText(propList);
        }
        else
        {
            holder.txtMementoPropList.setVisibility(View.INVISIBLE);
        }

        return row;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public Memento getMemento(int position) {
        return matchingMementos.get(position);
    }

}
