package com.wow.carlauncher.dialog;

import android.content.Context;
import android.widget.ListView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.BaseDialog;

/**
 * Created by 10124 on 2018/3/29.
 */

public class ListDialog extends BaseDialog {

    private ListView listView;

    public ListView getListView() {
        return listView;
    }

    public ListDialog(Context context) {
        super(context);
        setGravityCenter();
        setContent(R.layout.dialog_listview);

        listView = (ListView) findViewById(R.id.lv_list);
        System.out.println("!!!!!!!!!!!!!!" + listView);
    }
}