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

    int DummyRequestCode = 11;
}
