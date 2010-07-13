package com.semicolonapps.onereaderpro.activities;

import android.app.SearchManager;
import android.content.Intent;
import com.semicolonapps.onereaderpro.model.Keychain;

public class SearchActivity extends BaseItemsActivity {
    protected void setItems() {
        Intent intent = getIntent();

        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            setTitle("Search - " + query);
            items = Keychain.search(query);
        }
    }
}
