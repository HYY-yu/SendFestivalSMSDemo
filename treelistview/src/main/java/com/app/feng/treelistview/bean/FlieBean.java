package com.app.feng.treelistview.bean;

import com.app.feng.treelistview.annotation.NodeID;
import com.app.feng.treelistview.annotation.NodeName;
import com.app.feng.treelistview.annotation.NodeParentID;

/**
 * 这是一个示例类，展示了使用TreeList程序的数据存储结点。（如数据存储在数据库中的方式）
 * Created by feng on 2015/11/11.
 */
public class FlieBean {
    @NodeID
    private int id;
    @NodeParentID
    private int pid;
    @NodeName
    private String name;

    public FlieBean(int id, String name, int pid) {
        this.id = id;
        this.name = name;
        this.pid = pid;
    }

    //文件地址
    private String addr;

    //...肯定还有其他的属性
}
