package com.app.feng.treelistview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.feng.treelistview.treeUtils.Node;
import com.app.feng.treelistview.treeUtils.TreeListViewAdapter;

import java.util.List;

/**
 * Created by feng on 2015/11/16.
 */
public class SimpleTreeListViewAdapter<T> extends TreeListViewAdapter<T> {


    /**
     * datas
     *
     * @param context
     * @param treeView
     * @param datas              记录要显示的所有列表项。
     * @param defaultExpandLevel 默认展开层级，如果传错，默认为1（只展开根节点）.
     */
    public SimpleTreeListViewAdapter(Context context, ListView treeView, List<T> datas, int defaultExpandLevel)
            throws IllegalAccessException {
        super(context, treeView, datas, defaultExpandLevel);
    }

    @Override
    public View getConvertView(Node node, int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.simple_treelist_item, parent, false);
            holder = new ViewHolder();
            holder.mImage = (ImageView) convertView.findViewById(R.id.simple_treelist_item_imageview);
            holder.mText = (TextView) convertView.findViewById(R.id.simple_treelist_item_textview);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();

        }

        if(node.getIcon() == -1){
            holder.mImage.setVisibility(View.INVISIBLE);
        }else {
            holder.mImage.setVisibility(View.VISIBLE);
            holder.mImage.setImageResource(node.getIcon());
        }

        holder.mText.setText(node.getName());

        return convertView;
    }

    public class ViewHolder {
        ImageView mImage;
        TextView mText;

    }
}
