package com.semicolonapps.onereaderpro.activities;

import android.app.Activity;
import android.content.Context;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends Activity {

    public void setContentView(int layoutResID) {
        LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.setContentView(inflator.inflate(layoutResID, null));
    }

    public void setContentView(View view) {
        super.setContentView(view);
        setCopyHandlersFor(view);
    }

    public void setCopyHandlersFor(View view) {
        for(View touchable : view.getTouchables()) {
            if("copyable".equals(touchable.getTag())) {
                touchable.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        clipboard.setText(((TextView) view).getText());
                        Toast.makeText(BaseActivity.this, "Copied...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    protected void setTextOn(int id, String text) {
        ((TextView) findViewById(id)).setText(text);
    }
}

