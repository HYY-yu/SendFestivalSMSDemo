package com.app.feng.sendfestivalsmsdemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.feng.sendfestivalsmsdemo.R;
import com.app.feng.sendfestivalsmsdemo.activity.ChooseMessageActivity;
import com.app.feng.treelistview.SimpleTreeListViewAdapter;
import com.app.feng.treelistview.bean.FlieBean;
import com.app.feng.treelistview.treeUtils.Node;
import com.app.feng.treelistview.treeUtils.TreeListViewAdapter;

import java.util.ArrayList;
import java.util.List;


public class FestivalTypeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    //自定义的属性集
    private ListView listView;
    private SimpleTreeListViewAdapter<FlieBean> simpleTreeListViewAdapter;
    private List<FlieBean> datas = new ArrayList<>();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FestivalTypeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FestivalTypeFragment newInstance(String param1, String param2) {
        FestivalTypeFragment fragment = new FestivalTypeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FestivalTypeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_festival_type, container, false);

        listView = (ListView) mainView.findViewById(R.id.fragment_festival_type_listview);

        initDatas();
        try {
            simpleTreeListViewAdapter = new SimpleTreeListViewAdapter<>(getActivity(), listView, datas, 0);
            simpleTreeListViewAdapter.setViewLeftPadding(80);
            simpleTreeListViewAdapter.setOnTreeNodeListener(new TreeListViewAdapter.OnTreeNodeListener() {
                @Override
                public void onNodeClick(Node node, int position) {
                    if (node.isLeaf()){
                        Intent intent = new Intent(getActivity(), ChooseMessageActivity.class);
                        intent.putExtra("festivalName", node.getName().trim());
                        startActivity(intent);
                    }

                }
            });
            listView.setAdapter(simpleTreeListViewAdapter);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return mainView;
    }

    private void initDatas() {

        FlieBean bean = new FlieBean(1, "传统节日", 0);
        datas.add(bean);
        bean = new FlieBean(2, "春节", 1);
        datas.add(bean);
        bean = new FlieBean(3, "国庆节", 1);
        datas.add(bean);
        bean = new FlieBean(4, "元旦", 1);
        datas.add(bean);
        bean = new FlieBean(5, "小年", 1);
        datas.add(bean);
        bean = new FlieBean(6, "七夕节", 1);
        datas.add(bean);
        bean = new FlieBean(7, "端午节", 1);
        datas.add(bean);
        bean = new FlieBean(8, "元宵节", 1);
        datas.add(bean);
        bean = new FlieBean(9, "除夕", 1);
        datas.add(bean);
        bean = new FlieBean(10, "西方节日", 0);
        datas.add(bean);
        bean = new FlieBean(11, "圣诞节", 10);
        datas.add(bean);
        bean = new FlieBean(12, "情人节", 10);
        datas.add(bean);
        bean = new FlieBean(13, "愚人节", 10);
        datas.add(bean);
        bean = new FlieBean(14, "贺词大全", 0);
        datas.add(bean);
        bean = new FlieBean(15, "开业短信", 14);
        datas.add(bean);
        bean = new FlieBean(16, "升职短信", 14);
        datas.add(bean);
        bean = new FlieBean(17, "乔迁短信", 14);
        datas.add(bean);
        bean = new FlieBean(18, "结婚生子", 14);
        datas.add(bean);
        bean = new FlieBean(19, "喜得贵子", 18);
        datas.add(bean);
        bean = new FlieBean(20, "结婚短信", 18);
        datas.add(bean);
        bean = new FlieBean(21, "日常问候", 0);
        datas.add(bean);
        bean = new FlieBean(22, "健康关怀", 21);
        datas.add(bean);
        bean = new FlieBean(23, "生活知识", 22);
        datas.add(bean);
        bean = new FlieBean(24, "生病问候", 22);
        datas.add(bean);
        bean = new FlieBean(25, "健康知识", 22);
        datas.add(bean);
        bean = new FlieBean(26, "幽默祝福", 21);
        datas.add(bean);
        bean = new FlieBean(27, "生日祝福", 21);
        datas.add(bean);
        bean = new FlieBean(28, "爱情宝典", 21);
        datas.add(bean);
        bean = new FlieBean(29, "思念短信", 28);
        datas.add(bean);
        bean = new FlieBean(30, "情话短信", 28);
        datas.add(bean);
        bean = new FlieBean(31, "表白短信", 28);
        datas.add(bean);
        bean = new FlieBean(32, "感谢短信", 21);
        datas.add(bean);
        bean = new FlieBean(33, "早安晚安", 21);
        datas.add(bean);
        bean = new FlieBean(34, "早安", 33);
        datas.add(bean);
        bean = new FlieBean(35, "晚安", 33);
        datas.add(bean);

    }
}
