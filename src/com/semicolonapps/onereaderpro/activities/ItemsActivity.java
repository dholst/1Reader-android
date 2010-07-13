package com.semicolonapps.onereaderpro.activities;

import com.semicolonapps.onereaderpro.model.Keychain;

public class ItemsActivity extends BaseItemsActivity {
    protected void setItems() {
        String groupName = getIntent().getExtras().getString("group");
        setTitle(groupName);
        items = Keychain.groupFor(groupName);
    }
}
