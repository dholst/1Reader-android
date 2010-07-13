package com.semicolonapps.onereaderpro.activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import com.semicolonapps.onepassword.Item;
import com.semicolonapps.onepassword.LoginItem;
import com.semicolonapps.onepassword.NoteItem;
import com.semicolonapps.onereaderpro.R;
import com.semicolonapps.onereaderpro.model.Keychain;
import com.semicolonapps.onereaderpro.view.ItemsAdapter;

import java.util.List;

public abstract class BaseItemsActivity extends ListActivity implements AdapterView.OnItemClickListener {
    private static final String UNKNOWN_ITEM = "Don't know how to show that one yet";
    private static final String ITEM_ERROR = "Error unlocking item";
    protected static Item CURRENT_ITEM;
    protected List items;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items);
        setItems();
        setListAdapter(new ItemsAdapter(this, items));
        getListView().setOnItemClickListener(this);
    }

    protected abstract void setItems();

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CURRENT_ITEM = (Item) items.get(position);
        Class activity = activityFor(CURRENT_ITEM);

        if(activity == null) {
            showAlert(UNKNOWN_ITEM);
        }
        else {
            try {
                long duration = System.currentTimeMillis();
                Keychain.getInstance().decrypt(CURRENT_ITEM);
                duration = System.currentTimeMillis() - duration;
                Log.i("1Reader", "Unlocked in " + duration + " milliseconds");
                startActivity(new Intent(BaseItemsActivity.this, activity));
            }
            catch(Exception e) {
                showAlert(ITEM_ERROR);
            }
        }
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }

    private Class activityFor(Item item) {
        if(item.getClass().isAssignableFrom(LoginItem.class)) {
            return LoginItemActivity.class;
        }

        if(item.getClass().isAssignableFrom(NoteItem.class)) {
            return NoteItemActivity.class;
        }

        return GenericItemActivity.class;
    }
}
