package com.semicolonapps.onereaderpro.activities;

import android.os.Bundle;
import android.widget.ListView;
import com.semicolonapps.onepassword.GenericFieldsItem;
import com.semicolonapps.onereaderpro.R;
import com.semicolonapps.onereaderpro.model.Field;
import com.semicolonapps.onereaderpro.view.FieldsAdapter;

import java.util.*;

public class GenericItemActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generic_item);

        GenericFieldsItem item = (GenericFieldsItem) ItemsActivity.CURRENT_ITEM;

        setTitle(item.getName());

        ListView fields = (ListView) findViewById(R.id.fields_list);
        fields.setDivider(null);
        fields.setDividerHeight(0);
        fields.setAdapter(new FieldsAdapter(this, toList(item.getFields())));
    }

    private List<Field> toList(Map<String, String> fields) {
        List<Field> list = new ArrayList<Field>();

        for(Map.Entry<String, String> field : fields.entrySet()) {
            list.add(new Field(field.getKey(), field.getValue()));
        }

        Collections.sort(list, new Comparator<Field>() {
            public int compare(Field o1, Field o2) {
                return o1.name.compareTo(o2.name);
            }
        });

        return list;
    }
}
