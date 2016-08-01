package com.veempees.lol;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
    private boolean showEmptyPropertyGroups = false;
    private boolean hideAdditionalProperties = false;
    private boolean showQtyBeforeItem = true;
    private boolean showCountAfterProperty = true;

    public PropertyListAdapter(Context context, SharedPreferences sharedPrefs) {
        this.ctx = context;
        renderingPropertiesChanged(sharedPrefs);
    }

    public void renderingPropertiesChanged(SharedPreferences sharedPrefs)
    {
        /*this.showEmptyPropertyGroups = sharedPrefs.getBoolean(Constants.PREF_SHOW_EMPTY_PROPERTY_GROUPS, false);
        this.hideAdditionalProperties = sharedPrefs.getBoolean(Constants.PREF_SHOW_ADDITIONAL_PROPERTIES_IN_ITEM, false);
        this.showQtyBeforeItem = sharedPrefs.getBoolean(Constants.PREF_SHOW_QUANTITY_BEFORE_ITEM, true);
        this.showCountAfterProperty = sharedPrefs.getBoolean(Constants.PREF_SHOW_COUNT_AFTER_PROPERTY, true);*/
    }

    @Override
    public int getGroupCount()
    {
        // one group is one property
        // how many properties exist?
        int count = ItemFramework.getInstance().getPropertyCount(this.showEmptyPropertyGroups);
        Logger.i(String.format("There are %d Props" + (this.showEmptyPropertyGroups ? "" : " with content"), count));
        return count;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        // one group is one property
        // the number of items belonging to this property
        int count = ItemFramework.getInstance().getItemCountByPropId(getGroupId(groupPosition));
        Logger.i(String.format("There are %d items in the %dth group", count, groupPosition));
        return count;
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        Property p = ItemFramework.getInstance().getProp(getGroupId(groupPosition));
        Logger.i(String.format("The %dth Prop is %s", groupPosition, p.toString()));
        return p;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Item i = ItemFramework.getInstance().getItemById(getChildId(groupPosition, childPosition));
        Logger.i(String.format("The %dth Prop's %dth Item is %s", groupPosition, childPosition, i.toString()));
        return i;
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        int ID = ItemFramework.getInstance().getPropIdFromPosition(groupPosition, this.showEmptyPropertyGroups);
        Logger.i(String.format("The ID of the %dth Prop is %d", groupPosition, ID));
        return ID;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        long groupID = getGroupId(groupPosition);
        int ID = ItemFramework.getInstance().getItemIDByGroupAndPosition(groupID, childPosition);
        Logger.i(String.format("The ID of the %dth Prop's %dth Item is %d", groupPosition, childPosition, ID));
        return ID;
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

        Property p = (Property)getGroup(groupPosition);
        String groupText = p.getValue();

        if (this.showCountAfterProperty)
        {
            groupText += " (";
            groupText += getChildrenCount(groupPosition);
            groupText += ")";
        }
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

        Item i = (Item)getChild(groupPosition, childPosition);

        holder.txtTitle.setText(createItemText(i));


        /*TODO if (this.showAdditionalProperties)
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
        */
        holder.chkTick.setTag(i);
        holder.chkTick.setChecked(i.isDone());
        return row;
    }

    private String createItemText(Item i)
    {
        String title;

        if (this.showQtyBeforeItem)
        {
            title = "";
        }
        else
        {
            title = i.getText();
        }

        if (i.getQtty().length() > 0)
        {
            if (this.showQtyBeforeItem)
            {
                title += i.getQtty();
                title += " ";
            }
            else
            {
                title += " ";
                title += i.getQtty();
            }
        }
        if (i.getMmt().length() > 0)
        {
            if (this.showQtyBeforeItem)
            {
                title += i.getMmt();
                title += " ";
            }
            else
            {
                title += " ";
                title += i.getMmt();
            }
        }
        if (this.showQtyBeforeItem)
        {
            title += i.getText();
        }
        else
        {
            // we are done
        }

        return title;
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
