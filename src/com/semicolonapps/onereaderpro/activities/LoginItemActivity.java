package com.semicolonapps.onereaderpro.activities;

import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;
import com.semicolonapps.onepassword.LoginItem;
import com.semicolonapps.onereaderpro.R;

public class LoginItemActivity extends BaseActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_item);

        LoginItem item = (LoginItem) ItemsActivity.CURRENT_ITEM;

        setTitle(item.getName());
        setTextOn(R.id.item_url, item.getUrl());
        Linkify.addLinks((TextView) findViewById(R.id.item_url), Linkify.ALL);
        setTextOn(R.id.login_item_username, item.getUsername());
        setTextOn(R.id.login_item_password, item.getPassword());
    }
}
