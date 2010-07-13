package com.semicolonapps.onereaderpro.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.semicolonapps.onereaderpro.R;
import com.semicolonapps.onereaderpro.model.Group;

import java.util.List;

public class GroupsAdapter extends ArrayAdapter<Group> {
    private List<Group> groups;

    public GroupsAdapter(Context context, List<Group> groups) {
        super(context, 0, groups);
        this.groups = groups;
    }

    public View getView(int position, View view, ViewGroup parent) {
        if(view == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.group, null);
        }

        Group group = groups.get(position);

        if(group != null) {
            TextView title = (TextView) view.findViewById(R.id.group_name);
            title.setText(group.name);

            TextView count = (TextView) view.findViewById(R.id.group_count);
            count.setText("(" + group.count + ")");
        }

        return view;
    }
}
