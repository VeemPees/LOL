package com.veempees.lol;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ItemEntryActivity extends AppCompatActivity {

    AutoCompleteTextView editItem;
    Spinner spnQuantity;
    Spinner spnMeasurement;
    List<Spinner> spnProps;
    TextView btnAddProperty;
    TextView btnRemoveProperty;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_entry);

        editItem = (AutoCompleteTextView) findViewById(R.id.textNewItem);

        final MementoAdapter adapter = new MementoAdapter(this);
        editItem.setAdapter(adapter);

        editItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Memento memento = adapter.getMemento(position);
                copyFromThis(memento);
            }
        });

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editItem, InputMethodManager.SHOW_IMPLICIT);

        spnQuantity = (Spinner) findViewById(R.id.spinnerQuantity);

        CreateQttList();

        spnMeasurement = (Spinner) findViewById(R.id.spinnerMeasurement);

        CreateMmtList();

        spnProps = new ArrayList<Spinner>();

        Spinner spnProp1 = (Spinner) findViewById(R.id.spinnerProperty1);
        spnProps.add(spnProp1);

        btnAddProperty = (TextView) findViewById(R.id.textAddProp);

        btnAddProperty.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AddOneProperty("");
            }

        });

        btnRemoveProperty = (TextView) findViewById(R.id.textRemoveProp);
        btnRemoveProperty.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (spnProps.size() > 1)
                {
                    RemoveOneProperty();
                }
            }

        });

        btnNext = (Button) findViewById(R.id.btnNext);

        btnNext.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CreateNewItem();
            }

        });

        CreatePropList(spnProp1, "");
    }

    protected void copyFromThis(Memento itemOrMemento) {

        // first remove all properties
        while (spnProps.size() > 0)
        {
            RemoveOneProperty();
        }

        // find out how many properties are in this item or memento
        int propCount = itemOrMemento.getPropertyIds().size();
        if (propCount < 1)
        {
            // this is a problem, but we can survive
            AddOneProperty("");
            return;
        }

        for (int propId : itemOrMemento.getPropertyIds())
        {
            Property prop = ItemFramework.getInstance().getProp(propId);
            AddOneProperty(prop.getValue());
        }
    }

    private void CreateQttList() {

        ArrayAdapter<String> qtyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ItemFramework.getInstance().getQtts());

        qtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnQuantity.setAdapter(qtyAdapter);
    }


    private void CreateMmtList() {
        ArrayAdapter<String> mmtAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ItemFramework.getInstance().getMmts());

        mmtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnMeasurement.setAdapter(mmtAdapter);

    }

    private void AddOneProperty(String selectThis) {

        // add one more property means one more spinner at the tail
        Spinner spnNew = new Spinner(this);

        CreatePropList(spnNew, selectThis);

        spnProps.add(spnNew);

        LinearLayout ll = (LinearLayout)findViewById(R.id.spinnerContainer);

        int spinnerCount = ll.getChildCount();

        if (spinnerCount == 0)
        {
            // there is no spinner, this will be the first one
            spnNew.setId(R.id.spinnerProperty1);
        }
        ll.addView(spnNew, spnProps.size() - 1);
    }

    private void RemoveOneProperty() {

        // remove one property means remove last spinner
        LinearLayout ll = (LinearLayout) findViewById(R.id.spinnerContainer);

        ll.removeViewAt(ll.getChildCount() - 1);

        int newSpinnerCount = spnProps.size() - 1;
        Spinner s = spnProps.get(newSpinnerCount);
        s = null;
        spnProps.remove(newSpinnerCount);
    }

    private void Reset() {
        editItem.setText("");
    }

    private void CreateNewItem() {
        Editable currentItem = editItem.getText();

        if (currentItem.toString().length() == 0)
        {
            return;
        }

        List<Property> newProps = new ArrayList<Property>();

        for (Spinner spn : spnProps)
        {
            Property prop = (Property) spn.getSelectedItem();
            boolean alreadyAdded = false;
            for (Property propAlreadyAdded : newProps)
            {
                if (propAlreadyAdded.getValue().compareToIgnoreCase(prop.getValue()) == 0)
                {
                    alreadyAdded = true; // do not add a property twice
                }
            }
            if (!alreadyAdded)
            {
                newProps.add(prop);
            }
        }

        String qtt = (String)spnQuantity.getSelectedItem();
        String mmt = (String)spnMeasurement.getSelectedItem();

        int qttID = ItemFramework.getInstance().lookUpQtt(qtt);
        int mmtID = ItemFramework.getInstance().lookUpMmt(mmt);

        if (ItemFramework.getInstance().doesItemExist(currentItem.toString()))
        {
            Logger.i(currentItem.toString() + " already exists");
            Toast.makeText(this, R.string.item_already_exists, Toast.LENGTH_LONG).show();

            // try to change the props to that of the existing one
            // TODO
            //copyFromThis(i);
        } else {
            Item i = new Item(ItemFramework.getInstance().generateID(), false, currentItem.toString(), new BigDecimal(qttID), new BigDecimal(mmtID));

            for (Property p : newProps)
            {
                i.addProperty(p.getId());
            }

            ItemFramework.getInstance().addItem(i);
            Logger.i(currentItem.toString() + " created");
            Reset();
        }
    }

    private void CreatePropList(Spinner spn, String selectThis) {
        List<Property> list = new ArrayList<Property>();
        int selectedIndex = -1;
        int index = -1;

        for (Property p : ItemFramework.getInstance().getProps())
        {
            index++;
            list.add(p);

            if (selectThis.length() > 0)
            {
                if (p.getValue().compareToIgnoreCase(selectThis) == 0)
                {
                    selectedIndex = index;
                }
            }
        }

        ArrayAdapter<Property> dataAdapter = new ArrayAdapter<Property>(this, android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spn.setAdapter(dataAdapter);
        if (selectedIndex != -1)
        {
            spn.setSelection(selectedIndex);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(0);
        super.onBackPressed();
    }
}
