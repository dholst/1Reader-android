package com.semicolonapps.onereaderpro.activities;

import android.os.Bundle;
import com.semicolonapps.onepassword.NoteItem;
import com.semicolonapps.onereaderpro.R;

public class NoteItemActivity extends BaseActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_item);

        NoteItem item = (NoteItem) ItemsActivity.CURRENT_ITEM;

        setTitle(item.getName());
        setTextOn(R.id.note_item_note, item.getNotes());
    }
}
