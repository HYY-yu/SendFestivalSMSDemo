package com.app.feng.treelistview;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.app.feng.treelistview.bean.FlieBean;
import com.app.feng.treelistview.treeUtils.Node;
import com.app.feng.treelistview.treeUtils.TreeListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    private ListView listView;
    private SimpleTreeListViewAdapter<FlieBean> simpleTreeListViewAdapter;

    private List<FlieBean> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.id_listview);

        initDatas();

        try {
            simpleTreeListViewAdapter = new SimpleTreeListViewAdapter<>(this, listView, datas, 1);
            simpleTreeListViewAdapter.setViewLeftPadding(80);
            simpleTreeListViewAdapter.setOnTreeNodeListener(new TreeListViewAdapter.OnTreeNodeListener() {
                @Override
                public void onNodeClick(Node node, int position) {
                    if (node.isLeaf())
                        Toast.makeText(MainActivity.this, "点击" + node.getName(), Toast.LENGTH_SHORT).show();
                }
            });

            listView.setAdapter(simpleTreeListViewAdapter);

            /*listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {




                    return false;
                }
            });*/

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void initDatas() {

        FlieBean bean = new FlieBean(1, "根目录1", 0);
        datas.add(bean);
        bean = new FlieBean(2, "目录1", 1);
        datas.add(bean);
        bean = new FlieBean(3, "目录2", 1);
        datas.add(bean);
        bean = new FlieBean(4, "根目录2", 0);
        datas.add(bean);
        bean = new FlieBean(5, "根目录3", 0);
        datas.add(bean);
        bean = new FlieBean(6, "根目录4", 0);
        datas.add(bean);
        bean = new FlieBean(7, "目录1", 6);
        datas.add(bean);
        bean = new FlieBean(8, "子目录1-1", 7);
        datas.add(bean);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
