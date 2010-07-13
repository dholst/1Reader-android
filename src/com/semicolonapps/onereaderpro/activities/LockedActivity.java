package com.semicolonapps.onereaderpro.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.semicolonapps.onepassword.OnePasswordKeychain;
import com.semicolonapps.onepassword.dropbox.Authentication;
import com.semicolonapps.onepassword.dropbox.Sync;
import com.semicolonapps.onepassword.dropbox.SyncProgress;
import com.semicolonapps.onepassword.dropbox.Token;
import com.semicolonapps.onereaderpro.R;
import com.semicolonapps.onereaderpro.model.DropboxToken;
import com.semicolonapps.onereaderpro.model.Keychain;

import java.io.File;

public class LockedActivity extends BaseActivity {
    private static final String DROPBOX_PREFS = "dropboxPrefs";
    private static final Token CONSUMER_TOKEN = new Token(DropboxToken.KEY, DropboxToken.SECRET);
    private static final int SET_PROGRESS_MESSAGE = 1;
    private static final int SET_PROGRESS_MAX = 2;
    private static final int INCREMENT_PROGRESS = 3;
    private static final int DISMISS_PROGRESS = 4;
    private static final int SYNC_ERROR = 5;
    private static final int DROPBOX_LOGIN = 123;

    private AlertDialog dropboxDialog;
    private ProgressDialog progressDialog;
    private EditText password;
    private TextView message;
    private Button button;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.unlock);
        password = (EditText) findViewById(R.id.password);
        message = (TextView) findViewById(R.id.unlock_message);
        button = (Button) findViewById(R.id.unlock_button);
    }

    protected void onResume() {
        super.onResume();

        File keychainFile = new File(Preferences.getKeychainLocation(this));

        if(keychainFile.exists() && keychainFile.isDirectory()) {
            Keychain.setInstance(new OnePasswordKeychain(keychainFile));
            keychainFound();
        }
        else {
            keychainNotFound();
        }
    }

    private void keychainNotFound() {
        message.setText("Keychain not found, set location in settings or sync with Dropbox");
        message.setVisibility(View.VISIBLE);
        button.setOnClickListener(null);
    }

    private void keychainFound() {
        message.setText("Invalid password");
        message.setVisibility(View.INVISIBLE);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                message.setVisibility(View.INVISIBLE);
                progressDialog = ProgressDialog.show(LockedActivity.this, "", "Checking Password...", true);
                new UnlockThread(unlockHandler, password.getText().toString()).start();
            }
        });
    }

    final Handler unlockHandler = new Handler() {
        public void handleMessage(Message msg) {
            boolean unlocked = msg.getData().getBoolean("unlocked");
            progressDialog.dismiss();
            password.setText("");

            if(unlocked) {
                startActivity(new Intent(LockedActivity.this, UnlockedActivity.class));
            }
            else {
                message.setVisibility(View.VISIBLE);
            }
        }
    };

    private class UnlockThread extends Thread {
        private Handler handler;
        private String password;

        public UnlockThread(Handler handler, String password) {
            this.handler = handler;
            this.password = password;
        }

        public void run() {
            Message message = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putBoolean("unlocked", Keychain.getInstance().unlock(password));
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }

    public boolean onSearchRequested() {
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.settings) {
            startActivity(new Intent(this, Preferences.class));
            return true;
        }
        else if(item.getItemId() == R.id.sync) {
            syncDropbox();
            return true;
        }

        return false;
    }

    private void syncDropbox() {
        Token accessToken = getAccessToken();

        if(accessToken == null) {
            showDialog(DROPBOX_LOGIN);
        }
        else {
            syncDropbox(accessToken);
        }
    }

    private void syncDropbox(Token accessToken) {
        if(new Authentication(CONSUMER_TOKEN).check(accessToken)) {
            saveAccessToken(accessToken);
            progressDialog = new ProgressDialog(LockedActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("Syncing...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            new SyncThread(accessToken).start();
        }
        else {
            saveAccessToken(new Token(null, null));
            syncDropbox();
        }
    }

    private Token getAccessToken() {
        String accessKey = getSharedPreferences(DROPBOX_PREFS, 0).getString("access_key", null);
        String accessSecret = getSharedPreferences(DROPBOX_PREFS, 0).getString("access_secret", null);

        if(accessKey != null && accessSecret != null) {
            return new Token(accessKey, accessSecret);
        }
        else {
            return null;
        }
    }

    private void saveAccessToken(Token accessToken) {
        SharedPreferences.Editor editor = getSharedPreferences(DROPBOX_PREFS, 0).edit();
        editor.putString("access_key", accessToken.key);
        editor.putString("access_secret", accessToken.secret);
        editor.commit();
    }

    protected Dialog onCreateDialog(int id) {
        return (id == DROPBOX_LOGIN) ? createDropboxDialog() : null;
    }

    private Dialog createDropboxDialog() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dropbox, (ViewGroup) findViewById(R.id.dropbox_layout));

        final EditText dropboxEmail = (EditText) layout.findViewById(R.id.dropbox_username);
        final EditText dropboxPassword = (EditText) layout.findViewById(R.id.dropbox_password);
        Button authorize = (Button) layout.findViewById(R.id.dropbox_authorize);

        authorize.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Authentication authentication = new Authentication(CONSUMER_TOKEN);
                Token accessToken = authentication.getAccessTokenFor(dropboxEmail.getText().toString(), dropboxPassword.getText().toString());

                if(accessToken != null) {
                    syncDropbox(accessToken);
                    dropboxDialog.dismiss();
                    dropboxEmail.setText("");
                    dropboxPassword.setText("");
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        dropboxDialog = builder.create();
        return dropboxDialog;
    }

    final Handler syncHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case SET_PROGRESS_MESSAGE:
                    progressDialog.setMessage(msg.obj.toString());
                    break;

                case SET_PROGRESS_MAX:
                    Integer max = (Integer) msg.obj;

                    if(max > 0) {
                        progressDialog.setMax(max);
                    }
                    break;

                case INCREMENT_PROGRESS:
                    progressDialog.incrementProgressBy(1);
                    break;

                case DISMISS_PROGRESS:
                    progressDialog.dismiss();
                    break;

                case SYNC_ERROR:
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(LockedActivity.this);
                    builder.setMessage(msg.obj.toString());
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    builder.create().show();
                    break;
            }
        }
    };

    final SyncProgress syncProgress = new SyncProgress() {
        public void started() {
        }

        public void completed() {
            syncHandler.sendMessage(syncHandler.obtainMessage(DISMISS_PROGRESS));
        }

        public void retrievingEncryptionKeys() {
            syncHandler.sendMessage(syncHandler.obtainMessage(SET_PROGRESS_MESSAGE, "Getting keys..."));
        }

        public void retrievingItemList() {
            syncHandler.sendMessage(syncHandler.obtainMessage(SET_PROGRESS_MESSAGE, "Getting items..."));
        }

        public void itemsToSync(int i) {
            syncHandler.sendMessage(syncHandler.obtainMessage(SET_PROGRESS_MAX, i));
        }

        public void retrievingItem(String id) {
            syncHandler.sendMessage(syncHandler.obtainMessage(INCREMENT_PROGRESS));
        }
    };

    private class SyncThread extends Thread {
        private Token accessToken;

        public SyncThread(Token accessToken) {
            this.accessToken = accessToken;
        }

        public void run() {
            String local = Preferences.getKeychainLocation(LockedActivity.this);
            String remote = Preferences.getDropboxKeychainLocation(LockedActivity.this);

            try {
                new Sync(CONSUMER_TOKEN, accessToken).sync(remote, local, syncProgress);
                Keychain.setInstance(new OnePasswordKeychain(new File(local)));
            }
            catch(Exception e) {
                Log.e("1Reader", "Error syncing", e);
                syncHandler.sendMessage(syncHandler.obtainMessage(SYNC_ERROR, "Unable to sync, please check your local and remote locations in the settings"));
            }

        }
    }
}