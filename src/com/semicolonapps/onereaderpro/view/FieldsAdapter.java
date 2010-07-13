package com.semicolonapps.onereaderpro.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.semicolonapps.onereaderpro.R;
import com.semicolonapps.onereaderpro.activities.BaseActivity;
import com.semicolonapps.onereaderpro.model.Field;

import java.util.List;

public class FieldsAdapter extends ArrayAdapter<Field> {
    private List<Field> fields;

    public FieldsAdapter(Context context, List<Field> fields) {
        super(context, 0, fields);
        this.fields = fields;
    }

    public View getView(int position, View view, ViewGroup parent) {
        if(view == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.field, null);
        }

        Field field = fields.get(position);

        if(field != null) {
            TextView fieldName = (TextView) view.findViewById(R.id.field_name);
            fieldName.setText(field.name);

            TextView fieldValue = (TextView) view.findViewById(R.id.field_value);
            fieldValue.setText(field.value);

            BaseActivity activity = (BaseActivity) getContext();
            activity.setCopyHandlersFor(view);
        }

        return view;
    }
}
