package com.app.feng.treelistview.treeUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Created by feng on 2015/11/15.
 */
public abstract class TreeListViewAdapter<T> extends BaseAdapter {

    protected Context context;
    private List<Node> mAllNodes;
    private List<Node> mVisibleNodes;
    private ListView mTreeView;

    protected LayoutInflater inflater;

    // set node left padding
    private int leftPadding = 0;

    //设置Adapter的TreeNodeListener
    public interface OnTreeNodeListener {
        /**
         * 设置树的叶子节点的点击事件。
         *
         * @param node
         * @param position
         */
        void onNodeClick(Node node, int position);
    }

    private OnTreeNodeListener mListener;

    public void setOnTreeNodeListener(OnTreeNodeListener mListener) {
        this.mListener = mListener;
    }

    /**
     * datas
     *
     * @param context
     * @param datas              记录要显示的所有列表项。
     * @param defaultExpandLevel 默认展开层级，如果传错，默认为1（只展开根节点）.
     */
    public TreeListViewAdapter(Context context, ListView treeView, List<T> datas, int defaultExpandLevel) throws IllegalAccessException {
        this.context = context;

        mAllNodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel);

        mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);

        inflater = LayoutInflater.from(context);


        mTreeView = treeView;

        mTreeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                expandOrCollapse(position);

                if (mListener != null) {
                    mListener.onNodeClick((Node) getItem(position), position);
                }
            }
        });

    }

    /**
     * 点击列表项能收缩或展开
     *
     * @param position
     */
    private void expandOrCollapse(int position) {

        Node node = mVisibleNodes.get(position);

        if (node != null) {
            if (node.isLeaf()) {
                return;
            }
            node.setExpand(!node.isExpand());
            mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
            notifyDataSetChanged();
        }
    }


    @Override
    public int getCount() {
        return mVisibleNodes.size();
    }

    @Override
    public Object getItem(int position) {
        //拿到Node
        return mVisibleNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        //拿到Node的ID
        return mVisibleNodes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Node node = mVisibleNodes.get(position);
        convertView = getConvertView(node, position, convertView, parent);

        convertView.setPadding(px2dip(context, leftPadding == 0 ? (20 * node.getLevel()) : (leftPadding * node.getLevel())), 3, 3, 3);

        return convertView;
    }

    private int px2dip(Context context, int i) {

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (i / scale + 0.5f);
    }

    public void setViewLeftPadding(int padding) {
        this.leftPadding = padding;
    }


    public abstract View getConvertView(Node node, int position, View convertView, ViewGroup parent);
}
