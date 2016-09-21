package com.veempees.lol;

public interface Constants {

    String LOG_PREFIX = "com.veempees.LOL";
    int REQUEST_ACCOUNT_PICKER = 1000;
    int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    int REQUEST_AUTHORIZATION = 1001;
    String PREF_ACCOUNT_NAME = "accountName";
    String[] SCOPES = { "https://www.googleapis.com/auth/drive", "https://spreadsheets.google.com/feeds" };
    String SCRIPT_ID = "MxHLP1QI91n-nKR__hXfJ90Nptr_xnHQR";

    String PREF_SHOW_EMPTY_PROPERTY_GROUPS = "show_empty_property_groups";
    String PREF_SHOW_ADDITIONAL_PROPERTIES_IN_ITEM = "show_additional_properties_in_item";
    String PREF_SHOW_QUANTITY_BEFORE_ITEM = "show_quantity_before_item";
    String PREF_SHOW_COUNT_AFTER_PROPERTY = "show_count_after_group";

    int MENU_ITEM_DELETE = 1;
    int MENU_ITEM_EDIT = 2;

    String PROPERTY_ID = "PropertyId";
    int DUMMY_PROPERTY_ID = 0xffff;

    int ADD_ITEM_REQUEST_CODE = 11;
    int EDIT_ITEM_REQUEST_CODE = 12;
    int NEXT_ITEM_INDEX_START = 64000;

    int ASYNC_REQUEST_GET = 1;
    int ASYNC_REQUEST_ADD = 2;
    int ASYNC_REQUEST_ADD_PROP = 3;

    String INTENT_EXTRA_ID_KEY = "ID";
    String INTENT_EXTRA_VALUE_KEY = "Value";
}
