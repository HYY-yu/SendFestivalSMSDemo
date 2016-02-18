package com.app.feng.sendfestivalsmsdemo.fragment;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.app.feng.sendfestivalsmsdemo.R;
import com.app.feng.sendfestivalsmsdemo.activity.SendMessageActivity;
import com.app.feng.sendfestivalsmsdemo.control.SmsCollectProvider;
import com.app.feng.sendfestivalsmsdemo.element.CollectMessage;

public class FestivalSmsCollectFragment extends ListFragment {

    public FestivalSmsCollectFragment() {
        // Required empty public constructor
    }

    private static final int LOADER_ID = 2;
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
                View view = inflater.inflate(R.layout.item_message_listview, parent, false);
                return view;
            }

            @Override
            public void bindView(View view, Context context, final Cursor cursor) {
                final TextView textView = (TextView) view.findViewById(R.id.listview_textView);
                Button button = (Button) view.findViewById(R.id.listview_button);

                final String sms = cursor.getString(cursor.getColumnIndex(CollectMessage.COL_CONTENT));

                textView.setText("    " + sms);

                final Button btn_delete = (Button) view.findViewById(R.id.button_share);
                ToggleButton btn_star = (ToggleButton) view.findViewById(R.id.button_star);
                Drawable drawable = getResources().getDrawable(R.mipmap.icon_delete2);
                btn_delete.setBackground(drawable);
                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.post(new Runnable() {
                            @Override
                            public void run() {
                                int t = getParentFragment().getActivity().getContentResolver().delete(SmsCollectProvider.CONTENT_URI,
                                        CollectMessage.COL_CONTENT + " like '" + sms.substring(0, 8) + "%'", null);
                                Log.i("delete", t + " ");
                            }
                        });
                    }
                });

                btn_star.setChecked(true);
                btn_star.setClickable(false);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SendMessageActivity.toSendMessageActivity(getParentFragment().getActivity(),
                                cursor.getString(cursor.getColumnIndex(CollectMessage.COL_FESTIVAL_NAME)), textView.getText().toString());
                    }
                });
            }
        };

        setListAdapter(cursorAdapter);

    }

    private void initLoader() {
        getLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                CursorLoader cursorLoader = new CursorLoader(getParentFragment().getActivity(), SmsCollectProvider.CONTENT_URI, null, null, null, null);

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