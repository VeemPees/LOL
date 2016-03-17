package com.veempees.lol;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by Pesti_Gabor on 3/16/2016.
 */
public class PropertyListAdapter extends BaseExpandableListAdapter {

    static class GroupViewHolder {
        TextView txtTitle;
        int id;
    }

    static class ChildViewHolder {
        TextView txtTitle;
        TextView txtPropList;
        CheckBox chkTick;
    }

    private final Context ctx;

    public PropertyListAdapter(Context context) {
        this.ctx = context;
    }

    @Override
    public int getGroupCount() {
        return 4;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 3;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {

            LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
            row = inflater.inflate(R.layout.property_list_item, parent, false);

            GroupViewHolder holder = new GroupViewHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.textTitle);

            row.setTag(holder);
        }

        GroupViewHolder holder = (GroupViewHolder) row.getTag();

        //TODOProperty prop = (Property) getGroup(groupPosition);

        //TODOString groupText = prop.getValue();
        String groupText = "Group";

        /*TODO
        if (this.showQtyAfterProperty)
        {
            groupText += " (";
            groupText += getChildrenCount(groupPosition);
            groupText += ")";
        }*/
        holder.txtTitle.setText(groupText);

        return row;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
            row = inflater.inflate(R.layout.property_list_child_item, parent, false);

            ChildViewHolder holder = new ChildViewHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.textTitle);
            holder.txtPropList = (TextView) row.findViewById(R.id.textPropList);
            holder.chkTick = (CheckBox) row.findViewById(R.id.checkBoxTick);


            holder.chkTick.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Item i = (Item) cb.getTag();
                    i.setDone(!i.isDone());
                    notifyDataSetChanged();
                }
            });


            row.setTag(holder);

        }


        ChildViewHolder holder = (ChildViewHolder) row.getTag();


        holder.txtTitle.setText("Child");
        holder.txtPropList.setText("Props");
        holder.chkTick.setChecked(true);
        /*TODO
        Property prop = (Property) getGroup(groupPosition);

        Item i = (Item) getChild(groupPosition, childPosition);

        holder.txtTitle.setText(createItemText(i));*/


        /*TODO if (!this.hideAdditionalProperties)
        {
            String propList = "";

            for (int propId : i.getPropertyIds()) {
                if (prop.getId() != propId) {
                    propList += ItemFramework.getInstance().getProperty(propId)
                            .getValue();
                    propList += " ";
                }
            }

            holder.txtPropList.setVisibility(View.VISIBLE);
            holder.txtPropList.setText(propList);
        }
        else
        {
            holder.txtPropList.setVisibility(View.INVISIBLE);
        }
        //holder.chkTick.setTag(i);
        holder.chkTick.setChecked(i.isDone());
        */
        return row;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
