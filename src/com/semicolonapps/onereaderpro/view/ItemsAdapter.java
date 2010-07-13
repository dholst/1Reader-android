package com.semicolonapps.onereaderpro.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.semicolonapps.onepassword.Item;
import com.semicolonapps.onereaderpro.R;

import java.util.List;

public class ItemsAdapter extends ArrayAdapter<Item> {
    private List<Item> items;

    public ItemsAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        this.items = items;
    }

    public View getView(int position, View view, ViewGroup parent) {
        if(view == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.item, null);
        }

        Item item = items.get(position);

        if(item != null) {
            TextView title = (TextView) view.findViewById(R.id.item_name);
            title.setText(item.getName());

            TextView domain = (TextView) view.findViewById(R.id.item_domain);

            if(item.getDomain() == null || item.getDomain().trim().equals("")) {
                domain.setVisibility(View.GONE);
            }
            else {
                domain.setText(item.getDomain());
                view.requestLayout();
            }
        }

        return view;
    }
}
