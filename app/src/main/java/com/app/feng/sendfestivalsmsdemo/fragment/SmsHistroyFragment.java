package com.app.feng.sendfestivalsmsdemo.fragment;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.feng.sendfestivalsmsdemo.R;
import com.app.feng.sendfestivalsmsdemo.control.SmsHistoryProvider;
import com.app.feng.sendfestivalsmsdemo.element.FlowLayout;
import com.app.feng.sendfestivalsmsdemo.element.HistoryMessage;

/**
 * Created by feng on 2015/10/25.
 */
public class SmsHistroyFragment extends ListFragment {

    private static final int LOADER_ID = 1;
    private LayoutInflater inflater;
    private CursorAdapter cursorAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inflater = LayoutInflater.from(getParentFragment().getActivity());

        initLoader();

        setupListAdapter();
    }

    private void setupListAdapter() {
        cursorAdapter = new CursorAdapter(getParentFragment().getActivity(), null, false) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = inflater.inflate(R.layout.item_sms_histroy_fragment, parent, false);


                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView textView_Msg = (TextView) view.findViewById(R.id.id_histroy_tv_msg);
                FlowLayout flowLayout = (FlowLayout) view.findViewById(R.id.id_histroy_flowlayout);
                TextView textView_date = (TextView) view.findViewById(R.id.id_histroy_tv_date);
                TextView textView_festival = (TextView) view.findViewById(R.id.id_histroy_tv_festival);

                textView_Msg.setText(cursor.getString(cursor.getColumnIndex(HistoryMessage.COLUMN_MSG)));
                textView_festival.setText(cursor.getString(cursor.getColumnIndex(HistoryMessage.COLUMN_FESTIVAL_NAME)));
                textView_date.setText(cursor.getString(cursor.getColumnIndex(HistoryMessage.COLUMN_DATE)));

                String names = cursor.getString(cursor.getColumnIndex(HistoryMessage.COLUMN_NAME));
                if (TextUtils.isEmpty(names)) {
                    return;
                }
                flowLayout.removeAllViews();
                for (String name :
                        names.split(":")) {
                    addTag(name, flowLayout);
                }

            }
        };

        setListAdapter(cursorAdapter);

    }

    private void addTag(String name, FlowLayout flowLayout) {
        TextView textView = (TextView) inflater.inflate(R.layout.item_flowlayout_2, flowLayout, false);
        textView.setText(name);
        textView.setTextColor(Color.WHITE);
        flowLayout.addView(textView);
    }

    private void initLoader() {
        getLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                CursorLoader cursorLoader = new CursorLoader(getParentFragment().getActivity(), SmsHistoryProvider.CONTENT_URI, null, null, null, null);

                return cursorLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (loader.getId() == LOADER_ID) {
                    cursorAdapter.swapCursor(data);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                //当Cursor变化
                cursorAdapter.swapCursor(null);
            }
        });

    }
}
