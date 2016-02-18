package com.app.feng.sendfestivalsmsdemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.app.feng.sendfestivalsmsdemo.R;
import com.app.feng.sendfestivalsmsdemo.activity.ChooseMessageActivity;
import com.app.feng.sendfestivalsmsdemo.element.Festival;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2015/10/22.
 */
public class FestivalCategoryFragment extends Fragment {

    private GridView gridView;

    private LayoutInflater layoutInflater;

    private ArrayAdapter<Festival> arrayAdapter;

    private List<Festival> hot_festivals = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDatas();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_festival_category, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        gridView = (GridView) view.findViewById(R.id.gridview);

        layoutInflater = LayoutInflater.from(getActivity());
       /* Cursor c = getActivity().getContentResolver().query(MessageProvider.CONTENT_URI_FESTIVAL,
                null, null, null, null);*/

      /*  cursorAdapter = new CursorAdapter(getActivity(), c, false) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = layoutInflater.inflate(R.layout.item_festival_fragment, parent, false);
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView textView = (TextView) view.findViewById(R.id.textview);
                textView.setText(cursor.getString(cursor.getColumnIndex(Festival.FESTIVAL_NAME)));

            }
        };*/

        //Adapter的新写法 (弃用)
        arrayAdapter = new ArrayAdapter<Festival>(getActivity(), -1, hot_festivals) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.item_festival_fragment, parent, false);
                }

                TextView textView = (TextView) convertView.findViewById(R.id.textview);
                textView.setText(getItem(position).getName());

                return convertView;
            }
        };
        gridView.setAdapter(arrayAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO:跳转
                Intent intent = new Intent(getActivity(), ChooseMessageActivity.class);
                Festival c = (Festival) arrayAdapter.getItem(position);
                intent.putExtra("festivalName", c.getName());
                startActivity(intent);
            }
        });
    }

    private void initDatas() {
        Festival festival = new Festival(100,"国庆节");
        hot_festivals.add(festival);
        festival = new Festival(101,"中秋节");
        hot_festivals.add(festival);
        festival = new Festival(102,"元旦");
        hot_festivals.add(festival);
        festival = new Festival(103,"春节");
        hot_festivals.add(festival);
        festival = new Festival(104,"端午节");
        hot_festivals.add(festival);
        festival = new Festival(105,"七夕节");
        hot_festivals.add(festival);
        festival = new Festival(106,"圣诞节");
        hot_festivals.add(festival);
        festival = new Festival(107,"除夕");
        hot_festivals.add(festival);
        festival = new Festival(108,"元宵节");
        hot_festivals.add(festival);
    }
}
