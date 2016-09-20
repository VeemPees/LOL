package com.veempees.lol;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PropertyRenameActivity extends AppCompatActivity {

    EditText editProperty;
    int propId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_rename);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editProperty = (EditText) findViewById(R.id.editPropertyRename);

        Bundle extras = getIntent().getExtras();


        // get data via the key
        propId = extras.getInt(Constants.PROPERTY_ID);
        if (propId == Constants.DUMMY_PROPERTY_ID)
        {
            // This is not an edit (of an existing), but a new
            TextView title = (TextView) findViewById(R.id.textPropertyEditTitle);

            title.setText(R.string.new_property);
        }
        else
        {
            editProperty.setText(ItemFramework.getInstance().getProp(propId).getValue());
        }

        Button btnDone = (Button) findViewById(R.id.buttonPropertyEditDone);
        btnDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String propName = editProperty.getText().toString();

                if (propId == Constants.DUMMY_PROPERTY_ID)
                {
                    // This is not an edit (of an existing), but a new
                    ItemFramework.getInstance().setNewPropCandidate(propName);
                    ItemFramework.getInstance().uploadNewProp(PropertyRenameActivity.this);
                }
                else
                {
                    ItemFramework.getInstance().getProp(propId).setValue(propName);
                }

                setResult(1);
                finish();
            }
        });
    }

}
