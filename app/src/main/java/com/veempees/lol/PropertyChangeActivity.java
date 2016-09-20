package com.veempees.lol;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PropertyChangeActivity extends AppCompatActivity {

    ArrayAdapter<Property> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_change);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent myIntent = new Intent(PropertyChangeActivity.this, PropertyRenameActivity.class);
                myIntent.putExtra(Constants.PROPERTY_ID, Constants.DUMMY_PROPERTY_ID);
                startActivityForResult(myIntent, Constants.MENU_ITEM_EDIT);

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new ArrayAdapter<Property>(this, android.R.layout.simple_list_item_1);
        rebuildAdapter();

        ListView lv = (ListView) findViewById(R.id.listPropertiesView);
        lv.setAdapter(adapter);
        registerForContextMenu(lv);
    }

    private void rebuildAdapter() {
        adapter.clear();
        for (Property p : ItemFramework.getInstance().getProps())
        {
            adapter.add(p);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;

        Property p = adapter.getItem(aInfo.position);

        menu.setHeaderTitle(p.getValue());
        menu.add(Menu.NONE, Constants.MENU_ITEM_DELETE, Menu.NONE, R.string.delete_property);
        menu.add(Menu.NONE, Constants.MENU_ITEM_EDIT, Menu.NONE, R.string.edit_property);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Property p = adapter.getItem(aInfo.position);

        switch (item.getItemId()) {
            case Constants.MENU_ITEM_DELETE:
                // Delete the property
                ItemFramework.getInstance().RemoveProp(p.getId());
                rebuildAdapter();
                this.adapter.notifyDataSetChanged();
                return true;
            case Constants.MENU_ITEM_EDIT:
                Intent myIntent = new Intent(PropertyChangeActivity.this, PropertyRenameActivity.class);
                myIntent.putExtra(Constants.PROPERTY_ID, p.getId());
                startActivityForResult(myIntent, Constants.MENU_ITEM_EDIT);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.MENU_ITEM_EDIT)
        {
            rebuildAdapter();
            this.adapter.notifyDataSetChanged();
        }
    }
}
