package com.semicolonapps.onereaderpro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.semicolonapps.onereaderpro.R;
import com.semicolonapps.onereaderpro.model.Group;
import com.semicolonapps.onereaderpro.model.Keychain;
import com.semicolonapps.onereaderpro.view.GroupsAdapter;

import java.util.ArrayList;
import java.util.List;

public class UnlockedActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private List<Group> groups = new ArrayList<Group>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups);

        groups.clear();

        for(String groupName : Keychain.groups()) {
            groups.add(new Group(groupName, Keychain.groupFor(groupName).size()));
        }

        ListView list = (ListView) findViewById(R.id.groups);
        list.setAdapter(new GroupsAdapter(this, groups));
        list.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ItemsActivity.class);
        String groupName = groups.get(position).name;
        intent.putExtra("group", groupName);
        startActivity(intent);
    }
}
