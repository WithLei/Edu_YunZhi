package com.android.renly.edu_yunzhi.Common;

import com.simon.multilevellist.tree.INode;

public class BaseNode implements INode {

    private String name;

    public BaseNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                '}';
    }
}